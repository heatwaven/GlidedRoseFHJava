package com.gildedrose.pricing;

import com.gildedrose.domain.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    private final Map<CurrencyPair, BigDecimal> exchangeRates = new HashMap<>();

    public CurrencyConverter() {
        // Initialize with some default exchange rates
        setExchangeRate(Currency.getInstance("USD"), Currency.getInstance("EUR"), BigDecimal.valueOf(0.85));
        setExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("USD"), BigDecimal.valueOf(1.18));
    }

    public void setExchangeRate(Currency from, Currency to, BigDecimal rate) {
        exchangeRates.put(new CurrencyPair(from, to), rate);
    }

    public Money convert(Money money, Currency targetCurrency) {
        if (money.getCurrency().equals(targetCurrency)) {
            return money;
        }

        BigDecimal rate = exchangeRates.get(new CurrencyPair(money.getCurrency(), targetCurrency));
        if (rate == null) {
            throw new IllegalArgumentException("No exchange rate found for " +
                money.getCurrency().getCurrencyCode() + " to " + targetCurrency.getCurrencyCode());
        }

        return new Money(
            money.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP),
            targetCurrency
        );
    }

    private record CurrencyPair(Currency from, Currency to) {}
}
