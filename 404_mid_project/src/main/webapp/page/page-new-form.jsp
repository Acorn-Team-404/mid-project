<%@ page import="model.page.StayDao" %>
<%@ page import="model.page.StayDto" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    List<String> imageList = new ArrayList<>();
    List<StayDto> stayList = StayDao.getInstance().selectAll();

    Long users_num = (Long) session.getAttribute("usersNum");
    String usersId = (String) session.getAttribute("usersId");
    if (usersId == null) {
%>
<script>
    alert("로그인이 필요합니다.");
    location.href = "${pageContext.request.contextPath}/user/login-form.jsp";
</script>
<%
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>페이지 작성</title>
    <jsp:include page="/WEB-INF/include/resource.jsp"></jsp:include>
    <!-- Toast UI Editor -->
    <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
    <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
    <script src="https://uicdn.toast.com/editor/latest/i18n/ko-kr.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/include/navbar.jsp"></jsp:include>

    <div class="container mt-5">
        <div class="card shadow p-4">
            <h2 class="mb-4 text-primary">숙소 상세 페이지 작성</h2>
            <form action="page-save.jsp" method="post" id="saveForm" enctype="multipart/form-data">
                <!-- 숙소 선택 -->
                <div class="mb-4">
                    <label for="stayNum" class="form-label fw-bold">숙소 선택</label>
                    <select name="stayNum" id="stayNum" class="form-select" required>
                        <option value="">-- 숙소 선택 --</option>
                        <% for(StayDto stay : stayList) { %>
                            <option value="<%=stay.getStayNum() %>">
                                <%=stay.getStayName() %> (<%=stay.getStayAddr() %>)
                            </option>
                        <% } %>
                    </select>
                </div>

                <!-- 이미지 업로드 -->
                <div class="mb-4">
                    <label class="form-label fw-bold">슬라이드 이미지 첨부</label>
                    <input type="file" name="imageFiles" class="form-control" multiple accept="image/*">
                </div>

                <!-- 소개글 작성 -->
                <div class="mb-4">
                    <label class="form-label fw-bold">소개글 작성</label>
                    <div id="editor"></div>
                    <input type="hidden" name="pageContent" id="hiddenContent">
                    <input type="hidden" name="writer" value="<%=usersId %>">
                </div>

                <!-- 예약/이용/환불 안내 -->
                <div class="mb-4">
                    <label for="notice_reserve" class="form-label fw-bold">예약 안내</label>
                    <input type="text" name="pageReserve" id="notice_reserve" class="form-control" />

                    <label for="notice_guide" class="form-label fw-bold mt-3">이용 안내</label>
                    <input type="text" name="pageGuide" id="notice_guide" class="form-control" />

                    <label for="notice_refund" class="form-label fw-bold mt-3">환불 규정</label>
                    <input type="text" name="pageRefund" id="notice_refund" class="form-control" />
                </div>

                <!-- 저장 버튼 -->
                <div class="text-end">
                    <button class="btn btn-success">저장</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        const editor = new toastui.Editor({
            el: document.querySelector('#editor'),
            height: '500px',
            initialEditType: 'wysiwyg',
            previewStyle: 'vertical',
            language: 'ko'
        });

        document.querySelector("#saveForm").addEventListener("submit", (e) => {
            const content = editor.getHTML();
            document.querySelector("#hiddenContent").value = content;
        });
    </script>
</body>
</html>
