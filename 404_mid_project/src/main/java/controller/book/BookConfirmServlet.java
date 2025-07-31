package controller.book;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/booking/confirm")
public class BookConfirmServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 먼저 데이터 없이 JSP 로만 포워딩 되도록
        request.getRequestDispatcher("/booking/confirm.jsp").forward(request, response);
	}
}
