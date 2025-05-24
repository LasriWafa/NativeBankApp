package ma.bankati.service.moneyServices.serviceDirham;

import lombok.Getter;
import lombok.Setter;
import ma.bankati.dao.currencyDao.ICurrencyDao;
import ma.bankati.model.data.Devise;
import ma.bankati.model.data.MoneyData;
import ma.bankati.service.moneyServices.IMoneyService;

@Getter @Setter
public class ServiceDh implements IMoneyService {
    private ICurrencyDao dao;

    public ServiceDh() { }

    public ServiceDh(ICurrencyDao dao) {
        this.dao = dao;
    }

    @Override
    public MoneyData convertAmount(double amount, String fromCurrency, String toCurrency) {
        double rate = getExchangeRate(fromCurrency, toCurrency);
        double convertedAmount = amount * rate;
        return new MoneyData(convertedAmount, Devise.valueOf(toCurrency));
    }

    @Override
    public double getExchangeRate(String fromCurrency, String toCurrency) {
        // Fixed exchange rates for demonstration
        if (fromCurrency.equals(toCurrency)) {
            return 1.0;
        }
        
        switch (fromCurrency + "_" + toCurrency) {
            case "MAD_EUR":
                return 0.092; // 1 MAD = 0.092 EUR
            case "MAD_USD":
                return 0.10;  // 1 MAD = 0.10 USD
            case "MAD_GBP":
                return 0.078; // 1 MAD = 0.078 GBP
            case "EUR_MAD":
                return 10.87; // 1 EUR = 10.87 MAD
            case "USD_MAD":
                return 10.0;  // 1 USD = 10.0 MAD
            case "GBP_MAD":
                return 12.82; // 1 GBP = 12.82 MAD
            default:
                // For other currency pairs, convert through MAD
                double toMAD = getExchangeRate(fromCurrency, "MAD");
                double fromMAD = getExchangeRate("MAD", toCurrency);
                return toMAD * fromMAD;
        }
    }
}
