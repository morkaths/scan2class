<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div class="page-content">
            <div class="container py-5 text-center">

                <!-- Header Section -->
                <div class="mb-5">
                    <h3 class="display-6 fw-bold mb-2">${classroom.name}</h3>
                    <h4 class="text-muted mb-2">${session.name}</h4>
                    <c:if test="${not empty session.room}">
                        <h5 class="text-primary mb-3"><i class="bi bi-geo-alt-fill"></i> ${session.room}</h5>
                    </c:if>
                    <c:if test="${empty session.room and not empty classroom.room}">
                        <h5 class="text-primary mb-3"><i class="bi bi-geo-alt-fill"></i> ${classroom.room}</h5>
                    </c:if>
                    
                    <span class="badge bg-success fs-6 px-3 py-2 rounded-pill">
                        <i class="bi bi-broadcast me-1"></i> Đang diễn ra
                    </span>
                </div>

                <!-- QR Code Area -->
                <div class="row justify-content-center mb-5">
                    <div class="col-auto">
                        <div class="card shadow-lg border-0">
                            <div class="card-body p-4 bg-white rounded-3">
                                <div id="qrcode" class="d-flex justify-content-center"></div>
                            </div>
                        </div>
                        <p class="mt-3 text-muted small">Quét mã QR để điểm danh</p>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="d-flex justify-content-center gap-3">
                    <c:if test="${session.active}">
                        <form
                            action="${pageContext.request.contextPath}/classrooms/${classroom.id}/sessions/${session.id}/end"
                            method="post"
                            onsubmit="return confirm('Bạn có chắc chắn muốn kết thúc phiên điểm danh này?');">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="btn btn-danger w-100">End Session</button>
                        </form>
                    </c:if>
                    <a href="<c:url value='/classrooms/${classroom.id}/sessions/${session.id}'/>"
                        class="btn btn-primary btn-lg px-4">
                        <i class="bi bi-list-check me-2"></i> Xem danh sách
                    </a>
                </div>

            </div>
        </div>

        <!-- QR Code Library -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/qrcodejs/1.0.0/qrcode.min.js"></script>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                // Dynamic URL Generation
                var protocol = window.location.protocol;
                var host = window.location.host;
                var contextPath = "${pageContext.request.contextPath}";

                var rootUrl = protocol + "//" + host + contextPath;
                var targetUrl = rootUrl + "/attend?token=${session.token}";

                console.log("QR Code Target URL: " + targetUrl);

                // Generate QR Code
                var qrcode = new QRCode(document.getElementById("qrcode"), {
                    text: targetUrl,
                    width: 300,
                    height: 300,
                    colorDark: "#000000",
                    colorLight: "#ffffff",
                    correctLevel: QRCode.CorrectLevel.H
                });
            });
        </script>