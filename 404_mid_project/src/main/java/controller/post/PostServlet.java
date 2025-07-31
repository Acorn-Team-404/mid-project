package controller.post;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.post.postDao;
import model.post.postDto;

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
		
		// 추출된 path 정보에 따라 요청을 분기 처리
		// 1. 게시글 리스트
		if(path.equals("/list.post")) {
			System.out.println("게시글 리스트");
			
			// 게시글 불러오기
			postDto dto=new postDto();
		    List<postDto> list=new postDao().selectAll();
		    
		    // request 영역에 저장
		    req.setAttribute("postList", list);
		    
		    // jsp로 포워딩
		    req.getRequestDispatcher("/post/list.jsp").forward(req, res);
		
		// 2. 게시글 1개 조회
		}else if(path.equals("/get.post")) {
			System.out.println("게시글 상세보기");
			
			// 게시글 번호 파라미터 받기
			String numStr = req.getParameter("num");
			int num=0;
			try {
		    	num = Integer.parseInt(numStr);
        	} catch (NumberFormatException e) {
	            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post number");
	            return;
	        }
			
			// 게시글 조회 - DB
			postDto dto=new postDao().getByPostNum(num);
			
			// 게시글 존재 X - 404
			if (dto == null) {
	            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
	            return;
	        }
			
			// jsp
			req.setAttribute("post", dto);
			req.getRequestDispatcher("/post/view.jsp").forward(req, res);
			
		
		// 3. 게시글 업로드	
		}else if(path.equals("/upload.post")) {
			System.out.println("게시글 업로드");
			
			
			// 요청 전달자 객체 얻어내기
			RequestDispatcher rd=req.getRequestDispatcher("/list.jsp");
			// 응답을 위임하기
			rd.forward(req, res);
			
		// 4. 게시글 수정
		}else if(path.equals("/update.post")) {
			System.out.println("게시글 수정");
			
		// 5. 게시글 삭제
		}else if(path.equals("/delete.post")) {
			System.out.println("게시글 삭제");
			
		}
		
		
	}
}
