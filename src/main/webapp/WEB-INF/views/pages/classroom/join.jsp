<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div class="container py-5 d-flex justify-content-center align-items-center" style="min-height: 80vh;">
            <div class="card shadow-lg border-0" style="max-width: 500px; width: 100%;">
                <div class="card-body text-center p-5">
                    <div class="mb-4">
                        <i class="bi bi-door-open text-primary" style="font-size: 4rem;"></i>
                    </div>

                    <h2 class="fw-bold mb-2">${classroom.name}</h2>
                    <p class="text-muted mb-4">
                        <i class="bi bi-person-badge me-1"></i> Giảng viên: <strong>${classroom.owner.fullname}</strong>
                    </p>

                    <div class="alert alert-light border mb-4 text-start">
                        <div class="d-flex mb-2">
                            <i class="bi bi-geo-alt me-2 text-secondary"></i>
                            <span>Phòng: ${classroom.room}</span>
                        </div>
                        <div class="d-flex">
                            <i class="bi bi-people me-2 text-secondary"></i>
                            <span>Sĩ số hiện tại: ${classroom.participants.size()}</span>
                        </div>
                    </div>

                    <button id="btnJoin" class="btn btn-primary btn-lg w-100 rounded-pill" onclick="joinClassroom()">
                        Xác nhận tham gia
                    </button>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
            function joinClassroom() {
                const btn = document.getElementById('btnJoin');
                const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
                const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

                if (!csrfToken || !csrfHeader) {
                    Swal.fire('Lỗi', 'Không tìm thấy CSRF Token. Hãy kiểm tra lại thẻ meta!', 'error');
                    return;
                }
                
                btn.disabled = true;
                btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Đang xử lý...';
                
                fetch('<c:url value="/classrooms/join" />', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        [csrfHeader]: csrfToken
                    },
                    body: 'code=${code}'
                })
                .then(response => {
                	if (!response.ok) {
                        return response.json().then(err => { throw new Error(err.message || 'Lỗi server'); });
                    }
                    return response.json();
                 })
                 .then(data => {
				    Swal.fire({
				        icon: 'success',
				        title: 'Thành công!',
				        text: data.message || 'Bạn đã tham gia lớp học.',
				        timer: 2000,
				        showConfirmButton: false
				    }).then(() => {
				        window.location.href = '<c:url value="/classrooms/" />' + data.classroomId;
				    });
				})
				.catch(error => {
				    console.error('Lỗi khi tham gia lớp:', error);
				    Swal.fire({
				        icon: 'error',
				        title: 'Thất bại',
				        text: error.message || 'Không thể kết nối đến máy chủ.'
				    });
				    btn.disabled = false;
				    btn.innerHTML = 'Xác nhận tham gia';
				});
            }
        </script>