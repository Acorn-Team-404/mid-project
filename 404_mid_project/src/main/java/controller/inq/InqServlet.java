package controller.inq;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.inq.InquiryDao;
import model.inq.InquiryDto;
import model.noti.NotificationDao;
import model.noti.NotificationDto;

@WebServlet("*.inq")
public class InqServlet extends HttpServlet{ 
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 사용자 요청 path 추출
		String uri = req.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		
		if(path.equals("/saveInquiry.inq")) {
			HttpSession session = req.getSession(false);
	        if (session == null || session.getAttribute("usersNum") == null) {
	            resp.sendRedirect(req.getContextPath() + "/user/login-form.jsp");
	            return;
	        }
			long userNum=(Long)session.getAttribute("usersNum");
			String stayNumStr = req.getParameter("stayNum");
			long stayNum = 0;

			if (stayNumStr != null && !stayNumStr.trim().isEmpty()) {
			    stayNum = Long.parseLong(stayNumStr.trim());
			}
			String inqType=req.getParameter("inqType");
			String title=req.getParameter("title");
			String content=req.getParameter("content");
			long inqNum = InquiryDao.getInstance().getSequence();
			
			InquiryDto dto=new InquiryDto();
			dto.setNum(inqNum);
			dto.setStayNum(stayNum==0 ? null : stayNum);
			dto.setUsersNum(userNum);
			dto.setTitle(title);
			dto.setContent(content);
			dto.setType(inqType);
			
			boolean inqIsSuccess = InquiryDao.getInstance().insert(dto);
			resp.sendRedirect(req.getContextPath()+"/inquiry/list.jsp");
			
			
			// 문의 답변 시 사용자에게 답변 알림 INSERT
			if(inqIsSuccess) {
				long notiRecipientNum = userNum;
				long notiSenderNum = 0;
				int notiTypeCode = 40;
				int notiTargetTypeCode = 40;
				String notiTargetNum = String.valueOf(inqNum);
				String notiMessage = content;
				
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
				} else {
					System.out.println("알림 데이터 저장 실패");
				}
			}
			
			
		}
	}
}
