package ma.bankati.dao.accountDao.databaseDao;

import ma.bankati.config.DatabaseConnection;
import ma.bankati.dao.accountDao.IAccountDao;
import ma.bankati.model.account.Account;
import ma.bankati.model.account.AccountStatus;
import ma.bankati.model.users.User;
import ma.bankati.dao.userDao.DatabaseDao.UserDbDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDbDao implements IAccountDao {
    private final Connection connection;
    private final UserDbDao userDao;

    public AccountDbDao() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.userDao = new UserDbDao();
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                accounts.add(mapToAccount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all accounts", e);
        }
        return accounts;
    }

    @Override
    public Account findById(Long id) {
        String query = "SELECT * FROM accounts WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToAccount(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account by id: " + id, e);
        }
        return null;
    }

    @Override
    public Account findByUser(User user) {
        String query = "SELECT * FROM accounts WHERE user_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapToAccount(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account for user: " + user.getId(), e);
        }
        return null;
    }

    @Override
    public Account save(Account account) {
        String query = "INSERT INTO accounts (user_id, balance, creation_date, status) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, account.getUser().getId());
            pstmt.setDouble(2, account.getBalance());
            pstmt.setDate(3, Date.valueOf(account.getCreationDate()));
            pstmt.setString(4, account.getStatus().name());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
            return account;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account", e);
        }
    }

    @Override
    public void update(Account account) {
        String query = "UPDATE accounts SET balance = ?, status = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, account.getBalance());
            pstmt.setString(2, account.getStatus().name());
            pstmt.setLong(3, account.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating account failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account: " + account.getId(), e);
        }
    }

    @Override
    public void delete(Account account) {
        deleteById(account.getId());
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM accounts WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting account failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting account: " + id, e);
        }
    }

    private Account mapToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setBalance(rs.getDouble("balance"));
        account.setCreationDate(rs.getDate("creation_date").toLocalDate());
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        
        // Get the user
        Long userId = rs.getLong("user_id");
        User user = userDao.findById(userId);
        account.setUser(user);
        
        return account;
    }
} 