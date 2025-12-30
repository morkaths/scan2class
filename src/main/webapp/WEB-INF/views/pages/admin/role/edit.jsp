<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="page-heading">
	<div class="page-title">
		<div class="col-12 col-md-6 order-md-1 order-last">
                <h3>Edit Role</h3>
                <p class="text-subtitle text-muted">Update role information.</p>
            </div>
            <div class="col-12 col-md-6 order-md-2 order-first">
                <nav aria-label="breadcrumb" class="breadcrumb-header float-start float-lg-end">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="<c:url value='/' />">Dashboard</a></li>
                        <li class="breadcrumb-item"><a href="<c:url value='/admin/roles' />">Role</a></li>
                        <li class="breadcrumb-item active" aria-current="page">Edit</li>
                    </ol>
                </nav>
            </div>
	</div>
	<section class="section">
		<div class="card">
			<div class="card-header">Role Information</div>
			<div class="card-body">
				<form:form
					action="${pageContext.request.contextPath}/admin/roles/edit"
					method="post" modelAttribute="dto">
					<div class="mb-3">
						<label for="code" class="form-label">Role Code</label>
						<form:input path="code" cssClass="form-control" id="code"
							required="required" />
						<form:errors path="code" cssClass="text-danger" />
					</div>
					<div class="mb-3">
						<label for="name" class="form-label">Role Name</label>
						<form:input path="name" cssClass="form-control" id="name"
							required="required" />
						<form:errors path="name" cssClass="text-danger" />
					</div>
					<div class="mb-3">
						<label class="form-label">Permissions</label>
						<div class="row">
							<c:forEach var="permission" items="${permissions}">
								<c:if test="${not empty permission.id}">
									<div class="col-md-6">
										<div class="form-check">
											<form:checkbox path="permissionIds" value="${permission.id}"
												cssClass="form-check-input" id="permission-${permission.id}" />
											<label class="form-check-label"
												for="permission-${permission.id}">
												${permission.name} </label>
										</div>
									</div>
								</c:if>
							</c:forEach>
						</div>
						<form:errors path="permissionIds" cssClass="text-danger" />
					</div>
					<div class="d-flex justify-content-end">
						<a href="<c:url value='/admin/roles' />"
							class="btn btn-secondary me-2">Cancel</a>
						<button type="submit" class="btn btn-primary">Add Role</button>
					</div>
				</form:form>
			</div>
		</div>
	</section>
</div>