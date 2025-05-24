package ma.bankati.service.UserService;

import ma.bankati.model.users.User;
import java.util.List;

public interface IUserService {
    User createUser(String firstName, String lastName, String username, String password);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    void updateUser(User user);
    void deleteUser(Long id);
} 