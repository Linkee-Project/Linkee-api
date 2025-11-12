const API_BASE = "/api/v1/auth/login";

async function handleLogin(event) {
    event.preventDefault();

    alert("1")
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const message = document.getElementById("message");

    if (!email || !password) {
        message.textContent = "이메일과 비밀번호를 입력해주세요.";
        return;
    }

    try {
        const res = await fetch(API_BASE, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({
                userEmail: email,
                password: password
            }),
        });
        alert("2")

        // ✅ JWT 로그인에서는 JSON 응답을 받아야 하므로 추가
        const data = await res.json();

        if (!res.ok || !data.accessToken) {
            message.textContent = "로그인 실패: 이메일 또는 비밀번호를 확인해주세요.";
            return;
        }

        // ✅ 토큰 저장
        localStorage.setItem("accessToken", data.accessToken);
        localStorage.setItem("refreshToken", data.refreshToken);

        message.style.color = "#0094F6";
        message.textContent = "로그인 성공! 페이지로 이동 중...";

        setTimeout(() => window.location.href = "/home/home.html", 1000);



    } catch (err) {
        alert("err"+err)

        console.error(err);
        message.textContent = "서버 연결 오류가 발생했습니다.";
    }
}
