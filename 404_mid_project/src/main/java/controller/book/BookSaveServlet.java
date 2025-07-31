package controller.book;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.book.BookDto;

@WebServlet("/booking/submit")
public class BookSaveServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 세션 정보가 없으면 새로 생성 X
		HttpSession session = req.getSession(false);
		
		/*UserDto user = (UserDto)session.getAttribute("user");
		
		String name = user.getName();
		String email = user.getEmail();
		String phone = user.getPhone();*/
		
		
		
	}
}
