<%@page import="model.admin.IndexDao"%>
<%@page import="model.admin.IndexCarouselDto"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
  request.setCharacterEncoding("UTF-8");

  // AJAX로 act가 오면 서버 작업만 수행하고 JSON 응답 후 종료
  String act = request.getParameter("act");
  if ("set".equals(act) || "delete".equals(act)) {
      response.setContentType("application/json; charset=UTF-8");
      boolean ok = false;
      try {
          int num = Integer.parseInt(request.getParameter("num"));
          if ("set".equals(act)) {
              ok = IndexDao.getInstance().setIndexCarouselNum(num);     // num = ic_index_num
          } else { // delete
              ok = IndexDao.getInstance().delete(num);                   // num = ic_num
          }
      } catch (Exception e) {
          ok = false;
      }
      out.print("{\"ok\":" + ok + "}");
      return; // 여기서 렌더링 종료 → 본문 HTML은 내려가지 않음
  }

  // 여기는 일반 GET 렌더링
  IndexDao idxDao = IndexDao.getInstance();
  List<IndexCarouselDto> list = idxDao.selectList();
  int currentIdxNum = idxDao.getIndexCarouselNum();
  String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Index 캐러셀 선택</title>
  <jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />

<div class="container my-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">
      <i class="bi bi-images me-2"></i>인덱스 캐러셀 목록
    </h2>
    <!-- 업로드/추가 폼으로 이동 (경로는 너 프로젝트에 맞게 수정) -->
    <a href="<%=ctx%>/admin/image-upload.jsp" class="btn btn-success">
      <i class="bi bi-plus-lg me-1"></i>캐러셀 추가
    </a>
  </div>

  <div class="alert alert-secondary d-flex align-items-center" role="alert">
    <i class="bi bi-info-circle me-2"></i>
    <div>현재 적용 그룹번호: <strong><%= currentIdxNum %></strong></div>
  </div>

  <div class="card shadow-sm">
    <div class="card-body p-0">
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-light">
            <tr>
              <th style="width:90px;">번호</th>
              <th>캐러셀 그룹명</th>
              <th style="width:160px;">그룹번호</th>
              <th style="width:220px;" class="text-center">설정</th>
              <th style="width:120px;" class="text-center">삭제</th>
            </tr>
          </thead>
          <tbody>
          <%
            if (list == null || list.isEmpty()) {
          %>
            <tr>
              <td colspan="5" class="text-center text-muted py-5">등록된 캐러셀이 없습니다.</td>
            </tr>
          <%
            } else {
              for (IndexCarouselDto dto : list) {
                boolean isActive = dto.getIcIndexNum() == currentIdxNum;
          %>
            <tr>
              <td><%= dto.getIcNum() %></td>
              <td>
                <div class="d-flex align-items-center gap-2">
                  <span class="fw-semibold"><%= dto.getIcGroupName() %></span>
                  <% if (isActive) { %>
                    <span class="badge bg-primary rounded-pill">사용중</span>
                  <% } %>
                </div>
              </td>
              <td>
                <code class="text-body-secondary"><%= dto.getIcIndexNum() %></code>
              </td>
              <td class="text-center">
                <button type="button"
                        class="btn btn-outline-primary btn-sm"
                        onclick="setActive(<%= dto.getIcIndexNum() %>)">
                  <i class="bi bi-check2-circle me-1"></i>이 그룹 적용
                </button>
              </td>
              <td class="text-center">
                <button type="button"
                        class="btn btn-outline-danger btn-sm"
                        onclick="deleteItem(<%= dto.getIcNum() %>)">
                  <i class="bi bi-trash me-1"></i>삭제
                </button>
              </td>
            </tr>
          <%
              }
            }
          %>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/include/footer.jsp" />

<script>
  const ctx = '<%=ctx%>';
  const thisPage = location.pathname; // 현재 JSP 경로

  function setActive(icIndexNum) {
    fetch(thisPage, {
      method: 'POST',
      headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'},
      body: 'act=set&num=' + encodeURIComponent(icIndexNum)
    })
    .then(r => r.json())
    .then(j => j.ok ? location.reload() : alert('설정 실패'))
    .catch(() => alert('설정 중 오류'));
  }

  function deleteItem(icNum) {
    if (!confirm('삭제하시겠습니까?')) return;
    fetch(thisPage, {
      method: 'POST',
      headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'},
      body: 'act=delete&num=' + encodeURIComponent(icNum)
    })
    .then(r => r.json())
    .then(j => j.ok ? location.reload() : alert('삭제 실패'))
    .catch(() => alert('삭제 중 오류'));
  }
</script>

</body>
</html>
