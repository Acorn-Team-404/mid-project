package controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/user/signup")
public class SignupServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String usersId = request.getParameter("usersId");
        String usersName = request.getParameter("usersName");
        String usersPassword = request.getParameter("usersPassword");
        String usersCheckPassword = request.getParameter("usersCheckPassword");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String birth = request.getParameter("birth");
        String checkIdAction = request.getParameter("checkIdAction");
        
        String hashed = null;
        
        PrintWriter out = response.getWriter();

        // 기본 유효성 검사
        if(usersId == null || !usersId.matches("^[a-zA-Z0-9]{4,20}$")){
    		out.println("<script>alert('아이디 형식이 올바르지 않습니다. (아이디는 영문+숫자 4-20자 입니다.)'); history.back();</script>");
    		return;
    	}
    	
    	if(usersPassword == null || !usersPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{10,}$")){
    		out.println("<script>alert('비밀번호 형식이 올바르지 않습니다. (아이디는 영문+숫자 4-20자 입니다.)'); history.back();</script>");
    		return;
    	}
    	
    	if (usersPassword == null || usersCheckPassword == null || usersPassword.isBlank() || usersCheckPassword.isBlank()) {
    		out.println("<script>alert('비밀번호를 입력해주세요.'); history.back();</script>");
    		return;
    	}else if (!usersPassword.equals(usersCheckPassword)) {
    		out.println("<script>alert('비밀번호가 일치하지 않습니다.(비밀번호는 대문자, 소문자, 숫자, 튿수문자를 포함한 10자 이상입니다.)'); history.back();</script>");
    		return;
    	}else {//비밀번호가 일치하면 암호화
    		hashed = BCrypt.hashpw(usersPassword, BCrypt.gensalt());
    	}
    	
    	boolean isSuccess = false;
    	
    	if (!"on".equals(checkIdAction)) { //아이디 중복확인을 누르지 않음
    		out.println("<script>alert('아이디 중복확인을 해주세요.'); history.back();</script>");
    		return;
    	}


        UserDto dto = new UserDto();
        dto.setUsersId(usersId);
        dto.setUsersName(usersName);
        dto.setUsersPw(hashed);
        dto.setEmail(email);
        dto.setPhone(phone);
        dto.setBirth(birth);

        isSuccess = UserDao.getInstance().insert(dto);

        // 결과에 따라 응답 처리
        if (isSuccess) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?usersId=" + usersId);
            out.println("<script>alert('회원가입 완료'); history.back();</script>");
    		return;
        } else {
            alertAndBack(response, "회원가입 실패! 다시 시도해주세요.");
        }
    }

    private void alertAndBack(HttpServletResponse response, String msg) throws IOException {
        response.getWriter().println("<script>alert('" + msg + "'); history.back();</script>");
    }
		
}

