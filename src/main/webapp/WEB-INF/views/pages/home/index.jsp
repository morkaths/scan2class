<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<section class="hero-section text-center py-5 py-md-5 d-flex align-items-center bg-primary text-white" 
         style="min-height: 80vh; background: linear-gradient(135deg, #435ebe 0%, #647ed1 100%);">
    <div class="container">
        <h1 class="display-4 display-md-2 fw-bold mb-3 text-white">Hệ thống Điểm danh Thông minh</h1>
        <p class="lead mb-4 mx-auto shadow-text" style="max-width: 700px;">
            Quản lý lớp học hiệu quả và theo dõi điểm danh bảo mật với QR Code động và xác thực vị trí GPS.
        </p>
        <div class="d-flex flex-column flex-sm-row justify-content-center gap-3 px-4">
            <a href="<c:url value='/classrooms/create' />" 
               class="btn btn-light btn-lg px-md-5 fw-bold text-primary shadow">Tạo lớp học</a>
            <a href="#features" 
               class="btn btn-outline-light btn-lg px-md-5 shadow-sm">Tìm hiểu thêm</a>
        </div>
    </div>
</section>

<section id="features" class="py-5">
    <div class="container">
        <div class="text-center mb-5 px-3">
            <h2 class="fw-bold">Tính năng nổi bật</h2>
            <p class="text-muted">Tại sao bạn nên chọn Scan2Class?</p>
        </div>
        <div class="row g-4 px-2">
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm text-center p-4 hover-up">
                    <div class="card-body">
                        <div class="icon-box mb-3 mx-auto bg-primary-subtle rounded-circle d-flex align-items-center justify-content-center" style="width: 80px; height: 80px;">
                            <i class="bi bi-qr-code text-primary fs-1"></i>
                        </div>
                        <h4 class="card-title fw-bold">QR Code Động</h4>
                        <p class="card-text text-muted">Ngăn chặn việc gian lận bằng cách tạo mã QR tự động thay đổi mỗi vài giây.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm text-center p-4 hover-up">
                    <div class="card-body">
                        <div class="icon-box mb-3 mx-auto bg-success-subtle rounded-circle d-flex align-items-center justify-content-center" style="width: 80px; height: 80px;">
                            <i class="bi bi-geo-alt text-success fs-1"></i>
                        </div>
                        <h4 class="card-title fw-bold">Xác thực GPS</h4>
                        <p class="card-text text-muted">Đảm bảo sinh viên có mặt trực tiếp tại lớp thông qua công nghệ định vị toàn cầu.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm text-center p-4 hover-up">
                    <div class="card-body">
                        <div class="icon-box mb-3 mx-auto bg-warning-subtle rounded-circle d-flex align-items-center justify-content-center" style="width: 80px; height: 80px;">
                            <i class="bi bi-graph-up text-warning fs-1"></i>
                        </div>
                        <h4 class="card-title fw-bold">Báo cáo Thời gian thực</h4>
                        <p class="card-text text-muted">Theo dõi điểm danh tức thì và xuất báo cáo chi tiết cho giảng viên và quản trị viên.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<section id="about" class="py-5">
    <div class="container">
        <div class="row align-items-center g-5">
            <div class="col-lg-6 order-2 order-lg-1">
			    <h2 class="fw-bold mb-4">Cách mạng hóa trải nghiệm lớp học</h2>
			    <p class="text-muted mb-4 lead">
			        Tạm biệt các bảng danh sách giấy và việc gọi tên thủ công. Scan2Class tự động hóa quy trình điểm danh, tiết kiệm thời gian giảng dạy quý báu.
			    </p>
			    
			    <div class="row g-3">
			        <div class="col-sm-6 mb-2">
			            <div class="d-flex align-items-center">
			                <i class="bi bi-check-circle-fill text-success fs-5 me-2 d-inline-flex align-self-center"></i>
			                <span class="fw-medium">Nhanh chóng & Bảo mật</span>
			            </div>
			        </div>
			
			        <div class="col-sm-6 mb-2">
			            <div class="d-flex align-items-center">
			                <i class="bi bi-check-circle-fill text-success fs-5 me-2 d-inline-flex align-self-center"></i>
			                <span class="fw-medium">Dễ dàng sử dụng</span>
			            </div>
			        </div>
			        
			        <div class="col-sm-6 mb-2">
			            <div class="d-flex align-items-center">
			                <i class="bi bi-check-circle-fill text-success fs-5 me-2 d-inline-flex align-self-center"></i>
			                <span class="fw-medium">Giao diện di động mượt</span>
			            </div>
			        </div>
			        
			        <div class="col-sm-6 mb-2">
			            <div class="d-flex align-items-center">
			                <i class="bi bi-check-circle-fill text-success fs-5 me-2 d-inline-flex align-self-center"></i>
			                <span class="fw-medium">Hỗ trợ thống kê</span>
			            </div>
			        </div>
			    </div>
			</div>
            <div class="col-lg-6 order-1 order-lg-2">
                <img src="https://images.unsplash.com/photo-1522202176988-66273c2fd55f?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" 
                     alt="Sinh viên đang học tập" 
                     class="img-fluid rounded-4 shadow-lg animate-float">
            </div>
        </div>
    </div>
</section>

<style>
    /* Hiệu ứng trôi nổi nhẹ cho ảnh */
    .animate-float {
        animation: float 6s ease-in-out infinite;
    }
    @keyframes float {
        0% { transform: translateY(0px); }
        50% { transform: translateY(-20px); }
        100% { transform: translateY(0px); }
    }
    /* Hiệu ứng hover cho card */
    .hover-up {
        transition: all 0.3s ease;
    }
    .hover-up:hover {
        transform: translateY(-10px);
    }
    /* Text Shadow cho Hero */
    .shadow-text {
        text-shadow: 0 2px 4px rgba(0,0,0,0.2);
    }
</style>