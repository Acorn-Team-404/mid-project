package controller.post;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/post/")
@MultipartConfig(
		fileSizeThreshold = 1024*1024*10, //업로드 처리하기 위한 메모리 사이즈(10 Mega byte)
		maxFileSize = 1024*1024*50, //업로드되는 최대 파일 사이즈(50 Mega byte)
		maxRequestSize = 1024*1024*60 //이 요청의 최대 사이즈(60 Mega byte), 파일외의 다른 문자열도 전송되기 때문에
	)
public class PostServlet extends HttpServlet{
	
}
