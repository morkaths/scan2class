<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="en">

        </head>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="_csrf" content="${_csrf.token}" />
        <meta name="_csrf_header" content="${_csrf.headerName}" />
        <title>${assets.title} - Scan2Class</title>

        <!-- Main CSS -->
        <link rel="stylesheet" crossorigin href="<c:url value='/assets/compiled/css/app.css' />">
        <link rel="stylesheet" crossorigin href="<c:url value='/assets/compiled/css/app-dark.css' />">
        <link rel="stylesheet" href="<c:url value='/assets/compiled/css/iconly.css' />">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <!-- Custom CSS -->
        <c:if test="${not empty assets.stylesheets}">
            <c:forEach var="css" items="${assets.stylesheets}">
                <link rel="stylesheet" href="<c:url value='${css}' />">
            </c:forEach>
        </c:if>

        <style>
            body {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }

            .main-content {
                flex: 1;
                margin-top: 56px;
            }

            .hero-section {
                background: linear-gradient(135deg, #435ebe 0%, #25396f 100%);
                color: white;
                padding: 100px 0;
            }
        </style>
        </head>

        <body>
            <nav class="navbar navbar-expand-md fixed-top">
                <div class="container">
                    <a class="navbar-brand fw-bold" href="<c:url value='/' />">
                        <i class="bi bi-qr-code-scan me-2"></i>Scan2Class
                    </a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNav">
                        <ul class="navbar-nav ms-auto align-items-center">
                            <li class="nav-item"><a class="nav-link" href="#features">Features</a></li>
                            <li class="nav-item"><a class="nav-link" href="#about">About</a></li>
                            <li class="nav-item"><a class="nav-link" href="#contact">Contact</a></li>

                            <c:choose>
                                <c:when test="${pageContext.request.userPrincipal != null}">
                                    <li class="nav-item ms-lg-3 dropdown">
                                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                            data-bs-toggle="dropdown" aria-expanded="false">
                                            <i class="bi bi-person-circle me-1"></i>
                                            ${pageContext.request.userPrincipal.name}
                                        </a>
                                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                                            <li><a class="dropdown-item" href="<c:url value='/classrooms' />">My
                                                    Classrooms</a></li>
                                            <li>
                                                <hr class="dropdown-divider">
                                            </li>
                                            <li><a class="dropdown-item text-danger"
                                                    href="<c:url value='/auth/logout' />">Logout</a></li>
                                        </ul>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="nav-item ms-lg-3">
                                        <a href="<c:url value='/auth/login' />"
                                            class="btn btn-primary btn-sm px-4">Login</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                </div>
            </nav>

            <main class="main-content">
                <jsp:include page="/WEB-INF/views/${content}.jsp" />
            </main>

            <footer class="py-4 mt-5">
                <div class="container text-center">
                    <p class="mb-0">&copy; 2024 Scan2Class. All rights reserved.</p>
                </div>
            </footer>

            <!-- Components Script -->
            <script src="<c:url value='/assets/static/js/components/dark.js' />"></script>
            <!-- Perfect Scrollbar JS -->
            <script src="<c:url value='/assets/extensions/perfect-scrollbar/perfect-scrollbar.min.js' />"></script>
            <!-- Main JS -->
            <script src="<c:url value='/assets/compiled/js/app.js' />"></script>
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

            <!-- Custom JS -->
            <c:if test="${not empty assets.scripts}">
                <c:forEach var="js" items="${assets.scripts}">
                    <script src="<c:url value='${js}' />"></script>
                </c:forEach>
            </c:if>
        </body>

        </html>