<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>숙소 등록</title>
  <jsp:include page="/WEB-INF/include/resource.jsp" /> <!-- Bootstrap, Icons 포함 -->
  <style>
    .drop-zone {
      border: 2px dashed #0d6efd;
      cursor: pointer;
      border-radius: 0.25rem;
      background-color: #f8f9fa;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 1rem;
      transition: background-color 0.2s;
    }
    .drop-zone.dragover { background-color: #e9ecef; }
    .preview-item {
      position: relative;
      margin: 0.25rem;
    }
    .preview-item img {
      max-width: 120px;
      border: 1px solid #dee2e6;
      border-radius: 0.25rem;
    }
    .remove-btn {
		  position: absolute;
		  top: -0.25rem;
		  right: -0.25rem;
		  background-color: #ffffffcc;
		  color: #dc3545;
		  border: none;
		  /* 둥근 사각형으로 변경 */
		  border-radius: 0.25rem;
		  width: 1.25rem;
		  height: 1.25rem;
		  font-size: 1rem;
		  line-height: 1.25rem;
		  text-align: center;
		  cursor: pointer;
		}
    #stayDropZone { height: 250px; }
    .room-card .drop-zone { height: 150px; }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/include/navbar.jsp" />
<div class="container my-5">
  <h2 class="mb-4"><i class="bi bi-house-add me-2"></i>숙소 등록</h2>
  <form action="${pageContext.request.contextPath}/saveStay" method="post" enctype="multipart/form-data" id="stayForm">
    <!-- 숙소 정보 -->
    <!-- 1. 숙소 이름과 연락처를 같은 행에 배치 -->
    <div class="row g-3 mb-3">
      <div class="col-md-6">
        <label class="form-label">숙소 이름 *</label>
        <input type="text" class="form-control" name="stay_name" required>
      </div>
      <div class="col-md-6">
        <label class="form-label">연락처 *</label>
        <input type="text" class="form-control" name="stay_contact" placeholder="연락처를 입력하세요">
      </div>
    </div>

    <!-- 2. 지역(select)과 상세주소를 같은 행에 배치 -->
    <div class="row g-3 mb-3">
      <div class="col-md-4">
        <label class="form-label">지역 *</label>
        <select class="form-select" name="stay_region" required>
          <option value="">선택하세요</option>
          <option value="서울">서울</option>
          <option value="경기">경기</option>
          <option value="인천">인천</option>
          <option value="강원">강원</option>
          <option value="충북">충북</option>
          <option value="충남">충남</option>
          <option value="전북">전북</option>
          <option value="전남">전남</option>
          <option value="경북">경북</option>
          <option value="경남">경남</option>
          <option value="제주">제주</option>
        </select>
      </div>
      <div class="col-md-8">
        <label class="form-label">상세주소 *</label>
        <input type="text" class="form-control" name="stay_address" placeholder="상세주소를 입력하세요" required>
      </div>
    </div>

    <!-- 3. 위도 · 경도 입력은 기존대로 -->
    <div class="row g-3 mb-3">
      <div class="col-md-6">
        <label class="form-label">위도</label>
        <input type="text" class="form-control" name="stay_lat">
      </div>
      <div class="col-md-6">
        <label class="form-label">경도</label>
        <input type="text" class="form-control" name="stay_lng">
      </div>
    </div>
    <div class="mb-3">
      <label class="form-label">편의시설</label>
      <input type="text" class="form-control" name="stay_facilities" placeholder="예: WiFi,Parking,Pool">
    </div>

    <!-- 숙소 대표 이미지 -->
    <h5 class="mt-4">숙소 대표 이미지 등록</h5>
    <div class="drop-zone mb-3" id="stayDropZone">
      <span class="text-muted">이미지를 드래그하거나 클릭하세요</span>
      <input type="file" id="stayFileInput" name="uploadFile" multiple hidden>
    </div>
    <div id="stayPreview" class="d-flex flex-wrap mb-4"></div>

    <!-- 객실 등록 -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">객실 정보</h5>
        <p>총 객실 수: <span id="roomCountDisplay">0</span></p>
        <div id="roomContainer"></div>
        <button type="button" class="btn btn-outline-primary mt-3" id="addRoomBtn">[+] 객실 추가하기</button>
      </div>
    </div>

    <div class="text-end">
      <button type="submit" class="btn btn-success btn-lg">등록하기</button>
    </div>
  </form>
</div>

<script>
  // 파일을 Data URL로 읽어오는 유틸 함수
  function readFileAsDataURL(file) {
    return new Promise(function(resolve, reject) {
      var reader = new FileReader();
      reader.onload = function() { resolve(reader.result); };
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }

  // 드래그&드롭 + 클릭 파일 입력 + 미리보기 처리 함수
  function bindDropZone(dz, fi, pv) {
    var filesArr = [];
    dz.addEventListener('click', function() { fi.click(); });
    dz.addEventListener('dragover', function(e) {
      e.preventDefault();
      dz.classList.add('dragover');
    });
    dz.addEventListener('dragleave', function() {
      dz.classList.remove('dragover');
    });
    dz.addEventListener('drop', function(e) {
      e.preventDefault();
      dz.classList.remove('dragover');
      filesArr = filesArr.concat(Array.from(e.dataTransfer.files));
      update();
    });
    fi.addEventListener('change', function() {
      filesArr = filesArr.concat(Array.from(fi.files));
      update();
    });

    function update() {
      pv.innerHTML = '';
      var dt = new DataTransfer();
      filesArr.forEach(function(f, i) {
        if (!f.type.startsWith('image/')) return;
        readFileAsDataURL(f).then(function(url) {
          var div = document.createElement('div');
          div.className = 'preview-item';
          var img = document.createElement('img');
          img.src = url;
          var btn = document.createElement('button');
          btn.type = 'button';
          btn.className = 'remove-btn';
          btn.innerText = '×';
          btn.addEventListener('click', function() {
            filesArr.splice(i, 1);
            update();
          });
          div.appendChild(img);
          div.appendChild(btn);
          pv.appendChild(div);
        });
        dt.items.add(f);
      });
      fi.files = dt.files;
    }
  }

  // 숫자만 입력하고 3자리마다 콤마 찍기
  function formatNumberInput(e) {
    var val = e.target.value.replace(/[^0-9]/g, '');
    e.target.value = val.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  // 객실 카드 동적 추가/삭제 및 재인덱싱 로직
  var roomCount = 0;
  var display = document.getElementById('roomCountDisplay');
  var container = document.getElementById('roomContainer');
  var addRoomBtn = document.getElementById('addRoomBtn');

  addRoomBtn.addEventListener('click', function() {
    var idx = roomCount++;
    var card = document.createElement('div');
    card.className = 'card mb-3 room-card';
    card.setAttribute('data-room-idx', idx);

    var cardNumber = container.children.length + 1;
    var innerHTML =
      '<div class="card-body">' +
        '<div class="d-flex justify-content-between align-items-center mb-2">' +
          '<h6 class="card-title mb-0">객실 #' + cardNumber + '</h6>' +
          '<button type="button" class="btn btn-sm btn-danger remove-room-btn">×</button>' +
        '</div>' +
        '<div class="row g-3">' +
          '<div class="col-md-6">' +
            '<label class="form-label">객실 이름</label>' +
            '<input type="text" class="form-control" name="rooms[' + idx + '].roomName" required>' +
          '</div>' +
          '<div class="col-md-6">' +
          '<label class="form-label">객실 유형</label>' +
          '<select class="form-select" name="rooms[' + idx + '].roomType" required>' +
            '<option value="">선택</option>' +
            '<option value="standard">standard</option>' +
            '<option value="twin">twin</option>' +
            '<option value="family">family</option>' +
          '</select>' +
       		'</div>' +
          '<div class="col-md-4">' +
            '<label class="form-label">가격</label>' +
            '<div class="input-group">' +
              '<span class="input-group-text">₩</span>' +
              '<input type="text" class="form-control price-input" name="rooms[' + idx + '].roomPrice" inputmode="numeric" pattern="[0-9,]*" required>' +
            '</div>' +
          '</div>' +
          '<div class="col-md-4">' +
            '<label class="form-label">성인 최대</label>' +
            '<input type="number" class="form-control" name="rooms[' + idx + '].roomAdultMax" value="2">' +
          '</div>' +
          '<div class="col-md-4">' +
            '<label class="form-label">총 인원</label>' +
            '<input type="number" class="form-control" name="rooms[' + idx + '].roomPaxMax" value="2">' +
          '</div>' +
          '<div class="col-md-4">' +
            '<label class="form-label">어린이 최대</label>' +
            '<input type="number" class="form-control" name="rooms[' + idx + '].roomChildrenMax" value="0">' +
          '</div>' +
          '<div class="col-md-4">' +
            '<label class="form-label">유아 최대</label>' +
            '<input type="number" class="form-control" name="rooms[' + idx + '].roomInfantMax" value="0">' +
          '</div>' +
          '<div class="col-12">' +
            '<label class="form-label">객실 설명</label>' +
            '<textarea class="form-control" name="rooms[' + idx + '].roomContent" rows="2"></textarea>' +
          '</div>' +
          '<div class="col-12">' +
            '<label class="form-label">객실 이미지 업로드</label>' +
            '<div class="drop-zone p-3" id="roomDropZone-' + idx + '">' +
              '<span class="text-muted">이미지를 드래그하거나 클릭</span>' +
              '<input type="file" class="d-none" id="roomFileInput-' + idx + '" name="uploadFile" multiple>' +
            '</div>' +
            '<div id="roomPreview-' + idx + '" class="d-flex flex-wrap mt-2"></div>' +
          '</div>' +
        '</div>' +
      '</div>';

    card.innerHTML = innerHTML;
    container.appendChild(card);

    // 가격 포맷 적용
    card.querySelector('.price-input').addEventListener('input', formatNumberInput);

    // 드롭존 바인딩
    bindDropZone(
      document.getElementById('roomDropZone-' + idx),
      document.getElementById('roomFileInput-' + idx),
      document.getElementById('roomPreview-' + idx)
    );

    // 삭제 버튼 이벤트
    card.querySelector('.remove-room-btn').addEventListener('click', function() {
      container.removeChild(card);
      reindexRooms();
    });

    // 추가 후 재인덱싱
    reindexRooms();
  });

  // 카드 삭제 및 순서 변경 시 넘버링/네임 재정렬
  function reindexRooms() {
    var cards = container.querySelectorAll('.room-card');
    Array.prototype.forEach.call(cards, function(card, i) {
      card.querySelector('.card-title').textContent = '객실 #' + (i + 1);
      Array.prototype.forEach.call(card.querySelectorAll('[name]'), function(el) {
        el.name = el.name
          .replace(/rooms\[\d+\]/, 'rooms[' + i + ']')
          .replace(/roomImages\[\d+\]/, 'roomImages[' + i + ']');
      });
    });
    display.innerText = cards.length;
  }

  // 초기 바인딩
  bindDropZone(
    document.getElementById('stayDropZone'),
    document.getElementById('stayFileInput'),
    document.getElementById('stayPreview')
  );
</script>
</body>
</html>
