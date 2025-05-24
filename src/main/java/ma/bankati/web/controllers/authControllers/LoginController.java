package ma.bankati.web.controllers.authControllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.bankati.model.users.User;
import ma.bankati.model.users.ERole;
import ma.bankati.service.authentification.IAuthentificationService;

import java.io.IOException;

@WebServlet(urlPatterns = "/login", loadOnStartup = 2)
public class LoginController extends HttpServlet {

    private IAuthentificationService authService;

    @Override
    public void init() throws ServletException {
        System.out.println("LoginController initialization started...");
        try {
            authService = (IAuthentificationService) getServletContext().getAttribute("authService");
            if (authService == null) {
                System.err.println("Authentication service not found in servlet context");
                throw new ServletException("Authentication service not initialized");
            }
            System.out.println("LoginController initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing LoginController: " + e.getMessage());
            throw new ServletException("Failed to initialize LoginController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/Login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("lg");
            String password = request.getParameter("pss");

            if (!authService.validateLoginForm(username, password)) {
                authService.getFieldErrors().forEach(request::setAttribute);
                request.setAttribute("globalMessage", authService.getGlobalMessage());
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
                return;
            }

            User user = authService.connect(username, password);

            if (user != null) {
                request.getSession().setAttribute("connectedUser", user);
                request.getSession().setAttribute("globalMessage", authService.getGlobalMessage());
                
                // Redirect based on user role
                if (user.getRole() == ERole.USER) {
                    response.sendRedirect(request.getContextPath() + "/client");
                } else if (user.getRole() == ERole.ADMIN) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                request.setAttribute("globalMessage", authService.getGlobalMessage());
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in LoginController.doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("globalMessage", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("/Login.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println("LoginController destroyed");
    }
}
