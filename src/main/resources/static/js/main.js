document.addEventListener('DOMContentLoaded', () => {

    // --- 1. 알림 팝업 로직 ---
    let notificationTrigger = document.getElementById('notification-trigger');
    let notificationPopup = document.getElementById('notification-popup');

    if (notificationTrigger && notificationPopup) {
        notificationTrigger.addEventListener('click', (event) => {
            event.stopPropagation(); // 이벤트 버블링 중단
            notificationPopup.classList.toggle('show');
        });

        // 팝업 외부 클릭 시 팝업 닫기
        document.addEventListener('click', (event) => {
            if (notificationPopup.classList.contains('show') && !notificationTrigger.contains(event.target)) {
                notificationPopup.classList.remove('show');
            }
        });
    }


    // --- 2. 로그인/회원가입 모달 공통 로직 ---

    // 모달 요소
    let loginModalOverlay = document.getElementById('login-modal-overlay');
    let signupModalOverlay = document.getElementById('signup-modal-overlay');

    // 모달 닫기 트리거
    let loginModalClose = document.getElementById('login-modal-close');   // 로그인 모달 닫기(X)
    let signupModalClose = document.getElementById('signup-modal-close'); // 회원가입 모달 닫기(X)

    // 모달 전환 트리거
    let switchToSignupTrigger = document.getElementById('switch-to-signup-trigger'); // 로그인 -> 회원가입

    // (추가) "로그인" 탭 (비로그인시 나타남)
    let loginModalTrigger = document.getElementById('login-modal-trigger');

    // 폼 요소
    let modalLoginForm = document.getElementById('modalLoginForm');
    let modalSignupForm = document.getElementById('modalSignupForm');


    // --- 3. '로그인 필수' 인터셉터 로직 ---

    // (IS_LOGGED_IN 변수는 userBaseLayout.html에서 <script> 태그로 주입됨)
    if (typeof IS_LOGGED_IN !== 'undefined' && !IS_LOGGED_IN) {
        // 로그인이 안 된 상태 (IS_LOGGED_IN === false)
        let loginRequiredLinks = document.querySelectorAll('.login-required');

        if (loginRequiredLinks.length > 0) {
            loginRequiredLinks.forEach(link => {
                link.addEventListener('click', (event) => {
                    event.preventDefault();
                    if (loginModalOverlay) {
                        loginModalOverlay.classList.add('show');
                    }
                });
            });
        }
    }
    // --- 인터셉터 로직 끝 ---


    // --- 4. 모달 전환 로직 ---

    // (추가) [열기] 하단 '로그인' 탭 클릭 시 -> 로그인 모달 열기
    if (loginModalTrigger) {
        loginModalTrigger.addEventListener('click', () => {
            if (loginModalOverlay) {
                loginModalOverlay.classList.add('show');
            }
        });
    }

    // [전환] 로그인 모달의 'Sign Up' 버튼 클릭 시 -> 회원가입 모달 열기
    if (switchToSignupTrigger) {
        switchToSignupTrigger.addEventListener('click', () => {
            if (loginModalOverlay) loginModalOverlay.classList.remove('show');
            if (signupModalOverlay) signupModalOverlay.classList.add('show');
        });
    }


    // --- 5. 모달 닫기 공통 로직 ---

    // 로그인 모달 'X' 버튼
    if (loginModalClose) {
        loginModalClose.addEventListener('click', () => {
            if (loginModalOverlay) loginModalOverlay.classList.remove('show');
        });
    }

    // 회원가입 모달 'X' 버튼
    if (signupModalClose) {
        signupModalClose.addEventListener('click', () => {
            if (signupModalOverlay) signupModalOverlay.classList.remove('show');
        });
    }

    // 로그인 모달 배경 클릭 시 닫기
    if (loginModalOverlay) {
        loginModalOverlay.addEventListener('click', (event) => {
            if (event.target === loginModalOverlay) {
                loginModalOverlay.classList.remove('show');
            }
        });
    }

    // 회원가입 모달 배경 클릭 시 닫기
    if (signupModalOverlay) {
        signupModalOverlay.addEventListener('click', (event) => {
            if (event.target === signupModalOverlay) {
                signupModalOverlay.classList.remove('show');
            }
        });
    }


    // --- 6. 폼 제출 로직 ---

    // (A) 로그인 폼 제출 (fetch 사용)
    if (modalLoginForm) {
        modalLoginForm.addEventListener('submit', async (event) => { // async 추가
            event.preventDefault();
            let idInput = modalLoginForm.querySelector('input[name="id"]').value;
            let passwordInput = modalLoginForm.querySelector('input[name="password"]').value;

            try {
                let response = await fetch("/api/member/login", {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: idInput, password: passwordInput })
                });

                let result = await response.json();

                if (response.ok) {
                    // 로그인 성공 (Controller에서 200 OK 반환)
                    alert(result.message); // "로그인 성공!"
                    window.location.reload(); // 세션이 적용된 페이지로 새로고침
                } else {
                    // 로그인 실패 (Controller에서 401 Unauthorized 등 반환)
                    alert(result.message); // "아이디 또는 비밀번호가..."
                }
            } catch (error) {
                console.error("Login request failed:", error);
                alert("로그인 요청 중 오류가 발생했습니다.");
            }
        });
    }

    // (B) 회원가입 폼 제출 (fetch 사용)
    if (modalSignupForm) {
        // sign.html에서 가져온 유효성 검사 로직
        const regex = {
            id: /^[a-z0-9]{5,20}$/,
            password: /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,16}$/,
            email: /^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/
        };

        modalSignupForm.addEventListener('submit', async (e) => { // async 추가
            e.preventDefault();

            // (수정) 폼 입력 순서 변경에 맞춰 변수 순서 변경 (가독성)
            const email = modalSignupForm.email.value.trim();
            const id = modalSignupForm.id.value.trim();
            const username = modalSignupForm.username.value.trim();
            const pw = modalSignupForm.password.value;
            const pwCheck = modalSignupForm.passwordCheck.value;

            // --- 유효성 검사 ---
            if (!regex.email.test(email)) return alert("이메일 형식이 잘못되었습니다.");
            if (!regex.id.test(id)) return alert("ID 형식이 잘못되었습니다. (영문 소문자/숫자 5~20자)");
            if (username.length < 2) return alert("사용자 이름을 2자 이상 입력해주세요.");
            if (!regex.password.test(pw)) return alert("비밀번호 형식이 잘못되었습니다. (영문/숫자/특수문자 포함 8~16자)");
            if (pw !== pwCheck) return alert("비밀번호가 일치하지 않습니다.");
            // --- 유효성 검사 끝 ---

            try {
                let signupData = {
                    id: id,
                    password: pw,
                    username: username,
                    email: email
                };

                let response = await fetch("/api/member/signup", {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(signupData)
                });

                let result = await response.json();

                if (response.ok) {
                    // 회원가입 성공 (Controller에서 200 OK 반환)
                    alert(result.message); // "회원가입 완료! 로그인해주세요."

                    // (수정) 회원가입 모달 닫고 로그인 모달 열기
                    if (signupModalOverlay) signupModalOverlay.classList.remove('show');
                    if (loginModalOverlay) loginModalOverlay.classList.add('show');
                } else {
                    // 회원가입 실패 (Controller에서 409 Conflict 등 반환)
                    alert(result.message); // "이미 사용 중인 ID입니다." 등
                }
            } catch (error) {
                console.error("Signup request failed:", error);
                alert("회원가입 요청 중 오류가 발생했습니다.");
            }
        });
    }
});