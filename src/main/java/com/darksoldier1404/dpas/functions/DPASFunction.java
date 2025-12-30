package com.darksoldier1404.dpas.functions;


import com.darksoldier1404.dpas.AFKShop;
import com.darksoldier1404.dpas.shop.PointShop;
import com.darksoldier1404.dpas.user.AFKUser;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.api.placeholder.PlaceholderBuilder;
import com.darksoldier1404.dppc.api.worldguard.WorldGuardAPI;
import com.darksoldier1404.dppc.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
            p.sendMessage(plugin.getPrefix() + "§c이미 존재하는 잠수상점 이름입니다.");
        } else {
            PointShop shop = new PointShop(name, new DInventory("AFK Shop : " + name, 54, true, true, plugin));
            AFKShop.data.put(name, shop);
            AFKShop.data.save(name);
            p.sendMessage(plugin.getPrefix() + "§a잠수상점 §f" + name + "§a(이)가 성공적으로 생성되었습니다.");
        }
    }

    public static void editShopItems(Player p, String name) {
        if (!isExistingShop(name)) {
            p.sendMessage(plugin.getPrefix() + "§c존재하지 않는 잠수상점 이름입니다.");
        } else {
            PointShop shop = AFKShop.data.get(name);
            shop.openItemEditor(p);
        }
    }

    public static void editShopPrice(Player p, String name) {
        if (!isExistingShop(name)) {
            p.sendMessage(plugin.getPrefix() + "§c존재하지 않는 잠수상점 이름입니다.");
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
            p.sendMessage(plugin.getPrefix() + "§c시간 형식이 올바르지 않습니다. 예: 30s, 5m, 60");
            return;
        }
        plugin.getConfig().set("Settings.AfkPointPerInterval.TimeInSeconds", seconds);
        plugin.getConfig().set("Settings.AfkPointPerInterval.PointAmount", pointAmount);
        plugin.saveConfig();
        p.sendMessage(plugin.getPrefix() + "§a잠수 포인트 설정이 성공적으로 저장되었습니다. 시간: §f" + seconds + "초§a, 포인트: §f" + pointAmount + "§a.");
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
            p.sendMessage(plugin.getPrefix() + "§c존재하지 않는 잠수상점 이름입니다.");
        } else {
            PointShop shop = AFKShop.data.get(name);
            shop.getInventory().setPages(page);
            AFKShop.data.put(name, shop);
            AFKShop.data.save(name);
            p.sendMessage(plugin.getPrefix() + "§a잠수상점 §f" + name + "§a의 최대 페이지가 §f" + page + "§a(으)로 설정되었습니다.");
        }
    }

    public static void addWorldGuardWorld(Player p, String name, World world) {
        // using config
        if (world == null) {
            world = p.getWorld();
        }
        AFKShop.getInstance().getConfig().set("WorldGuardAreas." + world.getName() + "." + name, true);
        AFKShop.getInstance().saveConfig();
        p.sendMessage(plugin.getPrefix() + "§a구역이 성공적으로 추가되었습니다: §f" + world.getName() + "§a - §f" + name);
        refreshAllTasks();
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
            if (AFKShop.getInstance().getConfig().getConfigurationSection("WorldGuardAreas") == null) return;
            for (String worldName : AFKShop.getInstance().getConfig().getConfigurationSection("WorldGuardAreas").getKeys(false)) {
                World world = Bukkit.getWorld(worldName);
                if (world != null && p.getWorld().getName().equalsIgnoreCase(world.getName())) {
                    if (AFKShop.getInstance().getConfig().getConfigurationSection("WorldGuardAreas." + worldName) == null)
                        continue;
                    for (String regionName : AFKShop.getInstance().getConfig().getConfigurationSection("WorldGuardAreas." + worldName).getKeys(false)) {
                        if (WorldGuardAPI.isPlayerInRegion(p, regionName)) {
                            if (AFKShop.currentAFKTasks.containsKey(p.getUniqueId())) continue;
                            BukkitTask afkTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                                AFKUser user = AFKShop.udata.get(p.getUniqueId());
                                user.addAfkPoints(getAfkPointAmount());
                                AFKShop.udata.put(p.getUniqueId(), user);
                                Bukkit.getPlayer(user.getUUID()).sendMessage(plugin.getPrefix() + "§a잠수 포인트 §f" + getAfkPointAmount() + "§a점을 획득하였습니다! 현재 포인트: §f" + user.getPoint() + "§a점.");
                            }, getAfkPointIntervalTime() * 20L, getAfkPointIntervalTime() * 20L);
                            AFKShop.currentAFKTasks.put(p.getUniqueId(), afkTask);
                        } else {
                            if (AFKShop.currentAFKTasks.containsKey(p.getUniqueId())) {
                                AFKShop.currentAFKTasks.get(p.getUniqueId()).cancel();
                                AFKShop.currentAFKTasks.remove(p.getUniqueId());
                            }
                        }
                    }
                }
            }
        }), 0L, 20L);
    }

    public static void buyPointShopItem(Player p, PointShop shop, DInventory.PageItemSet pageItemSet) {
        AFKUser user = AFKShop.udata.get(p.getUniqueId());
        int price = shop.findPrice(pageItemSet.getPage(), pageItemSet.getSlot());
        if (price <= 0) {
            p.sendMessage(plugin.getPrefix() + "§c이 아이템은 구매할 수 없습니다.");
            return;
        }
        if (user.getPoint() < price) {
            p.sendMessage(plugin.getPrefix() + "§c잠수 포인트가 부족합니다. 현재 포인트: §f" + user.getPoint() + "§c, 필요 포인트: §f" + price + "§c.");
            return;
        }
        if (!InventoryUtils.hasEnoughSpace(p.getInventory().getStorageContents(), pageItemSet.getItem())) {
            p.sendMessage(plugin.getPrefix() + "§c인벤토리에 공간이 부족합니다.");
            return;
        }
        user.subtractAfkPoints(price);
        AFKShop.udata.put(p.getUniqueId(), user);
        p.getInventory().addItem(shop.getInventory().getPageItems().get(pageItemSet.getPage())[pageItemSet.getSlot()]);
        p.sendMessage(plugin.getPrefix() + "§a아이템을 구매하였습니다! 잠수 포인트 §f" + price + "§a가 차감되었습니다. 남은 포인트: §f" + user.getPoint() + "§a.");
    }

    public static void openShop(Player p, String name) {
        if (!isExistingShop(name)) {
            p.sendMessage(plugin.getPrefix() + "§c존재하지 않는 잠수상점 이름입니다.");
        } else {
            PointShop shop = AFKShop.data.get(name);
            shop.openShop(p);
        }
    }
}
