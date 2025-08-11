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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // ✅ GET도 동일 로직으로 처리하고, 절대 응답에 쓰지 않는다.
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // ❌ include 환경에서 절대 응답에 쓰지 않는다. (contentType/PrintWriter 금지)

        // 1) target_type
        String targetType = req.getParameter("target_type");
        if (targetType == null || targetType.isEmpty()) {
            String servletPath = req.getServletPath(); // "/post.img"
            targetType = servletPath.substring(1).replace(".img", ""); // "post"
        }

        // 2) target_id
        String targetIdStr = req.getParameter("target_id");
        int targetId;
        try {
            targetId = Integer.parseInt(targetIdStr);
        } catch (Exception e) {
            System.out.println("[ImageUpload] invalid target_id: " + targetIdStr);
            return; // ✅ 무출력 종료
        }

        // 3) partName 기본값
        String partName = req.getParameter("partName");
        if (partName == null || partName.isEmpty()) partName = "uploadFile";

        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(HOST, PORT);
            ftp.setControlEncoding("UTF-8");
            ftp.login(USER, PASSWORD);
            ftp.sendCommand("OPTS UTF8 ON");
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            int successCount = 0;
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                if (!partName.equals(part.getName()) || part.getSize() == 0) continue;

                // ✅ 이미지 MIME만 허용한다.
                String ctype = part.getContentType();
                if (ctype == null || !ctype.startsWith("image/")) {
                    System.out.println("[ImageUpload] skip non-image: " + part.getSubmittedFileName());
                    continue;
                }

                String originalName = part.getSubmittedFileName();
                if (originalName == null || originalName.isEmpty()) {
                    System.out.println("[ImageUpload] empty filename part skipped");
                    continue;
                }

                String extension = "";
                int dot = originalName.lastIndexOf('.');
                if (dot != -1) extension = originalName.substring(dot);

                String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
                String uuid = UUID.randomUUID().toString();
                String savedName = today + "_" + uuid + extension;

                // FTP는 ISO-8859-1로 파일명을 맞춘다.
                String ftpFileName = new String(savedName.getBytes("UTF-8"), "ISO-8859-1");

                try (InputStream in = part.getInputStream()) {
                    boolean uploaded = ftp.storeFile(REMOTE_DIR + ftpFileName, in);
                    if (uploaded) {
                        boolean inserted = ImageDao.getInstance()
                                .insertAutoSort(originalName, savedName, targetType, targetId);
                        if (inserted) {
                            successCount++;
                            System.out.println("[ImageUpload] uploaded: " + originalName + " -> " + savedName);
                            if ("profile".equals(targetType)) {
                                boolean ok = ImageDao.getInstance()
                                        .updateUserProfileImage(targetId, savedName);
                                System.out.println("[ImageUpload] profile update: " + ok);
                            }
                        } else {
                            System.out.println("[ImageUpload] DB insert failed: " + originalName);
                        }
                    } else {
                        System.out.println("[ImageUpload] FTP upload failed: " + originalName);
                    }
                }
            }
            System.out.println("[ImageUpload] success files: " + successCount);
            ftp.logout();

        } catch (Exception e) {
            // ❗ 절대 resp에 쓰지 않는다.
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try { ftp.disconnect(); } catch (IOException ignore) {}
            }
        }
        // ✅ 여기서도 끝. 어떤 출력도 하지 않는다.
    }
}
