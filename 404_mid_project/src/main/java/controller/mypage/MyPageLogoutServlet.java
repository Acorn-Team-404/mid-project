package controller.mypage;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 그저 마이페이지에 만들어 둔 로그아웃을 활성화 시키고 싶었던..
@WebServlet("/logout")
public class MyPageLogoutServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate(); // 세션 삭제
		resp.sendRedirect(req.getContextPath() + "/index.jsp");
	}
}
