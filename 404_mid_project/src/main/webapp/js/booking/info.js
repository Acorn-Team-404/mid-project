// 객실선택 버튼 클릭 시 예약폼으로 값 전달 (모달, 선택 반영 포함)
// 1. 객실타입 카드 선택 했을 때 모달창 띄우기
document.addEventListener('DOMContentLoaded', function () {
    const cards = document.querySelectorAll('.room-card');
    const selectButtons = document.querySelectorAll('.btn-room-select');

    // 카드 클릭 시 상세정보 toggle
    cards.forEach(card => {
        card.addEventListener('click', function () {
            const detail = card.querySelector('.room-detail');
            const isVisible = detail.style.display === 'block';

            // 다른 카드 펼침 닫기
            document.querySelectorAll('.room-detail').forEach(d => d.style.display = 'none');

            // 현재 카드 토글
            if (!isVisible) {
                detail.style.display = 'block';
            }
        });
    });

    // 2. 객실 선택 버튼 클릭 시 예약 폼으로 값 전달 + 스타일 적용
    selectButtons.forEach(btn => {
        btn.addEventListener('click', function (event) {
            event.stopPropagation(); // 카드 클릭 방지

            const roomNum = this.dataset.roomNum;
            const roomName = this.dataset.roomName;
            const roomPrice = this.dataset.roomPrice;

            // 예약 폼에 값 채우기
            document.querySelector('#roomNum').value = roomNum;
            document.querySelector('#roomName').value = roomName;
            document.querySelector('#roomPrice').value = roomPrice;

            document.querySelector('#selectedRoomName').textContent = roomName;
            document.querySelector('#selectedRoomPrice').textContent = `₩${Number(roomPrice).toLocaleString()}`;

            // 시각적 강조
            cards.forEach(c => c.classList.remove('selected-card'));
            this.closest('.room-card').classList.add('selected-card');
        });
    });
});

