package com.gildedrose.cart;

import com.gildedrose.domain.CartItem;
import com.gildedrose.domain.Item;
import com.gildedrose.domain.Money;
import com.gildedrose.pricing.CurrencyConverter;
import com.gildedrose.pricing.PriceCalculator;
import com.gildedrose.pricing.discount.DiscountStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;

public class ShoppingCart {
    private final List<CartItem> items;
    private final List<DiscountStrategy> discountStrategies;
    private final CurrencyConverter currencyConverter;

    public ShoppingCart(PriceCalculator priceCalculator) {
        this.items = new ArrayList<>();
        this.discountStrategies = new ArrayList<>();
        this.currencyConverter =  priceCalculator.getCurrencyConverter();
    }

    public void addItem(Item item, int quantity) {
        items.add(new CartItem(item, quantity));
    }

    public Money calculateTotal(Currency targetCurrency) {
        Money total = items.stream()
            .map(this::calculateItemTotal)
            .reduce(new Money(BigDecimal.ZERO, targetCurrency), Money::add);

        return currencyConverter.convert(total, targetCurrency);
    }

    private Money calculateItemTotal(CartItem cartItem) {
        return discountStrategies.stream()
            .map(strategy -> strategy.applyDiscount(cartItem))
            .min(Comparator.comparing(Money::getAmount))
            .orElse(cartItem.getItem().getBasePrice().multiply(cartItem.getQuantity()));
    }
}
