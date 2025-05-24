package ma.bankati.web.controllers.admin;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.bankati.dao.creditDao.ICreditRequestDao;
import ma.bankati.dao.accountDao.IAccountDao;
import ma.bankati.dao.userDao.IUserDao;
import ma.bankati.model.credit.CreditRequest;
import ma.bankati.model.credit.CreditStatus;
import ma.bankati.model.users.User;
import ma.bankati.model.account.Account;

import java.io.IOException;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class CreditRequestController {
    private ICreditRequestDao creditRequestDao;
    private IAccountDao accountDao;
    private IUserDao userDao;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void init(ServletContext context) throws ServletException {
        creditRequestDao = (ICreditRequestDao) context.getAttribute("creditRequestDao");
        accountDao = (IAccountDao) context.getAttribute("accountDao");
        userDao = (IUserDao) context.getAttribute("userDao");
        
        if (creditRequestDao == null || accountDao == null || userDao == null) {
            throw new ServletException("Required DAOs not found in servlet context");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || "/".equals(path)) {
            // Show all credit requests
            List<CreditRequest> creditRequests = creditRequestDao.findAll();
            request.setAttribute("requests", creditRequests);
            request.setAttribute("dateFormatter", DATE_FORMATTER);
            request.getRequestDispatcher("/WEB-INF/views/admin/credit-requests.jsp").forward(request, response);
        } else if ("/approve".equals(path)) {
            // Approve credit request
            Long id = Long.parseLong(request.getParameter("id"));
            CreditRequest creditRequest = creditRequestDao.findById(id);
            
            if (creditRequest != null && creditRequest.getStatus() == CreditStatus.PENDING) {
                creditRequest.setStatus(CreditStatus.APPROVED);
                creditRequestDao.update(creditRequest);
                
                User user = userDao.findById(creditRequest.getUser().getId());
                if (user != null) {
                    Account userAccount = accountDao.findByUser(user);
                    if (userAccount != null) {
                        double currentBalance = userAccount.getBalance();
                        userAccount.setBalance(currentBalance + creditRequest.getAmount());
                        accountDao.update(userAccount);
                    }
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/credit-requests");
        } else if ("/reject".equals(path)) {
            // Reject credit request
            Long id = Long.parseLong(request.getParameter("id"));
            String reason = request.getParameter("reason");
            CreditRequest creditRequest = creditRequestDao.findById(id);
            
            if (creditRequest != null && creditRequest.getStatus() == CreditStatus.PENDING) {
                creditRequest.setStatus(CreditStatus.REJECTED);
                creditRequest.setRejectionReason(reason);
                creditRequestDao.update(creditRequest);
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/credit-requests");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if ("/reject".equals(path)) {
            // Reject credit request
            Long id = Long.parseLong(request.getParameter("id"));
            String reason = request.getParameter("rejectionReason");
            CreditRequest creditRequest = creditRequestDao.findById(id);
            
            if (creditRequest != null && creditRequest.getStatus() == CreditStatus.PENDING) {
                creditRequest.setStatus(CreditStatus.REJECTED);
                creditRequest.setRejectionReason(reason);
                creditRequestDao.update(creditRequest);
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/credit-requests");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
} 