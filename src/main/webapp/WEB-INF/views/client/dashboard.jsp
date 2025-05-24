<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    pageContext.setAttribute("formatter", formatter);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Client Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/client'/>">Bankati Client</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/client/profile">Profile</a>
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
        <div class="row">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Account Balance</h5>
                    </div>
                    <div class="card-body">
                        <h3 class="card-text">${account.balance} ${sessionScope.selectedCurrency}</h3>
                        <a href="<c:url value='/client/balance'/>" class="btn btn-primary">View in Other Currencies</a>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Quick Actions</h5>
                    </div>
                    <div class="card-body">
                        <a href="<c:url value='/client/credit-request'/>" class="btn btn-success mb-2 w-100">New Credit Request</a>
                        <a href="<c:url value='/client/credit-requests'/>" class="btn btn-info w-100">View Credit Requests</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Recent Credit Requests</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Amount</th>
                                        <th>Purpose</th>
                                        <th>Date</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${requests}" var="request">
                                        <tr>
                                            <td>${request.id}</td>
                                            <td><fmt:formatNumber value="${request.amount}" type="currency" currencySymbol="MAD"/></td>
                                            <td>${request.purpose}</td>
                                            <td>${request.requestDate.format(formatter)}</td>
                                            <td>
                                                <span class="badge bg-${request.status == 'PENDING' ? 'warning' : request.status == 'APPROVED' ? 'success' : 'danger'}">
                                                    ${request.status}
                                                </span>
                                            </td>
                                            <td>
                                                <c:if test="${request.status == 'PENDING'}">
                                                    <form action="<c:url value='/client/credit-request/delete'/>" method="post" class="d-inline">
                                                        <input type="hidden" name="requestId" value="${request.id}">
                                                        <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
                                                    </form>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 