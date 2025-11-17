const LIKE_KEY = "likedMenus";

document.addEventListener("DOMContentLoaded", () => {
    console.log("mypick.js 실행됨 ✅");

    const container = document.getElementById("myPickList");
    if (!container) {
        console.error("찜목록 컨테이너를 찾을 수 없습니다.");
        return;
    }

    const likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    renderLikedMenus(likedMenus, container);
});

// 찜목록 렌더링
function renderLikedMenus(likedMenus, container) {
    container.innerHTML = "";

    if (likedMenus.length === 0) {
        container.innerHTML = `
            <div class="empty-message" style="text-align:center; padding:50px; color:#777;">
                찜한 메뉴가 없습니다 ☕
            </div>
        `;
        return;
    }

    likedMenus.forEach(menu => {
        const menuCard = document.createElement("div");
        menuCard.classList.add("menu-item");

        // menu.html의 구조 그대로 반영
        menuCard.innerHTML = `
            <img src="${menu.image}" alt="${menu.name}" class="menu-image">
            <div class="menu-details">
                <div class="menu-name">${menu.name}</div>
                ${renderTemp(menu.temp)}
                <div class="menu-price" data-price="${menu.price}">
                    ${menu.price.toLocaleString()}원
                </div>
            </div>
            <span class="like-button liked">❤</span>
        `;

        // 찜 해제 버튼
        const likeBtn = menuCard.querySelector(".like-button");
        likeBtn.addEventListener("click", (event) => {
            event.stopPropagation();
            removeFromLiked(menu.name);
            menuCard.remove();

            const updated = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
            if (updated.length === 0) renderLikedMenus(updated, container);
        });

        // 메뉴 클릭 시 상세 페이지 이동
        menuCard.addEventListener("click", () => {
            console.log(menu.name + " 클릭됨");
            // window.location.href = `/menu/detail/${menu.name}`; // 필요시 주석 해제
        });

        container.appendChild(menuCard);
    });
}

// 찜 해제 (이름 기준)
function removeFromLiked(menuName) {
    let liked = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    liked = liked.filter(item => item.name !== menuName);
    localStorage.setItem(LIKE_KEY, JSON.stringify(liked));
    console.log(`찜 해제됨: ${menuName}`);
}

// ICE/HOT 텍스트 렌더링 함수
function renderTemp(temp) {
    if (!temp) return '';
    if (temp === 'ICE/HOT') {
        return `
            <span class="menu-temp">ICE</span>
            <span class="menu-temp hot">HOT</span>
        `;
    }
    if (temp === 'ICE') return `<span class="menu-temp">ICE</span>`;
    if (temp === 'HOT') return `<span class="menu-temp hot">HOT</span>`;
    return `<span class="menu-temp">${temp}</span>`;
}
