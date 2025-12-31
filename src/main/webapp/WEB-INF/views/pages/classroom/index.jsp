<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container py-5">
	<div class="card shadow-sm border-0">
		<div class="card-header py-2 d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
		    <div>
		        <h5 class="mb-0 fw-bold text-primary align-items-center">
		            <i class="bi bi-mortarboard-fill fs-4"></i>
		            <span>Quản lý Lớp học</span>
		        </h5>
		        <small class="text-muted d-block mt-1">Danh sách các lớp học của bạn</small>
		    </div>
		
		    <div class="d-grid d-md-block">
		        <a href="<c:url value='/classrooms/create' />" class="btn btn-primary px-4 shadow-sm">
		            <i class="bi bi-plus-lg me-1"></i>
		            Thêm lớp mới
		        </a>
		    </div>
		</div>
		<div class="card-body p-0">
			<c:choose>
				<c:when test="${empty classrooms}">
					<div class="text-center py-5">
						<i class="bi bi-inbox text-muted" style="font-size: 3rem;"></i>
						<p class="mt-3 text-muted">Bạn chưa tạo lớp học nào.</p>
						<a href="<c:url value='/classrooms/create' />"
							class="btn btn-outline-primary btn-sm mt-2"> Tạo lớp ngay </a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="table-responsive p-3">
						<table id="table" class="table table-striped">
							<thead>
								<tr>
									<th class="ps-4">Thông tin lớp</th>
									<th>Phòng</th>
									<th>Trạng thái</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="classroom" items="${classrooms}">
									<tr>
										<td class="ps-4"><a
											href="<c:url value='/classrooms/${classroom.id}' />"
											class="text-decoration-none">
												<div class="d-flex align-items-center">
													<div class="bg-light rounded p-2 me-3 text-center"
														style="width: 45px; height: 45px;">
														<i class="bi bi-hash text-primary h5 m-0"></i>
													</div>
													<div>
														<div class="fw-bold text-dark">${classroom.code}</div>
														<div class="small text-muted">${classroom.name}</div>
													</div>
												</div>
										</a></td>
										<td>
											<div class="align-items-center text-secondary">
												<i class="bi bi-geo-alt"></i> <span>${classroom.room}</span>
											</div>
										</td>
										<td><c:choose>
												<c:when test="${classroom.status == 1}">
													<span
														class="badge bg-success-subtle text-success border border-success-subtle rounded-pill">
														<i class="bi bi-check-circle me-1"></i>Đang hoạt động
													</span>
												</c:when>
												<c:otherwise>
													<span
														class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill">
														<i class="bi bi-x-circle me-1"></i>Đã kết thúc
													</span>
												</c:otherwise>
											</c:choose></td>
										<td class="text-end pe-4">
										    <div class="dropdown" style="position: static;">
										        <button class="btn btn-link text-dark p-0 shadow-none" 
										                type="button" 
										                data-bs-toggle="dropdown" 
										                aria-expanded="false">
										            <i class="bi bi-three-dots-vertical fs-5"></i>
										        </button>
										        
										        <ul class="dropdown-menu dropdown-menu-end shadow border-0 animated-fade-in">
										            <li>
										                <a class="dropdown-item py-2" href="<c:url value='/classrooms/${classroom.id}' />">
										                    <i class="bi bi-eye me-2 text-primary"></i>
										                    Xem chi tiết
										                </a>
										            </li>
										            <li>
										                <a class="dropdown-item py-2" href="<c:url value='/classrooms/edit/${classroom.id}' />">
										                    <i class="bi bi-pencil me-2 text-secondary"></i>
										                    Chỉnh sửa
										                </a>
										            </li>
										            <li>
										                <hr class="dropdown-divider">
										            </li>
										            <li>
										                <form action="<c:url value='/classrooms/delete/${classroom.id}' />" 
										                      method="post" 
										                      onsubmit="return confirm('Bạn có chắc chắn muốn xoá lớp học này? Mọi dữ liệu điểm danh liên quan sẽ bị mất vĩnh viễn.');"
										                      id="delete-form-${classroom.id}">
										                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
										                    <button type="submit" class="dropdown-item py-2 text-danger">
										                        <i class="bi bi-trash me-2"></i>
										                        Xoá lớp học
										                    </button>
										                </form>
										            </li>
										        </ul>
										    </div>
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