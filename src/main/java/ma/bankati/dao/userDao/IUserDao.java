package ma.bankati.dao.userDao;

import ma.bankati.dao.CrudDao;
import ma.bankati.model.users.User;

import java.util.Optional;

public interface IUserDao extends CrudDao<User, Long> {

    Optional<User> findByLoginAndPassword(String username, String password);
}
