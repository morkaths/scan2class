<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container py-5">
	<div class="card shadow-sm border-0">
		<div
			class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
			<div>
				<h5 class="mb-0 fw-bold text-primary">
					<i class="bi bi-mortarboard-fill me-2"></i>Quản lý Lớp học
				</h5>
				<small class="text-muted">Danh sách các lớp học của bạn</small>
			</div>
			<a href="<c:url value='/classrooms/create' />"
				class="btn btn-primary"> <i class="bi bi-plus-lg me-1"></i>Thêm
				lớp mới
			</a>
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
					<div class="table-responsive">
						<table class="table table-hover align-middle mb-0">
							<thead class="bg-light">
								<tr>
									<th class="ps-4">Thông tin lớp</th>
									<th>Phòng</th>
									<th>Trạng thái</th>
									<th class="text-end pe-4">Thao tác</th>
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
											<div class="d-flex align-items-center text-secondary">
												<i class="bi bi-geo-alt me-2"></i> <span>${classroom.room}</span>
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
											<div class="btn-group" role="group">
												<a href="<c:url value='/classrooms/${classroom.id}' />"
													class="btn btn-primary btn-sm" title="Xem chi tiết">
													<i class="bi bi-eye-fill me-1"></i>Chi tiết
												</a>

												<div class="btn-group" role="group">
													<button id="btnGroupDrop1" type="button"
														class="btn btn-outline-secondary btn-sm dropdown-toggle"
														data-bs-toggle="dropdown" aria-expanded="false">
														<i class="bi bi-three-dots-vertical"></i>
													</button>
													<ul class="dropdown-menu dropdown-menu-end"
														aria-labelledby="btnGroupDrop1">
														<li><a class="dropdown-item"
															href="<c:url value='/classrooms/edit/${classroom.id}' />">
																<i class="bi bi-pencil me-2"></i>Chỉnh sửa
														</a></li>
														<li>
															<hr class="dropdown-divider">
														</li>
														<li>
															<form
																action="<c:url value='/classrooms/delete/${classroom.id}' />"
																method="post" style="display: inline;">
																<input type="hidden" name="${_csrf.parameterName}"
																	value="${_csrf.token}" />
																<button type="submit" class="dropdown-item text-danger"
																	onclick="return confirm('Bạn có chắc muốn xóa lớp này?')">
																	<i class="bi bi-trash me-2"></i>Xóa
																</button>
															</form>
														</li>
													</ul>
												</div>
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