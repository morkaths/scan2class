<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>


                <div class="container py-5">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            ${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty message}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i>
                            ${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <div class="row">
					    <div class="col-12 mb-4">
					        <div class="card shadow-sm border-0">
					            <div class="card-body p-3 p-md-4"> <div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
					                    
					                    <div class="w-100">
					                        <div class="d-flex flex-wrap align-items-center mb-2">
					                        	<span class="badge bg-primary me-1">${classroom.code}</span>
					                            <h2 class="mb-0 fw-bold me-2 fs-4 fs-md-2">${classroom.name}</h2>
					                            <c:if test="${isOwner}">
						                            <a href="<c:url value='/classrooms/edit/${classroom.id}' />">
							                            <i class="bi bi-pencil"></i>
							                        </a>
						                        </c:if>
					                        </div>
					                        <div class="text-muted mb-2 small">
					                            <i class="bi bi-geo-alt me-1"></i>Phòng: ${classroom.room}
					                            <span class="badge ${classroom.status == 1 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'} rounded-pill">
					                                ${classroom.status == 1 ? 'Đang hoạt động' : 'Đã kết thúc'}
					                            </span>
					                        </div>
					                    </div>
					
					                    <div class="d-flex flex-column flex-sm-row gap-2 w-100 w-md-auto">
                                            <a href="<c:url value='/attend/classrooms/${classroom.id}/analytics' />"
					                                class="btn btn-outline-primary w-100 w-sm-auto">
					                            <i class="bi bi-graph-up me-2"></i>Thống kê
					                        </a>
					                        
					                        <c:if test="${isOwner}">
						                        <button type="button" class="btn btn-info text-white w-100 w-sm-auto" 
						                                data-bs-toggle="modal" data-bs-target="#joinQrModal">
						                            <i class="bi bi-qr-code me-2"></i>Mã mời
						                        </button>
						                        
						                        <a href="<c:url value='/classrooms/${classroom.id}/sessions/create' />"
						                           class="btn btn-primary w-100 w-sm-auto">
						                            <i class="bi bi-plus-circle me-2"></i>Tạo phiên
						                        </a>
					                        </c:if>
					                    </div>
					
					                </div>
					            </div>
					        </div>
					    </div>
					</div>

                        <div class="col-12">
                            <div class="card shadow-sm border-0">
                                <div class="card-header border-bottom-0 pt-4 px-4">
                                    <ul class="nav nav-tabs card-header-tabs" role="tablist">
                                        <li class="nav-item">
                                            <button class="nav-link active fw-bold" data-bs-toggle="tab"
                                                data-bs-target="#sessions" type="button" role="tab">
                                                <i class="bi bi-calendar-event me-2"></i>Các phiên điểm danh
                                            </button>
                                        </li>
                                        <li class="nav-item">
                                            <button class="nav-link fw-bold" data-bs-toggle="tab"
                                                data-bs-target="#students" type="button" role="tab">
                                                <i class="bi bi-people me-2"></i>Danh sách sinh viên
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                                <div class="card-body p-0">
                                    <div class="tab-content">
                                        <div class="tab-pane fade show active" id="sessions" role="tabpanel">
                                            <c:choose>
                                                <c:when test="${empty classroom.sessions}">
                                                    <div class="text-center py-5">
                                                        <i class="bi bi-calendar-x text-muted"
                                                            style="font-size: 2rem;"></i>
                                                        <p class="mt-3 text-muted mb-0">Chưa có phiên điểm danh nào.</p>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="table-responsive p-3">
                                                        <table id="table" class="table table-striped">
                                                            <thead>
                                                                <tr>
                                                                    <th class="ps-4">Phiên</th>
                                                                    <th>Thời gian</th>
                                                                    <th>Trạng thái</th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="session" items="${classroom.sessions}">
                                                                    <tr>
                                                                        <td class="ps-4">
                                                                            <div class="fw-bold">${session.name}</div>
                                                                            <small class="text-muted">
                                                                                <javatime:format
                                                                                    value="${session.startTime}"
                                                                                    pattern="dd/MM/yyyy" />
                                                                            </small>
                                                                        </td>
                                                                        <td>
                                                                            <i class="bi bi-clock me-1 text-muted"></i>
                                                                            <javatime:format
                                                                                value="${session.startTime}"
                                                                                pattern="HH:mm" /> -
                                                                            <javatime:format value="${session.endTime}"
                                                                                pattern="HH:mm" />
                                                                        </td>
                                                                        <td>
                                                                            <span
                                                                                class="badge ${session.active ? 'bg-success' : 'bg-secondary'}">
                                                                                ${session.active ? 'Hoạt động' : 'Kết
                                                                                thúc'}
                                                                            </span>
                                                                        </td>
                                                                        <td class="text-end pe-4">
                                                                            <c:if test="${isOwner}">
                                                                            <div class="dropdown"
                                                                                style="position: static;">
                                                                                <button
                                                                                    class="btn btn-link text-dark p-0"
                                                                                    type="button"
                                                                                    data-bs-toggle="dropdown"
                                                                                    aria-expanded="false">
                                                                                    <i
                                                                                        class="bi bi-three-dots-vertical"></i>
                                                                                </button>
                                                                                <ul
                                                                                    class="dropdown-menu dropdown-menu-end shadow-sm border-0">
                                                                                    <li>
                                                                                        <a class="dropdown-item py-2"
                                                                                            href="<c:url value='/classrooms/${classroom.id}/sessions/${session.id}' />">
                                                                                            <i
                                                                                                class="bi bi-eye me-2 text-primary"></i>
                                                                                            Chi tiết
                                                                                        </a>
                                                                                    </li>
                                                                                    <li>
                                                                                        <a class="dropdown-item py-2"
                                                                                            href="<c:url value='/classrooms/${classroom.id}/sessions/${session.id}/edit' />">
                                                                                            <i
                                                                                                class="bi bi-pencil me-2 text-secondary"></i>
                                                                                            Chỉnh sửa
                                                                                        </a>
                                                                                    </li>

                                                                                    <li>
                                                                                        <hr class="dropdown-divider">
                                                                                    </li>

                                                                                    <li>
                                                                                        <form
                                                                                            action="<c:url value='/classrooms/${classroom.id}/sessions/${session.id}/delete' />"
                                                                                            method="post"
                                                                                            onsubmit="return confirm('Bạn có chắc chắn muốn xoá phiên điểm danh này? Hành động này không thể hoàn tác và sẽ xoá tất cả dữ liệu điểm danh liên quan.');"
                                                                                            id="delete-form-${session.id}">
                                                                                            <input type="hidden"
                                                                                                name="${_csrf.parameterName}"
                                                                                                value="${_csrf.token}" />
                                                                                            <button type="submit"
                                                                                                class="dropdown-item py-2 text-danger">
                                                                                                <i
                                                                                                    class="bi bi-trash me-2"></i>
                                                                                                Xoá phiên
                                                                                            </button>
                                                                                        </form>
                                                                                    </li>
                                                                                </ul>
                                                                            </div>
                                                                            </c:if>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="tab-pane fade" id="students" role="tabpanel">
                                            <c:choose>
                                                <c:when test="${empty classroom.participants}">
                                                    <div class="text-center py-5">
                                                        <i class="bi bi-person-x text-muted"
                                                            style="font-size: 2rem;"></i>
                                                        <p class="mt-3 text-muted mb-0">Chưa có sinh viên nào tham gia.
                                                        </p>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="table-responsive p-3">
                                                        <table id="table" class="table table-striped">
                                                            <thead>
                                                                <tr>
                                                                    <th class="ps-4">Sinh viên</th>
                                                                    <th>Email</th>
                                                                    <th>Tham gia lúc</th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="participant"
                                                                    items="${classroom.participants}">
                                                                    <tr>
                                                                        <td class="ps-4">
                                                                            <div class="d-flex align-items-center">
                                                                                <div class="p-2 me-3 text-center"
                                                                                    style="width: 40px; height: 40px;">
                                                                                    <i
                                                                                        class="bi bi-person-fill text-secondary"></i>
                                                                                </div>
                                                                                <div>
                                                                                    <div class="fw-bold">
                                                                                        ${participant.user.fullname}
                                                                                    </div>
                                                                                    <small
                                                                                        class="text-muted">@${participant.user.username}</small>
                                                                                </div>
                                                                            </div>
                                                                        </td>
                                                                        <td>${participant.user.email}</td>
                                                                        <td>
                                                                            <javatime:format
                                                                                value="${participant.createdAt}"
                                                                                pattern="dd/MM/yyyy HH:mm" />
                                                                        </td>
                                                                        <td class="text-end pe-4">
                                                                            <c:if test="${isOwner}">
                                                                            <form
                                                                                action="${pageContext.request.contextPath}/classrooms/${classroom.id}/participants/${participant.user.id}/remove"
                                                                                method="post" style="display: inline;"
                                                                                onsubmit="return confirm('Bạn có chắc chắn muốn mời sinh viên ${participant.user.username} ra khỏi lớp?');">

                                                                                <input type="hidden"
                                                                                    name="${_csrf.parameterName}"
                                                                                    value="${_csrf.token}" />

                                                                                <button type="submit"
                                                                                    class="btn btn-outline-danger btn-sm"
                                                                                    title="Mời khỏi lớp">
                                                                                    <i class="bi bi-trash"></i>
                                                                                </button>
                                                                            </form>
                                                                            </c:if>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Join QR Modal -->
                <div class="modal fade" id="joinQrModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Mã QR tham gia lớp học</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                            </div>
                            <div class="modal-body text-center">
                                <div id="joinQrcode" class="d-flex justify-content-center mb-3"></div>
                                <p class="text-muted">Quét mã để tham gia lớp học</p>
                                <div class="input-group mb-3">
                                    <input type="text" class="form-control" id="joinUrlInput" readonly>
                                    <button class="btn btn-outline-secondary" type="button" id="copyBtn"
                                        onclick="copyJoinLink()">Copy</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdnjs.cloudflare.com/ajax/libs/qrcodejs/1.0.0/qrcode.min.js"></script>
                <script>
                    document.addEventListener('DOMContentLoaded', function () {
                        // Simple-DataTables initialization for multiple tables
                        if (typeof simpleDatatables !== 'undefined') {
                            const sessionsTable = new simpleDatatables.DataTable(document.getElementById("sessionsTable"));
                            const studentsTable = new simpleDatatables.DataTable(document.getElementById("studentsTable"));
                        }
                    });

                    // Apply standard adaptations to these new tables
                    function adaptCustomTable(table) {
                        // Re-use logic from simple-datatables.js if possible, or just let it be basic first.
                        // The standard script in assets/static/js/pages/simple-datatables.js only inits ID "table"
                        // We need to trigger the same listeners or just rely on basic styles.
                        // For now, simple init is enough, the CSS will handle the look.
                    }
                </script>
                <script>
                    document.addEventListener("DOMContentLoaded", function () {
                        var protocol = window.location.protocol;
                        var host = window.location.host;
                        var contextPath = "${pageContext.request.contextPath}";
                        var rootUrl = protocol + "//" + host + contextPath;
                        // Use joinCode from model
                        var joinUrl = rootUrl + "/classrooms/join?code=${joinCode}";

                        document.getElementById("joinUrlInput").value = joinUrl;

                        new QRCode(document.getElementById("joinQrcode"), {
                            text: joinUrl,
                            width: 250,
                            height: 250,
                            colorDark: "#000000",
                            colorLight: "#ffffff",
                            correctLevel: QRCode.CorrectLevel.H
                        });
                    });

                    function copyJoinLink() {
                        var copyText = document.getElementById("joinUrlInput");
                        copyText.select();
                        copyText.setSelectionRange(0, 99999);
                        navigator.clipboard.writeText(copyText.value).then(function () {
                            // Feedback could be added here
                        }, function (err) {
                            console.error('Async: Could not copy text: ', err);
                        });
                    }
                </script>