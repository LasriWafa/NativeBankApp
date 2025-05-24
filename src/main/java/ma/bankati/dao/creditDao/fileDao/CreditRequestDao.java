package ma.bankati.dao.creditDao.fileDao;

import ma.bankati.dao.creditDao.ICreditRequestDao;
import ma.bankati.model.credit.CreditRequest;
import ma.bankati.model.credit.CreditStatus;
import ma.bankati.model.users.User;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

public class CreditRequestDao implements ICreditRequestDao {
    private final Path dataDir;
    private final Path creditRequestsFile;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public CreditRequestDao() {
        this.dataDir = Paths.get("data");
        this.creditRequestsFile = dataDir.resolve("credit_requests.txt");
        initializeFile();
    }

    private void initializeFile() {
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            if (!Files.exists(creditRequestsFile)) {
                Files.createFile(creditRequestsFile);
                // Write header
                Files.write(creditRequestsFile, "ID-UserID-Amount-Purpose-RequestDate-Status-RejectionReason\n".getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize credit requests file", e);
        }
    }

    private CreditRequest map(String line) {
        try {
            System.out.println("Parsing line: " + line);
            // Split the line into parts, but keep the date together
            String[] parts = new String[7];
            int currentIndex = 0;
            int startIndex = 0;
            
            // Find the first 4 parts (ID, UserID, Amount, Purpose)
            for (int i = 0; i < 4; i++) {
                int nextIndex = line.indexOf('-', startIndex);
                if (nextIndex == -1) {
                    System.out.println("Invalid line format, skipping");
                    return null;
                }
                parts[currentIndex++] = line.substring(startIndex, nextIndex);
                startIndex = nextIndex + 1;
            }
            
            // Get the date (next 10 characters)
            if (startIndex + 10 > line.length()) {
                System.out.println("Invalid date format, skipping");
                return null;
            }
            parts[currentIndex++] = line.substring(startIndex, startIndex + 10);
            startIndex += 10;
            
            // Get the status and rejection reason
            String remaining = line.substring(startIndex);
            String[] statusParts = remaining.split("-", 2);
            parts[currentIndex++] = statusParts[0]; // Status
            parts[currentIndex] = statusParts.length > 1 ? statusParts[1] : ""; // Rejection reason
            
            System.out.println("Split into parts:");
            for (int i = 0; i < parts.length; i++) {
                System.out.println("Part " + i + ": " + parts[i]);
            }

            Long id = Long.parseLong(parts[0]);
            Long userId = Long.parseLong(parts[1]);
            double amount = Double.parseDouble(parts[2]);
            String purpose = parts[3];
            String dateStr = parts[4];
            System.out.println("Parsing date: " + dateStr);
            LocalDateTime requestDate = LocalDate.parse(dateStr, DATE_FORMATTER).atStartOfDay();
            
            // Get status from parts[6] and clean it
            String statusStr = parts[6].split("-")[0].trim();
            System.out.println("Parsing status: " + statusStr);
            CreditStatus status = CreditStatus.valueOf(statusStr);
            String rejectionReason = parts[6].contains("-") ? parts[6].substring(parts[6].indexOf("-") + 1) : "";

            User user = new User();
            user.setId(userId);

            CreditRequest request = CreditRequest.builder()
                    .id(id)
                    .user(user)
                    .amount(amount)
                    .purpose(purpose)
                    .requestDate(requestDate)
                    .status(status)
                    .rejectionReason(rejectionReason)
                    .build();
            
            System.out.println("Successfully parsed request: " + request.getId());
            return request;
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            System.err.println("Error details: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String mapToFileLine(CreditRequest request) {
        return String.format("%d-%d-%.2f-%s-%s-%s-%s",
                request.getId(),
                request.getUser().getId(),
                request.getAmount(),
                request.getPurpose(),
                request.getRequestDate().format(DATE_FORMATTER),
                request.getStatus(),
                request.getRejectionReason() != null ? request.getRejectionReason() : "");
    }

    @Override
    public List<CreditRequest> findAll() {
        try {
            System.out.println("Reading credit requests from: " + creditRequestsFile.toAbsolutePath());
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(creditRequestsFile.toFile()), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            System.out.println("Found " + lines.size() + " lines in file");
            if (!lines.isEmpty()) {
                System.out.println("First line: " + lines.get(0));
                if (lines.size() > 1) {
                    System.out.println("Second line: " + lines.get(1));
                }
            }
            
            return lines.stream()
                    .skip(1) // Skip header
                    .map(this::map)
                    .filter(request -> request != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading credit requests file: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to read credit requests", e);
        }
    }

    @Override
    public CreditRequest findById(Long id) {
        try {
            return Files.lines(creditRequestsFile)
                    .skip(1) // Skip header
                    .map(this::map)
                    .filter(request -> request != null && request.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read credit requests", e);
        }
    }

    @Override
    public List<CreditRequest> findByUser(User user) {
        try {
            System.out.println("Finding credit requests for user: " + user.getId());
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(creditRequestsFile.toFile()), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            System.out.println("Total lines in file: " + lines.size());
            
            List<CreditRequest> requests = lines.stream()
                    .skip(1) // Skip header
                    .map(this::map)
                    .filter(request -> request != null && request.getUser().getId().equals(user.getId()))
                    .collect(Collectors.toList());
            
            System.out.println("Found " + requests.size() + " requests for user " + user.getId());
            return requests;
        } catch (IOException e) {
            System.err.println("Error finding credit requests for user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to read credit requests", e);
        }
    }

    @Override
    public List<CreditRequest> findByStatus(CreditStatus status) {
        try {
            return Files.lines(creditRequestsFile)
                    .skip(1) // Skip header
                    .map(this::map)
                    .filter(request -> request != null && request.getStatus() == status)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read credit requests", e);
        }
    }

    @Override
    public CreditRequest save(CreditRequest request) {
        try {
            if (request.getId() == null) {
                request.setId(getNextId());
            }
            if (request.getRequestDate() == null) {
                request.setRequestDate(LocalDateTime.now());
            }
            if (request.getStatus() == null) {
                request.setStatus(CreditStatus.PENDING);
            }
            if (request.getRejectionReason() == null) {
                request.setRejectionReason("");
            }

            Files.write(creditRequestsFile, (mapToFileLine(request) + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            return request;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save credit request", e);
        }
    }

    @Override
    public void update(CreditRequest request) {
        List<CreditRequest> requests = findAll();
        requests.removeIf(r -> r.getId().equals(request.getId()));
        requests.add(request);
        rewriteFile(requests);
    }

    @Override
    public void delete(CreditRequest request) {
        List<CreditRequest> requests = findAll();
        requests.removeIf(r -> r.getId().equals(request.getId()));
        rewriteFile(requests);
    }

    @Override
    public void deleteById(Long id) {
        List<CreditRequest> requests = findAll();
        requests.removeIf(r -> r.getId().equals(id));
        rewriteFile(requests);
    }

    private void rewriteFile(List<CreditRequest> requests) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("ID-UserID-Amount-Purpose-RequestDate-Status-RejectionReason");
            requests.stream()
                    .map(this::mapToFileLine)
                    .forEach(lines::add);
            Files.write(creditRequestsFile, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to rewrite credit requests file", e);
        }
    }

    private Long getNextId() {
        try {
            return Files.lines(creditRequestsFile)
                    .skip(1) // Skip header
                    .map(this::map)
                    .filter(request -> request != null)
                    .mapToLong(CreditRequest::getId)
                    .max()
                    .orElse(0L) + 1;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate next ID", e);
        }
    }
} 