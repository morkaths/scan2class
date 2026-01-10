<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <div class="row h-100">
            <div class="col-lg-5 col-12">
                <div id="auth-left">
                    <div class="auth-logo">
                        <a href="<c:url value='/'/>">
                            <jsp:include page="/WEB-INF/views/fragments/logo.jsp" />
                        </a>
                    </div>
                    <h1 class="auth-title">Log in.</h1>
                    <p class="auth-subtitle mb-5">Log in with your data that you entered during registration.</p>

                    <c:if test="${not empty message}">
                        <div class="alert alert-success" role="alert">
                            ${message}
                        </div>
                    </c:if>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">
                            ${errorMessage}
                        </div>
                    </c:if>

                    <form:form modelAttribute="loginForm" action="${pageContext.request.contextPath}/auth/login"
                        method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                        <form:errors path="username" cssClass="text-danger" />
                        <div class="form-group position-relative has-icon-left mb-4">
                            <form:input path="username" type="text" class="form-control form-control-xl"
                                placeholder="Username" />
                            <div class="form-control-icon">
                                <i class="bi bi-person"></i>
                            </div>
                        </div>
                        <form:errors path="password" cssClass="text-danger" />
                        <div class="form-group position-relative has-icon-left mb-4">
                            <form:input path="password" type="password" class="form-control form-control-xl"
                                placeholder="Password" />
                            <div class="form-control-icon">
                                <i class="bi bi-shield-lock"></i>
                            </div>
                        </div>
                        <div class="form-check form-check-lg d-flex align-items-end">
                            <input class="form-check-input me-2" type="checkbox" value="" id="flexCheckDefault">
                            <label class="form-check-label text-gray-600" for="flexCheckDefault">
                                Keep me logged in
                            </label>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block btn-lg shadow-lg mt-5">Log in</button>
                    </form:form>

                    <div class="text-center mt-4 mb-4">
                        <span class="text-gray-600">OR</span>
                    </div>

                    <a href="${pageContext.request.contextPath}/oauth2/authorization/google" class="btn btn-lg btn-block btn-outline-primary shadow-sm d-flex align-items-center justify-content-center">
                        <img src="${pageContext.request.contextPath}/assets/static/images/logos/google-icon-logo.svg" alt="Google" style="width: 20px; height: 20px; margin-right: 10px;" />
                        Login with Google
                    </a>
                    <div class="text-center mt-5 text-lg fs-4">
                        <p class="text-gray-600">Don't have an account?
                            <a href="<c:url value='/auth/register'/>" class="font-bold">Sign up</a>.
                        </p>
                        <p><a class="font-bold" href="auth-forgot-password.html">Forgot password?</a>.</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-7 d-none d-lg-block">
                <div id="auth-right">

                </div>
            </div>
        </div>