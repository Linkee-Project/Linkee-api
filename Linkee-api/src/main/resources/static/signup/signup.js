const API_BASE = "/api/v1/auth/signup";

async function handleSignup(event) {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const nickname = document.getElementById("nickname").value.trim();
    const password = document.getElementById("password").value.trim();
    const confirm = document.getElementById("confirm").value.trim();
    const message = document.getElementById("message");

    if (!email || !nickname || !password || !confirm) {
        message.textContent = "모든 필드를 입력해주세요.";
        return;
    }

    if (password !== confirm) {
        message.textContent = "비밀번호가 일치하지 않습니다.";
        return;
    }

    try {
        const res = await fetch(API_BASE, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                userEmail: email,
                userNickname: nickname,
                userPassword: password,
            }),
        });

        const data = await res.text();

        if (!res.ok) {
            if (data.error && data.error.includes("이메일")) {
                emailError.textContent = data.error; // 🔹 이메일 밑에 표시
                emailError.style.color = "red";
            } else {
                message.textContent = data.error || "회원가입에 실패했습니다.";
                message.style.color = "red";
            }
            return;
        }

        message.style.color = "#0094F6";
        message.textContent = "회원가입 성공! 로그인 페이지로 이동 중...";

        setTimeout(() => {
            window.location.href = "/login/login.html";
        }, 1000);
    } catch (err) {
        console.error(err);
        message.textContent = "서버 연결 오류가 발생했습니다.";
    }
}
