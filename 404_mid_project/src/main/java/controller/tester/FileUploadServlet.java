package controller.tester;

import java.io.*;
import java.util.UUID;

import org.apache.commons.net.ftp.FTPClient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/uploadToNAS")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

	// NAS 접속 정보
    private static final String HOST = "danpung.myds.me";
    private static final int PORT = 21;
    private static final String USER = "team404";
    private static final String PASSWORD = "#w3770AmyK@q*r";
    private static final String REMOTE_DIR = "/team404/upload/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        // 폼에서 업로드된 파일 정보 받기
        Part filePart = request.getPart("uploadFile");
        if (filePart == null || filePart.getSize() == 0) {
            out.println("⚠️ 업로드된 파일이 없습니다.");
            return;
        }

        // 고유한 파일 이름 생성
        String originalFileName = filePart.getSubmittedFileName();
        String uuid = UUID.randomUUID().toString();
        String savedFileName = uuid + "_" + originalFileName;

        // NAS에 FTP로 업로드
        FTPClient ftp = new FTPClient();
        try (InputStream fileContent = filePart.getInputStream()) {
            ftp.connect(HOST, PORT);
            ftp.login(USER, PASSWORD);
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            boolean uploaded = ftp.storeFile(REMOTE_DIR + savedFileName, fileContent);

            if (uploaded) {
                // DB에 저장
                boolean inserted = ImageDao.getInstance().insert(savedFileName);
                if (inserted) {
                    out.println("✅ 파일 업로드 및 DB 등록 성공: " + savedFileName);
                } else {
                    out.println("⚠️ 파일 업로드 성공했지만 DB 저장 실패");
                }
            } else {
                out.println("❌ NAS 업로드 실패");
            }

            ftp.logout();
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("❌ 오류 발생: " + e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace(out);
                }
            }
        }
    }
}