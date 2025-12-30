<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="page-heading">
	<div class="page-title">
		<div class="row">
			<div class="col-12 col-md-6 order-md-1 order-last">
				<h3>Create User</h3>
				<p class="text-subtitle text-muted">Create a new user account.</p>
			</div>
			<div class="col-12 col-md-6 order-md-2 order-first">
				<nav aria-label="breadcrumb"
					class="breadcrumb-header float-start float-lg-end">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="<c:url value='/' />">Dashboard</a></li>
						<li class="breadcrumb-item"><a
							href="<c:url value='/admin/users' />">Users</a></li>
						<li class="breadcrumb-item active" aria-current="page">Create
							User</li>
					</ol>
				</nav>
			</div>
		</div>
	</div>
	<section class="section">
		<div class="card">
			<div class="card-header">
				<h4 class="card-title">User Information</h4>
			</div>
			<div class="card-body">
				<form:form
					action="${pageContext.request.contextPath}/admin/users/create"
					method="post" modelAttribute="dto">
					<div class="row">
						<div class="col-md-12">
							<div class="mb-3">
								<label for="fullname" class="form-label">Full Name</label>
								<form:input path="fullname" cssClass="form-control"
									id="fullname" required="required" />
								<form:errors path="fullname" cssClass="text-danger" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="mb-3">
								<label for="username" class="form-label">Username</label>
								<form:input path="username" cssClass="form-control"
									id="username" required="required" />
								<form:errors path="username" cssClass="text-danger" />
							</div>
						</div>
						<div class="col-md-6">
							<div class="mb-3">
								<label for="email" class="form-label">Email</label>
								<form:input path="email" cssClass="form-control" id="email"
									required="required" />
								<form:errors path="email" cssClass="text-danger" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="mb-3">
								<label for="password" class="form-label">Password</label>
								<form:password path="password" cssClass="form-control"
									id="password" required="required" />
								<form:errors path="password" cssClass="text-danger" />
							</div>
						</div>
						<div class="col-md-6">
							<div class="mb-3">
								<label for="status" class="form-label">Status</label>
								<form:select path="status" cssClass="form-select" id="status">
									<form:option value="1" label="Active" />
									<form:option value="0" label="Inactive" />
								</form:select>
								<form:errors path="status" cssClass="text-danger" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="mb-3">
								<label class="form-label">Roles</label>
								<div class="row">
									<c:forEach var="role" items="${roles}">
										<div class="col-md-6">
											<div class="form-check">
												<form:checkbox path="roleIds" value="${role.id}"
													cssClass="form-check-input" id="role-${role.id}" />
												<label class="form-check-label" for="role-${role.id}">
													${role.name} </label>
											</div>
										</div>
									</c:forEach>
								</div>
								<form:errors path="roleIds" cssClass="text-danger" />
							</div>
						</div>
					</div>
					<div class="d-flex justify-content-end">
						<a href="<c:url value='/admin/users' />"
							class="btn btn-secondary me-2">Cancel</a>
						<button type="submit" class="btn btn-primary">Add User</button>
					</div>
				</form:form>
			</div>
		</div>
	</section>
</div>