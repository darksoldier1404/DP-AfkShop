package com.blueearthcat.dpas;

import com.blueearthcat.dpas.commands.DPAfkCommand;
import com.blueearthcat.dpas.commands.DPAfkShopCommand;
import com.blueearthcat.dpas.data.AFKUser;
import com.blueearthcat.dpas.data.AfkData;
import com.blueearthcat.dpas.events.DPASEvent;
import com.blueearthcat.dpas.functions.DPASFunction;
import com.darksoldier1404.dppc.data.DPlugin;
import com.darksoldier1404.dppc.data.DataContainer;
import com.darksoldier1404.dppc.data.DataType;
import com.darksoldier1404.dppc.utils.PluginUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AfkShop extends DPlugin {
    public static AfkShop plugin;
    // AFK 장소 및 포인트 관련 데이터
    public static AfkData afkData;
    // 상점 데이터
    public static DataContainer<String, YamlConfiguration> shops;
    // 유저별 데이터
//    public static DataContainer<String, YamlConfiguration> udata;
    public static DataContainer<UUID, AFKUser> afkuser;
    // 전역 작업 스케줄러
    public static BukkitTask globalTask;
    // 잠수 시간 추적 (초 단위)
    public static Map<UUID, Integer> afkTime = new HashMap<>();
    // 총 잠수 시간 추적 (초 단위)
    public static Map<UUID, Integer> afkTotalTime = new HashMap<>();

    public static AfkShop getInstance() {
        return plugin;
    }

    // 상점 관련 전역 작업 스케줄러
    public static BukkitTask task;

    public AfkShop() {
        super(true);
        plugin = this;
        init();
    }

    @Override
    public void onLoad() {
        DPASFunction.placeholderInit();
        shops = loadDataContainer(new DataContainer<>(this, DataType.YAML, "shops"));
        afkuser = loadDataContainer(new DataContainer<>(this, DataType.CUSTOM, "afkuser"), AFKUser.class);
//        udata = loadDataContainer(new DataContainer<>(this, DataType.YAML, "udata"));
        PluginUtil.addPlugin(plugin, 26098);
    }

    @Override
    public void onEnable() {
        DPASFunction.init();
        plugin.getServer().getPluginManager().registerEvents(new DPASEvent(), plugin);
        getCommand("dpafk").setExecutor(new DPAfkCommand().getExecutor());
        getCommand("dpafkshop").setExecutor(new DPAfkShopCommand().getExecutor());
    }

    @Override
    public void onDisable() {
        saveDataContainer();
        if (task != null) task.cancel();
    }
}
