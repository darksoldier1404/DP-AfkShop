package com.darksoldier1404.dpas.user;

import com.darksoldier1404.dppc.data.DataCargo;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class AFKUser implements DataCargo {
    private UUID uuid;
    private String name;
    private long point;
    private long afkTime;
    private long totalAfkTime;

    public AFKUser() {
    }

    public AFKUser(UUID uuid, String name, long point, long afkTime, long totalAfkTime) {
        this.uuid = uuid;
        this.name = name;
        this.point = point;
        this.afkTime = afkTime;
        this.totalAfkTime = totalAfkTime;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getAfkTime() {
        return afkTime;
    }

    public void setAfkTime(long afkTime) {
        this.afkTime = afkTime;
    }

    public void addAfkPoints(int afkPointAmount) {
        this.point += afkPointAmount;
    }

    public void subtractAfkPoints(int price) {
        this.point -= price;
    }

    public long getTotalAfkTime() {
        return totalAfkTime;
    }

    public void setTotalAfkTime(long totalAfkTime) {
        this.totalAfkTime = totalAfkTime;
    }

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("UUID", uuid.toString());
        data.set("Name", name);
        data.set("Point", point);
        data.set("AFKTime", afkTime);
        data.set("TotalAFKTime", totalAfkTime);
        return data;
    }

    @Override
    public AFKUser deserialize(YamlConfiguration data) {
        if (data.contains("UUID") && data.contains("Point") && data.contains("AFKTime")) {
            UUID uuid = UUID.fromString(data.getString("UUID"));
            String name = data.getString("Name", "none");
            long point = data.getLong("Point");
            long afkTime = data.getLong("AFKTime");
            long totalAfkTime = data.getLong("TotalAFKTime", 0);
            return new AFKUser(uuid, name, point, afkTime, totalAfkTime);
        }
        return null;
    }
}
