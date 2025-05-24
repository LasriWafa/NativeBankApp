package ma.bankati.service.account;

import ma.bankati.dao.accountDao.IAccountDao;
import ma.bankati.model.account.Account;
import ma.bankati.model.account.AccountStatus;
import ma.bankati.model.users.User;

public class AccountServiceImpl implements IAccountService {
    private final IAccountDao accountDao;

    public AccountServiceImpl(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account createAccount(User user, double initialBalance) {
        Account account = Account.builder()
                .user(user)
                .balance(initialBalance)
                .status(AccountStatus.ACTIVE)
                .creationDate(java.time.LocalDate.now())
                .build();
        return accountDao.save(account);
    }

    @Override
    public Account getAccount(User user) {
        Account account = accountDao.findByUser(user);
        if (account == null) {
            // Create a new account if one doesn't exist
            account = createAccount(user, 0.0);
        }
        return account;
    }

    @Override
    public void updateBalance(User user, double newBalance) {
        Account account = getAccount(user);
        account.setBalance(newBalance);
        accountDao.update(account);
    }

    @Override
    public void addToBalance(User user, double amount) {
        Account account = getAccount(user);
        account.setBalance(account.getBalance() + amount);
        accountDao.update(account);
    }

    @Override
    public void subtractFromBalance(User user, double amount) {
        Account account = getAccount(user);
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountDao.update(account);
        } else {
            throw new IllegalStateException("Insufficient funds");
        }
    }

    @Override
    public void updateStatus(User user, AccountStatus status) {
        Account account = getAccount(user);
        account.setStatus(status);
        accountDao.update(account);
    }
} 