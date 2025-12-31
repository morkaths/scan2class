<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="javatime"
uri="http://sargue.net/jsptags/time" %>

<div class="container py-5">
  <!-- Flash Messages -->
  <c:if test="${not empty message}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
      ${message}
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
      ></button>
    </div>
  </c:if>
  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
      ${errorMessage}
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
      ></button>
    </div>
  </c:if>

  <div class="row g-4">
    <!-- Card 1: Session Information -->
    <div class="col-md-4">
      <div class="card shadow-sm border-0 mb-4">
        <div class="card-header bg-white py-3">
          <h5 class="card-title mb-0">Thông tin phiên điểm danh</h5>
        </div>
        <div class="card-body">
          <h4 class="card-title text-primary mb-3">${session.name}</h4>

          <div class="mb-3">
            <label class="text-muted small text-uppercase fw-bold"
              >Trạng thái</label
            >
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
            <label class="text-muted small text-uppercase fw-bold"
              >Bắt đầu</label
            >
            <div class="fw-bold fs-5">
              <javatime:format
                value="${session.startTime}"
                pattern="HH:mm dd/MM/yyyy"
              />
            </div>
          </div>

          <div class="mb-3">
            <label class="text-muted small text-uppercase fw-bold"
              >Kết thúc</label
            >
            <div class="fw-bold fs-5">
              <c:if test="${not empty session.endTime}">
                <javatime:format
                  value="${session.endTime}"
                  pattern="HH:mm dd/MM/yyyy"
                />
              </c:if>
              <c:if test="${empty session.endTime}"> -- </c:if>
            </div>
          </div>

          <div class="d-grid gap-2 mt-4">
            <c:choose>
              <c:when test="${session.active}">
                <a
                  href="<c:url value='/classrooms/${classroom.id}/sessions/${session.id}/qr'/>"
                  class="btn btn-outline-primary"
                >
                  <i class="bi bi-qr-code"></i> Hiển thị mã QR
                </a>

                <form
                  action="${pageContext.request.contextPath}/classrooms/${classroom.id}/sessions/${session.id}/end"
                  method="post"
                  onsubmit="return confirm('Bạn có chắc chắn muốn kết thúc phiên điểm danh này?');"
                >
                  <input
                    type="hidden"
                    name="${_csrf.parameterName}"
                    value="${_csrf.token}"
                  />
                  <button type="submit" class="btn btn-danger w-100">
                    Kết thúc phiên
                  </button>
                </form>
              </c:when>
              <c:otherwise>
                <a
                  href="#"
                  class="btn btn-outline-primary disabled"
                  aria-disabled="true"
                  tabindex="-1"
                >
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

      <!-- Stats Card -->
       <div class="card shadow-sm border-0">
        <div class="card-header bg-white py-3">
          <h5 class="card-title mb-0">Thống kê</h5>
        </div>
        <div class="card-body">
            <div style="position: relative; height: 250px; width: 100%;">
                <canvas id="sessionChart"></canvas>
            </div>
        </div>
      </div>
    </div>

    <!-- Card 2: List of Attendances -->
    <div class="col-md-8">
      <div class="card h-100 shadow-sm border-0">
        <div
          class="card-header bg-white py-3 d-flex justify-content-between align-items-center"
        >
          <h5 class="card-title mb-0">Danh sách điểm danh</h5>
          <div>
              <a href="<c:url value='/attend/classrooms/${classroom.id}/analytics'/>" class="btn btn-sm btn-outline-primary me-2">
                 <i class="bi bi-graph-up"></i> Thống kê
              </a>
	          <span class="badge bg-info text-dark">
	            ${session.records.size()} / ${classroom.participants.size()} có mặt
	          </span>
          </div>
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
              <table id="table" class="table table-striped">
                <thead>
                  <tr>
                    <th>Username</th>
                    <th>Thời gian</th>
                    <th>Thiết bị</th>
                    <th>Trạng thái</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach
                    var="participant"
                    items="${classroom.participants}"
                  >
                    <!-- Lookup attendance record efficiently -->
                    <c:set
                      var="record"
                      value="${session.attendanceMap[participant.user.id]}"
                    />

                    <tr>
                      <td class="fw-bold">${participant.user.username}</td>
                      <td>
                        <c:choose>
                          <c:when test="${not empty record}">
                            <<javatime:format
                              value="${record.checkin}"
                              pattern="HH:mm dd/MM/yyyy"
                            />
                          </c:when>
                          <c:otherwise>
                            <span class="text-muted">--</span>
                          </c:otherwise>
                        </c:choose>
                      </td>
                      <td>
                        <c:choose>
                          <c:when test="${not empty record}">
                            <small
                              class="text-muted"
                              title="${record.deviceInfo}"
                            >
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
                            <!-- Check if current user is owner (Teacher) -->
                            <c:set
                              var="isOwner"
                              value="${classroom.owner.username == pageContext.request.userPrincipal.name}"
                            />

                            <c:choose>
                              <c:when test="${isOwner}">
                                <div class="dropdown" style="position: static;">
                                  <button
                                    class="btn btn-sm dropdown-toggle status-btn-${participant.user.id} ${record.status == 'PRESENT' ? 'btn-success' : (record.status == 'LATE' ? 'btn-warning text-dark' : 'btn-secondary')}"
                                    type="button"
                                    data-bs-toggle="dropdown"
                                    aria-expanded="false"
                                    id="dropdown-${participant.user.id}"
                                  >
                                    ${record.status}
                                  </button>
                                  <ul
                                    class="dropdown-menu"
                                    aria-labelledby="dropdown-${participant.user.id}"
                                  >
                                    <li>
                                      <a
                                        class="dropdown-item status-update"
                                        href="#"
                                        data-user-id="${participant.user.id}"
                                        data-session-id="${session.id}"
                                        data-status="PRESENT"
                                        data-class="btn-success"
                                        >PRESENT</a
                                      >
                                    </li>
                                    <li>
                                      <a
                                        class="dropdown-item status-update"
                                        href="#"
                                        data-user-id="${participant.user.id}"
                                        data-session-id="${session.id}"
                                        data-status="ABSENT"
                                        data-class="btn-danger"
                                        >ABSENT</a
                                      >
                                    </li>
                                    <li>
                                      <a
                                        class="dropdown-item status-update"
                                        href="#"
                                        data-user-id="${participant.user.id}"
                                        data-session-id="${session.id}"
                                        data-status="LATE"
                                        data-class="btn-warning text-dark"
                                        >LATE</a
                                      >
                                    </li>
                                  </ul>
                                </div>
                              </c:when>
                              <c:otherwise>
                                <c:choose>
                                  <c:when test="${record.status == 'PRESENT'}">
                                    <span class="badge bg-success"
                                      >PRESENT</span
                                    >
                                  </c:when>
                                  <c:when test="${record.status == 'LATE'}">
                                    <span class="badge bg-warning text-dark"
                                      >LATE</span
                                    >
                                  </c:when>
                                  <c:otherwise>
                                    <span class="badge bg-secondary"
                                      >${record.status}</span
                                    >
                                  </c:otherwise>
                                </c:choose>
                              </c:otherwise>
                            </c:choose>
                          </c:when>
                          <c:otherwise>
                             <!-- No Record - Allow Create if Owner -->
                            <c:set
                              var="isOwner"
                              value="${classroom.owner.username == pageContext.request.userPrincipal.name}"
                            />
                             <c:choose>
                               <c:when test="${isOwner}">
                                 <div class="dropdown" style="position: static;">
                                   <button
                                     class="btn btn-sm btn-danger dropdown-toggle status-btn-${participant.user.id}"
                                     type="button"
                                     data-bs-toggle="dropdown"
                                     aria-expanded="false"
                                     id="dropdown-${participant.user.id}"
                                   >
                                     ABSENT
                                   </button>
                                   <ul
                                     class="dropdown-menu"
                                     aria-labelledby="dropdown-${participant.user.id}"
                                   >
                                     <li>
                                       <a
                                         class="dropdown-item status-update"
                                         href="#"
                                         data-user-id="${participant.user.id}"
                                         data-session-id="${session.id}"
                                         data-status="PRESENT"
                                         data-class="btn-success"
                                         >PRESENT</a
                                       >
                                     </li>
                                     <li>
                                       <a
                                         class="dropdown-item status-update"
                                         href="#"
                                         data-user-id="${participant.user.id}"
                                         data-session-id="${session.id}"
                                         data-status="ABSENT"
                                         data-class="btn-danger"
                                         >ABSENT</a
                                       >
                                     </li>
                                     <li>
                                       <a
                                         class="dropdown-item status-update"
                                         href="#"
                                         data-user-id="${participant.user.id}"
                                         data-session-id="${session.id}"
                                         data-status="LATE"
                                         data-class="btn-warning text-dark"
                                         >LATE</a
                                       >
                                     </li>
                                   </ul>
                                 </div>
                               </c:when>
                               <c:otherwise>
                                 <span class="badge bg-danger">ABSENT</span>
                               </c:otherwise>
                             </c:choose>
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

 <!-- Toast Container -->
 <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
   <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true">
     <div class="toast-header">
       <i class="bi bi-info-circle-fill me-2 text-primary"></i>
       <strong class="me-auto">Thông báo</strong>
       <small>Vừa xong</small>
       <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
     </div>
     <div class="toast-body" id="toast-message">
       Cập nhật thành công.
     </div>
   </div>
 </div>

 <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
 <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
 <script>
 $(document).ready(function() {
     // Fetch Session Stats
     const sessionId = '${session.id}';
     $.ajax({
       url: '${pageContext.request.contextPath}/attend/sessions/' + sessionId + '/stats',
       type: 'GET',
       success: function(data) {
           const ctx = document.getElementById('sessionChart').getContext('2d');
           // data: { presentCount, lateCount, absentCount, totalStudents ... }
           new Chart(ctx, {
               type: 'pie',
               data: {
                   labels: ['Có mặt', 'Đi muộn', 'Vắng'],
                   datasets: [{
                       data: [data.presentCount, data.lateCount, data.absentCount],
                       backgroundColor: [
                           '#198754', // Success green
                           '#ffc107', // Warning yellow
                           '#dc3545'  // Danger red
                       ],
                       borderWidth: 1
                   }]
               },
               options: {
                   responsive: true,
                   maintainAspectRatio: false,
                   plugins: {
                       legend: {
                           position: 'bottom',
                       }
                   }
               }
           });
       },
       error: function(err) {
           console.error("Failed to load stats", err);
       }
     });

     $('.status-update').click(function(e) {
         e.preventDefault();
// ... existing code ...
         var userId = $(this).data('user-id');
         var sessionId = $(this).data('session-id');
         var newStatus = $(this).data('status');
         var btnClass = $(this).data('class');
         var note = "Manual update by teacher";
         
         var csrfToken = $("meta[name='_csrf']").attr("content");
         var csrfHeader = $("meta[name='_csrf_header']").attr("content");

         $.ajax({
             url: '${pageContext.request.contextPath}/attend/manual-update',
             type: 'POST',
             data: {
                 userId: userId,
                 sessionId: sessionId,
                 status: newStatus,
                 note: note
             },
             beforeSend: function(xhr) {
                 if (csrfHeader && csrfToken) {
                     xhr.setRequestHeader(csrfHeader, csrfToken);
                 }
             },
             success: function(response) {
                 if (response.success) {
                     // Update Button UI
                     var btn = $('.status-btn-' + userId);
                     btn.removeClass('btn-success btn-danger btn-secondary btn-warning text-dark');
                     btn.addClass(btnClass);
                     btn.text(newStatus);
                     
                     // Show Toast
                     $('#toast-message').text(response.message);
                     var toastEl = document.getElementById('liveToast');
                     var toast = new bootstrap.Toast(toastEl);
                     toast.show();
                     
                     // Optionally reload stats?
                 } else {
                     alert(response.message);
                 }
             },
             error: function(xhr) {
                 var msg = "Lỗi khi cập nhật";
                 if(xhr.responseJSON && xhr.responseJSON.message) {
                     msg = xhr.responseJSON.message;
                 }
                 alert("Error: " + msg);
             }
         });
     });
 });
 </script>
