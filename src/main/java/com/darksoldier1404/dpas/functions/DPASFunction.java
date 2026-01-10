package com.darksoldier1404.dpas.functions;


import com.darksoldier1404.dpas.AFKShop;
import com.darksoldier1404.dpas.area.AFKArea;
import com.darksoldier1404.dpas.shop.PointShop;
import com.darksoldier1404.dpas.user.AFKUser;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.api.placeholder.PlaceholderBuilder;
import com.darksoldier1404.dppc.utils.InventoryUtils;
import com.darksoldier1404.dppc.utils.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class DPASFunction {
    private static final AFKShop plugin = AFKShop.getInstance();
    private static BukkitTask task;

    public static void initPlaceholder() {
        new PlaceholderBuilder.Builder(plugin)
                .identifier("dpas")
                .version("1.0.0")
                .onRequest((p, str) -> {
                    if (str.equals("point")) {
                        AFKUser user = AFKShop.udata.get(p.getUniqueId());
                        if (user != null) {
                            return String.valueOf(user.getPoint());
                        } else {
                            return "0";
                        }
                    }
                    return null;
                }).build();
    }

    public static boolean isExistingShop(String name) {
        return AFKShop.data.containsKey(name);
    }

    public static void createShop(Player p, String name) {
        if (isExistingShop(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("shop_name_already_exists"));
        } else {
            PointShop shop = new PointShop(name, new DInventory(plugin.getLang().getWithArgs("shop_title", name), 54, true, true, plugin));
            AFKShop.data.put(name, shop);
            AFKShop.data.save(name);
            p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("shop_created_successfully", name));
        }
    }

    public static void editShopItems(Player p, String name) {
        if (!isExistingShop(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("shop_name_not_exists"));
        } else {
            PointShop shop = AFKShop.data.get(name);
            shop.openItemEditor(p);
        }
    }

    public static void editShopPrice(Player p, String name) {
        if (!isExistingShop(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("shop_name_not_exists"));
        } else {
            PointShop shop = AFKShop.data.get(name);
            shop.openPriceEditor(p);
        }
    }

    public static void setAfkPointPerInterval(Player p, String time, int pointAmount) {
        int seconds;
        try {
            if (time.endsWith("s")) {
                seconds = Integer.parseInt(time.substring(0, time.length() - 1));
            } else if (time.endsWith("m")) {
                seconds = Integer.parseInt(time.substring(0, time.length() - 1)) * 60;
            } else if (time.endsWith("h")) {
                seconds = Integer.parseInt(time.substring(0, time.length() - 1)) * 3600;
            } else {
                seconds = Integer.parseInt(time);
            }
        } catch (NumberFormatException e) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("invalid_time_format"));
            return;
        }
        plugin.getConfig().set("Settings.AfkPointPerInterval.TimeInSeconds", seconds);
        plugin.getConfig().set("Settings.AfkPointPerInterval.PointAmount", pointAmount);
        plugin.saveConfig();
        p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("afk_point_setting_saved", String.valueOf(seconds), String.valueOf(pointAmount)));
        refreshAllTasks();
    }

    public static int getAfkPointIntervalTime() {
        return plugin.getConfig().getInt("Settings.AfkPointPerInterval.TimeInSeconds", 1);
    }

    public static int getAfkPointAmount() {
        return plugin.getConfig().getInt("Settings.AfkPointPerInterval.PointAmount", 1);
    }

    public static void setMaxPage(Player p, String name, int page) {
        if (!isExistingShop(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("shop_name_not_exists"));
        } else {
            PointShop shop = AFKShop.data.get(name);
            shop.getInventory().setPages(page);
            AFKShop.data.put(name, shop);
            AFKShop.data.save(name);
            p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("shop_max_page_set", name, String.valueOf(page)));
        }
    }

    public static void refreshAllTasks() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (AFKShop.currentAFKTasks.containsKey(p.getUniqueId())) {
                AFKShop.currentAFKTasks.get(p.getUniqueId()).cancel();
                AFKShop.currentAFKTasks.remove(p.getUniqueId());
            }
        }
        initTask();
    }

    public static void initTask() {
        if (task != null) {
            task.cancel();
        }
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> Bukkit.getOnlinePlayers().forEach(p -> {
            for (AFKArea area : AFKShop.areas.values()) {
                if (area.isInArea(p)) {
                    if (AFKShop.currentAFKTasks.containsKey(p.getUniqueId())) continue;
                    BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                        AFKUser user = AFKShop.udata.get(p.getUniqueId());
                        int pointAmount = getAfkPointAmount();
                        user.addAfkPoints(pointAmount);
                        AFKShop.udata.put(p.getUniqueId(), user);
                        p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("afk_point_gained", String.valueOf(pointAmount), String.valueOf(user.getPoint())));
                    }, 0L, getAfkPointIntervalTime() * 20L);
                    AFKShop.currentAFKTasks.put(p.getUniqueId(), task);
                } else {
                    if (AFKShop.currentAFKTasks.containsKey(p.getUniqueId())) {
                        AFKShop.currentAFKTasks.get(p.getUniqueId()).cancel();
                        AFKShop.currentAFKTasks.remove(p.getUniqueId());
                    }
                }
            }
        }), 0L, 20L);
    }

    public static void buyPointShopItem(Player p, PointShop shop, DInventory.PageItemSet pageItemSet) {
        AFKUser user = AFKShop.udata.get(p.getUniqueId());
        int price = shop.findPrice(pageItemSet.getPage(), pageItemSet.getSlot());
        if (price <= 0) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("item_not_purchasable"));
            return;
        }
        if (user.getPoint() < price) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("insufficient_afk_points", String.valueOf(user.getPoint()), String.valueOf(price)));
            return;
        }
        if (!InventoryUtils.hasEnoughSpace(p.getInventory().getStorageContents(), pageItemSet.getItem())) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("inventory_full"));
            return;
        }
        user.subtractAfkPoints(price);
        AFKShop.udata.put(p.getUniqueId(), user);
        p.getInventory().addItem(shop.getInventory().getPageItems().get(pageItemSet.getPage())[pageItemSet.getSlot()]);
        p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("item_purchased", String.valueOf(price), String.valueOf(user.getPoint())));
    }

    public static void openShop(Player p, String name) {
        if (!isExistingShop(name)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("shop_name_not_exists"));
        } else {
            PointShop shop = AFKShop.data.get(name);
            shop.openShop(p);
        }
    }


    /// area functions

    public static boolean isExistingArea(String name) {
        return AFKShop.areas.containsKey(name);
    }

    public static void addArea(Player p, String areaName) {
        if (isExistingArea(areaName)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("area_name_already_exists"));
        } else {
            AFKArea area = new AFKArea();
            area.setName(areaName);
            AFKShop.areas.put(areaName, area);
            AFKShop.areas.save(areaName);
            p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("area_added_successfully", areaName));
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("set_area_location_instruction"));
        }
    }

    public static void switchAreaSetMode(Player p) {
        if (AFKShop.areaSetMode.containsKey(p.getUniqueId())) {
            AFKShop.areaSetMode.remove(p.getUniqueId());
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("area_set_mode_disabled"));
        } else {
            AFKShop.areaSetMode.put(p.getUniqueId(), Tuple.of(null, null));
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("area_set_mode_enabled"));
        }
    }

    public static void setAreaLocation(Player p, String areaName) {
        if (!isExistingArea(areaName)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("area_name_not_exists"));
        } else {
            AFKArea area = AFKShop.areas.get(areaName);
            Tuple<Location, Location> locs = AFKShop.areaSetMode.get(p.getUniqueId());
            if (locs == null) {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("area_location_not_set"));
                return;
            }
            if (locs.getA() == null || locs.getB() == null) {
                p.sendMessage(plugin.getPrefix() + plugin.getLang().get("area_both_points_needed"));
                return;
            }
            area.setPos1(locs.getA());
            area.setPos2(locs.getB());
            AFKShop.areas.put(areaName, area);
            AFKShop.areas.save(areaName);
            p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("area_location_set", areaName));
            switchAreaSetMode(p);
        }
    }

    public static void deleteArea(Player p, String areaName) {
        if (!isExistingArea(areaName)) {
            p.sendMessage(plugin.getPrefix() + plugin.getLang().get("area_name_not_exists"));
        } else {
            AFKShop.areas.delete(areaName);
            AFKShop.areas.remove(areaName);
            p.sendMessage(plugin.getPrefix() + plugin.getLang().getWithArgs("area_deleted", areaName));
        }
    }
}
