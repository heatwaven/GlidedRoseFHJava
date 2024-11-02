package com.gildedrose.quality;

import com.gildedrose.domain.Item;
import com.gildedrose.quality.strategy.ItemStrategy;

import java.util.List;

public class QualityUpdater {

        private final List<Item> items;
        private final QualityStrategyFactory strategyFactory;

        public QualityUpdater(List<Item> items, QualityStrategyFactory strategyFactory) {
            this.items = items;
            this.strategyFactory = strategyFactory;
        }

        public void updateQuality() {
            items.forEach(item -> {
                ItemStrategy strategy = strategyFactory.createStrategy(item.getName());
                strategy.update(item);
            });
        }

}
