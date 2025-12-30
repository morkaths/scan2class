<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="page-heading">
	<div class="page-title">
		<div class="row">
			<div class="col-12 col-md-6 order-md-1 order-last">
				<h3>User Management</h3>
				<p class="text-subtitle text-muted">Manage system users and
					their information.</p>
			</div>
			<div class="col-12 col-md-6 order-md-2 order-first">
				<nav aria-label="breadcrumb"
					class="breadcrumb-header float-start float-lg-end">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="<c:url value='/' />">Dashboard</a></li>
						<li class="breadcrumb-item active" aria-current="page">Users</li>
					</ol>
				</nav>
			</div>
		</div>
	</div>
	<section class="section">
		<div class="card">
			<div class="card-header">
				<h5 class="card-title">Users List</h5>
				<a href="<c:url value='/admin/users/create' />"
					class="btn btn-primary mb-3"> <i class="bi bi-plus-circle me-2"></i>Create
					User
				</a>
			</div>
			<div class="card-body">
				<table class="table table-striped" id="table">
					<thead>
						<tr>
							<th>ID</th>
							<th>Username</th>
							<th>Email</th>
							<th>Status</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${users}">
							<tr>
								<td>${user.id}</td>
								<td>${user.username}</td>
								<td>${user.email}</td>
								<td><c:choose>
										<c:when test="${user.status == 1}">
											<span class="badge bg-success">Active</span>
										</c:when>
										<c:otherwise>
											<span class="badge bg-danger">Inactive</span>
										</c:otherwise>
									</c:choose></td>
								<td class="text-end">
									<div class="dropdown" style="position: static;">
										<button class="btn btn-link text-dark p-0" type="button"
											id="dropdownMenuButton-${user.id}" data-bs-toggle="dropdown"
											aria-expanded="false">
											<i class="bi bi-three-dots-vertical"></i>
										</button>
										<ul class="dropdown-menu"
											aria-labelledby="dropdownMenuButton-${user.id}">
											<li><a class="dropdown-item" href="#"><i
													class="bi bi-eye me-2"></i>View</a></li>
											<li><a class="dropdown-item"
												href="<c:url value='/admin/users/edit/${user.id}' />"> <i
													class="bi bi-pencil me-2"></i>Edit
											</a></li>
											<li>
												<form
													action="${pageContext.request.contextPath}/admin/users/delete/${user.id}"
													method="post" style="display: inline;">
													<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" />
													<button type="submit" class="dropdown-item text-danger"
														onclick="return confirm('Are you sure you want to delete this user?');">
														<i class="bi bi-trash me-2"></i>Delete
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
		</div>
	</section>
</div>