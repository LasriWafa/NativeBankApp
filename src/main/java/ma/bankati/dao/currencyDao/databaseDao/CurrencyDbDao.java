package ma.bankati.dao.currencyDao.databaseDao;

import ma.bankati.config.DatabaseConnection;
import ma.bankati.dao.currencyDao.ICurrencyDao;

import java.sql.*;

public class CurrencyDbDao implements ICurrencyDao {
    private final Connection connection;

    public CurrencyDbDao() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public double getExchangeRate() {
        String query = "SELECT exchange_rate FROM currencies WHERE code = 'MAD'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getDouble("exchange_rate");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting exchange rate", e);
        }
        return 1.0; // Default to 1.0 if not found
    }
} 