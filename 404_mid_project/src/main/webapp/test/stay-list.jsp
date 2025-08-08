<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.admin.StayInfoDao, model.admin.StayInfoDto" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>숙소 목록</title>
  <jsp:include page="/WEB-INF/include/resource.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />

<div class="container my-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2><i class="bi bi-house-door me-2"></i>숙소 목록</h2>
    <!-- 숙소 등록 버튼 추가 -->
    <a href="<%=request.getContextPath()%>/test/new-stay-form.jsp"
       class="btn btn-success">
      숙소 등록
    </a>
  </div>

  <table class="table table-striped">
    <thead>
      <tr>
        <th>숙소 번호</th>
        <th>숙소 이름</th>
        <th>예약하기</th>
        <th>삭제하기</th>        
      </tr>
    </thead>
    <tbody>
    <%
      List<StayInfoDto> list = StayInfoDao.getInstance().getList();
      if (list != null && !list.isEmpty()) {
        for (StayInfoDto s : list) {
    %>
      <tr>
        <td><%= s.getStayNum() %></td>
        <td><%= s.getStayName() %></td>
        <td>
          <form action="<%=request.getContextPath()%>/booking/submit"
                method="get" style="margin:0;">
            <input type="hidden" name="stayNum" value="<%= s.getStayNum() %>">
            <button type="submit" class="btn btn-sm btn-primary">예약</button>
          </form>
        </td>
				<td>
				    <form action="<%=request.getContextPath()%>/test/stay-delete.jsp"
				          method="post" style="margin:0;"
				          onsubmit="return confirm('숙소를 삭제하시겠습니까?');">
				        <input type="hidden" name="stayNum" value="<%= s.getStayNum() %>">
				        <button type="submit" class="btn btn-sm btn-danger">삭제</button>
				    </form>
				</td>
      </tr>
    <%
        }
      } else {
    %>
      <tr>
        <td colspan="3" class="text-center">등록된 숙소가 없습니다.</td>
      </tr>
    <%
      }
    %>
    </tbody>
  </table>
</div>
</body>
</html>
