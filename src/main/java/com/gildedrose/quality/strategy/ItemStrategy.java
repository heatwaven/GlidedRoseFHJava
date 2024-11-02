package com.gildedrose.quality.strategy;

import com.gildedrose.domain.Item;

public interface ItemStrategy {
    void update(Item item);

    default void increaseQuality(Item item) {
        if (item.quality < 50) {
            item.quality++;
        }
    }

    default void decreaseQuality(Item item) {
        if (item.quality > 0) {
            item.quality--;
        }
    }
}
