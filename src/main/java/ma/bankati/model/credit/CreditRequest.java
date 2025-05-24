package ma.bankati.model.credit;

import lombok.Builder;
import lombok.Data;
import ma.bankati.model.users.User;

import java.time.LocalDateTime;

@Data
@Builder
public class CreditRequest {
    private Long id;
    private User user;
    private double amount;
    private String purpose;
    private LocalDateTime requestDate;
    private CreditStatus status;
    private String rejectionReason;

    @Override
    public String toString() {
        return String.format("CreditRequest[id=%d, amount=%.2f, purpose='%s', status=%s]",
            id, amount, purpose, status);
    }
} 