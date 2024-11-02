package com.gildedrose.pricing.discount;

import com.gildedrose.domain.CartItem;
import com.gildedrose.domain.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class SeasonalDiscount implements DiscountStrategy {
    private final Month discountMonth;
    private final BigDecimal discountPercentage;

    public SeasonalDiscount(Month discountMonth, BigDecimal discountPercentage) {
        this.discountMonth = discountMonth;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Money applyDiscount(CartItem cartItem) {
        if (LocalDate.now().getMonth() == discountMonth) {
            Money originalPrice = cartItem.getItem().getBasePrice()
                .multiply(cartItem.getQuantity());


            originalPrice.getAmount().subtract(originalPrice.getAmount().multiply(discountPercentage));

            return originalPrice;

        }
        return cartItem.getItem().getBasePrice().multiply(cartItem.getQuantity());
    }
}
