package ma.bankati.dao.accountDao;

import ma.bankati.dao.CrudDao;
import ma.bankati.model.account.Account;
import ma.bankati.model.users.User;

public interface IAccountDao extends CrudDao<Account, Long> {
    Account findByUser(User user);
} 