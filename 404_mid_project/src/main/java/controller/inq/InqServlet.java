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
			
			InquiryDto dto=new InquiryDto();
			dto.setStayNum(stayNum);
			dto.setUsersNum(userNum);
			dto.setTitle(title);
			dto.setContent(content);
			dto.setType(inqType);
			
			InquiryDao.getInstance().insert(dto);
			resp.sendRedirect(req.getContextPath()+"/inquiry/list.jsp");
		}
	}
}
