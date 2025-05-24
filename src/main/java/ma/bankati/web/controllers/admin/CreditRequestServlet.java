package ma.bankati.web.controllers.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/credit-requests/*")
public class CreditRequestServlet extends HttpServlet {
    private CreditRequestController creditRequestController;

    @Override
    public void init() throws ServletException {
        creditRequestController = new CreditRequestController();
        creditRequestController.init(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        creditRequestController.doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        creditRequestController.doPost(request, response);
    }
} 