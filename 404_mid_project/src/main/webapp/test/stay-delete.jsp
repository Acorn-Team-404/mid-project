<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.admin.StayInfoDao" %>
<%
    long stayNum = Long.parseLong(request.getParameter("stayNum"));
    boolean isSuccess = StayInfoDao.getInstance().deleteStay(stayNum);

    if (isSuccess) {
%>
    <script>
        alert('숙소가 삭제되었습니다.');
        location.href = '<%=request.getContextPath()%>/test/stay-list.jsp';
    </script>
<%
    } else {
%>
    <script>
        alert('삭제에 실패했습니다.');
        history.back();
    </script>
<%
    }
%>
