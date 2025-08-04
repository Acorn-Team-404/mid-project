package controller.image;

import java.io.*;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.net.ftp.FTPClient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.image.ImageDao;

@WebServlet("*.img")
@MultipartConfig
public class ImageUpload extends HttpServlet {
    private static final String HOST = "danpung.myds.me";
    private static final int PORT = 21;
    private static final String USER = "team404";
    private static final String PASSWORD = "#w3770AmyK@q*r";
    private static final String REMOTE_DIR = "/team404/upload/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        PrintWriter out = resp.getWriter();

        // 1. 요청 URI에서 target_type 추출
        String uri = req.getRequestURI();
        String context = req.getContextPath();
        String command = uri.substring(context.length() + 1); // e.g. room.img
        String targetType = command.replace(".img", "");      // e.g. room

        // 2. target_id 추출
        String targetIdStr = req.getParameter("target_id");
        int targetId = 0;
        try {
            targetId = Integer.parseInt(targetIdStr);
        } catch (NumberFormatException e) {
        	out.println("<script>alert('잘못된 target_id 값입니다.'); history.back();</script>");
            return;
        }

        // 3. FTP 연결
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(HOST, PORT);
            ftp.setControlEncoding("UTF-8");
            ftp.login(USER, PASSWORD);
            ftp.sendCommand("OPTS UTF8 ON");
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            // 4. 파일 반복 업로드
            Collection<Part> parts = req.getParts();
            int successCount = 0;
            
            // 메세지를 JS alert() 로 출력하기 위한 StringBuilder
            StringBuilder alertMessages = new StringBuilder();
            
            for (Part part : parts) {
                if (part.getName().equals("uploadFile") && part.getSize() > 0) {
                    String originalName = part.getSubmittedFileName();
                    String extension = "";
                    int dotIndex = originalName.lastIndexOf(".");
                    if (dotIndex != -1) {
                        extension = originalName.substring(dotIndex);
                    }

                    // 현재 날짜 문자열 얻기 (예: 20250803)
                    String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
                    // UUID 생성
                    String uuid = UUID.randomUUID().toString();
                    // 저장할 파일명: 날짜_UUID.확장자
                    String savedName = today + "_" + uuid + extension;
                    String ftpFileName = new String(savedName.getBytes("UTF-8"), "ISO-8859-1");

                    try (InputStream input = part.getInputStream()) {
                        boolean uploaded = ftp.storeFile(REMOTE_DIR + ftpFileName, input);
                        if (uploaded) {
                            // 자동 sort_order 방식으로 저장
                            boolean inserted = ImageDao.getInstance()
                                .insertAutoSort(originalName, savedName, targetType, targetId);
                            if (inserted) {
                                successCount++;
                                System.out.println("업로드 성공: " + originalName);
                                // 성공메세지 JS 출력
                                alertMessages.append("업로드 성공: ").append(originalName).append("\\n");
                                // 프로필 이미지일 경우 users 테이블 업데이트
                                if ("profile".equals(targetType)) {
                                    boolean updated = ImageDao.getInstance().updateUserProfileImage(targetId, savedName);
                                    if (updated) {
                                    	System.out.println("사용자 프로필 이미지 갱신 완료");
                                        alertMessages.append("사용자 프로필 이미지 갱신 완료\\n");
                                    } else {
                                    	System.out.println("사용자 테이블 갱신 실패");
                                        alertMessages.append("사용자 테이블 갱신 실패\\n");
                                    }
                                }
                            } else {
                            	System.out.println("DB 저장 실패: " + originalName);
                                alertMessages.append("DB 저장 실패: ").append(originalName).append("\\n");
                            }
                        } else {
                        	System.out.println("FTP 업로드 실패: " + originalName);
                            alertMessages.append("FTP 업로드 실패: ").append(originalName).append("\\n");
                        }
                    }
                }
            }

            System.out.println("총 성공 파일 수: " + successCount);
            alertMessages.append("총 성공 파일 수: ").append(successCount);
            ftp.logout();
            // 파일명에 따른 오류 방지용 HTML 이스케이프
            String safeMessage = alertMessages.toString().replace("'", "\\'");
            out.println("<script>alert('" + safeMessage + "'); history.back();</script>");

        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("오류 발생: " + e.getMessage());
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
