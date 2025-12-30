<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <div class="container py-5">
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-6">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4">
                                <h4 class="card-title fw-bold mb-4 text-center">Chỉnh sửa
                                    phiên điểm danh</h4>
                                <p class="text-center text-muted mb-4">Lớp: ${classroom.name}</p>

                                <form:form
                                    action="${pageContext.request.contextPath}/classrooms/${classroom.id}/sessions/${session.id}/edit"
                                    method="post" modelAttribute="dto">

                                    <!-- Geo Location Hidden Fields -->
                                    <form:hidden path="latitude" id="latitude" />
                                    <form:hidden path="longitude" id="longitude" />

                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <label class="form-label fw-bold mb-0">Vị trí điểm danh</label>
                                            <button type="button" class="btn btn-sm btn-outline-primary"
                                                id="get-location-btn">
                                                <i class="bi bi-geo-alt me-1"></i>Cập nhật vị trí hiện tại
                                            </button>
                                        </div>
                                        <div class="alert alert-light border d-flex align-items-center" role="alert"
                                            id="geo-status">
                                            <c:choose>
                                                <c:when test="${not empty dto.latitude}">
                                                    <i class="bi bi-check-circle-fill text-success me-2"></i>
                                                    <div>Đã có tọa độ: ${dto.latitude}, ${dto.longitude}</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="bi bi-info-circle-fill text-info me-2"></i>
                                                    <div>Chưa có thông tin vị trí.</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="name" class="form-label fw-bold">Tên buổi học
                                            <span class="text-danger">*</span>
                                        </label>
                                        <form:input path="name" cssClass="form-control" id="name"
                                            placeholder="VD: Tuần 1 - Giới thiệu" required="required" />
                                        <form:errors path="name" cssClass="text-danger small" />
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="duration" class="form-label fw-bold">Thêm
                                                thời gian (phút)</label>
                                            <form:input path="duration" type="number" cssClass="form-control"
                                                id="duration" min="0" max="180" />
                                            <div class="form-text">Nhập số phút muốn cộng thêm (0 để
                                                giữ nguyên).</div>
                                            <form:errors path="duration" cssClass="text-danger small" />
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="radius" class="form-label fw-bold">Bán kính
                                                (m)</label>
                                            <form:input path="radius" type="number" cssClass="form-control" id="radius"
                                                min="10" />
                                            <form:errors path="radius" cssClass="text-danger small" />
                                        </div>
                                    </div>

                                    <div class="d-grid gap-2 mt-3">
                                        <button type="submit" class="btn btn-primary py-2 fw-bold" id="submit-btn">
                                            <i class="bi bi-check-circle me-2"></i>Cập nhật
                                        </button>
                                        <a href="<c:url value='/classrooms/${classroom.id}' />"
                                            class="btn btn-outline-secondary py-2"> Hủy bỏ </a>
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
                    const getLocationBtn = document.getElementById("get-location-btn");

                    getLocationBtn.addEventListener("click", function () {
                        statusDiv.className = "alert alert-info border d-flex align-items-center";
                        statusDiv.innerHTML = '<div class="spinner-border spinner-border-sm me-2" role="status"></div><div>Đang lấy tọa độ...</div>';

                        if ("geolocation" in navigator) {
                            navigator.geolocation.getCurrentPosition(function (position) {
                                latInput.value = position.coords.latitude;
                                lngInput.value = position.coords.longitude;
                                statusDiv.className = "alert alert-success border d-flex align-items-center";
                                statusDiv.innerHTML = '<i class="bi bi-check-circle-fill me-2"></i><div>Đã cập nhật tọa độ mới.</div>';
                            }, function (error) {
                                statusDiv.className = "alert alert-danger border d-flex align-items-center";
                                statusDiv.innerHTML = '<i class="bi bi-exclamation-triangle-fill me-2"></i><div>Không thể lấy vị trí. Error: ' + error.message + '</div>';
                            });
                        } else {
                            statusDiv.className = "alert alert-danger border";
                            statusDiv.textContent = "Trình duyệt không hỗ trợ Geolocation.";
                        }
                    });
                });
            </script>