package controller.mypage;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.user.UserDao;
import model.user.UserDto;

@WebServlet("/update-phone")
public class MyPageUpdatePhoneServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 로그인 세션 ID 가져오기
		String usersId = (String) req.getSession().getAttribute("usersId");
		
		// 새 전화번호 받아오기
		String newPhone = req.getParameter("phone");
		
		//수정 정보 담을 DTO 만들기
		UserDto user = new UserDto();
		user.setUsersId(usersId);
		user.setPhone(newPhone);
		
		// DB 업데이트
		boolean result = UserDao.getInstance().updatePhone(user);
		
		resp.sendRedirect(req.getContextPath() +"/my-page");
	}
}
