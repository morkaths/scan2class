<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="page-heading">
	<div class="page-title">
		<div class="row">
			<div class="col-12 col-md-6 order-md-1 order-last">
				<h3>Create Classroom</h3>
				<p class="text-subtitle text-muted">Create a new classroom.</p>
			</div>
			<div class="col-12 col-md-6 order-md-2 order-first">
				<nav aria-label="breadcrumb"
					class="breadcrumb-header float-start float-lg-end">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="<c:url value='/' />">Dashboard</a></li>
						<li class="breadcrumb-item"><a
							href="<c:url value='/admin/classrooms' />">Classrooms</a></li>
						<li class="breadcrumb-item active" aria-current="page">Create
							Classroom</li>
					</ol>
				</nav>
			</div>
		</div>
	</div>
	<section class="section">
		<div class="card">
			<div class="card-header">
				<h4 class="card-title">Classroom Information</h4>
			</div>
			<div class="card-body">
				<form:form
					action="${pageContext.request.contextPath}/admin/classrooms/create"
					method="post" modelAttribute="dto">
					<div class="row">
						<div class="col-md-6">
							<div class="mb-3">
								<label for="code" class="form-label">Code</label>
								<form:input path="code" cssClass="form-control" id="code"
									required="required" />
								<form:errors path="code" cssClass="text-danger" />
							</div>
						</div>
						<div class="col-md-6">
							<div class="mb-3">
								<label for="name" class="form-label">Name</label>
								<form:input path="name" cssClass="form-control" id="name"
									required="required" />
								<form:errors path="name" cssClass="text-danger" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="mb-3">
								<label for="room" class="form-label">Room</label>
								<form:input path="room" cssClass="form-control" id="room" />
								<form:errors path="room" cssClass="text-danger" />
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
								<label for="ownerId" class="form-label">Owner</label>
								<form:select path="ownerId" cssClass="form-select" id="ownerId">
									<form:options items="${users}" itemValue="id"
										itemLabel="username" />
								</form:select>
								<form:errors path="ownerId" cssClass="text-danger" />
							</div>
						</div>
					</div>
					<div class="d-flex justify-content-end">
						<a href="<c:url value='/admin/classrooms' />"
							class="btn btn-secondary me-2">Cancel</a>
						<button type="submit" class="btn btn-primary">Create
							Classroom</button>
					</div>
				</form:form>
			</div>
		</div>
	</section>
</div>