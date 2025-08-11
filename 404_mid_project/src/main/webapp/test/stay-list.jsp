<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.admin.StayInfoDao, model.admin.StayInfoDto" %>
<%
    request.setCharacterEncoding("UTF-8");

    // AJAX 요청 처리
    String act = request.getParameter("act");
    if ("disable".equals(act) || "enable".equals(act)) {
        response.setContentType("application/json; charset=UTF-8");
        boolean ok = false;
        try {
            long stayNum = Long.parseLong(request.getParameter("stayNum"));
            if ("disable".equals(act)) {
                ok = StayInfoDao.getInstance().deleteStay(stayNum); // stay_delete = 'D'
            } else {
                ok = StayInfoDao.getInstance().restoreStay(stayNum); // stay_delete = 'N'
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.print("{\"ok\":" + ok + "}");
        return;
    }

    List<StayInfoDto> list = StayInfoDao.getInstance().getList();
    String ctx = request.getContextPath();
%>
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
    <h2 class="mb-0"><i class="bi bi-house-door me-2"></i>숙소 목록</h2>
    <a href="<%=ctx%>/test/new-stay-form.jsp" class="btn btn-success">
      <i class="bi bi-plus-lg me-1"></i>숙소 등록
    </a>
  </div>

  <div class="card shadow-sm">
    <div class="card-body p-0">
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-light">
            <tr>
              <th style="width:120px;">숙소 번호</th>
              <th>숙소 이름</th>
              <th style="width:140px;">숙소 상태</th>
              <th style="width:180px;" class="text-center">상태 변경</th>
            </tr>
          </thead>
          <tbody>
            <%
              if (list != null && !list.isEmpty()) {
                for (StayInfoDto s : list) {
                  boolean active = "N".equals(s.getStayDelete());
            %>
              <tr>
                <td><%= s.getStayNum() %></td>
                <td>
                  <a href="<%=ctx%>/page/page-view.jsp?stayNum=<%= s.getStayNum() %>"
                     class="text-decoration-none fw-semibold">
                    <%= s.getStayName() %>
                  </a>
                </td>
                <td>
                  <% if (active) { %>
                    <span class="badge bg-success">활성화</span>
                  <% } else { %>
                    <span class="badge bg-secondary">비활성화</span>
                  <% } %>
                </td>
                <td class="text-center">
                  <% if (active) { %>
                    <button class="btn btn-sm btn-outline-danger"
                            onclick="changeStatus(<%= s.getStayNum() %>, 'disable')">
                      <i class="bi bi-x-circle me-1"></i>비활성화
                    </button>
                  <% } else { %>
                    <button class="btn btn-sm btn-outline-primary"
                            onclick="changeStatus(<%= s.getStayNum() %>, 'enable')">
                      <i class="bi bi-check2-circle me-1"></i>활성화
                    </button>
                  <% } %>
                </td>
              </tr>
            <%
                }
              } else {
            %>
              <tr>
                <td colspan="4" class="text-center text-muted py-5">등록된 숙소가 없습니다.</td>
              </tr>
            <%
              }
            %>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<script>
  const thisPage = location.pathname;

  function changeStatus(stayNum, act) {
    const msg = act === 'disable' ? '숙소를 비활성화 하시겠습니까?' : '숙소를 활성화 하시겠습니까?';
    if (!confirm(msg)) return;

    fetch(thisPage, {
      method: 'POST',
      headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'},
      body: 'act=' + act + '&stayNum=' + encodeURIComponent(stayNum)
    })
    .then(r => r.json())
    .then(j => j.ok ? location.reload() : alert('처리 실패'))
    .catch(() => alert('오류 발생'));
  }
</script>

</body>
</html>
