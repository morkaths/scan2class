# Scan2Class - Hệ thống Điểm danh Thông minh

**Scan2Class** là một giải pháp quản lý điểm danh hiện đại dành cho giáo dục, kết hợp giữa mã QR, xác thực vị trí (Geofencing) và trí tuệ nhân tạo (AI) để đảm bảo tính chính xác, minh bạch và chống gian lận.

---

## 🚀 Tính năng chính

### 1. Điểm danh Chống gian lận

- **Mã QR Theo phiên:** Mỗi buổi học sử dụng một Token UUID duy nhất, ngăn chặn việc sử dụng mã cũ.
- **Xác thực Vị trí (Geofencing):** Sử dụng GPS để kiểm tra sinh viên có thực sự đang ở trong lớp học hay không (bán kính cho phép < 50m).
- **Định danh Thiết bị:** Ghi nhận thông tin thiết bị để phát hiện hành vi điểm danh hộ.

### 2. Quản lý & Theo dõi Real-time

- **Dashboard Giảng viên:** Theo dõi sỉ số lớp cập nhật tức thì qua **WebSockets**.
- **Thống kê trực quan:** Biểu đồ hóa dữ liệu chuyên cần với **Chart.js**.
- **Xuất Báo cáo:** Hỗ trợ xuất file Excel (.xlsx) chuẩn phòng đào tạo qua **Apache POI**.

### 3. Trợ lý ảo AI (S2C Bot)

- **Tích hợp Gemini AI:** Hỗ trợ giải đáp thắc mắc về quy định lớp học và tình hình học tập cá nhân bằng tiếng Việt qua cơ chế **RAG**.

---

## 🛠 Công nghệ sử dụng

### Backend

- **Core:** Java 17, Spring Framework 5.3 (MVC, ORM, Context)
- **Security:** Spring Security 5.8 (Form Login, Google OAuth2, JWT)
- **Database:** MySQL 8.0, Hibernate 5.6, Spring Data JPA
- **Communication:** WebSockets (STOMP/SockJS)

### Frontend

- **Engine:** JSP 2.3 / JSTL 1.2
- **UI Framework:** Bootstrap 5.3, jQuery 3.6
- **Charts:** Chart.js

### Tools & Others

- **AI:** Google Gemini API (GenAI SDK 1.0)
- **Build Tool:** Maven
- **DevOps:** Docker, Docker Compose

---

## ⚙️ Cấu hình và Cài đặt

### Yêu cầu hệ thống

- Docker & Docker Compose
- JDK 17 (nếu chạy local không qua Docker)
- Maven 3.x

### Triển khai nhanh với Docker

1. Sao chép file cấu hình mẫu:
   ```bash
   cp .env.example .env
   ```
2. Cập nhật các thông số trong `.env` (DB_PASSWORD, GOOGLE_CLIENT_ID, GEMINI_API_KEY, v.v.)
3. Khởi chạy hệ thống:
   ```bash
   docker-compose up -d --build
   ```
4. Truy cập hệ thống tại: `http://localhost:8080`

---

## 📄 Tài liệu chi tiết

- [Thiết kế Kỹ thuật (Technical Design)](TECHNICAL_DESIGN.md)

---

© 2025 Morkath/Scan2Class Team.
