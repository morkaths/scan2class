<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <div class="container py-5">
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-6">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4">
                                <h4 class="card-title fw-bold mb-4 text-center">Tạo phiên điểm danh</h4>
                                <p class="text-center text-muted mb-4">Lớp: ${classroom.name}</p>

                                <form:form
                                    action="${pageContext.request.contextPath}/classrooms/${classroom.id}/sessions"
                                    method="post" modelAttribute="dto">

                                    <!-- Geo Location Hidden Fields -->
                                    <form:hidden path="latitude" id="latitude" />
                                    <form:hidden path="longitude" id="longitude" />

                                    <div class="alert alert-info d-flex align-items-center" role="alert"
                                        id="geo-status">
                                        <i class="bi bi-geo-alt me-2"></i>
                                        <div>Đang lấy tọa độ vị trí của bạn...</div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="name" class="form-label fw-bold">Tên buổi học <span
                                                class="text-danger">*</span></label>
                                        <form:input path="name" cssClass="form-control" id="name"
                                            placeholder="VD: Tuần 1 - Giới thiệu" required="required" />
                                        <form:errors path="name" cssClass="text-danger small" />
                                    </div>

                                    <div class="mb-3">
                                        <label for="room" class="form-label fw-bold">Phòng học (Tùy chọn)</label>
                                        <form:input path="room" cssClass="form-control" id="room"
                                            placeholder="VD: P.101, A2-304 (Để trống nếu dùng phòng mặc định)" />
                                        <form:errors path="room" cssClass="text-danger small" />
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="duration" class="form-label fw-bold">Thời lượng (phút)</label>
                                            <form:input path="duration" type="number" cssClass="form-control"
                                                id="duration" min="1" max="180" />
                                            <form:errors path="duration" cssClass="text-danger small" />
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="radius" class="form-label fw-bold">Bán kính (m)</label>
                                            <form:input path="radius" type="number" cssClass="form-control" id="radius"
                                                min="10" />
                                            <form:errors path="radius" cssClass="text-danger small" />
                                        </div>
                                    </div>

                                    <div class="d-grid gap-2 mt-3">
                                        <button type="submit" class="btn btn-primary py-2 fw-bold" id="submit-btn"
                                            disabled>
                                            <i class="bi bi-play-circle me-2"></i>Bắt đầu điểm danh
                                        </button>
                                        <a href="<c:url value='/classrooms/${classroom.id}' />"
                                            class="btn btn-outline-secondary py-2">
                                            Hủy bỏ
                                        </a>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    const latInput = document.getElementById("latitude");
                    const lngInput = document.getElementById("longitude");
                    const statusDiv = document.getElementById("geo-status");
                    const submitBtn = document.getElementById("submit-btn");

                    if ("geolocation" in navigator) {
                        navigator.geolocation.getCurrentPosition(function (position) {
                            latInput.value = position.coords.latitude;
                            lngInput.value = position.coords.longitude;
                            statusDiv.className = "alert alert-success d-flex align-items-center";
                            statusDiv.innerHTML = '<i class="bi bi-check-circle-fill me-2"></i><div>Đã lấy được tọa độ vị trí.</div>';
                            submitBtn.disabled = false;
                        }, function (error) {
                            statusDiv.className = "alert alert-danger d-flex align-items-center";
                            statusDiv.innerHTML = '<i class="bi bi-exclamation-triangle-fill me-2"></i><div>Không thể lấy vị trí. Hãy cho phép truy cập GPS.</div>';
                            console.error("Geo error:", error);
                        });
                    } else {
                        statusDiv.className = "alert alert-danger";
                        statusDiv.textContent = "Trình duyệt không hỗ trợ Geolocation.";
                    }
                });
            </script>