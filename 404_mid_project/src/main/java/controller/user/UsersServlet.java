package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.user.UserDao;
import model.user.UserDto;

@WebServlet("*.user")
public class UsersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 사용자 요청 path 추출
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));

		
		//비밀번호 재설정
		if(path.equals("/resetPassword.user")) {
			System.out.println("시작");
			
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();

			// 파라미터 추출
			
			String usersId = (String)request.getSession().getAttribute("resetPasswordId");
			System.out.println(usersId);
			String newUsersPassword = request.getParameter("newUsersPassword");
			String newUsersCheckPassword = request.getParameter("newUsersCheckPassword");


			// 비밀번호 유효성 검사
			if (newUsersPassword == null || !newUsersPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{10,}$")) {
				alertAndBack(out, "비밀번호 형식이 올바르지 않습니다. (대소문자+숫자+특수문자 포함 10자 이상)");
				return;
			}

			// 비밀번호 확인 일치 여부
			if (newUsersCheckPassword == null || !newUsersPassword.equals(newUsersCheckPassword)) {
				alertAndBack(out, "비밀번호가 일치하지 않습니다.");
				return;
			}



			// 비밀번호 암호화
			String hashed = BCrypt.hashpw(newUsersPassword, BCrypt.gensalt());

			// DTO 생성
			UserDto dto = new UserDto();
			dto.setUsersPw(hashed);

			// DB에 insert
			boolean isSuccess = UserDao.getInstance().updateUserPassword(usersId, hashed);


			if (isSuccess) {
				request.getSession().removeAttribute("resetPasswordId");
				response.sendRedirect(request.getContextPath() + "/user/login-form.jsp");
				return;
			} else {
				alertAndBack(out, "비밀번호 변경 실패! 다시 시도해주세요.");
			}			
			
		}
		
		
		//비밀번호 찾기
		if(path.equals("/findPassword.user")) {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			
			String usersName = request.getParameter("usersName");
			String usersId = request.getParameter("usersId");
			String usersEmail = request.getParameter("usersEmail");
			
			boolean isVaild = UserDao.getInstance().findPassword(usersName, usersId, usersEmail);
			
			if (isVaild) {
				//비밀번호 재설정
				request.getSession().setAttribute("resetPasswordId", usersId);
				response.sendRedirect(request.getContextPath() + "/user/reset-password-form.jsp");

			}else {
				response.setContentType("text/html; charset=UTF-8");
		        PrintWriter out = response.getWriter();
		        out.println("<script>alert('입력한 정보와 일치하는 계정이 없습니다.'); history.back();</script>");
		        out.close();
			}		
		}
		
		
		//아이디 찾기
		if(path.equals("/findId.user")) {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			PrintWriter out = response.getWriter();
			
			String usersName = request.getParameter("usersName");
			String email = request.getParameter("email");
			
			String findId = UserDao.getInstance().findId(usersName, email);
			
			if(findId == null) {
				out.println("<script>alert('일치하는 정보의 아이디가 존재하지 않습니다. '); history.back();</script>");
			    return;	
			}else{
				out.print(usersName + " 님의 아이디는 " + findId + " 입니다.");
			}
		}
		
		
		
		//login.do 요청처리,로그인
		if(path.equals("/login.user")) {
			
			PrintWriter out = response.getWriter();

			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			String usersId = request.getParameter("usersId");
			String usersPassword = request.getParameter("usersPassword");
			String usersNum = request.getParameter("usersNum");
			String url = request.getParameter("url");
			
			if(url==null){
				//로그인 후에 인덱스 페이지로 갈수 있도록 한다. 
				String cPath=request.getContextPath();
				url=cPath+"/index.jsp";
			}	
		
			boolean isValid = false;
			
			UserDto dto = UserDao.getInstance().getByUserId(usersId);
			
			if(dto != null && dto.getUsersPw() != null){
				isValid = BCrypt.checkpw(usersPassword, dto.getUsersPw());
			}
			
			if(isValid){
				HttpSession session = request.getSession();
				session.setAttribute("usersId", dto.getUsersId());
				session.setAttribute("usersNum", dto.getUsersNum());
				session.setMaxInactiveInterval(60*60);
				
				// 로그인 성공 → 리다이렉트
				response.sendRedirect(url);
				System.out.println("로그인 성공");
			} else {
				// 로그인 실패 → 다시 login-form.jsp 로
				
				String encodedUrl = URLEncoder.encode(url, "UTF-8");
				out.println("<script>alert('입력한 정보와 일치하는 계정이 없습니다.'); history.back();</script>");
				//response.sendRedirect(request.getContextPath() + "/user/login-form.jsp?url=" + encodedUrl + "&error=1");
				System.out.println("로그인 실패");
			    
			}
		}
		
		//checkId.do 요청처리, 아이디 중복 확인
		if(path.equals("/checkId.user")) {
			
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
		
		
		// /signup.do 요청 처리, 회원가입
		if (path.equals("/signup.user")) {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();

			// 파라미터 추출
			String usersId = request.getParameter("usersId");
			String usersName = request.getParameter("usersName");
			String usersPassword = request.getParameter("usersPassword");
			String usersCheckPassword = request.getParameter("usersCheckPassword");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String birth = request.getParameter("birth");
			String checkIdAction = request.getParameter("checkIdAction");

			// 아이디 유효성 검사
			if (usersId == null || !usersId.matches("^[a-zA-Z0-9]{4,20}$")) {
				alertAndBack(out, "아이디 형식이 올바르지 않습니다. (영문+숫자 4~20자)");
				return;
			}

			// 비밀번호 유효성 검사
			if (usersPassword == null || !usersPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{10,}$")) {
				alertAndBack(out, "비밀번호 형식이 올바르지 않습니다. (대소문자+숫자+특수문자 포함 10자 이상)");
				return;
			}

			// 비밀번호 확인 일치 여부
			if (usersCheckPassword == null || !usersPassword.equals(usersCheckPassword)) {
				alertAndBack(out, "비밀번호가 일치하지 않습니다.");
				return;
			}

			// 아이디 중복확인 여부
			if (!"on".equals(checkIdAction)) {
				alertAndBack(out, "아이디 중복확인을 해주세요.");
				return;
			}

			// 비밀번호 암호화
			String hashed = BCrypt.hashpw(usersPassword, BCrypt.gensalt());

			// DTO 생성
			UserDto dto = new UserDto();
			dto.setUsersId(usersId);
			dto.setUsersName(usersName);
			dto.setUsersPw(hashed);
			dto.setUsersEmail(email);
			dto.setUsersPhone(phone);
			dto.setUsersBirth(birth);

			// DB에 insert
			boolean isSuccess = UserDao.getInstance().insert(dto);

			if (isSuccess) {
				out.println("<script>alert('회원가입이 완료되었습니다.'); location.href='" + request.getContextPath() + "/index.jsp';</script>");
			} else {
				alertAndBack(out, "회원가입 실패! 다시 시도해주세요.");
			}
			
		}///signup.do 끝부분
	}

	// 자바스크립트 alert 출력 후 뒤로가기, alertAndBack 함수를 이용해서 메세지 alert로 
	private void alertAndBack(PrintWriter out, String msg) {
		out.println("<script>alert('" + msg + "'); history.back();</script>");
	}
}
