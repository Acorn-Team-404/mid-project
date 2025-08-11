<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>이미지 업로드 테스트</title>
  <jsp:include page="/WEB-INF/include/resource.jsp" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/image-upload-form.css" />
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />
<div class="container col-10 my-4">
  <!-- action을 /saveCarousel 로 변경한다. -->
  <form id="uploadForm" method="post"
        action="<%= request.getContextPath() %>/saveCarousel"
        enctype="multipart/form-data">
    <div class="mb-3">
      <h1><strong>index 이미지 업로드</strong></h1>
    </div>

    <!-- 이 값이 이미지 target_id 로 쓰인다 (index_carousel.ic_index_num) -->
    <div class="mb-3">
      <label class="form-label">캐러셀 그룹 번호</label>
      <input type="number" name="target_id" class="form-control" value="1" required>
    </div>

    <div class="mb-3">
      <label class="form-label">캐러셀 그룹 이름</label>
      <input type="text" name="group_name" class="form-control" required>
    </div>

    <div class="mb-3">
      <label class="form-label">이미지 업로드</label>
      <div class="drop-zone" id="dropZone">
        이곳에 이미지를 끌어다 놓거나 클릭하여 선택하세요.
        <!-- name="uploadFile" 이어야 한다 (서블릿에서 partName으로 사용) -->
        <input type="file" name="uploadFile" id="fileInput" multiple hidden accept="image/*">
      </div>
      <div class="preview d-flex flex-wrap mt-3" id="preview"></div>
    </div>

    <button type="submit" class="btn btn-primary">업로드</button>
  </form>
</div>

<script>
  // (필요시) 여기 드롭존/미리보기 스크립트 붙여서 동일하게 사용하면 된다.
</script>
</body>
</html>
