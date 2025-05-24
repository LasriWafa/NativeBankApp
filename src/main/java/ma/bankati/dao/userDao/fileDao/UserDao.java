package ma.bankati.dao.userDao.fileDao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ma.bankati.dao.userDao.IUserDao;
import ma.bankati.model.users.ERole;
import ma.bankati.model.users.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements IUserDao {
    private final Path path;

    public UserDao() {
        try {
            // Create data directory if it doesn't exist
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectory(dataDir);
            }

            // Set up the writable users.txt file
            this.path = dataDir.resolve("users.txt");

            // If the file doesn't exist, create it with header and default admin user
            if (!Files.exists(path)) {
                List<String> lines = new ArrayList<>();
                lines.add("ID-FirstName-LastName-UserName-Password-Role-Created_At");
                lines.add("1-Admin-User-admin-1234-ADMIN-2024/03/21");
                Files.write(path, lines);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize UserDao", e);
        }
    }

    private User map(String fileLine) {
        String[] fields = fileLine.split("-");
        Long id = Long.parseLong(fields[0]);
        String firstName = fields[1].equals("null") ? null : fields[1];
        String lastName = fields[2].equals("null") ? null : fields[2];
        String username = fields[3].equals("null") ? null : fields[3];
        String password = fields[4].equals("null") ? null : fields[4];
        ERole role = fields[5].equals("null") ? null : (fields[5].equals("ADMIN") ? ERole.ADMIN : ERole.USER);
        LocalDate dateCreation = fields[6].equals("null") ? null :
                LocalDate.parse(fields[6], DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        return new User(id, firstName, lastName, username, password, role, dateCreation);
    }

    private String mapToFileLine(User user) {
        Long id = user.getId();
        String firstName = user.getFirstName() == null || user.getFirstName().trim().length() == 0 ? "null" : user.getFirstName();
        String lastName = user.getLastName() == null || user.getLastName().trim().length() == 0 ? "null" : user.getLastName();
        String username = user.getUsername() == null || user.getUsername().trim().length() == 0 ? "null" : user.getUsername();
        String password = user.getPassword() == null || user.getPassword().trim().length() == 0 ? "null" : user.getPassword();
        ERole role = user.getRole() == null ? null : user.getRole();
        String creationDate = user.getCreationDate() == null ? "null" : user.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).toString();

        return id + "-" + firstName + "-" + lastName + "-" + username + "-" + password + "-" + role + "-" + creationDate + System.lineSeparator();
    }

    private long newMaxId() {
        return findAll().stream().mapToLong(User::getId).max().orElse(0) + 1;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return findAll()
                .stream()
                .filter(user -> login.equals(user.getUsername()) && password.equals(user.getPassword()))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        try {
            return Files.readAllLines(path)
                    .stream()
                    .skip(1)
                    .map(line -> map(line))
                    .toList();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public User save(User newElement) {
        try {
            newElement.setId(newMaxId());
            newElement.setCreationDate(LocalDate.now());
            Files.writeString(path, mapToFileLine(newElement), StandardOpenOption.APPEND);
            return newElement;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public User findById(Long identity) {
        return findAll().stream()
                .filter(u -> u.getId().equals(identity))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteById(Long identity) {
        List<User> updatedList = findAll().stream()
                .filter(u -> !u.getId().equals(identity))
                .toList();

        rewriteFile(updatedList);
    }

    @Override
    public void delete(User element) {
        deleteById(element.getId());
    }

    @Override
    public void update(User newValuesElement) {
        List<User> updatedList = findAll().stream()
                .map(user -> user.getId().equals(newValuesElement.getId()) ? newValuesElement : user)
                .toList();

        rewriteFile(updatedList);
    }

    private void rewriteFile(List<User> users) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("ID-FirstName-LastName-UserName-Password-Role-Created_At"); // header
            for (User user : users) {
                lines.add(mapToFileLine(user).trim());
            }
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 