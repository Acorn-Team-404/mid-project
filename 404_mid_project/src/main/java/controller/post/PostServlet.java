package controller.post;


import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.image.ImageDao;
import model.post.CommentDao;
import model.post.CommentDto;
import model.post.PostDao;
import model.post.PostDto;


@WebServlet("*.post")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 업로드 처리하기 위한 메모리 사이즈(10 Mega byte)
		maxFileSize = 1024 * 1024 * 50, // 업로드되는 최대 파일 사이즈(50 Mega byte)
		maxRequestSize = 1024 * 1024 * 60 // 이 요청의 최대 사이즈(60 Mega byte), 파일외의 다른 문자열도 전송되기 때문에
)
public class PostServlet extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		// 사용자 요청 path 추출
		String uri = req.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		PostDao dao = PostDao.getInstance();

		// 추출된 path 정보에 따라 요청을 분기 처리
		// 1. 게시글 리스트
		if (path.equals("/list.post")) {
			System.out.println("게시글 리스트");

			// 게시글 불러오기
			PostDto dto = new PostDto();
			List<PostDto> list = dao.selectAll();

			// request 영역에 저장
			req.setAttribute("list", list);

			// jsp로 포워딩
			req.getRequestDispatcher("/post/list.jsp").forward(req, res);

		// 2. 게시글 상세보기
		} else if (path.equals("/view.post")) {
			System.out.println("게시글 상세보기");

			// 게시글 번호 파라미터 받기
			int num = Integer.parseInt(req.getParameter("num"));

			// 게시글 조회 - DB
			PostDto dto = dao.getByPostNum(num);
			Long usersNum = (Long) req.getSession().getAttribute("usersNum");
			Long postWriterNum = dto.getPostWriterNum();
			if(usersNum != null && postWriterNum != null && !postWriterNum.equals(usersNum)){
			PostDao.getInstance().addViews(num);
			
			} //로그인했는지 여부 알아내기 
			boolean isLogin = usersNum != null;
			req.setAttribute("isLogin", isLogin);

			// 댓글 목록을 DB에서 읽어오기
			List<CommentDto> commentList = CommentDao.getInstance().selectAll(num);

			req.setAttribute("dto", dto);
			req.setAttribute("commentList", commentList);

			req.getRequestDispatcher("/post/view.jsp").forward(req, res);

		// 3. 게시글 입력수정폼
		} else if (path.equals("/form.post")) {
			int num = Integer.parseInt(req.getParameter("num"));
			
			if (num != 0) {
				
				PostDto dto = dao.getInstance().getByPostNum(num);
				req.setAttribute("post", dto);
				req.getRequestDispatcher("/post/form.jsp").forward(req, res);
			}else {
				res.sendRedirect("/view.post?num=" + num);
			}
			
			
		// 4. 게시글 업로드
		} else if (path.equals("/upload.post")) {
			System.out.println("게시글 업로드");
			 // 0) 로그인 체크
		    HttpSession session = req.getSession(false);
		    Object userNumObj = (session != null) ? session.getAttribute("usersNum") : null;
		    if (userNumObj == null) {
		        res.setContentType("text/html; charset=UTF-8");
		        res.getWriter().println("<script>alert('로그인이 필요합니다.'); location.href='"
		                + req.getContextPath() + "/user/login.jsp';</script>");
		        return;
		    }
		    long writerNum = Long.parseLong(userNumObj.toString());
		    String writerId = (String)(session != null ? session.getAttribute("usersId") : null);

		    // 1) 파라미터 읽기 (멀티파트에서도 getParameter 가능)
		    String title   = req.getParameter("title");
		    String content = req.getParameter("content");

		    if (title == null || title.trim().isEmpty() ||
		        content == null || content.trim().isEmpty()) {
		        res.setContentType("text/html; charset=UTF-8");
		        res.getWriter().println("<script>alert('제목과 내용을 입력하세요.'); history.back();</script>");
		        return;
		    }

		    int postType = 0;
		    try { postType = Integer.parseInt(req.getParameter("post_type")); } catch (Exception ignore) {}
		    int postStayNum = 0;
		    try { postStayNum = Integer.parseInt(req.getParameter("post_stay_num")); } catch (Exception ignore) {}

		    // 2) 글번호 선발급 → DTO 구성 → 저장
		    int postNum = PostDao.getInstance().getSequence();

		    PostDto dto = new PostDto();
		    dto.setPostNum(postNum);               // 선발급 PK
		    dto.setPostWriterNum(writerNum);       // 작성자 번호
		    dto.setPostWriterId(writerId);         // (선택) 작성자 아이디
		    dto.setPostTitle(title);               // 제목
		    dto.setPostContent(content);           // 본문(HTML)
		    dto.setPostType(postType);             // 타입
		    dto.setPostStayNum(postStayNum);       // 연관 숙소

		    boolean ok = PostDao.getInstance().insert(dto);
		    if (!ok) {
		        res.setContentType("text/html; charset=UTF-8");
		        res.getWriter().println("<script>alert('게시글 저장 실패'); history.back();</script>");
		        return;
		    }

		    // 3) 이미지 업로드 include
		    //    - form의 파일 input name="images" 와 partName=images 가 일치해야 한다.
		    req.getRequestDispatcher(
		        "/post.img?target_type=post&target_id=" + postNum + "&partName=images"
		    ).include(req, res);

		    // 4) 완료 이동 (상세 보기)
		    res.sendRedirect(req.getContextPath() + "/post/view.jsp?num=" + postNum);
			
			
			
			
		// 5. 게시글 업데이트
		} else if (path.equals("/update.post")) {
			int num = Integer.parseInt(req.getParameter("num"));
			
			String title = req.getParameter("title");
			if (title == null || title.trim().isEmpty()) {
			    System.out.println("제목이 비어있습니다."); // 디버깅 로그
			    req.setAttribute("error", "제목을 입력해주세요.");
			    req.getRequestDispatcher("/post/edit-form.jsp").forward(req, res);
			    return;
			}
			
			PostDto dto = new PostDto();
			dto.setPostNum(num);
			dto.setPostTitle(title);
			dto.setPostContent(req.getParameter("content"));
			
			boolean success = dao.update(dto);
			if (success) {
				res.sendRedirect("view.post?num=" + num);
			} else {
				req.setAttribute("error", "게시글 저장 실패!");
				req.getRequestDispatcher("/post/edit-form.jsp").forward(req, res);
			}

		// 6. 게시글 삭제
		} else if (path.equals("/delete.post")) {
			System.out.println("게시글 삭제");
			int num = Integer.parseInt(req.getParameter("num"));
			dao.deleteByNum(num);
			res.sendRedirect("list.post");
		}
		

	}


	

}
