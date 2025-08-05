package controller.book;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.book.BookDao;
import model.book.BookDto;
import model.pay.PaymentDto;
import oracle.sql.DATE;

@WebServlet("/booking/confirm")
public class BookConfirmServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String bookNum = req.getParameter("bookNum");

        BookDao dao = BookDao.getInstance();
        
        try {
            Map<String, Object> data = dao.getBookDetails(bookNum);

            req.setAttribute("users", data.get("users"));
            req.setAttribute("stay", data.get("stay"));
            req.setAttribute("book", data.get("book"));
            req.setAttribute("room", data.get("room"));
            req.setAttribute("payment", data.get("payment"));            

            // 날짜 포맷
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH시 mm분 ss초");

            // 체크인, 체크아웃 날짜 출력 설정
            BookDto bDto = (BookDto) data.get("book");
            String checkInDateOnly = bDto.getBookCheckIn().substring(0, 10);
            String checkOutDateOnly = bDto.getBookCheckOut().substring(0, 10);
            Date checkInDate = Date.valueOf(checkInDateOnly);
            Date checkOutDate = Date.valueOf(checkOutDateOnly);

            String checkInFormat = dateFormat.format(checkInDate);
            String checkOutFormat = dateFormat.format(checkOutDate);

            PaymentDto pDto = (PaymentDto) data.get("payment");
            String paidAtDate = dateFormat.format(pDto.getPayPaidAt());
            String paidAtTime = timeFormat.format(pDto.getPayPaidAt());   

            // jsp 전달
            req.setAttribute("checkInFormat", checkInFormat);
            req.setAttribute("checkOutFormat", checkOutFormat);
            req.setAttribute("paidAtDate", paidAtDate);
            req.setAttribute("paidAtTime", paidAtTime);

            req.getRequestDispatcher("/booking/booking-confirm.jsp").forward(req, res);

        } catch (SQLException e) {
            e.printStackTrace(); // 로그 찍기
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "예약 내역 조회 중 오류 발생");
        }
    }
}

