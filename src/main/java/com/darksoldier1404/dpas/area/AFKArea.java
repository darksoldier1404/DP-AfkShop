package com.darksoldier1404.dpas.area;

import com.darksoldier1404.dppc.data.DataCargo;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class AFKArea implements DataCargo {
    private String name;
    private Location pos1;
    private Location pos2;

    public AFKArea() {
    }

    public AFKArea(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public boolean isInArea(Player p) {
        return isInArea(p.getLocation());
    }

    public boolean isInArea(Location loc) {
        if (loc == null || pos1 == null || pos2 == null || loc.getWorld() == null || pos1.getWorld() == null || pos2.getWorld() == null)
            return false;
        if (!loc.getWorld().getName().equals(pos1.getWorld().getName()) || !loc.getWorld().getName().equals(pos2.getWorld().getName()))
            return false;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        double p1x = pos1.getX();
        double p2x = pos2.getX();
        double p1y = pos1.getY();
        double p2y = pos2.getY();
        double p1z = pos1.getZ();
        double p2z = pos2.getZ();
        double x1 = Math.min(p1x, p2x);
        double y1 = Math.min(p1y, p2y);
        double z1 = Math.min(p1z, p2z);
        double x2 = Math.max(p1x, p2x);
        double y2 = Math.max(p1y, p2y);
        double z2 = Math.max(p1z, p2z);
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    @Override
    public YamlConfiguration serialize() {
        YamlConfiguration data = new YamlConfiguration();
        data.set("Name", name);
        data.set("POS1", pos1);
        data.set("POS2", pos2);
        return data;
    }

    @Override
    public AFKArea deserialize(YamlConfiguration data) {
        this.name = data.getString("Name");
        this.pos1 = data.getLocation("POS1");
        this.pos2 = data.getLocation("POS2");
        return this;
    }
}
