package ma.bankati.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Properties;
import ma.bankati.dao.userDao.IUserDao;
import ma.bankati.dao.currencyDao.ICurrencyDao;
import ma.bankati.dao.accountDao.IAccountDao;
import ma.bankati.dao.creditDao.ICreditRequestDao;
import ma.bankati.service.authentification.IAuthentificationService;
import ma.bankati.service.moneyServices.IMoneyService;
import ma.bankati.service.account.IAccountService;
import ma.bankati.service.credit.ICreditRequestService;
import ma.bankati.dao.userDao.DatabaseDao.UserDbDao;
import ma.bankati.dao.accountDao.databaseDao.AccountDbDao;
import ma.bankati.dao.creditDao.databaseDao.CreditRequestDbDao;
import ma.bankati.dao.currencyDao.databaseDao.CurrencyDbDao;
import ma.bankati.service.moneyServices.serviceDirham.ServiceDh;
import ma.bankati.service.authentification.AuthentificationServiceImpl;
import ma.bankati.service.account.AccountServiceImpl;
import ma.bankati.service.credit.CreditRequestServiceImpl;

@WebListener
public class WebContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        try {
            ServletContext application = ev.getServletContext();
            application.setAttribute("AppName", "Bankati");
            
            // Initialize DAOs
            IUserDao userDao = new UserDbDao();
            IAccountDao accountDao = new AccountDbDao();
            ICreditRequestDao creditRequestDao = new CreditRequestDbDao();
            ICurrencyDao currencyDao = new CurrencyDbDao();
            
            // Initialize Services
            IMoneyService moneyService = new ServiceDh(currencyDao);
            IAuthentificationService authService = new AuthentificationServiceImpl(userDao);
            IAccountService accountService = new AccountServiceImpl(accountDao);
            ICreditRequestService creditRequestService = new CreditRequestServiceImpl(creditRequestDao, accountService);
            
            // Register beans
            application.setAttribute("userDao", userDao);
            application.setAttribute("accountDao", accountDao);
            application.setAttribute("creditRequestDao", creditRequestDao);
            application.setAttribute("currencyDao", currencyDao);
            application.setAttribute("moneyService", moneyService);
            application.setAttribute("authService", authService);
            application.setAttribute("accountService", accountService);
            application.setAttribute("creditRequestService", creditRequestService);
            
            System.out.println("Application context initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize application context: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        ServletContext application = ev.getServletContext();
        application.removeAttribute("userDao");
        application.removeAttribute("accountDao");
        application.removeAttribute("creditRequestDao");
        application.removeAttribute("currencyDao");
        application.removeAttribute("moneyService");
        application.removeAttribute("authService");
        application.removeAttribute("accountService");
        application.removeAttribute("creditRequestService");
        application.removeAttribute("AppName");
        System.out.println("Application context destroyed");
    }
}
