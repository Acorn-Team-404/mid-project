package controller.tester;

import java.io.IOException;
import java.sql.Connection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.util.DBConnector;

@WebServlet("/dbtest")
public class DBTestServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
        try {
            // DbcpBean을 통해 커넥션 얻기
            conn = DBConnector.getConn();

            if (conn != null) {
                resp.getWriter().println("DBConnection is working.");
            } else {
                resp.getWriter().println("DBConnection is NOT working.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("error: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
	}
}
