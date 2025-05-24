package ma.bankati.web.controllers.mainController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.bankati.service.moneyServices.IMoneyService;
import ma.bankati.model.data.MoneyData;

import java.io.IOException;

@WebServlet(urlPatterns = "/home", loadOnStartup = 1)
public class HomeController extends HttpServlet
{
    private IMoneyService service;

    @Override
    public void init() throws ServletException {
        System.out.println("HomeController créé et initialisé");
        service = (IMoneyService) getServletContext().getAttribute("moneyService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                                throws ServletException, IOException {

        System.out.println("Call for HomeController doGet Method");

        // Convert a sample amount from MAD to EUR for demonstration
        MoneyData result = service.convertAmount(1000.0, "MAD", "EUR");
        request.setAttribute("result", result);

        // 🔁 Récupérer le chemin de la vue injecté par le filtre
        String viewPath = (String) request.getAttribute("viewPath");

        if (viewPath == null) {
            // Cas de sécurité si quelqu'un arrive ici sans rôle (non connecté ?)
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher(viewPath).forward(request, response);

    }
    @Override
    public void destroy() {
        System.out.println("HomeController détruit");
    }
}
