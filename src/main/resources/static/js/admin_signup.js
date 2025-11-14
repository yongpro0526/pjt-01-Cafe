let usedIds = ['admin', 'test', 'user123']; // 이미 사용 중인 ID 예시

// 아이디 중복 확인
function checkDuplicate() {
    let idInput = document.getElementById('id');
    let message = document.getElementById('checkMessage');
    let idValue = idInput.value.trim();

    if (usedIds.includes(idValue.toLowerCase())) {
        message.style.color = 'red';
        message.textContent = '이미 사용 중인 아이디입니다.';
    } else {
        message.style.color = 'green';
        message.textContent = '사용 가능한 아이디입니다!';
    }
}

// 비밀번호 일치 확인
function checkPasswordMatch() {
    let pw = document.getElementById('password').value;
    let pwCheck = document.getElementById('password-check').value;
    let pwMessage = document.getElementById('pwMessage');

    if (pwCheck === '') {
        pwMessage.textContent = '';
        return;
    }

    if (pw === pwCheck) {
        pwMessage.style.color = 'green';
        pwMessage.textContent = '비밀번호가 일치합니다 ✅';
    } else {
        pwMessage.style.color = 'red';
        pwMessage.textContent = '비밀번호가 일치하지 않습니다 ❌';
    }
}

// 폼 제출 시 비밀번호 확인
let signupForm = document.getElementById('signupForm');
signupForm.addEventListener('submit', function(e) {
    let pw = document.getElementById('password').value;
    let pwCheck = document.getElementById('password-check').value;

    if (pw !== pwCheck) {
        alert('비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
        e.preventDefault(); // 제출 막기
    }
});
