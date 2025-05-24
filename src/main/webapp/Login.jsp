<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bankati - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
        }
        .login-container {
            max-width: 400px;
            width: 100%;
            padding: 2rem;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .login-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        .login-header img {
            max-height: 60px;
            margin-bottom: 1rem;
        }
        .login-header h1 {
            color: #0d6efd;
            font-size: 1.8rem;
            font-weight: 600;
            margin: 0;
        }
        .form-floating {
            margin-bottom: 1rem;
        }
        .form-floating > .form-control {
            padding-left: 2.5rem;
            height: calc(3.5rem + 2px);
            line-height: 1.25;
            border: 1px solid #dee2e6;
            border-radius: 4px;
        }
        .form-floating > label {
            padding-left: 2.5rem;
        }
        .input-icon {
            position: absolute;
            left: 0.75rem;
            top: 50%;
            transform: translateY(-50%);
            color: #6c757d;
            z-index: 10;
        }
        .btn-login {
            width: 100%;
            padding: 0.75rem;
            font-weight: 500;
            border-radius: 4px;
        }
        .error-message {
            color: #dc3545;
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
        .alert {
            border-radius: 4px;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <img src="<%= request.getContextPath() %>/assets/img/logoBlue.png" alt="Bankati Logo">
            <h1>Bankati</h1>
        </div>
        
        <form action="login" method="post" autocomplete="off">
            <!-- Username field -->
            <div class="form-floating position-relative">
                <i class="bi bi-person-fill input-icon"></i>
                <input type="text" 
                       class="form-control" 
                       id="username" 
                       name="lg" 
                       placeholder="Username"
                       autocomplete="off">
                <label for="username">Username</label>
                <% if (request.getAttribute("usernameError") != null) { %>
                    <div class="error-message">${usernameError}</div>
                <% } %>
            </div>

            <!-- Password field -->
            <div class="form-floating position-relative">
                <i class="bi bi-lock-fill input-icon"></i>
                <input type="password" 
                       class="form-control" 
                       id="password" 
                       name="pss" 
                       placeholder="Password"
                       autocomplete="off">
                <label for="password">Password</label>
                <% if (request.getAttribute("passwordError") != null) { %>
                    <div class="error-message">${passwordError}</div>
                <% } %>
            </div>

            <% if (request.getAttribute("globalMessage") != null) { %>
                <div class="alert alert-danger text-center">
                    ${globalMessage}
                </div>
            <% } %>

            <button type="submit" class="btn btn-primary btn-login mt-3">
                Sign In
            </button>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
