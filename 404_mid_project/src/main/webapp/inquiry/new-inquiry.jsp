<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/inquiry/new-inquiry.jsp</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<style>
        .form-section {
            border-top: 1px solid #dee2e6;
            border-bottom: 1px solid #dee2e6;
            padding: 0.5rem;
            /* p-2 */
            margin: 1rem;
            /* m-3 */
        }

        .section-title {
            font-weight: bold;
            margin-bottom: 0.5rem;
        }
        .agree-txt{
	        background: #fff;
		    height: 150px;
		    overflow-y: auto;
		    padding: 20px;
		    border: 1px solid #d9d9d9;
		    font-size: 12px;
		    color: #666;
		    margin-top: 15px;
		    line-height: 20px;
		    border-radius: 8px;
		    box-sizing: border-box;						
	    }
</style>
</head>
<body>
	<div class="container">
        <div class="text-center mt-5">
            <h1 class="pb-4">문의하기</h1>
            <p>문의하신 내용은 마이페이지의 1:1 문의내역에서 확인가능합니다</p>
        </div>
        <div>
            <form action="/d/d">
                <div class="form-section">
                    <span class="form-label">문의할 숙소</span>
                    <select>
                        <option value="기타">기타</option>
                        <option value="a">a</option>
                        <option value="b">b</option>
                        <option value="c">c</option>
                        <option value="d">d</option>
                    </select>

                </div>
                <div class="form-section">
                    <span class="form-label">문의유형</span>
                    <label>
                        <input type="radio" name="inqType" value="예약" /> 예약
                    </label>
                    <label>
						<input type="radio" name="inqType" value="결제" /> 결제
					</label>
                    <label>
                        <input type="radio" name="inqType" value="숙소정보" /> 숙소정보
                    </label>
                    <label>
                        <input type="radio" name="inqType" value="시설문의" /> 시설문의
                    </label>
                    <label>
                        <input type="radio" name="inqType" value="기타" checked /> 기타
                    </label>
                </div>
                <div class="form-section">
                    <div class="mt-2">
                        <label for="userName" class="form-label">이름</label>
                        <input type="text" name="userName" class="bg-light" value="이름" readonly />
                    </div>
                    <div class="mt-1">
                        <label for="userPhone" class="form-label">전화번호</label>
                        <input type="text" name="userPhone" class="bg-light" value="전화번호" readonly />
                    </div>
                    <div class="mt-1">
                        <label for="userEmail" class="form-label">이메일</label>
                        <input type="text" name="userEmail" class="bg-light" value="이메일" readonly />
                    </div>
                    <small>회원정보를 확인해주세요.</small>
                </div>
                <div class="form-section">
                    <div>
                    	<label for="title" class="form-label">
                    		제목
                    		<span class="text-danger">*</span> 
                    	</label>
                        <input type="text" class="form-control" name="title" id="title" />
                    </div>
                    <div>
                    	<label for="content" class="form-label">
                    		내용
                    		<span class="text-danger">*</span> 
                    	</label>
                        <textarea class="form-control" name="content" id="content"></textarea>
                    </div>
                </div>
                <div  class="form-section">
                	<span>
                		개인정보 수집 및 이용에 대한 동의
                		<span class="text-danger">*</span> 
                	</span>
                	<div class="agree-txt">
						문의와 관련하여 귀사가 아래와 같이 개인정보를 수집 및 이용하는데 동의합니다.
						<br>
						<br>
						1. 개인정보 수집 및 이용 항목 <br>
						- 성명, 이메일, 연락처
						<br>
						<br>
						 2. 개인정보 수집 및 이용 목적 <br>
						- 문의에 대한 안내 및 서비스 제공
						<br>
						<br>
						3. 개인정보 보유 및 이용 기간 <br>
						- 문의 처리 완료 후 3년간 보관하며, 관련 법령에 따라 보존이 필요한 경우 해당 기간 동안 보관합니다. <br>
						- 전자상거래법: 계약 또는 청약철회 등에 관한 기록 5년 <br>
                        - 소비자보호법: 소비자 불만 또는 분쟁처리 기록 3년
						<br>
						<br>
						※ 위 사항에 대한 동의를 거부할 수 있으나, 미동의 시 문의에 대한 안내 및 서비스 제공이 제한됨을 알려드립니다.
                	</div>
                	<div>
                		<input type="checkbox" id="agree-ck" />
                		<label>동의합니다.</label>
                	</div>
                </div>
                <button type="submit">등록</button>
            </form>
        </div>
    </div>
    <script>
    </script>
</body>
</html>