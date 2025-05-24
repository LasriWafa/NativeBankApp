package ma.bankati.web.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.bankati.config.WebContext;

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Use WebContext to initialize all services and DAOs
        WebContext webContext = new WebContext();
        webContext.contextInitialized(sce);
        System.out.println("Application services initialized successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application context destroyed");
    }
} 