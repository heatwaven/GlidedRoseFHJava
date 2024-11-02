package com.gildedrose;

import com.gildedrose.cart.ShoppingCart;
import com.gildedrose.domain.Item;
import com.gildedrose.pricing.CurrencyConverter;
import com.gildedrose.pricing.PriceCalculator;
import com.gildedrose.pricing.discount.DiscountStrategy;
import com.gildedrose.quality.QualityStrategyFactory;
import com.gildedrose.quality.QualityUpdater;
import com.gildedrose.quality.strategy.ItemNameToStrategyMapping;
import com.gildedrose.quality.strategy.ItemStrategy;

import java.util.Arrays;
import java.util.List;

class GildedRose {
    private final QualityUpdater qualityUpdater;
    private final ShoppingCart shoppingCart;

    public GildedRose(Item[] items, List<DiscountStrategy> discountStrategies) {
        QualityStrategyFactory strategyFactory = new QualityStrategyFactory();
        this.qualityUpdater = new QualityUpdater(Arrays.asList(items), strategyFactory);

        CurrencyConverter currencyConverter = new CurrencyConverter();
        PriceCalculator priceCalculator = new PriceCalculator(discountStrategies, currencyConverter);
        this.shoppingCart = new ShoppingCart(priceCalculator);
    }

    public void updateQuality() {
        qualityUpdater.updateQuality();
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
