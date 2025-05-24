package ma.bankati.dao.creditDao.databaseDao;

import ma.bankati.config.DatabaseConnection;
import ma.bankati.dao.creditDao.ICreditRequestDao;
import ma.bankati.dao.userDao.IUserDao;
import ma.bankati.dao.userDao.DatabaseDao.UserDbDao;
import ma.bankati.model.credit.CreditRequest;
import ma.bankati.model.credit.CreditStatus;
import ma.bankati.model.users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreditRequestDbDao implements ICreditRequestDao {
    private final Connection connection;
    private final IUserDao userDao;

    public CreditRequestDbDao() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            this.userDao = new UserDbDao();
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database connection", e);
        }
    }

    @Override
    public List<CreditRequest> findAll() {
        List<CreditRequest> requests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM credit_requests";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                requests.add(mapToCreditRequest(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all credit requests", e);
        }
        return requests;
    }

    @Override
    public CreditRequest findById(Long id) {
        try {
            String sql = "SELECT * FROM credit_requests WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapToCreditRequest(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding credit request by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<CreditRequest> findByUser(User user) {
        List<CreditRequest> requests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM credit_requests WHERE user_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, user.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                requests.add(mapToCreditRequest(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding credit requests for user: " + user.getId(), e);
        }
        return requests;
    }

    @Override
    public List<CreditRequest> findByStatus(CreditStatus status) {
        List<CreditRequest> requests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM credit_requests WHERE status = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                requests.add(mapToCreditRequest(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding credit requests by status: " + status, e);
        }
        return requests;
    }

    @Override
    public CreditRequest save(CreditRequest request) {
        try {
            String sql = "INSERT INTO credit_requests (user_id, amount, purpose, request_date, status, rejection_reason) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setLong(1, request.getUser().getId());
            ps.setDouble(2, request.getAmount());
            ps.setString(3, request.getPurpose());
            ps.setTimestamp(4, Timestamp.valueOf(request.getRequestDate()));
            ps.setString(5, request.getStatus().toString());
            ps.setString(6, request.getRejectionReason());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating credit request failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    request.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating credit request failed, no ID obtained.");
                }
            }

            return request;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving credit request", e);
        }
    }

    @Override
    public void update(CreditRequest request) {
        try {
            String sql = "UPDATE credit_requests SET user_id = ?, amount = ?, purpose = ?, status = ?, rejection_reason = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setLong(1, request.getUser().getId());
            ps.setDouble(2, request.getAmount());
            ps.setString(3, request.getPurpose());
            ps.setString(4, request.getStatus().toString());
            ps.setString(5, request.getRejectionReason());
            ps.setLong(6, request.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating credit request failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating credit request", e);
        }
    }

    @Override
    public void delete(CreditRequest request) {
        deleteById(request.getId());
    }

    @Override
    public void deleteById(Long id) {
        try {
            String sql = "DELETE FROM credit_requests WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting credit request failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting credit request", e);
        }
    }

    private CreditRequest mapToCreditRequest(ResultSet rs) throws SQLException {
        Long userId = rs.getLong("user_id");
        User user = userDao.findById(userId);
        
        return CreditRequest.builder()
                .id(rs.getLong("id"))
                .user(user)
                .amount(rs.getDouble("amount"))
                .purpose(rs.getString("purpose"))
                .requestDate(rs.getTimestamp("request_date").toLocalDateTime())
                .status(CreditStatus.valueOf(rs.getString("status")))
                .rejectionReason(rs.getString("rejection_reason"))
                .build();
    }
} 