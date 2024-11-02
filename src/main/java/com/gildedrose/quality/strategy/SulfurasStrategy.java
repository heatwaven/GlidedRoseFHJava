package com.gildedrose.quality.strategy;

import com.gildedrose.domain.Item;

public class SulfurasStrategy implements ItemStrategy {
    @Override
    public void update(Item item) {
        // "Sulfuras" does not change in quality or sellIn
    }
}
