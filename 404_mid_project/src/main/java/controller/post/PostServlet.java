package controller.post;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.post.CommentDao;
import model.post.CommentDto;
import model.post.PostDao;
import model.post.PostDto;

@WebServlet("*.post")
@MultipartConfig(
		fileSizeThreshold = 1024*1024*10, //업로드 처리하기 위한 메모리 사이즈(10 Mega byte)
		maxFileSize = 1024*1024*50, //업로드되는 최대 파일 사이즈(50 Mega byte)
		maxRequestSize = 1024*1024*60 //이 요청의 최대 사이즈(60 Mega byte), 파일외의 다른 문자열도 전송되기 때문에
	)
public class PostServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// 사용자 요청 path 추출
		String uri = req.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		PostDao dao = PostDao.getInstance();
				
		
		// 추출된 path 정보에 따라 요청을 분기 처리
		// 1. 게시글 리스트
		if(path.equals("/list.post")) {
			System.out.println("게시글 리스트");
			
			// 게시글 불러오기
			PostDto dto=new PostDto();
		    List<PostDto> list=dao.selectAll();
		    
		    // request 영역에 저장
		    req.setAttribute("list", list);
		    
		    // jsp로 포워딩
		    req.getRequestDispatcher("/post/list.jsp").forward(req, res);
		
		// 2. 게시글 상세보기
		}else if(path.equals("/view.post")) {
			System.out.println("게시글 상세보기");
			
			// 게시글 번호 파라미터 받기
			int num = Integer.parseInt(req.getParameter("num"));
						
			// 게시글 조회 - DB
			PostDto dto=dao.getByPostNum(num);
			
			
			
			/*
			 * //로그인 String userName=(String)Session.getAttribute("userName"); //만일 본인 글 자세히
			 * 보기가 아니면 조회수 1을 증가시킨다. if(!dto.getPostWriterNum().equals(userName)){
			 * postDao.getInstance().views(num); } //로그인했는지 여부 알아내기 boolean isLogin =
			 * userName == null ? false : true;
			 */
			 
			//댓글 목록을 DB에서 읽어오기
			List<CommentDto> commentList=CommentDao.getInstance().selectAll(num);
			
						
			// jsp
			req.setAttribute("post", dto);
			req.getRequestDispatcher("/post/view.jsp").forward(req, res);
					
		// 3. 게시글 입력수정폼	
		}else if(path.equals("/form.post")) {
			String numStr = req.getParameter("num");
			if(numStr != null) {
				int num= Integer.parseInt(numStr);
				PostDto dto= dao.getByPostNum(num);
				req.setAttribute("post", dto);
			}

			req.getRequestDispatcher("/post/form.jsp").forward(req, res);
			
		// 4. 게시글 업로드
		}else if(path.equals("/upload.post")) {
			System.out.println("게시글 업로드");
			PostDto dto=new PostDto(); 

			int num=dao.getSequence();
			dto.setPostNum(num);
			dto.setPostTitle(req.getParameter("title"));
			dto.setPostContent(req.getParameter("content"));
			dao.insert(dto);
			
			req.getRequestDispatcher("/post/list.jsp").forward(req, res);
			
					
		// 5. 게시글 업데이트
		}else if(path.equals("/update.post")) {
			System.out.println("게시글 업데이트");
			PostDto dto=new PostDto();
			dto.setPostNum(Integer.parseInt(req.getParameter("num")));
			dto.setPostTitle(req.getParameter("title"));
			dto.setPostContent(req.getParameter("content"));
			dao.update(dto);
			res.sendRedirect("view.post?num=" + dto.getPostNum());
			
			
			
		// 6. 게시글 삭제
		}else if(path.equals("/delete.post")) {
			System.out.println("게시글 삭제");
			int num = Integer.parseInt(req.getParameter("num"));
			dao.deleteByNum(num);
			res.sendRedirect("list.post");
		}
		
		
	}
}
