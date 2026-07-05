
       

        const cats = ["Tất cả", "MacBook", "Gaming", "Đồ họa", "Văn phòng", "Học tập"];
        const tagMap = { hot: ["danger", "🔥 Hot"], new: ["success", "✨ Mới"], sale: ["warning", "🏷 Giảm"] };
        let activeFilter = "Tất cả";
        let cart = {};

        function fmt(n) { return (n / 1000000).toFixed(1).replace(/\.0$/, "") + " triệu"; }

        function showToast(msg) {
            document.getElementById("toast-msg").textContent = msg;
            bootstrap.Toast.getOrCreateInstance(document.getElementById("liveToast"), { delay: 2000 }).show();
        }

        function renderFilters() {
            document.getElementById("filters").innerHTML = cats.map(c => `
    <button class="filter-pill${c === activeFilter ? " active" : ""}" onclick="setFilter('${c}')">${c}</button>
  `).join("");
        }

        function setFilter(c) { activeFilter = c; renderFilters(); renderGrid(); }

        function getSorted(list) {
            const v = document.getElementById("sort").value;
            const arr = [...list];
            if (v === "price-asc") arr.sort((a, b) => a.price - b.price);
            else if (v === "price-desc") arr.sort((a, b) => b.price - a.price);
            else if (v === "name") arr.sort((a, b) => a.name.localeCompare(b.name));
            return arr;
        }

     function renderGrid() {
    const q = document.getElementById("search").value.toLowerCase();
    
    // Nếu 'products' chưa load kịp, hãy dừng lại
    if (typeof products === 'undefined') return;

    let list = products;
    
    // Logic lọc theo danh mục
    if (activeFilter !== "Tất cả") {
        list = list.filter(p => p.cat === activeFilter);
    }
    
    // Logic tìm kiếm
    if (q) {
        list = list.filter(p => (p.name + p.brand).toLowerCase().includes(q));
    }
    
    // ... phần còn lại của renderGrid giữ nguyên ...
}

       // js.js
function addCart(id, btn) {
    // 1. Tìm sản phẩm từ mảng 'products' mà ta đã truyền từ Java sang
    const p = products.find(prod => prod.productId === id); 
    
    if (!p) {
        console.error("Không tìm thấy sản phẩm với ID:", id);
        return;
    }

    cart[id] = (cart[id] || 0) + 1;
    
    // 2. Cập nhật số lượng trên giao diện
    const totalCount = Object.values(cart).reduce((a, b) => a + b, 0);
    document.getElementById("cart-count").textContent = totalCount;
    
    // 3. Hiệu ứng nút bấm
    btn.innerHTML = '<i class="bi bi-check"></i> Đã thêm';
    btn.classList.replace("btn-outline-dark", "btn-dark");
    showToast("✓ Đã thêm: " + p.productName); // Sửa p.name thành p.productName (theo class Java)
    
    setTimeout(() => { 
        btn.innerHTML = '<i class="bi bi-plus"></i> Thêm'; 
        btn.classList.replace("btn-dark", "btn-outline-dark"); 
    }, 1500);
    
    renderCartSidebar();
}

        function changeQty(id, delta) {
            cart[id] = (cart[id] || 0) + delta;
            if (cart[id] <= 0) delete cart[id];
            const total = Object.values(cart).reduce((a, b) => a + b, 0);
            document.getElementById("cart-count").textContent = total;
            renderCartSidebar();
        }

        function renderCartSidebar() {
    const keys = Object.keys(cart).filter(id => cart[id] > 0);
    const itemsEl = document.getElementById("cart-items");
    const footerEl = document.getElementById("cart-footer");
    
    if (!keys.length) {
        itemsEl.innerHTML = `<div class="text-center py-5 text-muted">🛒<p>Giỏ hàng trống</p></div>`;
        footerEl.style.setProperty("display", "none", "important"); 
        return;
    }

    let total = 0;
    itemsEl.innerHTML = keys.map(id => {
        // Lưu ý: dùng 'prod.productId' thay vì 'prod.id'
        const p = products.find(prod => prod.productId == id); 
        total += p.price * cart[id];
        
        return `<div class="d-flex align-items-center gap-3 py-3 border-bottom">
      <div class="flex-grow-1">
        <p class="fw-500 mb-0" style="font-size:13px">${p.productName}</p>
        <p class="text-muted mb-0" style="font-size:12px">${fmt(p.price)} / chiếc</p>
      </div>
      <div class="d-flex align-items-center gap-2">
        <button class="qty-btn" onclick="changeQty(${id},-1)">−</button>
        <span>${cart[id]}</span>
        <button class="qty-btn" onclick="changeQty(${id},1)">+</button>
      </div>
    </div>`;
    }).join("");
    
    document.getElementById("cart-total").textContent = (total / 1000000).toFixed(1) + " triệu";
    footerEl.style.setProperty("display", "block", "important");
}

        renderFilters();
        
