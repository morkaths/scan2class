<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

 <div class="container py-5">
     <!-- Flash Messages -->
     <c:if test="${not empty message}">
         <div class="alert alert-success alert-dismissible fade show" role="alert">
             ${message}
             <button type="button" class="btn-close" data-bs-dismiss="alert"
                 aria-label="Close"></button>
         </div>
     </c:if>
     <c:if test="${not empty errorMessage}">
         <div class="alert alert-danger alert-dismissible fade show" role="alert">
             ${errorMessage}
             <button type="button" class="btn-close" data-bs-dismiss="alert"
                 aria-label="Close"></button>
         </div>
     </c:if>

     <div class="row g-4">
         <!-- Card 1: Session Information -->
         <div class="col-md-4">
             <div class="card h-100 shadow-sm border-0">
                 <div class="card-header bg-white py-3">
                     <h5 class="card-title mb-0">Thông tin phiên điểm danh</h5>
                 </div>
                 <div class="card-body">
                     <h4 class="card-title text-primary mb-3">${session.name}</h4>

                     <div class="mb-3">
                         <label class="text-muted small text-uppercase fw-bold">Trạng thái</label>
                         <div>
                             <c:choose>
                                 <c:when test="${session.active}">
                                     <span class="badge bg-success">Hoạt động</span>
                                 </c:when>
                                 <c:otherwise>
                                     <span class="badge bg-secondary">Kết thúc</span>
                                 </c:otherwise>
                             </c:choose>
                         </div>
                     </div>

                     <div class="mb-3">
                         <label class="text-muted small text-uppercase fw-bold">Bắt đầu</label>
                         <div class="fw-bold fs-5">
                             <javatime:format value="${session.startTime}"
                                 pattern="HH:mm dd/MM/yyyy" />
                         </div>
                     </div>

                     <div class="mb-3">
                         <label class="text-muted small text-uppercase fw-bold">Kết thúc</label>
                         <div class="fw-bold fs-5">
                             <c:if test="${not empty session.endTime}">
                                 <javatime:format value="${session.endTime}"
                                     pattern="HH:mm dd/MM/yyyy" />
                             </c:if>
                             <c:if test="${empty session.endTime}">
                                 --
                             </c:if>
                         </div>
                     </div>

                     <div class="d-grid gap-2 mt-4">
                         <c:choose>
                             <c:when test="${session.active}">
                                 <a href="<c:url value='/classrooms/${classroom.id}/sessions/${session.id}/qr'/>"
                                     class="btn btn-outline-primary">
                                     <i class="bi bi-qr-code"></i> Hiển thị mã QR
                                 </a>

                                 <form
                                     action="${pageContext.request.contextPath}/classrooms/${classroom.id}/sessions/${session.id}/end"
                                     method="post"
                                     onsubmit="return confirm('Bạn có chắc chắn muốn kết thúc phiên điểm danh này?');">
                                     <input type="hidden" name="${_csrf.parameterName}"
                                         value="${_csrf.token}" />
                                     <button type="submit" class="btn btn-danger w-100">Kết thúc
                                         phiên</button>
                                 </form>
                             </c:when>
                             <c:otherwise>
                                 <a href="#" class="btn btn-outline-primary disabled"
                                     aria-disabled="true" tabindex="-1">
                                     <i class="bi bi-qr-code"></i> Hiển thị mã QR
                                 </a>

                                 <button type="button" class="btn btn-secondary w-100" disabled>
                                     Phiên đã kết thúc
                                 </button>
                             </c:otherwise>
                         </c:choose>
                     </div>
                 </div>
             </div>
         </div>

         <!-- Card 2: List of Attendances -->
         <div class="col-md-8">
             <div class="card h-100 shadow-sm border-0">
                 <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                     <h5 class="card-title mb-0">Danh sách điểm danh</h5>
                     <span class="badge bg-info text-dark">
                         ${session.records.size()} / ${classroom.participants.size()} có mặt
                     </span>
                 </div>
                 <div class="card-body p-0">
                     <c:if test="${empty classroom.participants}">
                         <div class="text-center py-5 text-muted">
                             <i class="bi bi-people fs-1 d-block mb-3"></i>
                             Chưa có sinh viên nào trong lớp học.
                         </div>
                     </c:if>

                     <c:if test="${not empty classroom.participants}">
                         <div class="table-responsive p-3">
                             <table id="table"
                                 class="table table-striped">
                                 <thead>
                                     <tr>
                                         <th>Username</th>
                                         <th>Thời gian</th>
                                         <th>Thiết bị</th>
                                         <th>Trạng thái</th>
                                     </tr>
                                 </thead>
                                 <tbody>
                                     <c:forEach var="participant" items="${classroom.participants}">
                                         <!-- Lookup attendance record efficiently -->
                                         <c:set var="record"
                                             value="${session.attendanceMap[participant.user.id]}" />

                                         <tr>
                                             <td class="fw-bold">${participant.user.username}</td>
                                             <td>
                                                 <c:choose>
                                                     <c:when test="${not empty record}">
                                                         <<javatime:format value="${record.checkin}"
                                                             pattern="HH:mm dd/MM/yyyy" />
                                                     </c:when>
                                                     <c:otherwise>
                                                         <span class="text-muted">--</span>
                                                     </c:otherwise>
                                                 </c:choose>
                                             </td>
                                             <td>
                                                 <c:choose>
                                                     <c:when test="${not empty record}">
                                                         <small class="text-muted"
                                                             title="${record.deviceInfo}">
                                                             ${record.deviceUid}
                                                         </small>
                                                     </c:when>
                                                     <c:otherwise>
                                                         <span class="text-muted">--</span>
                                                     </c:otherwise>
                                                 </c:choose>
                                             </td>
                                             <td>
                                                 <c:choose>
                                                     <c:when test="${not empty record}">
                                                         <c:choose>
                                                             <c:when
                                                                 test="${record.status == 'PRESENT'}">
                                                                 <span
                                                                     class="badge bg-success">PRESENT</span>
                                                             </c:when>
                                                             <c:when
                                                                 test="${record.status == 'LATE'}">
                                                                 <span
                                                                     class="badge bg-warning text-dark">LATE</span>
                                                             </c:when>
                                                             <c:otherwise>
                                                                 <span
                                                                     class="badge bg-secondary">${record.status}</span>
                                                             </c:otherwise>
                                                         </c:choose>
                                                     </c:when>
                                                     <c:otherwise>
                                                         <span class="badge bg-danger">ABSENT</span>
                                                     </c:otherwise>
                                                 </c:choose>
                                             </td>
                                         </tr>
                                     </c:forEach>
                                 </tbody>
                             </table>
                         </div>
                     </c:if>
                 </div>
             </div>
         </div>
     </div>
 </div>