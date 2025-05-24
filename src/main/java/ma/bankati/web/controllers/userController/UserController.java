package ma.bankati.web.controllers.userController;

import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.util.List;
import ma.bankati.dao.userDao.IUserDao;
import ma.bankati.model.users.ERole;
import ma.bankati.model.users.User;

public class UserController {
    private IUserDao userDao;

    public UserController() {
        // We'll initialize userDao in the first request
    }

    private void initUserDao(ServletContext context) {
        if (userDao == null) {
            userDao = (IUserDao) context.getAttribute("userDao");
        }
    }

    public void showAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initUserDao(request.getServletContext());
        List<User> users = userDao.findAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }

    public void showForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initUserDao(request.getServletContext());
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            Long id = Long.parseLong(idStr);
            User user = userDao.findById(id);
            request.setAttribute("user", user);
        }
        request.getRequestDispatcher("/WEB-INF/views/admin/user-form.jsp").forward(request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        initUserDao(request.getServletContext());
        Long id = Long.parseLong(request.getParameter("id"));
        userDao.deleteById(id);
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        initUserDao(request.getServletContext());
        String idStr = request.getParameter("id");
        Long id = (idStr == null || idStr.isEmpty()) ? null : Long.parseLong(idStr);

        User user = User.builder()
                .id(id)
                .firstName(request.getParameter("firstName"))
                .lastName(request.getParameter("lastName"))
                .username(request.getParameter("username"))
                .role(ERole.valueOf(request.getParameter("role")))
                .creationDate(java.time.LocalDate.now())
                .build();

        // Only update password if provided
        String password = request.getParameter("password");
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        } else if (id != null) {
            // Keep existing password if not provided during update
            User existingUser = userDao.findById(id);
            user.setPassword(existingUser.getPassword());
        }

        if (id == null) {
            userDao.save(user);
        } else {
            userDao.update(user);
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
