package com.gildedrose.quality.strategy;

import com.gildedrose.domain.Item;

public class BackstagePassesStrategy implements ItemStrategy {
    @Override
    public void update(Item item) {
        item.sellIn--;

        if (item.sellIn < 0) {
            item.quality = 0;
            return;
        }

        increaseQuality(item);
        if (item.sellIn < 10) {
            increaseQuality(item);
        }
        if (item.sellIn < 5) {
            increaseQuality(item);
        }
    }
}
