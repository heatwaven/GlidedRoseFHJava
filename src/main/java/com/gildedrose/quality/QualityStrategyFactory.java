package com.gildedrose.quality;

import com.gildedrose.quality.strategy.*;

import java.util.Arrays;

enum ItemNameToStrategyMapping {
    AGED_BRIE("Aged Brie", new AgedBrieStrategy()),
    BACKSTAGE_PASSES("Backstage passes to a TAFKAL80ETC concert", new BackstagePassesStrategy()),
    SULFURAS("Sulfuras, Hand of Ragnaros", new SulfurasStrategy()),
    NORMAL("", new NormalItemStrategy());

    private final String name;
    private final ItemStrategy strategy;

    ItemNameToStrategyMapping(String name, ItemStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public static ItemNameToStrategyMapping fromString(String name) {
        return Arrays.stream(ItemNameToStrategyMapping.values())
            .filter(e -> e.name.equals(name))
            .findFirst()
            .orElse(NORMAL);
    }

    public ItemStrategy getStrategy() {
        return strategy;
    }
}

public class QualityStrategyFactory {


    public ItemStrategy createStrategy(String itemName) {
        return  ItemNameToStrategyMapping.fromString(itemName).getStrategy();
    }


}
