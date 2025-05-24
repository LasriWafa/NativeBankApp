package ma.bankati.web.controllers.creditControllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.bankati.model.credit.CreditRequest;
import ma.bankati.model.credit.CreditStatus;
import ma.bankati.model.users.User;
import ma.bankati.service.credit.ICreditRequestService;

import java.io.IOException;
import java.util.List;

@WebServlet("/credit/*")
public class CreditRequestController extends HttpServlet {
    private ICreditRequestService creditRequestService;

    @Override
    public void init() throws ServletException {
        creditRequestService = (ICreditRequestService) getServletContext().getAttribute("creditRequestService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/":
                listAllRequests(request, response);
                break;
            case "/pending":
                listPendingRequests(request, response);
                break;
            case "/my-requests":
                listUserRequests(request, response);
                break;
            case "/request":
                showNewRequestForm(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showNewRequestForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/client/new-credit-request.jsp").forward(request, response);
    }

    private void listAllRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CreditRequest> requests = creditRequestService.getPendingRequests();
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/WEB-INF/views/credit/list.jsp").forward(request, response);
    }

    private void listPendingRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CreditRequest> requests = creditRequestService.getPendingRequests();
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/WEB-INF/views/credit/pending.jsp").forward(request, response);
    }

    private void listUserRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        List<CreditRequest> requests = creditRequestService.getUserRequests(user);
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/WEB-INF/views/credit/my-requests.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/request":
                createRequest(request, response);
                break;
            case "/delete":
                deleteRequest(request, response);
                break;
            case "/approve":
                approveRequest(request, response);
                break;
            case "/reject":
                rejectRequest(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        double amount = Double.parseDouble(request.getParameter("amount"));
        String purpose = request.getParameter("purpose");
        String description = request.getParameter("description");

        CreditRequest creditRequest = creditRequestService.requestCredit(user, amount, purpose);
        response.sendRedirect(request.getContextPath() + "/client/credit-requests");
    }

    private void deleteRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long requestId = Long.parseLong(request.getParameter("requestId"));
        CreditRequest creditRequest = creditRequestService.getUserRequests((User) request.getSession().getAttribute("connectedUser"))
                .stream()
                .filter(r -> r.getId().equals(requestId))
                .findFirst()
                .orElse(null);

        if (creditRequest != null) {
            creditRequestService.rejectRequest(creditRequest, "Request cancelled by user");
        }
        response.sendRedirect(request.getContextPath() + "/client/credit-requests");
    }

    private void approveRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long requestId = Long.parseLong(request.getParameter("requestId"));
        CreditRequest creditRequest = creditRequestService.getPendingRequests()
                .stream()
                .filter(r -> r.getId().equals(requestId))
                .findFirst()
                .orElse(null);

        if (creditRequest != null) {
            creditRequestService.approveRequest(creditRequest);
        }
        response.sendRedirect(request.getContextPath() + "/credit/pending");
    }

    private void rejectRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long requestId = Long.parseLong(request.getParameter("requestId"));
        String reason = request.getParameter("reason");
        CreditRequest creditRequest = creditRequestService.getPendingRequests()
                .stream()
                .filter(r -> r.getId().equals(requestId))
                .findFirst()
                .orElse(null);

        if (creditRequest != null) {
            creditRequestService.rejectRequest(creditRequest, reason);
        }
        response.sendRedirect(request.getContextPath() + "/credit/pending");
    }
} 