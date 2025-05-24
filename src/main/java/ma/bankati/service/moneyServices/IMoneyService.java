package ma.bankati.service.moneyServices;

import ma.bankati.model.data.MoneyData;

public interface IMoneyService {
    MoneyData convertAmount(double amount, String fromCurrency, String toCurrency);
    double getExchangeRate(String fromCurrency, String toCurrency);
}
