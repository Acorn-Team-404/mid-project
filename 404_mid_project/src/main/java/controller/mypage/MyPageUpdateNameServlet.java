package controller.mypage;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.user.UserDao;
import model.user.UserDto;

@WebServlet("/update-name")
public class MyPageUpdateNameServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 세션에 로그인 된 사용자 ID 가져오기
		String usersId = (String) req.getSession().getAttribute("usersId");
		
		
		// 새 이름 값 받기
		String newName = req.getParameter("usersName");
		
		// dto 값 담기
		UserDto user = new UserDto();
		System.out.println("변경 요청된 이름: " + newName);
		user.setUsersId(usersId);
		user.setUsersName(newName);
		
		boolean result = UserDao.getInstance().updateName(user);
		
		
		resp.setContentType("text/html;charset=UTF-8"); // 왜인지 이거 안넣으면 글자가 깨짐
		PrintWriter out = resp.getWriter();
		out.println("<script>");
		out.println("alert('성공적으로 처리되었습니다.');");
		out.println("location.href='" + req.getContextPath() + "/my-page';");
		out.println("</script>");
		out.close();
		// redirect my-page로
	  resp.sendRedirect(req.getContextPath() + "/my-page"); 

	}
}
