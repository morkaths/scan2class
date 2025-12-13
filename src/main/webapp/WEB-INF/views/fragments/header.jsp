<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<header>
	<nav class="navbar navbar-expand navbar-light navbar-top">
		<div class="container-fluid">
			<a href="#" class="burger-btn d-block">
				<i class="bi bi-justify fs-3"></i>
			</a>

			<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
				data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav ms-auto mb-lg-0">
					<li class="nav-item dropdown me-1">
							<a class="nav-link dropdown-toggle text-gray-600" href="#" id="langDropdown" data-bs-toggle="dropdown" aria-expanded="false">
									<c:choose>
											<c:when test="${sessionScope.lang == 'vi'}">
													<span class="fi fi-vn fs-4"></span>
											</c:when>
											<c:when test="${sessionScope.lang == 'en'}">
													<span class="fi fi-gb fs-4"></span>
											</c:when>
											<c:when test="${sessionScope.lang == 'ja'}">
													<span class="fi fi-jp fs-4"></span>
											</c:when>
											<c:otherwise>
													<i class="bi bi-translate fs-4"></i>
											</c:otherwise>
									</c:choose>
									Ngôn ngữ
							</a>
							<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="langDropdown">
									<li>
											<a class="dropdown-item" href="<c:url value='/?lang=vi' />">
													<span class="fi fi-vn me-2"></span> Tiếng Việt
											</a>
									</li>
									<li>
											<a class="dropdown-item" href="<c:url value='/?lang=en' />">
													<span class="fi fi-gb me-2"></span> English
											</a>
									</li>
									<li>
											<a class="dropdown-item" href="<c:url value='/?lang=ja' />">
													<span class="fi fi-jp me-2"></span> 日本語
											</a>
									</li>
							</ul>
					</li>
					<li class="nav-item dropdown me-3">
						<a class="nav-link active dropdown-toggle text-gray-600" href="#" data-bs-toggle="dropdown"
							data-bs-display="static" aria-expanded="false">
							<i class='bi bi-bell bi-sub fs-4'></i>
							<span class="badge badge-notification bg-danger">7</span>
						</a>
						<ul class="dropdown-menu dropdown-center  dropdown-menu-sm-end notification-dropdown"
							aria-labelledby="dropdownMenuButton">
							<li class="dropdown-header">
								<h6>Notifications</h6>
							</li>
							<li class="dropdown-item notification-item">
								<a class="d-flex align-items-center" href="#">
									<div class="notification-icon bg-primary">
										<i class="bi bi-cart-check"></i>
									</div>
									<div class="notification-text ms-4">
										<p class="notification-title font-bold">Successfully check out</p>
										<p class="notification-subtitle font-thin text-sm">Order ID #256</p>
									</div>
								</a>
							</li>
							<li class="dropdown-item notification-item">
								<a class="d-flex align-items-center" href="#">
									<div class="notification-icon bg-success">
										<i class="bi bi-file-earmark-check"></i>
									</div>
									<div class="notification-text ms-4">
										<p class="notification-title font-bold">Homework submitted</p>
										<p class="notification-subtitle font-thin text-sm">Algebra math homework</p>
									</div>
								</a>
							</li>
							<li>
								<p class="text-center py-2 mb-0"><a href="#">See all notification</a></p>
							</li>
						</ul>
					</li>
				</ul>

				<div class="dropdown">
					<sec:authorize access="isAuthenticated()">
						<a href="#" data-bs-toggle="dropdown" aria-expanded="false">
							<div class="user-menu d-flex">
								<div class="user-name text-end me-3">
									<h6 class="mb-0 text-gray-600">
										<sec:authentication property="principal.username" />
									</h6>
									<p class="mb-0 text-sm text-gray-600">
										<sec:authentication property="principal.authorities" />
									</p>
								</div>
								<div class="user-img d-flex align-items-center">
									<div class="avatar avatar-md">
										<img src="<c:url value='/assets/compiled/jpg/1.jpg' />">
									</div>
								</div>
							</div>
						</a>
						<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton"
							style="min-width: 11rem;">
							<li>
								<h6 class="dropdown-header">Hello,
									<sec:authentication property="principal.username" />!
								</h6>
							</li>
							<li><a class="dropdown-item" href="#"><i class="icon-mid bi bi-person me-2"></i> My
									Profile</a></li>
							<li><a class="dropdown-item" href="#"><i class="icon-mid bi bi-gear me-2"></i>
									Settings</a></li>
							<li><a class="dropdown-item" href="#"><i class="icon-mid bi bi-wallet me-2"></i>
									Wallet</a></li>
							<li>
								<hr class="dropdown-divider">
							</li>
							<li>
								<a class="dropdown-item" href="<c:url value='/auth/logout' />">
									<i class="icon-mid bi bi-box-arrow-left me-2"></i> Logout
								</a>
							</li>
						</ul>
					</sec:authorize>
					<sec:authorize access="!isAuthenticated()">
						<div class="d-flex gap-2">
							<a href="<c:url value='/auth/login' />" class="btn btn-primary">Đăng nhập</a>
							<a href="<c:url value='/auth/register' />" class="btn btn-outline-primary">Đăng ký</a>
						</div>
					</sec:authorize>
				</div>
			</div>
		</div>
	</nav>
</header>