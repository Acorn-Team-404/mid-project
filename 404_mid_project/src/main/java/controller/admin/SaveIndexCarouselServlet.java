package controller.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.admin.IndexDao;

@WebServlet("/saveCarousel")
@MultipartConfig
public class SaveIndexCarouselServlet extends HttpServlet{
	@Override
	  protected void doPost(HttpServletRequest req, HttpServletResponse res)
	      throws ServletException, IOException {

	    // 0) 로그인 체크를 수행한다.
	    HttpSession session = req.getSession(false);
	    Object userNumObj = (session != null) ? session.getAttribute("usersNum") : null;
	    if (userNumObj == null) {
	      res.setContentType("text/html; charset=UTF-8");
	      res.getWriter().println("<script>alert('로그인이 필요합니다.'); location.href='"
	          + req.getContextPath() + "/user/login.jsp';</script>");
	      return;
	    }

	    req.setCharacterEncoding("UTF-8"); // 요청 바디를 UTF-8로 해석한다.

	    // 1) 폼 파라미터를 읽는다.
	    //    target_id 는 index_carousel.ic_index_num 로 사용한다.
	    String targetIdStr = req.getParameter("target_id");
	    String groupName   = req.getParameter("group_name");

	    if (targetIdStr == null || targetIdStr.trim().isEmpty()
	        || groupName == null || groupName.trim().isEmpty()) {
	      res.setContentType("text/html; charset=UTF-8");
	      res.getWriter().println("<script>alert('그룹 번호와 그룹 이름을 입력하세요.'); history.back();</script>");
	      return;
	    }

	    int icIndexNum;
	    try {
	      icIndexNum = Integer.parseInt(targetIdStr.trim()); // 정수로 변환한다.
	    } catch (NumberFormatException e) {
	      res.setContentType("text/html; charset=UTF-8");
	      res.getWriter().println("<script>alert('그룹 번호가 올바르지 않습니다.'); history.back();</script>");
	      return;
	    }

	    // 2) 캐러셀 그룹 메타를 DB에 저장한다.
	    //    동일 ic_index_num에 여러 줄을 쌓아두고, 그룹 이름으로 식별/관리하는 용도라면 insert.
	    boolean metaSaved = IndexDao.getInstance().setIndexCarouselGroup(groupName, icIndexNum);
	    if (!metaSaved) {
	      res.setContentType("text/html; charset=UTF-8");
	      res.getWriter().println("<script>alert('캐러셀 그룹 저장 실패'); history.back();</script>");
	      return;
	    }

	    // 3) 이미지 업로드를 include한다.
	    //    - ImageUpload 서블릿은 *.img 로 매핑되어 있다.
	    //    - target_type=index 로 저장. (index 페이지 캐러셀 이미지라는 의미)
	    //    - partName=uploadFile (폼의 input name 과 반드시 동일해야 한다)
	    req.getRequestDispatcher(
	        "/index.img?target_type=index"
	      + "&target_id=" + icIndexNum
	      + "&partName=uploadFile"
	    ).include(req, res);

	    // (선택) 4) 방금 올린 그룹을 곧바로 현재 index 캐러셀로 반영하고 싶으면 주석 해제한다.
	    // IndexDao.getInstance().setIndexCarouselNum(icIndexNum);

	    // 5) 완료 후 이동한다. (관리 페이지나 미리보기 페이지 등)
	    res.sendRedirect(req.getContextPath() + "/admin/index-carousel-list.jsp");
	  }

	  @Override
	  protected void doGet(HttpServletRequest req, HttpServletResponse res)
	      throws ServletException, IOException {
	    // 보통 업로드는 POST만 허용한다.
	    res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	  }
}
