package controller.noti;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.noti.NotificationDao;
import model.noti.NotificationDto;


@WebServlet("")
public class NotiCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 사용자 요청 path 추출
		String uri = req.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		
		boolean isCommentSuccess = true; // 댓글 서블릿과 합치기 전 임시 변수
		
		if(isCommentSuccess) {
			
			long notiRecipientNum = Long.valueOf(req.getParameter("notiRecipientNum"));
			long notiSenderNum = Long.valueOf(req.getParameter("notiSenderNum"));
			int notiTypeCode = Integer.parseInt(req.getParameter("notiTypeCode"));
			int notiTargetTypeCode = Integer.parseInt(req.getParameter("notiTargetTypeCode"));
			String notiTargetNum = req.getParameter("notiTargetNum");
			String notiMessage = req.getParameter("notiMessage");
			
	
			NotificationDto notiDto = new NotificationDto();
			
			notiDto.setNotiRecipientNum(notiRecipientNum);
			notiDto.setNotiSenderNum(notiSenderNum);
			notiDto.setNotiTypeCode(notiTypeCode);
			notiDto.setNotiTargetTypeCode(notiTargetTypeCode);
			notiDto.setNotiTargetNum(notiTargetNum);
			notiDto.setNotiMessage(notiMessage);
			
			boolean isNotiSuccess = NotificationDao.getInstance().notiInsert(notiDto);
			
			if(isNotiSuccess) {
				System.out.println("알림 데이터 저장 성공");
				resp.sendRedirect("리디렉션 경로");
			} else {
				System.out.println("알림 데이터 저장 실패");
				resp.sendRedirect("리디렉션 경로");
			}
			
		}
	}
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 루트 요청을 index.jsp로 넘긴다.
        req.getRequestDispatcher("/index.jsp").forward(req, resp); // 서버 내부 포워드 한다.
    }
}




