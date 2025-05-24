package ma.bankati.dao.creditDao;

import ma.bankati.model.credit.CreditRequest;
import ma.bankati.model.credit.CreditStatus;
import ma.bankati.model.users.User;
import java.util.List;

public interface ICreditRequestDao {
    List<CreditRequest> findAll();
    CreditRequest findById(Long id);
    List<CreditRequest> findByUser(User user);
    List<CreditRequest> findByStatus(CreditStatus status);
    CreditRequest save(CreditRequest request);
    void update(CreditRequest request);
    void delete(CreditRequest request);
    void deleteById(Long id);
} 