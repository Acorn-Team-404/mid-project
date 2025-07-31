package controller.book;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.book.BookDao;

@WebServlet("/booking/testSeq")
public class BookSeqTestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookDao dao = BookDao.getInstance();
        String bookNum = dao.generateBookNum();
        
        System.out.println("생성된 예약번호: " + bookNum);  // 서버 콘솔에 찍힘
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<h1>생성된 예약번호: " + bookNum + "</h1>");
    }
}
