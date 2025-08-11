package controller.book;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.simple.JSONArray;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.book.BookDao;
import model.book.BookDto;

@WebServlet("/getDisabledDates")
public class DisabledDatesServlet extends HttpServlet{
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		long bookRoomNum = Long.parseLong(req.getParameter("bookRoomNum"));
		List<String> disabledDates = BookDao.getInstance().getDisabledDates(bookRoomNum); 
		
		res.setContentType("application/json; charset=UTF-8");
		
		//json-simple 라이브러리로 json array로 받음
		JSONArray jsonArray = new JSONArray();
		jsonArray.addAll(disabledDates);
		
		// 클라우언트에서 /getDisabled 호출하면 ["2000-00-00","2001-00-00"] 형태로 받게 된다.
		try (PrintWriter out = res.getWriter()){
			out.print(jsonArray.toJSONString());
		}
	}
}
