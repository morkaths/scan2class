<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>



<div class="container pt-5 mt-5">
    <div class="page-heading">
        <div class="page-title">
            <div class="row">
                <div class="col-12 col-md-6 order-md-1 order-last">
                    <h3>Thông tin cá nhân</h3>
                    <p class="text-subtitle text-muted">Quản lý thông tin tài khoản và cập nhật mật khẩu.</p>
                </div>
            </div>
        </div>

        <section class="section">
            <div class="row">
                <div class="col-12 col-lg-8">
                    <div class="card shadow-sm">
                        <div class="card-body p-4">
                            
                            <c:if test="${not empty message}">
                                <div class="alert alert-success alert-dismissible show fade">
                                    <i class="bi bi-check-circle me-2"></i>${message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>
                            
                            <form:form action="${pageContext.request.contextPath}/profile" method="post" modelAttribute="profileDto">
                                <form:hidden path="id" />
                                
                                <div class="form-group mb-4">
                                    <label for="username" class="form-label text-muted fw-bold small text-uppercase">Tên đăng nhập</label>
                                    <form:input path="username" cssClass="form-control bg-light" id="username" readonly="true" disabled="true" />
                                    <small class="text-muted fst-italic mt-1 d-block">Tên đăng nhập không thể thay đổi.</small>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                         <div class="form-group mb-4">
                                            <label for="fullname" class="form-label fw-bold">Họ và tên <span class="text-danger">*</span></label>
                                                <form:input path="fullname" cssClass="form-control" id="fullname" placeholder="Nhập họ tên của bạn" />
                                            <form:errors path="fullname" cssClass="text-danger small mt-1" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group mb-4">
                                            <label for="email" class="form-label fw-bold">Email <span class="text-danger">*</span></label>
                                                <form:input path="email" cssClass="form-control" id="email" type="email" placeholder="email@example.com" />
                                            <form:errors path="email" cssClass="text-danger small mt-1" />
                                        </div>
                                    </div>
                                </div>

                                <hr class="my-4 border-light">
                                
                                <h5 class="mb-3 text-primary"><i class="bi bi-shield-lock me-2"></i>Đổi mật khẩu</h5>
                                <div class="alert alert-light-secondary d-flex align-items-center mb-4" role="alert">
                                    <i class="bi bi-info-circle me-2"></i>
                                    <span class="small">Chỉ điền vào các ô bên dưới nếu bạn muốn thay đổi mật khẩu hiện tại.</span>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="newPassword" class="form-label fw-bold">Mật khẩu mới</label>
                                            <form:password path="newPassword" cssClass="form-control" id="newPassword" />
                                        <form:errors path="newPassword" cssClass="text-danger small mt-1" />
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="confirmPassword" class="form-label fw-bold">Xác nhận mật khẩu</label>
                                            <form:password path="confirmPassword" cssClass="form-control" id="confirmPassword" />
                                        <form:errors path="confirmPassword" cssClass="text-danger small mt-1" />
                                    </div>
                                </div>
                                <form:errors path="" cssClass="text-danger d-block mb-3" />

                                <div class="d-flex justify-content-end mt-4">
                                    <button type="submit" class="btn btn-primary px-4 py-2 fw-bold shadow">
                                        <i class="bi bi-save me-2"></i>Lưu thay đổi
                                    </button>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
                
                <div class="col-12 col-lg-4">
                <div class="card">
                    <div class="card-body">
                        <div class="d-flex justify-content-center align-items-center flex-column">
                            <div class="avatar avatar-2xl">
                                <img src="${not empty displayAvatar ? displayAvatar : '/assets/compiled/jpg/1.jpg'}" alt="Avatar">
                            </div>
                            <h3 class="mt-3">${displayName}</h3>
                            <p class="text-small">${displayEmail}</p>
                        </div>
                    </div>
                </div>
            </div>
            </div>
        </section>
    </div>
</div>
