package com.gildedrose.quality.strategy;


import com.gildedrose.domain.Item;

public class NormalItemStrategy implements ItemStrategy {
    @Override
    public void update(Item item) {
        decreaseQuality(item);
        item.sellIn--;

        if (item.sellIn < 0) {
            decreaseQuality(item);
        }
    }


}
