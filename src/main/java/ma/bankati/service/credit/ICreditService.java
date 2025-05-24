package ma.bankati.service.credit;

import ma.bankati.model.credit.CreditRequest;
import ma.bankati.model.users.User;

import java.util.List;

public interface ICreditService {
    CreditRequest requestCredit(User user, double amount, String purpose);
    List<CreditRequest> getUserRequests(User user);
    List<CreditRequest> getPendingRequests();
    void approveRequest(CreditRequest request);
    void rejectRequest(CreditRequest request, String reason);
} 