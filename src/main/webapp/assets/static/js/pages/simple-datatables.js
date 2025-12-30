let dataTable = new simpleDatatables.DataTable(
  document.getElementById("table") 
);

// Hàm tiện ích: Tách dropdown và search thành cấu trúc Bootstrap Row/Col
function adaptControlsLayout() {
  const container = dataTable.wrapper.querySelector(".dataTable-top");
  const dropdownWrapper = dataTable.wrapper.querySelector(".dataTable-dropdown");
  const searchWrapper = dataTable.wrapper.querySelector(".dataTable-search");
  
  if (container && dropdownWrapper && searchWrapper) {
      
      // 1. Tạo Row để chứa Dropdown và Search
      const controlRow = document.createElement("div");
      // mb-3: thêm khoảng cách dưới; align-items-center: căn giữa theo chiều dọc
      controlRow.classList.add("row", "mb-3", "align-items-center"); 
      
      // 2. Tạo cột cho Dropdown (căn trái)
      const dropdownCol = document.createElement("div");
      // col-12: chiếm hết trên mobile; col-md-auto: chiếm vừa đủ trên tablet/desktop
      dropdownCol.classList.add("col-12", "col-md-auto", "d-flex", "mb-2", "mb-md-0"); // mb-2 cho mobile, mb-md-0 loại bỏ khi đủ rộng
      
      // 3. Tạo cột cho Search (căn phải)
      const searchCol = document.createElement("div");
      // col-12: chiếm hết trên mobile; ms-auto: đẩy sang phải khi đủ rộng
      searchCol.classList.add("col-12", "col-md-4", "ms-md-auto"); // ms-md-auto chỉ căn phải từ medium trở lên

      // ... (Phần Di chuyển các phần tử và Gắn vào container giữ nguyên) ...
      dropdownCol.appendChild(dropdownWrapper);
      searchCol.appendChild(searchWrapper);
      
      controlRow.appendChild(dropdownCol);
      controlRow.appendChild(searchCol);
      
      container.innerHTML = '';
      container.appendChild(controlRow);
  }
  
  // Gọi các hàm áp dụng class Bootstrap
  adaptPageDropdownClasses();
  adaptSearchInputClasses();
}

// Áp dụng class cho dropdown số lượng hàng (Kích thước chuẩn)
function adaptPageDropdownClasses() {
  const selector = dataTable.wrapper.querySelector(".dataTable-selector");
  if (selector) {
    selector.classList.add("form-select"); 
  }
  
  const label = dataTable.wrapper.querySelector(".dataTable-dropdown label");
  if (label) {
    // Dùng Flexbox để căn chỉnh nội dung và dropdown (TẠO KHOẢNG CÁCH GIỮA SỐ VÀ TEXT)
    label.style.display = 'flex';
    label.style.alignItems = 'center';
    label.style.gap = '8px'; // Khoảng cách 8px giữa dropdown và text "entries per page"
  }
}

// Áp dụng class cho khung tìm kiếm (Kích thước chuẩn)
function adaptSearchInputClasses() {
    const searchInput = dataTable.wrapper.querySelector(".dataTable-input");
    if (searchInput) {
        searchInput.classList.add("form-control");
    }
}


// Hàm phân trang (được giữ nguyên để Bootstrap hóa)
function adaptPagination() {
  const paginations = dataTable.wrapper.querySelectorAll(
    "ul.dataTable-pagination-list"
  );
  for (const pagination of paginations) {
    pagination.classList.add(...["pagination", "pagination-primary", "justify-content-end"]);
    pagination.style.listStyle = "none";
    pagination.style.paddingLeft = "0";
  }
  const paginationLis = dataTable.wrapper.querySelectorAll(
    "ul.dataTable-pagination-list li"
  );
  for (const paginationLi of paginationLis) {
    paginationLi.classList.add("page-item");
  }
  const paginationLinks = dataTable.wrapper.querySelectorAll(
    "ul.dataTable-pagination-list li a"
  );
  for (const paginationLink of paginationLinks) {
    paginationLink.classList.add("page-link");
  }
}

const refreshPagination = () => {
  adaptPagination();
};

// Khởi tạo và áp dụng các thay đổi
dataTable.on("datatable.init", () => {
  adaptControlsLayout(); // Tách biệt Dropdown và Search
  refreshPagination();
});

dataTable.on("datatable.update", refreshPagination);
dataTable.on("datatable.sort", refreshPagination);
dataTable.on("datatable.page", adaptPagination);