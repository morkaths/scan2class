# Tài liệu Thiết kế Kỹ thuật: Hệ thống Điểm danh Lớp học Trực tuyến

**Phiên bản:** 1.1 (Updated Configuration)
**Ngày tạo:** 31/12/2025

---

## 1. Kiến trúc Hệ thống (System Architecture)

Dự án được xây dựng dựa trên mô hình **MVC (Model-View-Controller)** truyền thống, sử dụng **Spring Framework 5.x** thuần (Non-Boot) kết hợp với **Hibernate 5.x** trên nền tảng **Java 17**.

### Mô hình MVC

- **Model**: Các Entity JPA (`UserEntity`, `SessionEntity`, v.v.) ánh xạ trực tiếp với bảng trong MySQL. DTOs (`ClassroomStatsDTO`) được sử dụng để chuyển dữ liệu giữa các lớp.
- **View**: Sử dụng **JSP (JavaServer Pages)** kết hợp thư viện **JSTL** để render giao diện phía server. Frontend sử dụng **Bootstrap 5** và **jQuery**.
- **Controller**: Các lớp `@Controller` của Spring MVC xử lý Request, gọi Service và trả về View name.

### Cấu hình Hệ thống (Pure Java Configuration)

Dự án sử dụng cơ chế **Java Configuration** hiện đại (thay thế hoàn toàn `web.xml` và các file XML cũ), thể hiện khả năng làm chủ các API cấu hình cốt lõi của Spring:

1.  **`WebInitializer`** (thay thế `web.xml`):
    - Kế thừa `AbstractAnnotationConfigDispatcherServletInitializer`.
    - Tự động khởi tạo **Spring Container** khi ứng dụng deploy lên Tomcat.
    - Cấu hình **RootConfig** (`AppConfig`, `DataSourceConfig`, `JpaConfig`) cho tầng Service/Repository.
    - Cấu hình **ServletConfig** (`WebConfig`) cho tầng MVC.
    - Đăng ký `DispatcherServlet` để xử lý mọi request (`/`).

```java
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { AppConfig.class, DataSourceConfig.class, JpaConfig.class, JpaAuditingConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
```

2.  **`WebConfig`**:
    - Sử dụng `@EnableWebMvc` để kích hoạt Spring MVC.
    - Cấu hình **ViewResolver**: Ánh xạ view logic về folder `/WEB-INF/views/` + đuôi `.jsp`.
    - Cấu hình **ResourceHandlers**: Phục vụ file tĩnh (CSS/JS) từ thư mục `/assets/`.

---

## 2. Giải pháp Kỹ thuật Cốt lõi (Core Technical Solutions)

### 2.1. Cơ chế Mã QR Động (Dynamic QR Mechanism)

Mục tiêu là chống việc sinh viên chụp ảnh mã QR và gửi cho bạn bè điểm danh hộ.

- **Server-Side**:
  - Mỗi `SessionEntity` có một trường `token` (String).
  - Khi tạo phiên, Token được sinh ngẫu nhiên dùng `java.util.UUID`.
  - Có API để làm mới token định kỳ (ví dụ: mỗi 15-30 giây), cập nhật lại vào DB.
- **Client-Side (Giảng viên)**:
  - Sử dụng **JavaScript (`setInterval`)** để gọi API lấy token mới.
  - Sử dụng thư viện **`qrcode.js`** để vẽ lại mã QR với nội dung: `APP_URL/attend?token={DYNAMIC_TOKEN}`.

### 2.2. Xác thực Vị trí (Geolocation Validation)

Kết hợp định vị GPS của thiết bị và thuật toán Haversine để tạo "Geofencing" (Hàng rào ảo) quanh lớp học.

- **Logic**:

  1.  Client gửi tọa độ `(lat, lon)` và độ chính xác `accuracy` (do phần cứng GPS cung cấp).
  2.  Server lấy tọa độ tâm lớp học và bán kính cho phép (`radius`).
  3.  Tính khoảng cách bằng công thức **Haversine** (tính độ cong bề mặt trái đất).
  4.  So sánh: `Khoảng cách tính được` <= `Bán kính lớp` + `Sai số GPS cho phép`.

- **Code triển khai (Java)**:

```java
// Haversine Formula Implementation
public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371000; // Radius of earth in meters
    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}
```

### 2.3. Cơ chế Upsert Thống kê Chính xác (Analytics & Reporting)

Vấn đề lớn nhất của báo cáo điểm danh là hiển thị cả những sinh viên **không bao giờ đi học** (không có bản ghi trong bảng `attendance_records`).

- **Giải pháp**: Sử dụng **Native SQL Query** với `LEFT JOIN`.

  - Bảng gốc (Left Table): `class_participants` (Danh sách sinh viên trong lớp).
  - Bảng ghép (Right Table): `attendance_records` (Các lần điểm danh).
  - Việc `LEFT JOIN` đảm bảo sinh viên luôn xuất hiện, dù cột `status` bên phải là `NULL` (được tính là Vắng).

- **Query Mẫu**:

```sql
SELECT u.fullname,
       COALESCE(SUM(CASE WHEN ar.status = 'PRESENT' THEN 1 ELSE 0 END), 0) as present,
       COALESCE(SUM(CASE WHEN ar.status = 'ABSENT' THEN 1 ELSE 0 END), 0) as absent
FROM class_participants cp
JOIN users u ON cp.user_id = u.uid
LEFT JOIN attendance_records ar
       ON ar.user_id = u.uid
       AND ar.session_id IN (SELECT id FROM sessions WHERE class_id = :clsId)
WHERE cp.class_id = :clsId
GROUP BY u.uid
```

---

## 3. Thiết kế Cơ sở dữ liệu (Database Design)

### Entity Relationship Diagram (ERD) Overview

1.  **`users`**: Bảng người dùng trung tâm (`uid`, `username`, `password`, `email`).
2.  **`classes`**: Lớp học (`id`, `code`, `owner_id`).
    - Quan hệ n-n với Users thông qua bảng phụ `class_participants`.
3.  **`sessions`**: Các buổi điểm danh (`id`, `token`, `class_id`, `latitude`, `longitude`, `radius`).
    - Một Lớp có nhiều Buổi (1-n).
4.  **`attendance_records`**: Bản ghi điểm danh (`id`, `session_id`, `user_id`, `status` [PRESENT/LATE/ABSENT]).
    - Là bảng kết quả giao giữa User và Session.

### Audit & Security Fields

Để chống gian lận và hỗ trợ hậu kiểm (Audit Trail), các trường sau là bắt buộc:

- `device_uid` & `device_info`: Lưu User-Agent hoặc Fingerprint của thiết bị điểm danh. Giúp phát hiện 1 sinh viên điểm danh cho nhiều người trên cùng 1 máy.
- `cheat_status`: Cờ đánh dấu các hành vi đáng ngờ (ví dụ: thay đổi vị trí quá nhanh).
- `created_at`, `updated_at`: Thời gian thực của system (không thể làm giả bởi client).

---

## 4. Quy trình Nghiệp vụ (Business Flow)

1.  **Khởi tạo (Teacher)**:
    - Giảng viên vào chi tiết lớp -> Tạo phiên mới.
    - Hệ thống lấy tọa độ hiện tại của giảng viên làm tâm điểm danh.
2.  **Kích hoạt (System)**:
    - Hệ thống sinh Token đầu tiên -> Hiển thị QR.
    - `setInterval` chạy mỗi 30s để refresh QR.
3.  **Điểm danh (Student)**:
    - Sinh viên quét QR -> Redirect đến trang xác nhận `/attend?token=...`.
    - Trình duyệt yêu cầu quyền truy cập GPS (`navigator.geolocation`).
    - Client gửi POST request chứa: `token`, `lat`, `long`, `accuracy`.
4.  **Xử lý (Backend)**:
    - Kiểm tra Token còn hạn? -> Kiểm tra Sinh viên thuộc lớp?
    - Tính khoảng cách -> Nếu > Radius -> Reject.
    - Nếu hợp lệ -> `INSERT` vào `attendance_records` với status `PRESENT`.
5.  **Kết quả**:
    - Màn hình Teacher cập nhật realtime (qua Polling hoặc AJAX reload) số lượng sinh viên đã có mặt.

---

## 5. Kết luận & Hướng phát triển

### Đánh giá

- **Bảo mật**: Ở mức khá tốt nhờ cơ chế QR động (chống chụp ảnh) và Geofencing (chống điểm danh từ xa).
- **Hiệu năng**: Tốt cho quy mô vừa và nhỏ. Native Query giúp thống kê nhanh.

### Đề xuất Tương lai

1.  **Google OAuth2**: Thay thế cơ chế đăng nhập truyền thống để tăng tiện ích và bảo mật.
2.  **WebSockets (Stomp/SockJS)**: Thay thế cơ chế Polling (gọi API liên tục) bằng kết nối 2 chiều dể cập nhật danh sách sinh viên realtime mượt mà hơn.
3.  **IP Filtering**: Chặn các dải IP không thuộc mạng Wifi của trường học (tăng cường lớp bảo mật thứ 3).
4.  **Scalability**: Cần tách module tính toán thống kê ra một Service riêng hoặc sử dụng Redis để cache kết quả nếu lượng dữ liệu điểm danh lên tới hàng triệu bản ghi.

---

## 6. Tính năng Nổi bật (Outstanding Features)

Ngoài các giải pháp kỹ thuật cốt lõi, hệ thống còn sở hữu những tính năng thực tiễn, giải quyết triệt để các bài toán của điểm danh truyền thống:

### 🌟 Hệ thống Chống gian lận 3 Lớp (Triple-Layer Anti-Cheat)

1.  **Mã QR Động**: Mã làm mới mỗi 15-30 giây, khiến việc chụp ảnh màn hình trở nên vô dụng.
2.  **Geofencing (Hàng rào ảo)**: Kiểm tra tọa độ GPS của người học so với vị trí giảng viên. Nếu khoảng cách > 50-100m, hệ thống tự động từ chối.
3.  **Device Fingerprint**: (Đang phát triển) Ghi nhận thông tin thiết bị để cảnh báo khi 1 máy điểm danh cho nhiều người.

### 📊 Báo cáo & Thống kê Thông minh

- **Real-time Dashboard**: Giảng viên thấy sỉ số lớp nhảy số ngay lập tức khi SV quét mã.
- **Biểu đồ Trực quan**: Tích hợp **Chart.js** để hiển thị Tỷ lệ Chuyên cần (Pie Chart) và Top Vắng (Bar Chart).
- **Xuất Excel chuẩn**: Tính năng xuất báo cáo ra file Excel (.xlsx) với định dạng đẹp, sẵn sàng để nộp lên phòng đào tạo.

### 👥 Quản lý Lớp học Linh hoạt

- **Mã mời / Link tham gia**: Sinh viên có thể tự ghi danh vào lớp thông qua mã code hoặc QR tham gia lớp, giảm tải việc nhập liệu thủ công cho giảng viên.
- **Quản lý Sỉ số**: Giảng viên có quyền thêm/xóa sinh viên khỏi lớp dễ dàng.

### 📱 Giao diện Hiện đại & Tối ưu Mobile

- Thiết kế **Responsive** với Bootstrap 5, đảm bảo hiển thị tốt trên cả Laptop (Giảng viên) và Smartphone (Sinh viên).
- Sử dụng **AJAX** cho các thao tác điểm danh, mang lại trải nghiệm mượt mà không reload trang.
