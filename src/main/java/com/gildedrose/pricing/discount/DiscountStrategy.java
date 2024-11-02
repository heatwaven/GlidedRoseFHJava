package com.gildedrose.pricing.discount;

import com.gildedrose.domain.CartItem;
import com.gildedrose.domain.Money;

public interface DiscountStrategy {
    Money applyDiscount(CartItem item);
}
