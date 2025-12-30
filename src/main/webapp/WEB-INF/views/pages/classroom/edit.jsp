<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <div class="container py-5">
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-6">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4">
                                <h4 class="card-title fw-bold mb-4 text-center">Cập nhật thông tin lớp học</h4>

                                <form:form action="${pageContext.request.contextPath}/classrooms/edit" method="post"
                                    modelAttribute="dto">
                                    <form:hidden path="id" />

                                    <div class="mb-3">
                                        <label for="code" class="form-label fw-bold">Mã lớp</label>
                                        <form:input path="code" cssClass="form-control bg-light" id="code"
                                            readonly="true" />
                                    </div>

                                    <div class="mb-3">
                                        <label for="name" class="form-label fw-bold">Tên lớp <span
                                                class="text-danger">*</span></label>
                                        <form:input path="name" cssClass="form-control" id="name" required="required"
                                            placeholder="Nhập tên lớp học" />
                                        <form:errors path="name" cssClass="text-danger small" />
                                    </div>

                                    <div class="mb-3">
                                        <label for="room" class="form-label fw-bold">Phòng học</label>
                                        <form:input path="room" cssClass="form-control" id="room"
                                            placeholder="VD: A101" />
                                        <form:errors path="room" cssClass="text-danger small" />
                                    </div>

                                    <div class="mb-4">
                                        <label for="status" class="form-label fw-bold">Trạng thái</label>
                                        <form:select path="status" cssClass="form-select" id="status">
                                            <form:option value="1" label="Đang hoạt động" />
                                            <form:option value="0" label="Đã khóa" />
                                        </form:select>
                                        <form:errors path="status" cssClass="text-danger small" />
                                    </div>

                                    <div class="d-grid gap-2">
                                        <button type="submit" class="btn btn-primary py-2 fw-bold">
                                            <i class="bi bi-save me-2"></i>Lưu thay đổi
                                        </button>
                                        <a href="<c:url value='/classrooms/${dto.id}' />"
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