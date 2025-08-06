<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.admin.LinkDao" %>
<%@ page import="model.admin.LinkDto" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>동적 링크 페이지</title>
  <jsp:include page="/WEB-INF/include/resource.jsp" /> <!-- Bootstrap, Icons 등 -->
</head>
<body>
  <jsp:include page="/WEB-INF/include/navbar.jsp" />

  <div class="container my-5">
    <h1>각종 링크 연결 페이지</h1>

    <%
      // DAO에서 링크 목록 조회
      List<LinkDto> linkList = LinkDao.getInstance().getList();
      if (linkList == null || linkList.isEmpty()) {
    %>
      <p>등록된 링크가 없습니다.</p>
    <%
      } else {
    %>
      <ul class="list-unstyled">
    <%
        for (LinkDto dto : linkList) {
          // action URL (contextPath 자동 추가)
          String actionUrl = request.getContextPath() + dto.getLinkUrl();
    %>
        <li class="mb-2">
          <form action="<%= actionUrl %>" method="get" class="d-inline">
            <button type="submit" class="btn btn-primary">
              <%= dto.getLinkTitle() %>
            </button>
            <% 
              // link_action 컬럼에 "파라미터명=값" 형태로 저장했다면 hidden 으로 추가
              String act = dto.getLinkAction();
              if (act != null && act.trim().length() > 0) {
                  String[] parts = act.split("=", 2);
            %>
              <input type="hidden"
                     name="<%= parts[0] %>"
                     value="<%= parts.length>1 ? parts[1] : "" %>" />
            <% } %>
          </form>
        </li>
    <%
        }
    %>
      </ul>
    <%
      }
    %>
  </div>
</body>
</html>
