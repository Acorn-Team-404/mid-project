<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String address = (String) request.getAttribute("stayAddress");
	String stayName = (String) request.getAttribute("stayName");
	if (address == null) {
	    address = "서울특별시"; // 기본값
	}
	if (stayName == null) {
	    stayName = "숙소"; // 기본값
	}
%>

<div id="map" style="width:400px;height:350px;"></div>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=2ea177feb1613ebf3400663ade3a4e8e&libraries=services"></script>
<script>
var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };  

// 지도를 생성합니다    
var map = new kakao.maps.Map(mapContainer, mapOption); 

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();

//JSP에서 주소 가져오기
var address = "<%=address.replaceAll("\\r\\n|\\r|\\n", " ")%>";

// 주소로 좌표를 검색합니다
geocoder.addressSearch(address, function(result, status) {

   
    // 정상적으로 검색이 완료됐으면 
     if (status === kakao.maps.services.Status.OK) {

        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
        
        var latitude = result[0].y;   // 위도
        var longitude = result[0].x;  // 경도
        
        console.log("위도: " + latitude);
        console.log("경도: " + longitude);

        // 결과값으로 받은 위치를 마커로 표시합니다
        var marker = new kakao.maps.Marker({
            map: map,
            position: coords
        });

        // 인포윈도우로 장소에 대한 설명을 표시합니다
        var infowindow = new kakao.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:6px 0;"><%=stayName%></div>'
        });
        infowindow.open(map, marker);

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
    } else {
        document.getElementById('map').innerHTML = '<div class="text-center p-4">지도를 불러올 수 없습니다.</div>';
    }
});    
</script>
</body>
</html>