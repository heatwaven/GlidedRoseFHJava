package com.gildedrose.pricing.discount;

import com.gildedrose.domain.CartItem;
import com.gildedrose.domain.Money;

import java.math.BigDecimal;

public class BulkDiscount implements DiscountStrategy {
    private final int minimumQuantity;
    private final BigDecimal discountPercentage;

    public BulkDiscount(int minimumQuantity, BigDecimal discountPercentage) {
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Money applyDiscount(CartItem cartItem) {
        if (cartItem.getQuantity() >= minimumQuantity) {
            Money originalPrice = cartItem.getItem().getBasePrice()
                .multiply(cartItem.getQuantity());
            BigDecimal discountAmount = originalPrice.getAmount()
                .multiply(discountPercentage);
            return new Money(originalPrice.getAmount().subtract(discountAmount),
                originalPrice.getCurrency());
        }
        return cartItem.getItem().getBasePrice().multiply(cartItem.getQuantity());
    }
}
