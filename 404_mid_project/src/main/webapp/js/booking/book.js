document.addEventListener("DOMContentLoaded", () => {
    const pax = {
	adult: parseInt(document.querySelector("#adult")?.value) || 0,
children: parseInt(document.querySelector("#children")?.value) || 0,
        infant: parseInt(document.querySelector("#infant")?.value) || 0
    };

    const updateUI = () => {
        document.querySelector("#adultCount").textContent = pax.adult;
        document.querySelector("#childrenCount").textContent = pax.children;
        document.querySelector("#infantCount").textContent = pax.infant;

        document.querySelector("#adult").value = pax.adult;
        document.querySelector("#children").value = pax.children;
        document.querySelector("#infant").value = pax.infant;

        document.querySelector("#totPerson").textContent = pax.adult + pax.children + pax.infant;
    };

    // 전역 함수로 등록
    window.changeCount = (type, delta) => {
        if (pax.hasOwnProperty(type)) {
            pax[type] = Math.max(0, pax[type] + delta);
            updateUI();
        }
    };

    updateUI(); // 초기화
});
