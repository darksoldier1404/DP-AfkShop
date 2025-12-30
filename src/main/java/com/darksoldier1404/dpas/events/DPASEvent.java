package com.darksoldier1404.dpas.events;

import com.darksoldier1404.dpas.AFKShop;
import com.darksoldier1404.dpas.functions.DPASFunction;
import com.darksoldier1404.dpas.shop.PointShop;
import com.darksoldier1404.dpas.shop.ShopPrice;
import com.darksoldier1404.dpas.user.AFKUser;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.events.dinventory.DInventoryClickEvent;
import com.darksoldier1404.dppc.events.dinventory.DInventoryCloseEvent;
import com.darksoldier1404.dppc.utils.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DPASEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!AFKShop.udata.containsKey(p.getUniqueId())) {
            AFKUser user = new AFKUser(p.getUniqueId(), 0, 0);
            AFKShop.udata.put(p.getUniqueId(), user);
            AFKShop.udata.save(p.getUniqueId());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (AFKShop.udata.containsKey(p.getUniqueId())) {
            AFKShop.udata.save(p.getUniqueId());
        }
        if (AFKShop.currentAFKTasks.containsKey(p.getUniqueId())) {
            AFKShop.currentAFKTasks.get(p.getUniqueId()).cancel();
            AFKShop.currentAFKTasks.remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClick(DInventoryClickEvent e) {
        DInventory inv = e.getDInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv.isValidHandler(AFKShop.getInstance())) {
            if (inv.isValidChannel(0)) { // Point Shop
                e.setCancelled(true);
                if (e.getPageItemSet() != null) {
                    DPASFunction.buyPointShopItem(p, (PointShop) inv.getObj(), e.getPageItemSet());
                    return;
                }
            }
            if (inv.isValidChannel(2)) { // Item price editor
                e.setCancelled(true);
                if (e.getPageItemSet() != null) {
                    DInventory.PageItemSet pis = e.getPageItemSet();
                    Tuple<PointShop, DInventory.PageItemSet> tpl = Tuple.of((PointShop) inv.getObj(), pis);
                    AFKShop.currentPriceEdit.put(p.getUniqueId(), tpl);
                    p.sendMessage(AFKShop.getInstance().getPrefix() + "§a아이템의 가격을 설정하려면 채팅창에 가격을 입력해주세요.");
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(DInventoryCloseEvent e) {
        DInventory inv = e.getDInventory();
        Player p = (Player) e.getPlayer();
        if (inv.isValidHandler(AFKShop.getInstance())) {
            if (inv.isValidChannel(1)) { // item editor
                PointShop shop = (PointShop) inv.getObj();
                inv.applyChanges();
                shop.setInventory(inv);
                AFKShop.data.put(shop.getName(), shop);
                AFKShop.data.save(shop.getName());
                p.sendMessage(AFKShop.getInstance().getPrefix() + "§a아이템이 저장되었습니다.");
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();
        if (AFKShop.currentPriceEdit.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            Tuple<PointShop, DInventory.PageItemSet> tpl = AFKShop.currentPriceEdit.get(p.getUniqueId());
            try {
                int price = Integer.parseInt(msg);
                if (price < 0) {
                    p.sendMessage(AFKShop.getInstance().getPrefix() + "§c가격은 0 이상의 숫자여야 합니다.");
                    return;
                }
                DInventory.PageItemSet pis = tpl.getB();
                int page = pis.getPage();
                int slot = pis.getSlot();
                PointShop shop = tpl.getA();
                ShopPrice sp = shop.findShopPrice(page, slot);
                shop.getPriceList().remove(sp);
                if (sp != null) {
                    sp.setBuyPrice(price);
                } else {
                    sp = new ShopPrice(page, slot, price);
                    shop.getPriceList().add(sp);
                }
                shop.getPriceList().add(sp);
                p.sendMessage(AFKShop.getInstance().getPrefix() + "§a아이템의 가격이 §f" + price + "§a로 설정되었습니다.");
                AFKShop.currentPriceEdit.remove(p.getUniqueId());
                Bukkit.getScheduler().runTaskLater(AFKShop.getInstance(), () -> shop.openPriceEditor(p), 1L);
            } catch (NumberFormatException ex) {
                p.sendMessage(AFKShop.getInstance().getPrefix() + "§c유효한 숫자를 입력해주세요.");
            }
        }
    }
}
