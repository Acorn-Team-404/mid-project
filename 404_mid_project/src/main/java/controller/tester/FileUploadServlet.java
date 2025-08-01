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

        // 고유한 파일 이름 생성 + 인코딩
        // 1. 파일명 받기
        String originalFileName = filePart.getSubmittedFileName();
        
        // 2. URL-safe 인코딩 (공백 → %20, 특수문자까지 안전하게)
        String safeFileName = java.net.URLEncoder.encode(originalFileName, "UTF-8");
        // 3. UUID 결합
        String savedFileName = UUID.randomUUID().toString() + "_" + safeFileName;
        // 4. FTP용 파일명 인코딩 (FTPClient는 ISO-8859-1 문자셋으로 파일명 전송)
        String ftpFileName = new String(savedFileName.getBytes("UTF-8"), "ISO-8859-1");

        // NAS에 FTP로 업로드
        FTPClient ftp = new FTPClient();
        try (InputStream fileContent = filePart.getInputStream()) {
            ftp.connect(HOST, PORT);
            // 파일명 인코딩
            ftp.setControlEncoding("UTF-8");
            ftp.login(USER, PASSWORD);
            // 서버에 UTF-8 모드 사용 요청
            ftp.sendCommand("OPTS UTF8 ON");
            
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            boolean uploaded = ftp.storeFile(REMOTE_DIR + ftpFileName, fileContent);

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