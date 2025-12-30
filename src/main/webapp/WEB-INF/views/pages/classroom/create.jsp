<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <div class="container py-5">
                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-6">
                        <div class="card shadow-sm border-0">
                            <div class="card-body p-4">
                                <h4 class="card-title fw-bold mb-4 text-center">Thêm lớp học mới</h4>

                                <form:form action="${pageContext.request.contextPath}/classrooms/create" method="post"
                                    modelAttribute="dto">

                                    <div class="mb-3">
                                        <label for="code" class="form-label fw-bold">Mã lớp <span
                                                class="text-danger">*</span></label>
                                        <form:input path="code" cssClass="form-control" id="code"
                                            placeholder="VD: INT3306" required="required" />
                                        <form:errors path="code" cssClass="text-danger small" />
                                    </div>

                                    <div class="mb-3">
                                        <label for="name" class="form-label fw-bold">Tên lớp <span
                                                class="text-danger">*</span></label>
                                        <form:input path="name" cssClass="form-control" id="name"
                                            placeholder="VD: Lập trình Web" required="required" />
                                        <form:errors path="name" cssClass="text-danger small" />
                                    </div>

                                    <div class="mb-4">
                                        <label for="room" class="form-label fw-bold">Phòng học</label>
                                        <form:input path="room" cssClass="form-control" id="room"
                                            placeholder="VD: 302-G2" />
                                        <form:errors path="room" cssClass="text-danger small" />
                                    </div>

                                    <div class="d-grid gap-2">
                                        <button type="submit" class="btn btn-primary py-2 fw-bold">
                                            <i class="bi bi-plus-lg me-2"></i>Tạo lớp học
                                        </button>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>