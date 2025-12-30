package com.darksoldier1404.dpas.shop;

import org.bukkit.configuration.file.YamlConfiguration;

public class ShopPrice {
    private int page;
    private int slot;
    private int buyPrice;

    public ShopPrice(int page, int slot, int buyPrice) {
        this.page = page;
        this.slot = slot;
        this.buyPrice = buyPrice;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public YamlConfiguration serialize(YamlConfiguration data) {
        data.set("ShopPrice." + page + "." + slot + ".buyPrice", buyPrice);
        return data;
    }
}
