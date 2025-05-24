<%@ page import="ma.bankati.model.users.User" pageEncoding="UTF-8" %>
<html>
<head>
	<title>H O M E</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bootstrap.min.css">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
</head>
<%
	var user    = (User) session.getAttribute("connectedUser");
	var appName = (String) application.getAttribute("AppName");
%>
<body class="bgBlue Optima">

			<!-- ✅ NAVBAR HAUT -->
			<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
				<div class="container-fluid">
					<a class="navbar-brand d-flex align-items-center" href="#">
						<img src="<%= request.getContextPath() %>/assets/img/login.png" alt="Logo" width="40" height="40" class="d-inline-block align-text-top me-2">
						<strong class="blue ml-1"><%= appName %></strong>
					</a>
					
					<div class="ms-auto">
						<span class="navbar-text text-secondary me-3 small">
							Session ouverte pour : <b class="text-success"><%= user %></b>
						</span>
					</div>
				</div>
			</nav>
			
			<div class="container w-50 bg-white mt-5 border border-light rounded-circle mb-5">
				<div class="card-body text-center">
					<h1 class="mt-4 mb-3 text-danger font-weight-bold text-capitalize">
						Page publique
					</h1>
				</div>
			</div>
			
			
			<!-- ✅ FOOTER FIXÉ EN BAS -->
			<nav class="navbar footer-navbar fixed-bottom bg-white">
				<div class="container d-flex justify-content-between align-items-center w-100">
					
					<!-- Texte à gauche -->
					<span class="text-muted small">
						© <%= appName %> 2025 – Tous droits réservés
					</span>
					
					<!-- Bouton à droite -->
					<a href="logout" class="btn btn-outline-danger btn-sm d-flex align-items-center logout-btn font-weight-bold">
						<i class="bi bi-box-arrow-right me-1 mr-1"></i> Déconnexion
					</a>
				</div>
			</nav>
</body>
</html>
