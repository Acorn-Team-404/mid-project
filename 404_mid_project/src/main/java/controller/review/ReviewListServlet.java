package controller.review;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.review.ReviewDao;
import model.review.ReviewDto;

@WebServlet("/review/list")
public class ReviewListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stayNumStr = req.getParameter("stayNum");
        if (stayNumStr == null || stayNumStr.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "stayNum 누락");
            return;
        }

        long stayNum = Long.parseLong(stayNumStr);
        List<ReviewDto> reviews = ReviewDao.getInstance().getByStayNum(stayNum);
        req.setAttribute("reviews", reviews);
        req.setAttribute("stayNum", stayNum);
        req.getRequestDispatcher("/review/review-list.jsp").forward(req, resp);
    }
}
