<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    pageContext.setAttribute("formatter", formatter);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Profile - Bankati</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/client">Bankati Client</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link active" href="<c:url value='/client/profile'/>">Profile</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/client/balance'/>">Balance</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/client/credit-requests'/>">Credit Requests</a>
                    </li>
                </ul>
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/logout'/>">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card bg-dark text-white">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Profile Information</h5>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <strong>Username:</strong>
                            </div>
                            <div class="col-md-8">
                                ${user.username}
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <strong>First Name:</strong>
                            </div>
                            <div class="col-md-8">
                                ${user.firstName}
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <strong>Last Name:</strong>
                            </div>
                            <div class="col-md-8">
                                ${user.lastName}
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <strong>Role:</strong>
                            </div>
                            <div class="col-md-8">
                                ${user.role}
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <strong>Member Since:</strong>
                            </div>
                            <div class="col-md-8">
                                ${user.creationDate != null ? user.creationDate.format(formatter) : 'N/A'}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 