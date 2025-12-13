<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-md-8 col-12 offset-md-2">
    <div class="text-center">
        <img class="img-error" src="<c:url value='/assets/compiled/svg/error-403.svg' />" alt="Forbidden">
        <h1 class="error-title">Forbidden</h1>
        <p class="fs-5 text-gray-600">You are unauthorized to see this page.</p>
        <a href="<c:url value='/' />" class="btn btn-lg btn-outline-primary mt-3">Go Home</a>
    </div>
</div>
