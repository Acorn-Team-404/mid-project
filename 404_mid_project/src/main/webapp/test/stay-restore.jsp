<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.admin.StayInfoDao" %>
<%
    long stayNum = Long.parseLong(request.getParameter("stayNum"));
    boolean isSuccess = StayInfoDao.getInstance().restoreStay(stayNum);

    if (isSuccess) {
%>
    <script>
        alert('숙소가 활성화 되었습니다.');
        location.href = '<%=request.getContextPath()%>/test/stay-list.jsp';
    </script>
<%
    } else {
%>
    <script>
        alert('활성화에 실패했습니다.');
        history.back();
    </script>
<%
    }
%>
