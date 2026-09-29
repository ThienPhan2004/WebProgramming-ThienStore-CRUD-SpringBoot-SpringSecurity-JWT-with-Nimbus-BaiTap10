async function postJson(url, body) {
    const res = await fetch(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'same-origin',
        body: body ? JSON.stringify(body) : undefined
    });
    let data = null;
    try { data = await res.json(); } catch (e) { /* body rỗng */ }
    return {ok: res.ok, status: res.status, data};
}

function showError(result) {
    const box = document.getElementById('alert');
    if (!box) return;
    let msg = (result.data && result.data.detail) || 'Đã có lỗi xảy ra';
    if (result.data && result.data.errors) {
        msg = Object.values(result.data.errors).join(' • ');
    }
    box.textContent = msg;          // textContent: tránh XSS
    box.classList.remove('d-none');
}

const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const result = await postJson('/api/auth/login', {
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        });
        // Cookie HttpOnly đã được server set, không cần lưu token vào localStorage
        result.ok ? (window.location.href = '/profile') : showError(result);
    });
}

const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const result = await postJson('/api/auth/signup', {
            fullName: document.getElementById('fullName').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        });
        result.ok ? (window.location.href = '/login') : showError(result);
    });
}

const logoutBtn = document.getElementById('logoutBtn');
if (logoutBtn) {
    logoutBtn.addEventListener('click', async () => {
        await postJson('/api/auth/logout');
        window.location.href = '/login';
    });
}
