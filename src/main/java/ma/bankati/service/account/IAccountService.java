package ma.bankati.service.account;

import ma.bankati.model.account.Account;
import ma.bankati.model.account.AccountStatus;
import ma.bankati.model.users.User;

public interface IAccountService {
    Account createAccount(User user, double initialBalance);
    Account getAccount(User user);
    void updateBalance(User user, double newBalance);
    void addToBalance(User user, double amount);
    void subtractFromBalance(User user, double amount);
    void updateStatus(User user, AccountStatus status);
} 