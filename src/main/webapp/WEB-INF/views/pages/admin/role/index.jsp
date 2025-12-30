<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="page-heading">
    <div class="page-title">
        <div class="row">
            <div class="col-12 col-md-6 order-md-1 order-last">
                <h3>Role Management</h3>
                <p class="text-subtitle text-muted">Manage system roles and permissions.</p>
            </div>
            <div class="col-12 col-md-6 order-md-2 order-first">
                <nav aria-label="breadcrumb" class="breadcrumb-header float-start float-lg-end">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="<c:url value='/' />">Dashboard</a></li>
                        <li class="breadcrumb-item active" aria-current="page">Roles</li>
                    </ol>
                </nav>
            </div>
        </div>
    </div>
    <section class="section">
        <div class="card">
            <div class="card-header">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0">Roles List</h5>
                    <a href="<c:url value='/admin/roles/create' />" class="btn btn-primary">
                        <i class="bi bi-plus-circle me-2"></i>Add Role
                    </a>
                </div>
            </div>
            <div class="card-body">
                <table class="table table-striped" id="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Code</th>
                            <th>Name</th>
                            <th>Permissions</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="role" items="${roles}">
                            <tr>
                                <td>${role.id}</td>
                                <td><span class="badge bg-info">${role.code}</span></td>
                                <td>${role.name}</td>
                                <td>
                                    <c:if test="${not empty role.permissions}">
                                        <c:forEach var="perm" items="${role.permissions}">
                                            <span class="badge bg-secondary">${perm.name}</span>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty role.permissions}">
                                        <span class="text-muted">No permissions</span>
                                    </c:if>
                                </td>
                                <td class="text-end">
                                    <div class="dropdown" style="position: static;">
                                        <button class="btn btn-link text-dark p-0" type="button"
                                            id="dropdownMenuButton-${role.id}" data-bs-toggle="dropdown"
                                            aria-expanded="false">
                                            <i class="bi bi-three-dots-vertical"></i>
                                        </button>
                                        <ul class="dropdown-menu"
                                            aria-labelledby="dropdownMenuButton-${role.id}">
                                            <li><a class="dropdown-item" href="#"><i
                                                        class="bi bi-pencil me-2"></i>Edit</a></li>
                                            <li><a class="dropdown-item" href="#"><i
                                                        class="bi bi-shield-check me-2"></i>Manage
                                                    Permissions</a></li>
                                            <li>
                                                <hr class="dropdown-divider">
                                            </li>
                                            <li>
                                            	<form
                                                    action="${pageContext.request.contextPath}/admin/roles/delete/${role.id}"
                                                    method="post" style="display:inline;">
                                                    <input type="hidden" name="${_csrf.parameterName}"
                                                        value="${_csrf.token}" />
                                                    <button type="submit" class="dropdown-item text-danger"
                                                        onclick="return confirm('Are you sure you want to delete this role?');">
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