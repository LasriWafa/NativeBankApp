package ma.bankati.web.controllers.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.bankati.model.users.User;
import ma.bankati.model.account.Account;
import ma.bankati.model.data.MoneyData;
import ma.bankati.service.account.IAccountService;
import ma.bankati.service.moneyServices.IMoneyService;
import ma.bankati.service.moneyServices.serviceDirham.ServiceDh;

import java.io.IOException;

@WebServlet("/client/balance/*")
public class ClientBalanceController extends HttpServlet {
    private IAccountService accountService;
    private IMoneyService moneyService;

    @Override
    public void init() throws ServletException {
        accountService = (IAccountService) getServletContext().getAttribute("accountService");
        moneyService = new ServiceDh();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/")) {
            showBalance(request, response);
        } else if (path.equals("/convert")) {
            convertCurrency(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showBalance(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Account account = accountService.getAccount(user);
        String selectedCurrency = (String) request.getSession().getAttribute("selectedCurrency");
        if (selectedCurrency == null) {
            selectedCurrency = "MAD";
            request.getSession().setAttribute("selectedCurrency", selectedCurrency);
        }

        MoneyData balance = moneyService.convertAmount(account.getBalance(), "MAD", selectedCurrency);
        request.setAttribute("balance", String.format("%.2f", balance.getAmount()));
        request.setAttribute("currency", balance.getDevise().name());
        request.getRequestDispatcher("/WEB-INF/views/client/balance.jsp").forward(request, response);
    }

    private void convertCurrency(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("connectedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String toCurrency = request.getParameter("currency");
        if (toCurrency == null) {
            toCurrency = "MAD";
        }

        // Store the selected currency in session
        request.getSession().setAttribute("selectedCurrency", toCurrency);

        Account account = accountService.getAccount(user);
        MoneyData convertedAmount = moneyService.convertAmount(account.getBalance(), "MAD", toCurrency);

        request.setAttribute("balance", String.format("%.2f", convertedAmount.getAmount()));
        request.setAttribute("currency", convertedAmount.getDevise().name());
        request.getRequestDispatcher("/WEB-INF/views/client/balance.jsp").forward(request, response);
    }
} 