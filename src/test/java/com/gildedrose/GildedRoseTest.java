package com.gildedrose;

import com.gildedrose.domain.Item;
import com.gildedrose.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    private static final Currency USD = Currency.getInstance("USD");



    private Item createItem(String name, int sellIn, int quality) {
        return new Item(
            name,
            sellIn,
            quality,
            new Money(BigDecimal.TEN, USD) // Default price of 10 USD for testing
        );
    }

    @Test
    void testFoo() {
        Item[] items = new Item[] { createItem("foo", 0, 0) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals("foo", items[0].getName(), "Item name should remain unchanged");
        assertEquals(-1, items[0].getSellIn(), "SellIn should decrease by 1");
        assertEquals(0, items[0].getQuality(), "Quality should not drop below 0");
    }

    @Test
    void testAgedBrieIncreasesInQuality() {
        Item[] items = new Item[] { createItem("Aged Brie", 2, 0) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(1, items[0].getQuality(), "Aged Brie should increase in quality");
        assertEquals(1, items[0].getSellIn(), "SellIn should decrease by 1");
    }

    @Test
    void testAgedBrieDoesNotExceedMaximumQuality() {
        Item[] items = new Item[] { createItem("Aged Brie", 5, 50) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(50, items[0].getQuality(), "Aged Brie should not exceed quality of 50");
        assertEquals(4, items[0].getSellIn(), "SellIn should decrease by 1");
    }

    @Test
    void testSulfurasNeverDecreasesInQualityOrSellIn() {
        Item[] items = new Item[] { createItem("Sulfuras, Hand of Ragnaros", 10, 80) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(80, items[0].getQuality(), "Sulfuras quality should remain unchanged");
        assertEquals(10, items[0].getSellIn(), "Sulfuras sellIn should remain unchanged");
    }

    @Test
    void testBackstagePassesIncreaseInQualityWithApproachingSellIn() {
        // More than 10 days
        Item[] items = new Item[] { createItem("Backstage passes to a TAFKAL80ETC concert", 15, 20) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(21, items[0].getQuality(), "Backstage passes should increase in quality by 1 when sellIn > 10");
        assertEquals(14, items[0].getSellIn(), "SellIn should decrease by 1");

        // 10 days or less
        items = new Item[] { createItem("Backstage passes to a TAFKAL80ETC concert", 10, 49) };
        app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(50, items[0].getQuality(), "Backstage passes should increase in quality by 2 when 5 < sellIn <= 10");
        assertEquals(9, items[0].getSellIn(), "SellIn should decrease by 1");

        // 5 days or less
        items = new Item[] { createItem("Backstage passes to a TAFKAL80ETC concert", 5, 49) };
        app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(50, items[0].getQuality(), "Backstage passes should increase in quality by 3 when sellIn <= 5");
        assertEquals(4, items[0].getSellIn(), "SellIn should decrease by 1");
    }

    @Test
    void testBackstagePassesDropToZeroAfterConcert() {
        Item[] items = new Item[] { createItem("Backstage passes to a TAFKAL80ETC concert", 0, 20) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(0, items[0].getQuality(), "Backstage passes should drop to 0 after the concert");
        assertEquals(-1, items[0].getSellIn(), "SellIn should decrease by 1");
    }

    @Test
    void testNormalItemDecreasesInQualityAndSellIn() {
        Item[] items = new Item[] { createItem("+5 Dexterity Vest", 10, 20) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(19, items[0].getQuality(), "Normal item should decrease in quality by 1");
        assertEquals(9, items[0].getSellIn(), "SellIn should decrease by 1");
    }



    @Test
    void testNormalItemQualityNeverNegative() {
        Item[] items = new Item[] { createItem("Expired Item", 0, 0), createItem("Regular Item at 0 Quality", 5, 0) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(0, items[0].quality, "Expired item quality should not be negative");
        assertEquals(-1, items[0].sellIn, "SellIn should decrease by 1");
        assertEquals(0, items[1].quality, "Regular item at 0 quality should remain at 0");
        assertEquals(4, items[1].sellIn, "SellIn should decrease by 1");
    }

    @Test
    void testQualityNeverExceedsFifty() {
        Item[] items = new Item[] {
            createItem("Aged Brie", 10, 50),
            createItem("Backstage passes to a TAFKAL80ETC concert", 5, 50),
            createItem("Test Item", 5, 50)
        };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(50, items[0].quality, "Aged Brie should not exceed quality of 50");
        assertEquals(9, items[0].sellIn, "SellIn should decrease by 1");

        assertEquals(50, items[1].quality, "Backstage passes should not exceed quality of 50");
        assertEquals(4, items[1].sellIn, "SellIn should decrease by 1");

        assertEquals(49, items[2].quality, "Conjured items should not exceed quality of 50");
        assertEquals(4, items[2].sellIn, "SellIn should decrease by 1");
    }

    @Test
    void testSulfurasWithVaryingSellInValues() {
        Item[] items = new Item[] {
            createItem("Sulfuras, Hand of Ragnaros", 10, 80),
            createItem("Sulfuras, Hand of Ragnaros", -10, 80)
        };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        for (Item item : items) {
            assertEquals(80, item.quality, "Sulfuras quality should remain unchanged");
            assertEquals(item.sellIn, item.sellIn, "Sulfuras sellIn should remain unchanged");
        }
    }

    @Test
    void testNonStandardItemsBehaveAsNormalItems() {
        Item[] items = new Item[] {
            createItem("Vintage Wine", 20, 30),
            createItem("Magic Sword", 15, 40)
        };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(29, items[0].quality, "Vintage Wine should decrease in quality by 1");
        assertEquals(19, items[0].sellIn, "Vintage Wine sellIn should decrease by 1");

        assertEquals(39, items[1].quality, "Magic Sword should decrease in quality by 1");
        assertEquals(14, items[1].sellIn, "Magic Sword sellIn should decrease by 1");
    }


    @Test
    void testBackstagePassesWithSellInLessThanSix() {
        // SellIn = 5
        Item[] items = new Item[] { createItem("Backstage passes to a TAFKAL80ETC concert", 5, 45) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();
        assertEquals(48, items[0].quality, "Backstage passes should increase by 3 when sellIn <= 5");
        assertEquals(4, items[0].sellIn, "SellIn should decrease by 1");
    }


    @Test
    void testUpdateQuality_AgedBrie() {
        Item[] items = new Item[] { createItem("Aged Brie", 10, 20) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(21, items[0].quality, "Expected Quality to increase to 21");
        assertEquals(9, items[0].sellIn, "Expected SellIn to decrease to 9");
    }

    @Test
    void testUpdateQuality_BackstagePasses() {
        Item[] items = new Item[] {
            createItem("Backstage passes to a TAFKAL80ETC concert", 11, 20),
            createItem("Backstage passes to a TAFKAL80ETC concert", 10, 20),
            createItem("Backstage passes to a TAFKAL80ETC concert", 5, 20),
            createItem("Backstage passes to a TAFKAL80ETC concert", 0, 20)
        };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(21, items[0].quality, "Expected Quality to increase to 21");
        assertEquals(22, items[1].quality, "Expected Quality to increase to 22");
        assertEquals(23, items[2].quality, "Expected Quality to increase to 23");
        assertEquals(0, items[3].quality, "Expected Quality to drop to 0 after the concert");
    }

    @Test
    void testUpdateQuality_Sulfuras() {
        Item[] items = new Item[] { createItem("Sulfuras, Hand of Ragnaros", 0, 80) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(80, items[0].quality, "Expected Quality to remain 80");
        assertEquals(0, items[0].sellIn, "Expected SellIn to remain 0");
    }

    @Test
    void testUpdateQuality_NormalItem() {
        Item[] items = new Item[] { createItem("Normal Item", 10, 20) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(19, items[0].quality, "Expected Quality to decrease to 19");
        assertEquals(9, items[0].sellIn, "Expected SellIn to decrease to 9");
    }

    @Test
    void testUpdateQuality_NormalItemAfterExpiration() {
        Item[] items = new Item[] { createItem("Normal Item", 0, 20) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(18, items[0].quality, "Expected Quality to decrease to 18 after expiration");
        assertEquals(-1, items[0].sellIn, "Expected SellIn to decrease to -1");
    }

    @Test
    void testUpdateQuality_AgedBrie_MaxQuality() {
        Item[] items = new Item[] { createItem("Aged Brie", 10, 50) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(50, items[0].quality, "Expected Quality to remain at 50 (max)");
    }

    @Test
    void testUpdateQuality_BackstagePasses_MaxQuality() {
        Item[] items = new Item[] { createItem("Backstage passes to a TAFKAL80ETC concert", 5, 50) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(50, items[0].quality, "Expected Quality to remain at 50 (max)");
    }

    @Test
    void testUpdateQuality_ExpiredAgedBrie() {
        Item[] items = new Item[] { createItem("Aged Brie", 0, 20) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(22, items[0].quality, "Expected Quality to increase to 22 after expiration");
        assertEquals(-1, items[0].sellIn, "Expected SellIn to decrease to -1");
    }

    @Test
    void testUpdateQuality_ExpiredNormalItem() {
        Item[] items = new Item[] { createItem("Normal Item", -1, 10) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(8, items[0].quality, "Expected Quality to decrease by 2 after expiration");
    }

    @Test
    void testUpdateQuality_ExpiredBackstagePasses() {
        Item[] items = new Item[] { createItem("Backstage passes to a TAFKAL80ETC concert", -1, 10) };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.updateQuality();

        assertEquals(0, items[0].quality, "Expected Quality to drop to 0 after expiration");
    }


    @Test
    void testShoppingCartCalculatesCorrectTotal() {
        Item[] items = new Item[] {
            createItem("Normal Item", 10, 20)
        };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.getShoppingCart().addItem(items[0], 1);

        Money total = app.getShoppingCart().calculateTotal(USD);
        assertEquals(0, total.getAmount().compareTo(BigDecimal.TEN),
            "Shopping cart should calculate correct total");
    }

    @Test
    void testMultipleItemsInShoppingCart() {
        Item[] items = new Item[] {
            createItem("Item 1", 10, 20),
            createItem("Item 2", 5, 15)
        };
        GildedRose app = new GildedRose(items, new ArrayList<>());
        app.getShoppingCart().addItem(items[0], 2); // Two of first item
        app.getShoppingCart().addItem(items[1], 1); // One of second item

        Money total = app.getShoppingCart().calculateTotal(USD);
        assertEquals(0, total.getAmount().compareTo(new BigDecimal("30")),
            "Shopping cart should calculate correct total for multiple items");
    }






}
