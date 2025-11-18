const API_BASE = "/api/v1/notice";

let state = {
    page: 0,
    size: 10,
    totalPages: 0,
    items: [],
};

// ✅ JWT 토큰에서 role 확인
function getUserRoleFromToken() {
    const token = localStorage.getItem("accessToken");

    if (!token) return null;

    try {
        const base64 = token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/");
        const payload = JSON.parse(atob(base64));
        // 여러 구조 대응
        return (
            payload.role ||
            payload.auth ||
            payload.roles ||
            payload.authorities ||
            null
        );
    } catch (e) {
        console.error("토큰 파싱 실패", e);
        return null;
    }
}

// ✅ Authorization 헤더 생성
function getAuthHeader() {
    const token = localStorage.getItem("accessToken");
    return token ? { Authorization: `Bearer ${token}` } : {};
}

// ✅ 관리자 버튼 표시
function setupAdminButtons() {
    const role = getUserRoleFromToken();
    const adminBtn = document.getElementById("adminCreateBtn");
    if (!adminBtn) return;

    console.log("현재 사용자 role:", role);

    adminBtn.style.display =
        role && role.toUpperCase().includes("ADMIN")
            ? "inline-block"
            : "none";
}

// ✅ 공지 목록 불러오기
async function fetchList() {
    const list = document.getElementById("list");
    list.innerHTML = `<p>불러오는 중...</p>`;

    try {
        const res = await fetch(`${API_BASE}?page=${state.page}&size=${state.size}`, {
            headers: { ...getAuthHeader() },
        });
        if (!res.ok) throw new Error("목록 조회 실패");

        const data = await res.json();
        const content = data.content || [];
        state.items = content;
        state.totalPages = data.totalPages || 1;

        renderList(content);
        renderPager();
    } catch (e) {
        console.error(e);
        list.innerHTML = `<p>서버 연결 실패 — 더미 데이터로 표시합니다.</p>`;
        renderList(dummyData());
        showToast("더미 데이터를 불러왔습니다.");
    }
}

// ✅ 공지 목록 렌더링
function renderList(items) {
    const role = getUserRoleFromToken();
    const list = document.getElementById("list");

    list.innerHTML = items
        .map(
            (n) => `
        <div class="notice">
            <span class="badge">공지</span>
            <div class="title">${n.noticeTitle || "제목 없음"}</div>
            <div class="meta">작성일: ${new Date(
                n.createdAt || Date.now()
            ).toLocaleDateString()}</div>
            <div class="actions">
                <button class="open" onclick="openDetail(${
                n.noticeId || 0
            })">열기</button>
                ${
                role && role.toUpperCase().includes("ADMIN")
                    ? `
                    <button class="edit" onclick="editNotice(${n.noticeId})">수정</button>
                    <button class="delete" onclick="deleteNotice(${n.noticeId})">삭제</button>
                `
                    : ""
            }
            </div>
        </div>
    `
        )
        .join("");
}

// ✅ 페이지네이션
function renderPager() {
    document.getElementById(
        "pageInfo"
    ).textContent = `${state.page + 1} / ${state.totalPages}`;
    document.getElementById("prev").disabled = state.page <= 0;
    document.getElementById("next").disabled =
        state.page + 1 >= state.totalPages;
}

function goPage(delta) {
    const newPage = state.page + delta;
    if (newPage < 0 || newPage >= state.totalPages) {
        showToast("더 이상 페이지가 없습니다.");
        return;
    }
    state.page = newPage;
    fetchList();
}

function changeSize(v) {
    state.size = Number(v);
    state.page = 0;
    fetchList();
}

function filterClient() {
    const q = document.getElementById("q").value.toLowerCase();
    const filtered = state.items.filter((n) =>
        (n.noticeTitle || "").toLowerCase().includes(q)
    );
    renderList(filtered);
}

function clearSearch() {
    document.getElementById("q").value = "";
    renderList(state.items);
}

// ✅ 상세 보기
async function openDetail(id) {
    const modal = document.getElementById("modal");
    modal.classList.add("show");
    const body = document.getElementById("modalBody");
    body.innerHTML = "불러오는 중...";

    try {
        const res = await fetch(`${API_BASE}/${id}`, {
            headers: { ...getAuthHeader() },
        });
        if (!res.ok) throw new Error("상세 조회 실패");
        const data = await res.json();
        document.getElementById("modalTitle").textContent =
            data.noticeTitle || "제목 없음";
        body.innerHTML = data.noticeContent || "내용이 없습니다.";

    } catch {
        body.innerHTML = "상세 내용을 불러올 수 없습니다.";
    }
}

function closeModal() {
    document.getElementById("modal").classList.remove("show");
}

function reloadNotices() {
    fetchList();
}

// ✅ 공지 등록
function openCreateModal() {
    const modal = document.getElementById("createModal");
    modal.classList.add("show");
}

function closeCreateModal() {
    document.getElementById("createModal").classList.remove("show");
}

async function createNotice() {
    const title = document.getElementById("createTitle").value.trim();
    const content = document.getElementById("createContent").value.trim();

    if (!title || !content) {
        showToast("제목과 내용을 모두 입력하세요.");
        return;
    }

    try {
        const res = await fetch(API_BASE, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                ...getAuthHeader(),
            },
            body: JSON.stringify({
                noticeTitle: title,
                noticeContent: content,
            }),
        });

        if (!res.ok) throw new Error("등록 실패");

        showToast("공지사항이 등록되었습니다.");
        closeCreateModal();
        document.getElementById("createTitle").value = "";
        document.getElementById("createContent").value = "";
        fetchList();
    } catch (e) {
        console.error(e);
        showToast("등록 중 오류 발생");
    }
}

// ✅ 공지 수정
async function editNotice(id) {
    const title = prompt("새 제목을 입력하세요:");
    if (title == null) return;
    const content = prompt("새 내용을 입력하세요:");
    if (content == null) return;

    try {
        const res = await fetch(`${API_BASE}/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                ...getAuthHeader(),
            },
            body: JSON.stringify({
                noticeId: id,
                noticeTitle: title,
                noticeContent: content,
            }),
        });
        if (!res.ok) throw new Error("수정 실패");
        showToast("수정 완료");
        fetchList();
    } catch (e) {
        console.error(e);
        showToast("수정 중 오류 발생");
    }
}

// ✅ 공지 삭제
async function deleteNotice(id) {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    try {
        const res = await fetch(`${API_BASE}/${id}/delete`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                ...getAuthHeader(),
            },
            body: JSON.stringify({}),
        });

        if (!res.ok) throw new Error("삭제 실패");
        showToast("공지사항이 삭제되었습니다.");
        fetchList();
    } catch (e) {
        console.error(e);
        showToast("삭제 중 오류 발생");
    }
}

// ✅ 더미 데이터
function dummyData() {
    return [
        { noticeTitle: "링키 서비스 점검 안내", createdAt: Date.now() - 1000000 },
        { noticeTitle: "신규 기능 업데이트 공지", createdAt: Date.now() - 2000000 },
    ];
}

// ✅ 토스트 메시지
function showToast(msg) {
    const t = document.getElementById("toast");
    t.textContent = msg;
    t.classList.add("show");
    setTimeout(() => t.classList.remove("show"), 2000);
}

// ✅ 초기 실행
document.addEventListener("DOMContentLoaded", () => {
    fetchList();
    setupAdminButtons();

    // ✅ 홈으로 버튼 클릭 시 이동
    const homeBtn = document.getElementById("homeBtn");
    if (homeBtn) {
        homeBtn.addEventListener("click", () => {
            window.location.href = "../home/home.html";
        });
    }
});
