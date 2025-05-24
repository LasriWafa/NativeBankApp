package ma.bankati.web.controllers.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.bankati.model.account.Account;
import ma.bankati.model.credit.CreditRequest;
import ma.bankati.model.credit.CreditStatus;
import ma.bankati.model.users.User;
import ma.bankati.model.data.MoneyData;
import ma.bankati.service.account.IAccountService;
import ma.bankati.service.credit.ICreditRequestService;
import ma.bankati.service.moneyServices.IMoneyService;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/client/*", "/client/dashboard.jsp", "/client/credit-requests", "/client/credit-request", "/client/credit-request/delete", "/client/profile"}, loadOnStartup = 1)
public class ClientDashboardController extends HttpServlet {

    private IAccountService accountService;
    private ICreditRequestService creditRequestService;
    private IMoneyService moneyService;

    @Override
    public void init() throws ServletException {
        System.out.println("ClientDashboardController créé et initialisé");
        accountService = (IAccountService) getServletContext().getAttribute("accountService");
        creditRequestService = (ICreditRequestService) getServletContext().getAttribute("creditRequestService");
        moneyService = (IMoneyService) getServletContext().getAttribute("moneyService");
        
        if (accountService == null || creditRequestService == null || moneyService == null) {
            throw new ServletException("Required services not found in servlet context");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/client") || path.equals("/client/")) {
            showDashboard(request, response);
        } else if (path.equals("/client/profile")) {
            showProfile(request, response);
        } else if (path.equals("/client/balance")) {
            showBalance(request, response);
        } else if (path.equals("/client/credit-requests")) {
            showCreditRequests(request, response);
        } else if (path.equals("/client/credit-request")) {
            showNewCreditRequestForm(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/client")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (path.equals("/client/credit-request")) {
            createCreditRequest(request, response);
        } else if (path.equals("/client/credit-request/delete")) {
            deleteCreditRequest(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = accountService.getAccount(user);
        
        // Get the selected currency from session, default to MAD if not set
        String selectedCurrency = (String) request.getSession().getAttribute("selectedCurrency");
        if (selectedCurrency == null) {
            selectedCurrency = "MAD";
            request.getSession().setAttribute("selectedCurrency", selectedCurrency);
        }

        // Convert the balance to the selected currency
        MoneyData convertedBalance = moneyService.convertAmount(account.getBalance(), "MAD", selectedCurrency);
        account.setBalance(convertedBalance.getAmount());

        List<CreditRequest> requests = creditRequestService.getUserRequests(user);

        request.setAttribute("account", account);
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/WEB-INF/views/client/dashboard.jsp").forward(request, response);
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/client/profile.jsp").forward(request, response);
    }

    private void showBalance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Account account = accountService.getAccount(user);
        request.setAttribute("account", account);
        request.getRequestDispatcher("/WEB-INF/views/client/balance.jsp").forward(request, response);
    }

    private void showCreditRequests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        List<CreditRequest> requests = creditRequestService.getUserRequests(user);
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("/WEB-INF/views/client/credit-requests.jsp").forward(request, response);
    }

    private void showNewCreditRequestForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/client/credit-request.jsp").forward(request, response);
    }

    private void createCreditRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            double amount = Double.parseDouble(request.getParameter("amount"));
            String purpose = request.getParameter("purpose");
            String description = request.getParameter("description");

            CreditRequest creditRequest = creditRequestService.requestCredit(user, amount, purpose);
            response.sendRedirect(request.getContextPath() + "/client/credit-requests");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format");
            request.getRequestDispatcher("/WEB-INF/views/client/new-credit-request.jsp").forward(request, response);
        }
    }

    private void deleteCreditRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Long requestId = Long.parseLong(request.getParameter("requestId"));
            List<CreditRequest> userRequests = creditRequestService.getUserRequests(user);
            CreditRequest creditRequest = userRequests.stream()
                    .filter(r -> r.getId().equals(requestId))
                    .findFirst()
                    .orElse(null);

            if (creditRequest != null && creditRequest.getStatus() == CreditStatus.PENDING) {
                creditRequestService.rejectRequest(creditRequest, "Request cancelled by user");
            }
        } catch (NumberFormatException e) {
            // Invalid request ID, just redirect back to the list
        }
        
        response.sendRedirect(request.getContextPath() + "/client/credit-requests");
    }

    @Override
    public void destroy() {
        System.out.println("ClientDashboardController détruit");
    }
} 