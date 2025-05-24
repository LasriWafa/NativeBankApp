package ma.bankati.service.credit;

import ma.bankati.dao.creditDao.ICreditRequestDao;
import ma.bankati.model.credit.CreditRequest;
import ma.bankati.model.credit.CreditStatus;
import ma.bankati.model.users.User;
import ma.bankati.service.account.IAccountService;

import java.util.List;

public class CreditServiceImpl implements ICreditService {
    private final ICreditRequestDao creditRequestDao;
    private final IAccountService accountService;

    public CreditServiceImpl(ICreditRequestDao creditRequestDao, IAccountService accountService) {
        this.creditRequestDao = creditRequestDao;
        this.accountService = accountService;
    }

    @Override
    public CreditRequest requestCredit(User user, double amount, String purpose) {
        CreditRequest request = CreditRequest.builder()
                .user(user)
                .amount(amount)
                .purpose(purpose)
                .status(CreditStatus.PENDING)
                .build();
        return creditRequestDao.save(request);
    }

    @Override
    public List<CreditRequest> getUserRequests(User user) {
        return creditRequestDao.findByUser(user);
    }

    @Override
    public List<CreditRequest> getPendingRequests() {
        return creditRequestDao.findByStatus(CreditStatus.PENDING);
    }

    @Override
    public void approveRequest(CreditRequest request) {
        request.setStatus(CreditStatus.APPROVED);
        creditRequestDao.update(request);
        // Add the credit amount to the user's account
        accountService.addToBalance(request.getUser(), request.getAmount());
    }

    @Override
    public void rejectRequest(CreditRequest request, String reason) {
        request.setStatus(CreditStatus.REJECTED);
        request.setRejectionReason(reason);
        creditRequestDao.update(request);
    }
} 