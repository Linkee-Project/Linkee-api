const API_BASE = "/api/v1/inquiry";

let state = {
    page: 0,
    size: 10,
    totalPages: 0,
    items: [],
    selectedInquiryId: null,
};

// ✅ JWT 토큰에서 userId와 role 추출
function getUserFromToken() {
    const token = localStorage.getItem("accessToken");
    if (!token) return { userId: null, role: "USER" };

    try {
        const base64 = token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/");
        const payload = JSON.parse(atob(base64));

        // 다양한 구조 대응
        return {
            userId: payload.userId || payload.sub || null,
            role:
                payload.role ||
                payload.auth ||
                payload.roles ||
                payload.authorities ||
                "USER",
        };
    } catch (e) {
        console.error("토큰 파싱 실패", e);
        return { userId: null, role: "USER" };
    }
}

// ✅ JWT 헤더 생성
function getAuthHeader() {
    const token = localStorage.getItem("accessToken");
    return token ? { Authorization: `Bearer ${token}` } : {};
}

// ✅ 버튼 표시 (USER만 문의 등록 가능)
function setupButtons() {
    const { role } = getUserFromToken();
    const createBtn = document.getElementById("createBtn");
    if (!createBtn) return;
    createBtn.style.display = role.toUpperCase().includes("USER")
        ? "inline-block"
        : "none";
}

// ✅ 문의 목록 조회
async function fetchList() {
    const list = document.getElementById("list");
    list.innerHTML = `<p>불러오는 중...</p>`;

    try {
        const res = await fetch(`${API_BASE}?page=${state.page}&size=${state.size}`, {
            headers: { ...getAuthHeader() },
        });
        if (!res.ok) throw new Error("목록 조회 실패");

        const data = await res.json();
        let items = data.content || [];
        const { userId, role } = getUserFromToken();

        console.log("🔍 로그인 사용자:", { userId, role });
        console.log("🧾 서버 응답:", items);

        // USER는 자기 문의만 필터링
        if (state.userRole === "USER" && state.userId) {
            const matchedUser = items.find(i => i.userEmail === state.userId);
            if (matchedUser) {
                items = items.filter(i => i.userId === matchedUser.userId);
            }
        }

        state.items = items;
        state.totalPages = data.totalPages || 1;

        renderList(items, role);
        renderPager();
    } catch (e) {
        console.error(e);
        showToast("❌ 서버 연결 실패 — 더미 데이터 표시");
        renderList(dummyData(), "USER");
    }
}

// ✅ 문의 목록 렌더링
function renderList(items, role) {
    const list = document.getElementById("list");

    if (!items.length) {
        list.innerHTML = `<p>등록된 문의가 없습니다.</p>`;
        return;
    }

    list.innerHTML = items
        .map(i => {
            const createdAt = new Date(i.createdAt || Date.now()).toLocaleDateString();
            const canAnswer = role.toUpperCase() === "ADMIN" && i.answerStatus === "N";
            const isUserView = role.toUpperCase() === "USER";

            return `
                <div class="inquiry">
                    <div class="title">${i.inquiryTitle || "제목 없음"}</div>
                    <div class="meta">
                        ${role.toUpperCase() === "ADMIN" ? `작성자: ${i.userName || "알 수 없음"} · ` : ""}
                        ${createdAt}
                    </div>
                    <div class="content">${i.inquiryContent || "(내용 없음)"}</div>

                    <div class="answer">
                        <strong>답변 상태:</strong>
                        ${i.answerStatus === "Y" ? "✅ 답변 완료" : "⌛ 미답변"}
                        ${
                i.answerContent
                    ? `<p>${i.answerContent}</p>`
                    : i.answerStatus === "N" && isUserView
                        ? "<p>답변이 등록되면 표시됩니다.</p>"
                        : ""
            }
                    </div>

                    ${
                canAnswer
                    ? `<div class="actions">
                                <button class="btn btn-primary" onclick="openAnswerModal(${i.inquiryId})">
                                    답변 등록
                                </button>
                              </div>`
                    : ""
            }
                </div>`;
        })
        .join("");
}

// ✅ 페이지네이션
function renderPager() {
    document.getElementById("pageInfo").textContent = `${state.page + 1} / ${state.totalPages}`;
    document.getElementById("prev").disabled = state.page <= 0;
    document.getElementById("next").disabled = state.page + 1 >= state.totalPages;
}

function goPage(delta) {
    const newPage = state.page + delta;
    if (newPage < 0 || newPage >= state.totalPages) return;
    state.page = newPage;
    fetchList();
}

// ✅ 문의 등록
async function createInquiry() {
    const title = document.getElementById("createTitle").value.trim();
    const content = document.getElementById("createContent").value.trim();
    const { userId } = getUserFromToken();

    if (!title || !content) return showToast("제목과 내용을 입력하세요.");
    if (!userId) return showToast("로그인이 필요합니다.");

    try {
        const res = await fetch(API_BASE, {
            method: "POST",
            headers: { "Content-Type": "application/json", ...getAuthHeader() },
            body: JSON.stringify({
                inquiryTitle: title,
                inquiryContent: content,
                userId: userId,
            }),
        });

        if (!res.ok) throw new Error();
        closeCreateModal();
        showToast("✅ 문의 등록 완료");
        fetchList();
    } catch {
        showToast("❌ 문의 등록 실패");
    }
}

// ✅ 답변 등록
async function submitAnswer() {
    const content = document.getElementById("answerContent").value.trim();
    if (!content) return showToast("답변 내용을 입력하세요.");

    try {
        const res = await fetch(`${API_BASE}/answer`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json", ...getAuthHeader() },
            body: JSON.stringify({
                inquiryId: state.selectedInquiryId,
                answerContent: content,
            }),
        });

        if (!res.ok) throw new Error();
        closeAnswerModal();
        showToast("✅ 답변 등록 완료");
        fetchList();
    } catch {
        showToast("❌ 답변 등록 실패");
    }
}

// ✅ 모달 제어
function openCreateModal() {
    document.getElementById("createModal").classList.add("show");
}
function closeCreateModal() {
    document.getElementById("createModal").classList.remove("show");
}
function openAnswerModal(id) {
    state.selectedInquiryId = id;
    document.getElementById("answerModal").classList.add("show");
}
function closeAnswerModal() {
    document.getElementById("answerModal").classList.remove("show");
}

// ✅ 검색
function filterClient() {
    const q = document.getElementById("q").value.toLowerCase();
    const filtered = state.items.filter(i => i.inquiryTitle?.toLowerCase().includes(q));
    const { role } = getUserFromToken();
    renderList(filtered, role);
}
function clearSearch() {
    document.getElementById("q").value = "";
    const { role } = getUserFromToken();
    renderList(state.items, role);
}

// ✅ 더미 데이터 (백엔드 연결 실패 시)
function dummyData() {
    return [
        { inquiryId: 1, inquiryTitle: "링키 오류 문의", userName: "user01", inquiryContent: "로그인 안됩니다", answerStatus: "N", userId: 1 },
        { inquiryId: 2, inquiryTitle: "결제 문의", userName: "user02", inquiryContent: "결제가 두 번 되었어요", answerStatus: "Y", answerContent: "환불 처리 완료", userId: 2 },
    ];
}

// ✅ Toast 메시지
function showToast(msg) {
    const toast = document.getElementById("toast");
    toast.textContent = msg;
    toast.classList.add("show");
    setTimeout(() => toast.classList.remove("show"), 2000);
}

// ✅ 초기 실행
document.addEventListener("DOMContentLoaded", () => {
    setupButtons();
    fetchList();
});
