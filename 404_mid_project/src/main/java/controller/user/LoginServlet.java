package controller.user;

import java.io.IOException;
import java.net.URLEncoder;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.user.UserDao;
import model.user.UserDto;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String usersId = request.getParameter("usersId");
		String usersPassword = request.getParameter("usersPassword");
		String usersNum = request.getParameter("usersNum");
		String url = request.getParameter("url");
		
		if(url==null){
			//로그인 후에 인덱스 페이지로 갈수 있도록 한다. 
			String cPath=request.getContextPath();
			url=cPath+"/index.jsp";
		}	
	
		boolean isValid = false;
		
		UserDto dto = UserDao.getInstance().getByUserId(usersId);
		
		if(dto != null && dto.getUsersPw() != null){
			isValid = BCrypt.checkpw(usersPassword, dto.getUsersPw());
		}
		
		if(isValid){
			HttpSession session = request.getSession();
			session.setAttribute("usersId", usersId);
			session.setAttribute("usersNum", usersNum);
			session.setMaxInactiveInterval(60*60);
			System.out.println("세션 아이디: " + session.getId());
		    System.out.println("세션에 저장된 usersId: " + session.getAttribute("usersId"));
			
			// 로그인 성공 → 리다이렉트
			response.sendRedirect(url);
			System.out.println("로그인 성공");
		} else {
			// 로그인 실패 → 다시 login-form.jsp 로
			String encodedUrl = URLEncoder.encode(url, "UTF-8");
			response.sendRedirect(request.getContextPath() + "/user/login-form.jsp?url=" + encodedUrl + "&error=1");
			System.out.println("로그인 실패");
		}
	}
}
