# JavaBean 모바일 카페 앱 (메인 화면)

## 레이아웃 구조 (Thymeleaf Fragment 활용)

이 프로젝트는 Thymeleaf의 Fragment 기능을 사용하여 `userBaseLayout.html` 파일을 기반으로 공통 레이아웃(상단바, 하단 탭바 등)을 적용했습니다!

### Fragment 적용 방법

모든 메인 페이지(로그인,회원가입,관리자페이지 제외)는 반드시 `th:replace` 구문을 사용하여 `userBaseLayout.html`의 `setContent` Fragment를 호출해주세요!!

```html
<html lang="ko" xmlns:th="[http://www.thymeleaf.org](http://www.thymeleaf.org)">
<th:block th:replace="~{./include/userBaseLayout :: setContent( ~{:: .wrap} ) }">
    <div class="wrap">
        </div>
</th:block>
</html>
```

메인컨텐츠는 꼭 .wrap 안에 작성 부탁드립니다..!!