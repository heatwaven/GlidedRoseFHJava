package com.gildedrose;

import com.gildedrose.domain.Item;
import com.gildedrose.domain.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

public class TexttestFixture {

    private static final Currency USD = Currency.getInstance("USD");
    private static Item createItem(String name, int sellIn, int quality) {
        return new Item(
            name,
            sellIn,
            quality,
            new Money(BigDecimal.TEN, USD) // Default price of 10 USD for testing
        );
    }


    public static void main(String[] args) {
        System.out.println("OMGHAI!");

        Item[] items = new Item[] {
                createItem("+5 Dexterity Vest", 10, 20), //
                createItem("Aged Brie", 2, 0), //
                createItem("Elixir of the Mongoose", 5, 7), //
                createItem("Sulfuras, Hand of Ragnaros", 0, 80), //
                createItem("Sulfuras, Hand of Ragnaros", -1, 80),
                createItem("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                createItem("Backstage passes to a TAFKAL80ETC concert", 10, 49),
                createItem("Backstage passes to a TAFKAL80ETC concert", 5, 49),
                // this conjured item does not work properly yet
                createItem("Conjured Mana Cake", 3, 6) };

        GildedRose app = new GildedRose(items, new ArrayList<>());

        int days = 2;
        if (args.length > 0) {
            days = Integer.parseInt(args[0]) + 1;
        }

        for (int i = 0; i < days; i++) {
            System.out.println("-------- day " + i + " --------");
            System.out.println("name, sellIn, quality");
            for (Item item : items) {
                System.out.println(item);
            }
            System.out.println();
            app.updateQuality();
        }
    }

}
