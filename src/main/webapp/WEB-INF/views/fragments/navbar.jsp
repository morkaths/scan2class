<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<header class="fixed-top shadow-sm mb-5">
	<div class="header-top">
		<div class="container">
			<a class="navbar-brand fw-bold fs-3" href="<c:url value='/' />">
			    <i class="bi bi-qr-code-scan me-2" style="font-size: 1.5rem; vertical-align: middle;"></i>
			    <span>Scan2Class</span>
			</a>
			<div class="header-top-right">
				<div class="dropdown">
					<sec:authorize access="isAuthenticated()">
						<a href="#" data-bs-toggle="dropdown" aria-expanded="false">
							<div class="user-menu d-flex">
								<div class="user-name text-end me-3 d-none d-md-block">
									<h6 class="mb-0 text-gray-600">
										<sec:authentication property="principal.username" />
									</h6>
									<p class="mb-0 text-sm text-gray-600">
										<sec:authentication property="principal.user.email" />
									</p>
								</div>
								<div class="user-img d-flex align-items-center">
									<div class="avatar avatar-md">
										<img src="<c:url value='/assets/compiled/jpg/1.jpg' />">
									</div>
								</div>
							</div>
						</a>
						<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton" style="min-width: 11rem;">
							<li>
								<h6 class="dropdown-header">Hello,
									<sec:authentication property="principal.username" />!
								</h6>
							</li>
							<li>
								<a class="dropdown-item" href="/profile">
									<i class="icon-mid bi bi-person me-2"></i> 
									Hồ sơ
								</a>
							</li>
							<li>
								<a class="dropdown-item" href="<c:url value='/classrooms' />">
									<i class="icon-mid bi bi-gear me-2"></i>
									Lớp học
								</a>
							</li>
							<li>
								<hr class="dropdown-divider">
							</li>
							<li>
								<a class="dropdown-item" href="<c:url value='/auth/logout' />">
									<i class="icon-mid bi bi-box-arrow-left me-2"></i> Đăng xuất
								</a>
							</li>
						</ul>
					</sec:authorize>
					<sec:authorize access="!isAuthenticated()">
						<div class="d-flex gap-2">
							<a href="<c:url value='/auth/login' />" class="btn btn-primary">Đăng nhập</a>
							<a href="<c:url value='/auth/register' />" class="d-none d-md-block btn btn-outline-primary">Đăng ký</a>
						</div>
					</sec:authorize>
				</div>

				<a href="#" class="burger-btn d-block d-xl-none"> 
					<i class="bi bi-justify fs-3"></i>
				</a>
			</div>
		</div>
	</div>
	<nav class="main-navbar" id="navbar_scroll">
		<div class="container">
			<ul>
				<li class="menu-item ">
					<a href="<c:url value='/' />" class='menu-link'>
						<span><i class="bi bi-grid-fill"></i> Trang chủ</span>
					</a>
				</li>
				<li class="menu-item  has-sub">
					<a href="#" class='menu-link'>
						<span><i class="bi bi-stack"></i> Lớp học</span>
					</a>
					<div class="submenu ">
						<div class="submenu-group-wrapper">
							<ul class="submenu-group">
								<li class="submenu-item  ">
									<a href="<c:url value='/classrooms/create' />" class='submenu-link'>Tạo lớp học</a>
								</li>
								<li class="submenu-item  ">
									<a href="<c:url value='/classrooms' />" class='submenu-link'>Danh sách lớp học</a>
								</li>
								<li class="submenu-item  ">
									<a href="/classrooms/statistic" class='submenu-link'>Thống kê lớp học</a>
								</li>
							</ul>
						</div>
					</div>
				</li>

				<li class="menu-item active has-sub">
					<a href="#" class='menu-link'> 
						<span><i class="bi bi-grid-1x2-fill"></i> Quản lý</span>
					</a>
					<div class="submenu ">
						<div class="submenu-group-wrapper">
							<ul class="submenu-group">
								<li class="submenu-item  ">
									<a href="/admin/classrooms"class='submenu-link'>
										Quản lý lớp học
									</a>
								</li>
								<li class="submenu-item  ">
									<a href="admin/users"class='submenu-link'>
										Quản lý người dùng
									</a>
								</li>
								<li class="submenu-item  ">
									<a href="admin/roles"class='submenu-link'>
										Quản lý quyền
									</a>
								</li>
							</ul>
						</div>
					</div>
				</li>

				<li class="menu-item  has-sub">
					<a href="#" class='menu-link'>
						<span><i class="bi bi-life-preserver"></i> Hỗ trợ</span>
					</a>
					<div class="submenu ">
						<div class="submenu-group-wrapper">
							<ul class="submenu-group">
								<li class="submenu-item  ">
									<a href="/contact"
										class='submenu-link'>Liên hệ</a>
								</li>
								<li class="submenu-item  ">
									<a href="/community"
										class='submenu-link'>Cộng đồng</a>
								</li>
							</ul>
						</div>
					</div>
				</li>
				
			</ul>
		</div>
	</nav>

</header>