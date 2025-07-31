package controller.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.user.UserDao;

@WebServlet("/user/check-id")
public class CheckIdServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/plain; charset=UTF-8");
	    
	    String usersId = request.getParameter("usersId");
	   
	    if (usersId == null || usersId.trim().isEmpty()) {
            response.getWriter().print("empty");
            return;
        }
	    

        boolean isIdExist = UserDao.getInstance().isIdExist(usersId);
        response.getWriter().print(isIdExist ? "exist" : "ok");
	}


}
