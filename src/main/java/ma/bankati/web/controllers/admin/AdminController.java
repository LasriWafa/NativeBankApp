package ma.bankati.web.controllers.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.bankati.model.users.User;
import ma.bankati.service.credit.ICreditRequestService;
import ma.bankati.dao.userDao.IUserDao;
import ma.bankati.model.credit.CreditRequest;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/*", "/admin/dashboard.jsp"}, loadOnStartup = 1)
public class AdminController extends HttpServlet {

    private ICreditRequestService creditRequestService;

    @Override
    public void init() throws ServletException {
        System.out.println("AdminController créé et initialisé");
        creditRequestService = (ICreditRequestService) getServletContext().getAttribute("creditRequestService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || "/".equals(path)) {
            // Show admin dashboard
            List<CreditRequest> pendingRequests = creditRequestService.getPendingRequests();
            request.setAttribute("pendingRequests", pendingRequests.size());
            request.setAttribute("totalUsers", ((IUserDao) getServletContext().getAttribute("userDao")).findAll().size());
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } else if ("/credit-requests".equals(path)) {
            // Show credit requests management
            request.setAttribute("requests", creditRequestService.getPendingRequests());
            request.getRequestDispatcher("/WEB-INF/views/admin/credit-requests.jsp").forward(request, response);
        } else if ("/users".equals(path)) {
            // Redirect to user management
            response.sendRedirect(request.getContextPath() + "/users");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void destroy() {
        System.out.println("AdminController détruit");
    }
} 