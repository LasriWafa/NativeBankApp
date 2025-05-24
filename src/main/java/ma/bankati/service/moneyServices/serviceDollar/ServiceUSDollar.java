package ma.bankati.service.moneyServices.serviceDollar;

import lombok.Getter;
import lombok.Setter;
import ma.bankati.dao.currencyDao.ICurrencyDao;
import ma.bankati.model.data.Devise;
import ma.bankati.model.data.MoneyData;
import ma.bankati.service.moneyServices.IMoneyService;

@Getter @Setter
public class ServiceUSDollar implements IMoneyService {
    private ICurrencyDao dao;

    public ServiceUSDollar() {}

    public ServiceUSDollar(ICurrencyDao dao) {
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
            case "USD_EUR":
                return 0.92;  // 1 USD = 0.92 EUR
            case "USD_GBP":
                return 0.78;  // 1 USD = 0.78 GBP
            case "USD_MAD":
                return 10.0;  // 1 USD = 10.0 MAD
            case "EUR_USD":
                return 1.09;  // 1 EUR = 1.09 USD
            case "GBP_USD":
                return 1.28;  // 1 GBP = 1.28 USD
            case "MAD_USD":
                return 0.10;  // 1 MAD = 0.10 USD
            default:
                // For other currency pairs, convert through USD
                double toUSD = getExchangeRate(fromCurrency, "USD");
                double fromUSD = getExchangeRate("USD", toCurrency);
                return toUSD * fromUSD;
        }
    }
}
