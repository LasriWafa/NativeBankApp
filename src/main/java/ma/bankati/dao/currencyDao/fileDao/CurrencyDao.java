package ma.bankati.dao.currencyDao.fileDao;

import ma.bankati.dao.currencyDao.ICurrencyDao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao implements ICurrencyDao {
    private final Path path;

    public CurrencyDao() {
        try {
            // Create data directory if it doesn't exist
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectory(dataDir);
            }

            // Set up the currency.txt file
            this.path = dataDir.resolve("currency.txt");

            // If the file doesn't exist, create it with default values
            if (!Files.exists(path)) {
                List<String> lines = new ArrayList<>();
                lines.add("Currency Exchange Rate");
                lines.add("1.0"); // Default rate
                Files.write(path, lines);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize CurrencyDao", e);
        }
    }

    @Override
    public double getExchangeRate() {
        try {
            return Files.readAllLines(path)
                    .stream()
                    .skip(1)
                    .findFirst()
                    .map(Double::parseDouble)
                    .orElse(1.0);
        } catch (Exception e) {
            return 1.0;
        }
    }
}
