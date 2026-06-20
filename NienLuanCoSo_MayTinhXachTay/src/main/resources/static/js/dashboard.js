// ===== CHỈ GIỮ LẠI CÁC HÀM XỬ LÝ GIAO DIỆN (UI) =====

function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    if (sidebar && overlay) {
        sidebar.classList.toggle('-translate-x-full');
        overlay.classList.toggle('hidden');
    }
}

function toggleDarkMode() {
    document.documentElement.classList.toggle('dark');
}

function toggleNotifications() {
    const panel = document.getElementById('notificationsPanel');
    if (panel) panel.classList.toggle('hidden');
}

// Đóng thông báo khi click ra ngoài
document.addEventListener('click', (e) => {
    const panel = document.getElementById('notificationsPanel');
    if (panel && !panel.contains(e.target) && !e.target.closest('[onclick="toggleNotifications()"]')) {
        panel.classList.add('hidden');
    }
});

// Xóa hoàn toàn các hàm renderOrdersTable(), initStaticCharts(), mockData... 
// vì chúng ta đã dùng Thymeleaf để vẽ dữ liệu thật rồi!