package controller.mypage;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.user.UserDao;
import model.user.UserDto;

@WebServlet("/my-page")
public class MyPageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userId = (String) req.getSession().getAttribute("loginId");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UserDao userDao = UserDao.getInstance();
        UserDto user = userDao.getUserById(userId);

        req.setAttribute("user", user);
        req.getRequestDispatcher("/my-page/my-page.jsp").forward(req, resp);
    }
}
