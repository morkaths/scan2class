<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <div class="container py-5">
                <div class="row justify-content-center">
                    <div class="col-md-6 col-lg-5">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4 text-center">
                                <div class="mb-4">
                                    <i class="bi bi-geo-alt-fill text-primary display-1"></i>
                                </div>

                                <h4 class="card-title fw-bold mb-3">Xác thực vị trí</h4>
                                <p class="text-muted mb-4">
                                    Hệ thống cần xác minh vị trí của bạn để hoàn tất điểm danh. Vui lòng cho phép truy
                                    cập GPS.
                                </p>

                                <!-- Alert Messages -->
                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger" role="alert">
                                        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${errorMessage}
                                    </div>
                                </c:if>

                                <form action="${pageContext.request.contextPath}/attend/submit" method="post"
                                    id="attendance-form">
                                    <input type="hidden" name="token" value="${token}" />
                                    <input type="hidden" name="latitude" id="latitude" />
                                    <input type="hidden" name="longitude" id="longitude" />
                                    <input type="hidden" name="deviceInfo" id="deviceInfo" />
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                                    <div id="status-area" class="alert alert-light border mb-3 d-none"></div>

                                    <div class="d-grid gap-2">
                                        <button type="button" class="btn btn-primary py-2 fw-bold" id="verify-btn">
                                            <i class="bi bi-check-circle me-2"></i>Điểm danh ngay
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    const verifyBtn = document.getElementById("verify-btn");
                    const statusArea = document.getElementById("status-area");

                    const latInput = document.getElementById("latitude");
                    const lngInput = document.getElementById("longitude");
                    const deviceInput = document.getElementById("deviceInfo");

                    // Pre-fill Device Info
                    deviceInput.value = navigator.userAgent;

                    verifyBtn.addEventListener("click", function () {
                        // UI Loading state
                        verifyBtn.disabled = true;
                        verifyBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Đang lấy vị trí chính xác...';

                        statusArea.className = "alert alert-info border mb-3";
                        statusArea.classList.remove("d-none");
                        statusArea.innerHTML = '<i class="bi bi-satellite-dish me-2"></i>Đang kết nối GPS (có thể mất 15s)...';

                        if ("geolocation" in navigator) {
                            const options = {
                                enableHighAccuracy: true,
                                timeout: 20000,
                                maximumAge: 0
                            };

                            navigator.geolocation.getCurrentPosition(
                                // Success
                                function (position) {
                                    const accuracy = position.coords.accuracy;
                                    console.log("Accuracy: " + accuracy + " meters");

                                    // Accuracy Filtering check (500m limit)
                                    if (accuracy > 500) {
                                        verifyBtn.disabled = false;
                                        verifyBtn.innerHTML = '<i class="bi bi-arrow-clockwise me-2"></i>Thử lại (Độ chính xác thấp)';
                                        statusArea.className = "alert alert-warning border mb-3";
                                        statusArea.innerHTML = '<i class="bi bi-exclamation-triangle me-2"></i>Vị trí quá thiếu chính xác (' + Math.round(accuracy) + 'm > 500m). Vui lòng bật GPS và không dùng VPN.';
                                        return;
                                    }

                                    latInput.value = position.coords.latitude;
                                    lngInput.value = position.coords.longitude;

                                    statusArea.className = "alert alert-success border mb-3";
                                    statusArea.innerHTML = '<i class="bi bi-check-circle-fill me-2"></i>Đã lấy tọa độ (Sai số: ' + Math.round(accuracy) + 'm). Đang gửi...';

                                    // Send data via Fetch
                                    const csrfToken = document.querySelector('input[name="${_csrf.parameterName}"]').value;
                                    const token = document.querySelector('input[name="token"]').value;

                                    const formData = new URLSearchParams();
                                    formData.append('token', token);
                                    formData.append('latitude', position.coords.latitude);
                                    formData.append('longitude', position.coords.longitude);
                                    formData.append('accuracy', accuracy);
                                    formData.append('deviceInfo', navigator.userAgent);

                                    verifyBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Đang điểm danh...';

                                    fetch('${pageContext.request.contextPath}/attend/submit', {
                                        method: 'POST',
                                        headers: {
                                            'Content-Type': 'application/x-www-form-urlencoded',
                                            'X-CSRF-TOKEN': csrfToken
                                        },
                                        body: formData
                                    })
                                        .then(response => response.json().then(data => ({ status: response.status, body: data })))
                                        .then(result => {
                                            if (result.status === 200) {
                                                Swal.fire({
                                                    icon: 'success',
                                                    title: 'Thành công!',
                                                    html: result.body.message + (result.body.distance ? '<br>Khoảng cách tới lớp: <b>' + result.body.distance + 'm</b>' : ''),
                                                    timer: 3000,
                                                    showConfirmButton: false
                                                }).then(() => {
                                                    window.location.href = '${pageContext.request.contextPath}/';
                                                });
                                            } else {
                                                throw new Error(result.body.message || 'Có lỗi xảy ra.');
                                            }
                                        })
                                        .catch(error => {
                                            verifyBtn.disabled = false;
                                            verifyBtn.innerHTML = '<i class="bi bi-check-circle me-2"></i>Điểm danh ngay';

                                            Swal.fire({
                                                icon: 'error',
                                                title: 'Điểm danh thất bại',
                                                text: error.message
                                            });

                                            statusArea.className = "alert alert-danger border mb-3";
                                            statusArea.innerHTML = '<i class="bi bi-x-circle-fill me-2"></i>' + error.message;
                                        });
                                },
                                // Error
                                function (error) {
                                    verifyBtn.disabled = false;
                                    verifyBtn.innerHTML = '<i class="bi bi-arrow-clockwise me-2"></i>Thử lại';

                                    statusArea.className = "alert alert-danger border mb-3";
                                    let msg = "Không thể lấy vị trí.";
                                    if (error.code === error.PERMISSION_DENIED) {
                                        msg = "Bạn đã từ chối quyền truy cập GPS. Vui lòng cho phép để điểm danh.";
                                    } else if (error.code === error.POSITION_UNAVAILABLE) {
                                        msg = "Thông tin vị trí không khả dụng. Hãy bật GPS.";
                                    } else if (error.code === error.TIMEOUT) {
                                        msg = "Hết thời gian chờ. Hãy tắt bật lại GPS và thử lại.";
                                    }
                                    statusArea.innerHTML = '<i class="bi bi-x-circle-fill me-2"></i>' + msg;
                                },
                                options
                            );
                        } else {
                            verifyBtn.disabled = false;
                            statusArea.className = "alert alert-danger border mb-3";
                            statusArea.classList.remove("d-none");
                            statusArea.textContent = "Trình duyệt của bạn không hỗ trợ Geolocation.";
                        }
                    });
                });
            </script>