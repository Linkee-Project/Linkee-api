const API_BASE = "/api/v1/auth/login";

async function handleLogin(event) {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const message = document.getElementById("message");

    if (!email || !password) {
        message.textContent = "이메일과 비밀번호를 입력해주세요.";
        return;
    }

    try {
        const formData = new URLSearchParams();
        formData.append("username", email); // ⚠️ Spring Security는 username, password 키를 기본으로 찾음
        formData.append("password", password);

        const res = await fetch(API_BASE, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: formData.toString(),
        });

        if (res.redirected) {
            // formLogin은 redirect 응답을 주기 때문에 바로 이동
            window.location.href = res.url;
            return;
        }

        if (!res.ok) {
            message.textContent = "로그인 실패: 이메일 또는 비밀번호를 확인해주세요.";
            return;
        }

        message.style.color = "#0094F6";
        message.textContent = "로그인 성공! 페이지로 이동 중...";
        setTimeout(() => window.location.href = "/notice/notice.html", 1000);
    } catch (err) {
        console.error(err);
        message.textContent = "서버 연결 오류가 발생했습니다.";
    }
}
