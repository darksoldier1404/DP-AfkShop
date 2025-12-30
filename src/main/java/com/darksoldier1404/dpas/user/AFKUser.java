package com.darksoldier1404.dpas.user;

import com.darksoldier1404.dppc.data.DataCargo;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class AFKUser implements DataCargo {
    private UUID uuid;
    private long point;
    private long afkTime;

    public AFKUser() {
    }

    public AFKUser(UUID uuid, long point, long afkTime) {
        this.uuid = uuid;
        this.point = point;
        this.afkTime = afkTime;
    }

    public UUID getUUID() {
        return uuid;
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

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("UUID", uuid.toString());
        data.set("Point", point);
        data.set("AFKTime", afkTime);
        return data;
    }

    @Override
    public AFKUser deserialize(YamlConfiguration data) {
        if (data.contains("UUID") && data.contains("Point") && data.contains("AFKTime")) {
            UUID uuid = UUID.fromString(data.getString("UUID"));
            long point = data.getLong("Point");
            long afkTime = data.getLong("AFKTime");
            return new AFKUser(uuid, point, afkTime);
        }
        return null;
    }
}
