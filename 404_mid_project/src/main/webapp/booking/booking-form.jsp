<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/booking/</title>
</head>
<body>
	<div class="container">
		<form action="${pageContext.request.contextPath}/booking/submit" method="post">
			<div class="left-container">
				<h1>예약자 정보</h1>
				<div>
					<label for="name">이름</label>
					<input type="hidden" name="name" id="name"/>
				</div>
				<div>
					<label for="email">이메일</label>
					<input type="hidden"name="email" id="email"/>
				</div>
				<div>
					<label for="phone">전화번호</label>
					<input type="hidden" name="phone" id="phone"/>
				</div>
				
				<div>
					<h1>옵션</h1>
					<pre>숙박 시 제공되는 옵션을 선택하세요</pre>
					<div>
						<h3>추가 침대</h3>
						<input type="checkbox" name="bed" value="0"/>간이 침대
						<br />
						<input type="checkbox" name="bed" value="1"/>유아 침대
					</div>
					<div>
						<h3>도착</h3>
						<input type="radio" name="checkInTime" value="standard"/>정규 시간
						<br />
						<input type="radio" name="checkInTime" value="early"/>이른 체크인
						<br />
						<input type="radio" name="checkInTime" value="late"/>늦은 도착
					</div>				
				</div>
				
				<div>
					<h1>추가 요청사항</h1>
					<textarea name="bookRequest" id="bookRequest" cols="100" rows="10">요청사항을 입력해 주세요
					</textarea>
				</div>
				
				<div>
					<h2>예약 안내사항</h2>
					
					<h2>이용 안내</h2>
					
					<h2>환불 규정</h2>
					
					<h2>판매자 정보</h2>
				</div>
				
			</div>
			<div class="right-container">
				<div>
					<label for="room">객실 선택</label>
					<select id="room" name="room">
						<option value="">1</option>
						<option value="">2</option>
					</select>			
				</div>
				
				<div>
					<label for="checkIn">체크인 날짜</label>
					<input type="date" name="checkIn" id="checkIn"/>
					
					<label for="checkOut">체크아웃 날짜</label>
					<input type="date" name="checkOut" id="checkOut"/>
				</div>
				
				
				<div>
					<label for="adult">성인</label>
					<input type="number" name="adult" id="adult" value="2" min="0"/>
					<br />
					
					<label for="child">어린이</label>
					<input type="number" name="child" id="child" value="0" min="0"/>
					<br />
					
					<label for="infant">유아</label>
					<input type="number" name="infant" id="infant" value="0" min="0"/>
					
					<p>총 인원: <span id="totPerson">1</span>명</p>
				</div>
				
				<div>
					<strong>객실 요금</strong>
					<span id="roomPrice"></span>
					<br />
					
					<strong>추가 침대</strong>
					<span id="bedOption"></span>
					<br />
					
					<strong>도착 시간</strong>
					<span id="checkInOption"></span>
					<br />
					
					<strong>총액</strong>
					<span id="totPrice"></span>
				</div>

				<button type="submit">예약하기</button>
			</div>			
		</form>
	</div>
</body>
</html>