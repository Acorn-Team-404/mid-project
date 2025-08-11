package controller.image;

import java.io.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.apache.commons.net.ftp.FTPClient;

@WebServlet("/show.img")
public class ShowImageServlet extends HttpServlet{
	
	private static final String HOST = "danpung.myds.me";
    private static final int PORT = 21;
    private static final String USER = "team404";
    private static final String PASSWORD = "#w3770AmyK@q*r";
    private static final String REMOTE_DIR = "/team404/upload/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	String fileName = req.getParameter("imageName");

        if (fileName == null || fileName.isBlank()) {
            System.out.println("[ImageServeServlet] 파일명 누락 - 400 오류 발생");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "파일명이 누락됨");
            return;
        }
        // Null 체크 후 인코딩
        String ftpFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

        FTPClient ftp = new FTPClient();
        try {
            // UTF-8 인코딩
            ftp.setControlEncoding("UTF-8");
            ftp.connect(HOST, PORT);
            ftp.login(USER, PASSWORD);

            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            InputStream is = ftp.retrieveFileStream(REMOTE_DIR + ftpFileName);
            if (is == null) {
                System.out.println("[ImageServeServlet] 파일 없음 또는 다운로드 실패 - 404 오류 발생");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "파일 없음");
                return;
            }

            // MIME 타입 설정
            if (ftpFileName.endsWith(".png")) {
                resp.setContentType("image/png");
            } else if (ftpFileName.endsWith(".jpg") || ftpFileName.endsWith(".jpeg")) {
                resp.setContentType("image/jpeg");
            } else if (ftpFileName.endsWith(".gif")) {
                resp.setContentType("image/gif");
            } else {
                resp.setContentType("application/octet-stream");
            }
            
            OutputStream os = resp.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            is.close();
            boolean completed = ftp.completePendingCommand();
            System.out.println("[ImageServeServlet] FTP 이미지 로드됨: " + completed);
        } catch (Exception e) {
            System.out.println("[ImageServeServlet] 예외 발생: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류 발생");
        } finally {
            if (ftp.isConnected()) {
                ftp.disconnect();
            }
        }
    }
}
