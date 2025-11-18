// ==============================
// ICE / HOT 온도 선택
// ==============================
const segmented = document.querySelector('.segmented');
const buttons = Array.from(document.querySelectorAll('.segmented-btn'));

function updateContainerClass() {
    const active = buttons.find(b => b.classList.contains('active'));
    if (!active) return;

    segmented.classList.toggle('left', active.dataset.value === 'ice');
    segmented.classList.toggle('right', active.dataset.value === 'hot');
}

updateContainerClass();

buttons.forEach(btn => {
    btn.addEventListener('click', () => {
        buttons.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        updateContainerClass();
    });
});


// ==============================
// 옵션 / 가격 계산
// ==============================
let appliedOptionCounts = {};
let qty = 1;
const optionPrice = 500;

const qtyEl = document.getElementById('qty');
const priceEl = document.getElementById('price');
let pricePer = 0;

document.addEventListener('DOMContentLoaded', () => {
    pricePer = parseInt(priceEl.dataset.actualPrice);
    updatePrice();
});

document.getElementById('plus').onclick = () => { qty++; updatePrice(); };
document.getElementById('minus').onclick = () => { if (qty > 1) qty--; updatePrice(); };

function updatePrice() {
    qtyEl.textContent = qty;

    let optionTotal = 0;
    for (const key in appliedOptionCounts) {
        optionTotal += appliedOptionCounts[key] * optionPrice;
    }

    const total = (pricePer + optionTotal) * qty;
    priceEl.textContent = total.toLocaleString() + '원';
}


// ==============================
// 상세 정보 모달
// ==============================
const detailModal = document.getElementById('detailModal');
const optionModal = document.getElementById('optionModal');
const optionTitle = document.getElementById('optionTitle');
const optionContent = document.getElementById('optionContent');

document.getElementById('detailBtn').onclick = () => openModal(detailModal);
document.getElementById('shotBtn').onclick = () => openOption('샷 선택', ['샷 추가']);
document.getElementById('sweetBtn').onclick = () => openOption('당도 선택', ['바닐라 시럽 추가']);
document.getElementById('toppingBtn').onclick = () => openOption('토핑 선택', ['휘핑 크림 추가']);

function openModal(modal) {
    modal.style.display = 'flex';
    modal.querySelector('.close').onclick = () => modal.style.display = 'none';

    modal.onclick = (e) => {
        if (e.target === modal) modal.style.display = 'none';
    };
}

function openOption(title, items) {
    optionTitle.textContent = title;

    optionContent.innerHTML = items.map(item => {
        let val = appliedOptionCounts[item] || 0;
        return `
        <div class='option-row'>
            <span>${item}</span>
            <div class='opt-controls'>
                <span>500원</span>
                <button class="option-minus-btn" data-option="${item}">-</button>
                <span class='val'>${val}</span>
                <button class="option-plus-btn" data-option="${item}">+</button>
            </div>
        </div>
        `;
    }).join("");

    optionContent.querySelectorAll('.option-minus-btn').forEach(btn => {
        btn.onclick = () => adjustOption(btn.dataset.option, -1);
    });

    optionContent.querySelectorAll('.option-plus-btn').forEach(btn => {
        btn.onclick = () => adjustOption(btn.dataset.option, +1);
    });

    openModal(optionModal);
}

function adjustOption(option, delta) {
    let v = appliedOptionCounts[option] || 0;
    v = Math.max(0, v + delta);
    appliedOptionCounts[option] = v;

    openOption(optionTitle.textContent, Object.keys(appliedOptionCounts));
    updatePrice();
}

document.getElementById('applyOptionBtn').onclick = () => {
    optionModal.style.display = 'none';
    updatePrice();
};


// ==============================
// 장바구니 기능 (이미 완성)
// ==============================
document.querySelector('.add').addEventListener('click', function() {
    addToCart();
});

function addToCart() {
    let selectedTemp = document.querySelector('.segmented-btn.active').dataset.value;
    let tumblerUse = document.getElementById('tumbler').checked;
    let shotCount = appliedOptionCounts['샷 추가'] || 0;
    let vanillaSyrupCount = appliedOptionCounts['바닐라 시럽 추가'] || 0;
    let whippedCreamCount = appliedOptionCounts['휘핑 크림 추가'] || 0;
    let quantity = qty;

    // 메뉴 ID 불러오기
    let menuId = document.getElementById('menuId').value;

    let cartData = {
        menuId: menuId,
        quantity: quantity,
        temp: selectedTemp,
        tumblerUse: tumblerUse,
        shotCount: shotCount,
        vanillaSyrupCount: vanillaSyrupCount,
        whippedCreamCount: whippedCreamCount
    };

    fetch('/home/cart/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(cartData)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('장바구니에 추가되었습니다.');
                window.location.href = '/home/cart';
            } else {
                alert('장바구니 추가 실패: ' + result.message);
            }
        })
        .catch(e => {
            alert('장바구니 추가 오류 발생');
            console.error(e);
        });
}



// ==============================
// 예약 주문(=실제 주문하기)
// admin_orders.html에서 보이도록 DB Insert
// ==============================
document.querySelector('.order').addEventListener('click', async () => {

    const menuName = document.getElementById('menuName').value;
    const menuPrice = Number(document.getElementById('menuPrice').value);

    let optionTotal = 0;
    for (const key in appliedOptionCounts) {
        optionTotal += appliedOptionCounts[key] * optionPrice;
    }

    const temp = document.querySelector('.segmented-btn.active').dataset.value;
    const totalPrice = (menuPrice + optionTotal) * qty;

    // OrderVO 구조에 맞춤
    const orderData = {
        totalQuantity: qty,
        totalPrice: totalPrice,
        orderType: "매장",
        orderStatus: "주문접수",
        uId: "guest",
        orderItemList: [
            {
                menuItemName: menuName,
                quantity: qty
            }
        ]
    };

    try {
        const response = await fetch("/api/orders/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(orderData)
        });

        if (!response.ok) {
            alert("주문 생성 중 오류가 발생했습니다.");
            return;
        }

        alert("주문이 완료되었습니다!");

        // 고객 홈으로 이동
        window.location.href = "/home/";

    } catch (e) {
        console.error(e);
        alert("서버 오류로 주문 실패");
    }
});
