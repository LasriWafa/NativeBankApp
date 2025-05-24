<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Account Balance - Bankati</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .balance-display {
            font-size: 2.5rem;
            font-weight: bold;
            color: #ffffff;
            margin: 2rem 0;
        }
        .currency-option {
            padding: 1rem;
            margin: 0.5rem;
            border: 2px solid #343a40;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
            color: #ffffff;
            background-color: #343a40;
        }
        .currency-option:hover {
            border-color: #6c757d;
            background-color: #495057;
        }
        .currency-option.active {
            border-color: #6c757d;
            background-color: #495057;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/client">Bankati Client</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/client/profile'/>">Profile</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<c:url value='/client/balance'/>">Balance</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/client/credit-requests'/>">Credit Requests</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
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
                        <h5 class="card-title mb-0">Account Balance</h5>
                    </div>
                    <div class="card-body text-center">
                        <div class="balance-display">
                            ${balance} ${currency}
                        </div>
                        <div class="row mt-4">
                            <div class="col-12">
                                <h4 class="mb-3">Convert to:</h4>
                                <div class="d-flex justify-content-center flex-wrap">
                                    <a href="<c:url value='/client/balance/convert?currency=MAD'/>" 
                                       class="currency-option ${currency == 'MAD' ? 'active' : ''}">
                                        MAD (Moroccan Dirham)
                                    </a>
                                    <a href="<c:url value='/client/balance/convert?currency=EUR'/>" 
                                       class="currency-option ${currency == 'EUR' ? 'active' : ''}">
                                        EUR (Euro)
                                    </a>
                                    <a href="<c:url value='/client/balance/convert?currency=USD'/>" 
                                       class="currency-option ${currency == 'USD' ? 'active' : ''}">
                                        USD (US Dollar)
                                    </a>
                                    <a href="<c:url value='/client/balance/convert?currency=GBP'/>" 
                                       class="currency-option ${currency == 'GBP' ? 'active' : ''}">
                                        GBP (British Pound)
                                    </a>
                                </div>
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