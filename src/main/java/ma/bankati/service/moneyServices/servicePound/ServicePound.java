package ma.bankati.service.moneyServices.servicePound;

import lombok.Getter;
import lombok.Setter;
import ma.bankati.dao.currencyDao.ICurrencyDao;
import ma.bankati.model.data.Devise;
import ma.bankati.model.data.MoneyData;
import ma.bankati.service.moneyServices.IMoneyService;

@Getter @Setter
public class ServicePound implements IMoneyService {
    private ICurrencyDao dao;

    public ServicePound() {}

    public ServicePound(ICurrencyDao dao) {
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
            case "GBP_EUR":
                return 1.18;  // 1 GBP = 1.18 EUR
            case "GBP_USD":
                return 1.28;  // 1 GBP = 1.28 USD
            case "GBP_MAD":
                return 12.82; // 1 GBP = 12.82 MAD
            case "EUR_GBP":
                return 0.85;  // 1 EUR = 0.85 GBP
            case "USD_GBP":
                return 0.78;  // 1 USD = 0.78 GBP
            case "MAD_GBP":
                return 0.078; // 1 MAD = 0.078 GBP
            default:
                // For other currency pairs, convert through GBP
                double toGBP = getExchangeRate(fromCurrency, "GBP");
                double fromGBP = getExchangeRate("GBP", toCurrency);
                return toGBP * fromGBP;
        }
    }
}
