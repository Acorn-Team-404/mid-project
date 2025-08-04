<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
  // imageAction 값이 지정되지 않았다면 기본값 설정
  String imageAction = request.getParameter("imageAction");
  if (imageAction == null || imageAction.trim().isEmpty()) {
    imageAction = "none";
  }
%>

<form id="uploadForm" method="post" action="<%= request.getContextPath() %>/<%= imageAction %>.img" enctype="multipart/form-data">
	<div class="mb-3">
		<strong>업로드 Target_type: <%=imageAction %></strong>
	</div>
  <div class="mb-3">
    <label class="form-label">타겟 ID</label>
    <input type="number" name="target_id" class="form-control" value="1" required>
  </div>

  <div class="mb-3">
    <label class="form-label">이미지 업로드</label>
    <div class="drop-zone" id="dropZone">
      이곳에 이미지를 끌어다 놓거나 클릭하여 선택하세요.
      <input type="file" name="uploadFile" id="fileInput" multiple hidden>
    </div>
    <div class="preview d-flex flex-wrap mt-3" id="preview"></div>
  </div>

  <button type="submit" class="btn btn-primary">업로드</button>
</form>