// 찜 취소(하트 클릭)
async function cancelLike(element, event) {
    if (event) {
        event.preventDefault();
        event.stopPropagation();
    }

    let menuId = element.closest(".menu-item").dataset.menuId;

    let result = await fetch("/like/toggle", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: `menuId=${menuId}`
    }).then(res => res.json());

    // 삭제 성공 → UI에서 메뉴 제거
    if (!result) {
        element.closest(".menu-item").remove();
    }

    // 남은 메뉴 체크
    let remainItems = document.querySelectorAll(".menu-item");

    if (remainItems.length === 0) {
        document.querySelector(".menu-list-container").innerHTML = `
            <div style="text-align:center; padding:50px; color:#777;">
                찜한 메뉴가 없습니다 ☕
            </div>
        `;
    }
}


document.addEventListener("DOMContentLoaded", () => {

    const container = document.querySelector(".menu-list-container");
    if (!container) return;

    // 상세 페이지 이동 처리 (이벤트 위임)
    container.addEventListener("click", (e) => {

        // 하트 버튼 누르면 이동 방지
        if (e.target.classList.contains("like-button")) {
            e.preventDefault();
            e.stopPropagation();
            return;
        }

        // menu-item 요소 찾기
        const item = e.target.closest(".menu-item");
        if (!item) return;

        // 상세 페이지 이동
        const menuId = item.dataset.menuId;
        if (menuId) {
            window.location.href = `/home/order_detail?id=${menuId}`;
        }
    });

});
