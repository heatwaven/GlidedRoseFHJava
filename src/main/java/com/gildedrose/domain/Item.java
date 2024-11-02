package com.gildedrose.domain;

public class Item {

    public String name;

    public int sellIn;

    public int quality;

    public Money basePrice;

    public Item(String name, int sellIn, int quality, Money basePrice) {
        this.name = name;
        this.sellIn = sellIn;
        this.quality = quality;
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "Item{" +
            "name='" + name + '\'' +
            ", sellIn=" + sellIn +
            ", quality=" + quality +
            ", basePrice=" + basePrice +
            '}';
    }

    public String getName() {
        return name;
    }

    public int getSellIn() {
        return sellIn;
    }

    public int getQuality() {
        return quality;
    }

    public Money getBasePrice() {
        return basePrice;
    }
}
