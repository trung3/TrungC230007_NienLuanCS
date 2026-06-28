
        const products = [
            { id: 1, brand: "Apple", name: 'MacBook Air M3 13"', price: 27900000, specs: ["Apple M3", "8GB RAM", "256GB SSD", '13.6" IPS'], tag: "hot", cat: "MacBook", emoji: "💻" },
            { id: 2, brand: "Apple", name: 'MacBook Pro M3 Pro 14"', price: 49900000, specs: ["M3 Pro", "18GB RAM", "512GB SSD", '14.2" MiniLED'], tag: "new", cat: "MacBook", emoji: "💻" },
            { id: 3, brand: "Apple", name: 'MacBook Air M2 15"', price: 32900000, oldPrice: 34900000, specs: ["Apple M2", "8GB RAM", "256GB SSD", '15.3" IPS'], tag: "sale", cat: "MacBook", emoji: "💻" },
            { id: 4, brand: "ASUS", name: "ROG Strix G16 2025", price: 35500000, oldPrice: 38000000, specs: ["i9-13980HX", "RTX 4070", "32GB RAM", "1TB SSD"], tag: "sale", cat: "Gaming", emoji: "🎮" },
            { id: 5, brand: "MSI", name: "Titan GT77 HX", price: 72000000, specs: ["i9-13980HX", "RTX 4090", "64GB RAM", "4TB SSD"], tag: "hot", cat: "Gaming", emoji: "🎮" },
            { id: 6, brand: "Lenovo", name: "Legion Pro 7i Gen 9", price: 58000000, specs: ["i9-14900HX", "RTX 4080", "32GB RAM", "1TB SSD"], cat: "Gaming", emoji: "🎮" },
            { id: 7, brand: "Dell", name: "XPS 15 OLED", price: 42000000, oldPrice: 46000000, specs: ["i7-13700H", "RTX 4060", "16GB RAM", "512GB SSD"], tag: "sale", cat: "Đồ họa", emoji: "🖥️" },
            { id: 8, brand: "Dell", name: "Precision 7680", price: 85000000, specs: ["i9-13950HX", "RTX 3500 Ada", "64GB RAM", "2TB SSD"], cat: "Đồ họa", emoji: "🖥️" },
            { id: 9, brand: "Lenovo", name: "ThinkPad X1 Carbon Gen 12", price: 36000000, specs: ["Intel Ultra 7", "16GB RAM", "512GB SSD", '14" IPS'], cat: "Văn phòng", emoji: "💼" },
            { id: 10, brand: "HP", name: "Spectre x360 14", price: 31500000, specs: ["Intel Ultra 5", "16GB RAM", "512GB SSD", "OLED 2.8K"], tag: "new", cat: "Văn phòng", emoji: "💼" },
            { id: 11, brand: "ASUS", name: "Vivobook 15 OLED", price: 14900000, oldPrice: 17000000, specs: ["Ryzen 5 7530U", "8GB RAM", "512GB SSD", "OLED FHD"], tag: "sale", cat: "Học tập", emoji: "📚" },
            { id: 12, brand: "Acer", name: "Swift Go 14 AI", price: 18500000, specs: ["Intel Ultra 5", "16GB RAM", "512GB SSD", "IPS 2K"], tag: "new", cat: "Học tập", emoji: "📚" },
        ];

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
            let list = products;
            if (activeFilter !== "Tất cả") list = list.filter(p => p.cat === activeFilter);
            if (q) list = list.filter(p => (p.name + p.brand + p.specs.join()).toLowerCase().includes(q));
            const sorted = getSorted(list);
            document.getElementById("result-count").textContent = sorted.length + " sản phẩm";
            if (!sorted.length) {
                document.getElementById("grid").innerHTML = `<div class="col-12 text-center py-5 text-muted"><div style="font-size:48px">🔍</div><p class="mt-2">Không tìm thấy sản phẩm</p></div>`;
                return;
            }
            document.getElementById("grid").innerHTML = sorted.map(p => {
                const [tagColor, tagLabel] = p.tag ? tagMap[p.tag] : [];
                return `
    <div class="col-6 col-md-4 col-lg-3">
      <div class="card h-100 product-card bg-white">
        <div class="card-img-wrap">
          <span class="card-emoji">${p.emoji}</span>
          ${p.tag ? `<span class="badge bg-${tagColor} position-absolute top-0 start-0 m-2" style="font-size:10px;border-radius:20px;padding:3px 9px">${tagLabel}</span>` : ""}
        </div>
        <div class="card-body p-3">
          <p class="brand-label mb-1">${p.brand}</p>
          <p class="card-title mb-2">${p.name}</p>
          <div class="d-flex flex-wrap gap-1 mb-3">
            ${p.specs.map(s => `<span class="spec-badge">${s}</span>`).join("")}
          </div>
          <div class="d-flex align-items-center justify-content-between mt-auto">
            <div>
              <span class="price-main">${fmt(p.price)}</span>
              ${p.oldPrice ? `<span class="price-old ms-1">${fmt(p.oldPrice)}</span>` : ""}
            </div>
            <button class="btn btn-outline-dark add-btn" onclick="addCart(${p.id},this)">
              <i class="bi bi-plus"></i> Thêm
            </button>
          </div>
        </div>
      </div>
    </div>`;
            }).join("");
        }

        function addCart(id, btn) {
            cart[id] = (cart[id] || 0) + 1;
            const total = Object.values(cart).reduce((a, b) => a + b, 0);
            document.getElementById("cart-count").textContent = total;
            const p = products.find(p => p.id === id);
            btn.innerHTML = '<i class="bi bi-check"></i> Đã thêm';
            btn.classList.replace("btn-outline-dark", "btn-dark");
            showToast("✓ Đã thêm: " + p.name);
            setTimeout(() => { btn.innerHTML = '<i class="bi bi-plus"></i> Thêm'; btn.classList.replace("btn-dark", "btn-outline-dark"); }, 1500);
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
                itemsEl.innerHTML = `<div class="text-center py-5 text-muted"><div style="font-size:48px">🛒</div><p class="mt-2">Giỏ hàng trống</p></div>`;
                footerEl.style.setProperty("display", "none", "important"); return;
            }
            let total = 0;
            itemsEl.innerHTML = keys.map(id => {
                const p = products.find(p => p.id == id);
                total += p.price * cart[id];
                return `<div class="d-flex align-items-center gap-3 py-3 border-bottom">
      <span class="cart-item-emoji">${p.emoji}</span>
      <div class="flex-grow-1 min-width-0">
        <p class="fw-500 mb-0" style="font-size:13px;font-weight:500">${p.name}</p>
        <p class="text-muted mb-0" style="font-size:12px">${fmt(p.price)} / chiếc</p>
      </div>
      <div class="d-flex align-items-center gap-2">
        <button class="qty-btn" onclick="changeQty(${id},-1)">−</button>
        <span style="font-size:13px;font-weight:500;min-width:16px;text-align:center">${cart[id]}</span>
        <button class="qty-btn" onclick="changeQty(${id},1)">+</button>
      </div>
    </div>`;
            }).join("");
            document.getElementById("cart-total").textContent = (total / 1000000).toFixed(1).replace(/\.0$/, "") + " triệu";
            footerEl.style.setProperty("display", "block", "important");
        }

        renderFilters();
        renderGrid();
