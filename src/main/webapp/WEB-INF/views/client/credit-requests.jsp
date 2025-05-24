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
    <title>Credit Requests</title>
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/client/profile">Profile</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/client/balance">Balance</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/client/credit-requests">Credit Requests</a>
                    </li>
                </ul>
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="card-title mb-0">Credit Requests</h5>
                        <a href="${pageContext.request.contextPath}/client/credit-request" class="btn btn-success">New Request</a>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <c:if test="${empty requests}">
                                <div class="alert alert-info">No credit requests found.</div>
                            </c:if>
                            <c:if test="${not empty requests}">
                                <div class="alert alert-info">Found ${requests.size()} credit requests.</div>
                            </c:if>
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
                                            <td>${request.amount} MAD</td>
                                            <td>${request.purpose}</td>
                                            <td>${request.requestDate.format(formatter)}</td>
                                            <td>
                                                <span class="badge bg-${request.status == 'PENDING' ? 'warning' : request.status == 'APPROVED' ? 'success' : 'danger'}">
                                                    ${request.status}
                                                </span>
                                            </td>
                                            <td>
                                                <c:if test="${request.status == 'PENDING'}">
                                                    <form action="${pageContext.request.contextPath}/client/credit-request/delete" method="post" class="d-inline">
                                                        <input type="hidden" name="requestId" value="${request.id}">
                                                        <button type="submit" class="btn btn-danger btn-sm">Cancel</button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${request.status == 'REJECTED'}">
                                                    <button type="button" class="btn btn-info btn-sm" 
                                                            data-bs-toggle="modal" 
                                                            data-bs-target="#reasonModal${request.id}">
                                                        View Reason
                                                    </button>
                                                    <div class="modal fade" id="reasonModal${request.id}" tabindex="-1">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title">Rejection Reason</h5>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <p>${request.rejectionReason}</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
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