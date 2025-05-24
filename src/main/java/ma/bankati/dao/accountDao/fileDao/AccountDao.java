package ma.bankati.dao.accountDao.fileDao;

import ma.bankati.dao.accountDao.IAccountDao;
import ma.bankati.model.account.Account;
import ma.bankati.model.account.AccountStatus;
import ma.bankati.model.users.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccountDao implements IAccountDao {
    private final Path path;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public AccountDao() {
        try {
            // Create data directory if it doesn't exist
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectory(dataDir);
            }

            // Set up the writable accounts.txt file
            this.path = dataDir.resolve("accounts.txt");

            // If the file doesn't exist, create it with header
            if (!Files.exists(path)) {
                List<String> lines = new ArrayList<>();
                lines.add("ID-UserID-Balance-CreationDate-Status");
                Files.write(path, lines);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize account DAO", e);
        }
    }

    private Account map(String fileLine) {
        String[] fields = fileLine.split("-");
        return Account.builder()
                .id(Long.parseLong(fields[0]))
                .user(new User(Long.parseLong(fields[1]))) // Just set the ID, we'll load the full user if needed
                .balance(Double.parseDouble(fields[2]))
                .creationDate(LocalDate.parse(fields[3], DATE_FORMATTER))
                .status(AccountStatus.valueOf(fields[4]))
                .build();
    }

    private String mapToFileLine(Account account) {
        return String.format("%d-%d-%.2f-%s-%s",
                account.getId(),
                account.getUser().getId(),
                account.getBalance(),
                account.getCreationDate().format(DATE_FORMATTER),
                account.getStatus()
        );
    }

    @Override
    public List<Account> findAll() {
        try {
            return Files.readAllLines(path)
                    .stream()
                    .skip(1) // Skip header
                    .map(this::map)
                    .toList();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Account findById(Long id) {
        return findAll().stream()
                .filter(account -> account.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Account findByUser(User user) {
        return findAll().stream()
                .filter(account -> account.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Account save(Account account) {
        try {
            account.setId(newMaxId());
            account.setCreationDate(LocalDate.now());
            account.setStatus(AccountStatus.ACTIVE);
            Files.writeString(path, mapToFileLine(account) + System.lineSeparator(), 
                    java.nio.file.StandardOpenOption.APPEND);
            return account;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save account", e);
        }
    }

    @Override
    public void update(Account account) {
        List<Account> updatedList = findAll().stream()
                .map(a -> a.getId().equals(account.getId()) ? account : a)
                .toList();
        rewriteFile(updatedList);
    }

    @Override
    public void delete(Account account) {
        deleteById(account.getId());
    }

    @Override
    public void deleteById(Long id) {
        List<Account> updatedList = findAll().stream()
                .filter(a -> !a.getId().equals(id))
                .toList();
        rewriteFile(updatedList);
    }

    private void rewriteFile(List<Account> accounts) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("ID-UserID-Balance-CreationDate-Status"); // header
            for (Account account : accounts) {
                lines.add(mapToFileLine(account));
            }
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update accounts file", e);
        }
    }

    private long newMaxId() {
        return findAll().stream()
                .mapToLong(Account::getId)
                .max()
                .orElse(0) + 1;
    }
} 