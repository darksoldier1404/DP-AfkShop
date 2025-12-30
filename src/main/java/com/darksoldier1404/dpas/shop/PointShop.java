package com.darksoldier1404.dpas.shop;

import com.darksoldier1404.dpas.AFKShop;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.data.DataCargo;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PointShop implements DataCargo {
    private String name;
    private DInventory inventory;
    private final List<ShopPrice> priceList = new ArrayList<>();

    public PointShop() {
    }

    public PointShop(String name, DInventory inventory) {
        this.name = name;
        this.inventory = inventory;
        inventory.setObj(this);
    }

    public String getName() {
        return name;
    }

    public DInventory getInventory() {
        return inventory;
    }

    public void setInventory(DInventory inventory) {
        this.inventory = inventory;
    }

    public List<ShopPrice> getPriceList() {
        return priceList;
    }

    public int findPrice(int page, int slot) {
        for (ShopPrice shopPrice : priceList) {
            if (shopPrice.getPage() == page && shopPrice.getSlot() == slot) {
                return shopPrice.getBuyPrice();
            }
        }
        return 0;
    }

    @Nullable
    public ShopPrice findShopPrice(int page, int slot) {
        for (ShopPrice shopPrice : priceList) {
            if (shopPrice.getPage() == page && shopPrice.getSlot() == slot) {
                return shopPrice;
            }
        }
        return null;
    }

    public void openItemEditor(Player p) {
        DInventory inv = this.inventory.clone();
        inv.setChannel(1);
        inv.turnPage(0);
        inv.setObj(this);
        inv.openInventory(p);
    }

    public void openShop(Player p) {
        DInventory inv = this.inventory.clone();
        updatePriceLore(inv);
        inv.applyChanges();
        inv.setChannel(0);
        inv.turnPage(0);
        inv.setObj(this);
        inv.openInventory(p);
    }

    public void openPriceEditor(Player p) {
        DInventory inv = this.inventory.clone();
        inv.setChannel(2);
        inv.setObj(this);
        updatePriceLore(inv);
        inv.applyChanges();
        inv.openInventory(p);
    }

    public void updatePriceLore(DInventory inv) {
        inv.applyAllItemChanges((pis -> {
            int page = pis.getPage();
            int slot = pis.getSlot();
            int price = findPrice(page, slot);
            ItemStack item = pis.getItem();
            ItemMeta im = item.getItemMeta();
            List<String> lore = im.getLore() == null ? new ArrayList<>() : im.getLore();
            if (price > 0) {
                lore.add("§e가격: §f" + price + " 포인트");
                im.setLore(lore);
                item.setItemMeta(im);
            }
            pis.setItem(item);
            return pis;
        }));
        inv.update();
    }

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("name", name);
        for (ShopPrice shopPrice : priceList) {
            shopPrice.serialize(data);
        }
        inventory.serialize(data);
        return data;
    }

    @Override
    public PointShop deserialize(YamlConfiguration data) {
        this.name = data.getString("name");
        this.inventory = new DInventory("AFK Shop : " + name, 54, true, true, AFKShop.getInstance());
        inventory.deserialize(data);
        if (data.contains("ShopPrice")) {
            for (String pageKey : data.getConfigurationSection("ShopPrice").getKeys(false)) {
                int page = Integer.parseInt(pageKey);
                for (String slotKey : data.getConfigurationSection("ShopPrice." + pageKey).getKeys(false)) {
                    int slot = Integer.parseInt(slotKey);
                    int buyPrice = data.getInt("ShopPrice." + pageKey + "." + slotKey + ".buyPrice");
                    ShopPrice shopPrice = new ShopPrice(page, slot, buyPrice);
                    priceList.add(shopPrice);
                }
            }
        }
        return this;
    }
}
