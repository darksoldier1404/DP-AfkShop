package com.darksoldier1404.dpas;

import com.darksoldier1404.dpas.functions.DPASFunction;
import com.darksoldier1404.dpas.shop.PointShop;
import com.darksoldier1404.dpas.user.AFKUser;
import com.darksoldier1404.dppc.annotation.DPPCoreVersion;
import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.data.DPlugin;
import com.darksoldier1404.dppc.data.DataContainer;
import com.darksoldier1404.dppc.data.DataType;
import com.darksoldier1404.dppc.utils.PluginUtil;
import com.darksoldier1404.dpas.commands.DPASCommand;
import com.darksoldier1404.dpas.events.DPASEvent;
import com.darksoldier1404.dppc.utils.Tuple;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@DPPCoreVersion(since = "5.3.3")
public class AFKShop extends DPlugin {
    private static AFKShop plugin;
    public static DataContainer<String, PointShop> data;
    public static DataContainer<UUID, AFKUser> udata;
    public static final Map<UUID, Tuple<PointShop, DInventory.PageItemSet>> currentPriceEdit = new HashMap<>();
    public static final Map<UUID, BukkitTask> currentAFKTasks = new HashMap<>();

    public AFKShop() {
        super(false);
        plugin = this;
        init();
    }

    public static AFKShop getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        PluginUtil.addPlugin(plugin, 26098);
        data = loadDataContainer(new DataContainer<>(this, DataType.CUSTOM, "shops"), PointShop.class);
        udata = loadDataContainer(new DataContainer<>(this, DataType.CUSTOM, "users"), AFKUser.class);
        DPASFunction.initPlaceholder();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DPASEvent(), plugin);
        DPASCommand.init();
        DPASFunction.initTask();
    }

    @Override
    public void onDisable() {
        saveAllData();
    }
}
