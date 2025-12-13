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
            <h1 class="auth-title">Sign Up</h1>
            <p class="auth-subtitle mb-5">Input your data to register to our website.</p>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-success" role="alert">
                    ${errorMessage}
                </div>
            </c:if>

            <form:form modelAttribute="registerForm" action="${pageContext.request.contextPath}/auth/register" method="post">
                
                <form:errors path="email" cssClass="alert alert-danger" element="div" />
                <div class="form-group position-relative has-icon-left mb-4">
                    <form:input path="email" type="email" class="form-control form-control-xl" placeholder="Email"/>
                    <div class="form-control-icon">
                        <i class="bi bi-envelope"></i>
                    </div>
                </div>
                <form:errors path="username" cssClass="alert alert-danger" element="div" />
                <div class="form-group position-relative has-icon-left mb-4">
                    <form:input path="username" type="text" class="form-control form-control-xl" placeholder="Username"/>
                    <div class="form-control-icon">
                        <i class="bi bi-person"></i>
                    </div>
                </div>
                <form:errors path="password" cssClass="alert alert-danger" element="div" />
                <div class="form-group position-relative has-icon-left mb-4">
                    <form:input path="password" type="password" class="form-control form-control-xl" placeholder="Password"/>
                    <div class="form-control-icon">
                        <i class="bi bi-shield-lock"></i>
                    </div>
                </div>
                <form:errors path="confirmPassword" cssClass="alert alert-danger" element="div" />
                <div class="form-group position-relative has-icon-left mb-4">
                    <form:input path="confirmPassword" type="password" class="form-control form-control-xl" placeholder="Confirm Password"/>
                    <div class="form-control-icon">
                        <i class="bi bi-shield-lock"></i>
                    </div>
                </div>
                <button class="btn btn-primary btn-block btn-lg shadow-lg mt-5">Sign Up</button>
            </form:form>
            <div class="text-center mt-5 text-lg fs-4">
                <p class='text-gray-600'>Already have an account? <a href="<c:url value='/auth/login'/>" class="font-bold">Log in</a>.</p>
            </div>
        </div>
    </div>
    <div class="col-lg-7 d-none d-lg-block">
        <div id="auth-right">

        </div>
    </div>
</div>
