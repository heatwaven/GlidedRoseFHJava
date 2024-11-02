package com.gildedrose.pricing;

import com.gildedrose.domain.CartItem;
import com.gildedrose.domain.Money;
import com.gildedrose.pricing.discount.DiscountStrategy;

import java.util.Comparator;
import java.util.Currency;
import java.util.List;

public class PriceCalculator {
    private final List<DiscountStrategy> discountStrategies;
    private final CurrencyConverter currencyConverter;

    public PriceCalculator(List<DiscountStrategy> discountStrategies, CurrencyConverter currencyConverter) {
        this.discountStrategies = discountStrategies;
        this.currencyConverter = currencyConverter;
    }

    public Money calculatePrice(CartItem cartItem, Currency targetCurrency) {
        Money bestPrice = discountStrategies.stream()
            .map(strategy -> strategy.applyDiscount(cartItem))
            .min(Comparator.comparing(Money::getAmount))
            .orElse(cartItem.getItem().getBasePrice().multiply(cartItem.getQuantity()));

        return currencyConverter.convert(bestPrice, targetCurrency);
    }

    public CurrencyConverter getCurrencyConverter() {
        return currencyConverter;
    }
}
