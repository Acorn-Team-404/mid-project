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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usersId = (String) req.getSession().getAttribute("usersId");
        
        System.out.println("세션의 loginId: " + usersId);

        if (usersId == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login-form.jsp");
            return;
        }

        UserDao userDao = UserDao.getInstance();
        UserDto user = userDao.getByUserId(usersId);
        
        System.out.println("userDto: " + user);

        req.setAttribute("user", user);
        req.getRequestDispatcher("/my-page/my-page.jsp").forward(req, resp);
    }
}
