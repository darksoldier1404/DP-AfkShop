package com.darksoldier1404.dpas.commands;

import com.darksoldier1404.dpas.AFKShop;
import com.darksoldier1404.dpas.functions.DPASFunction;
import com.darksoldier1404.dppc.api.worldguard.WorldGuardAPI;
import com.darksoldier1404.dppc.builder.command.ArgumentIndex;
import com.darksoldier1404.dppc.builder.command.ArgumentType;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;

/*
/dpas create [이름] - 잠수상점 만들기
/dpas items [상점이름] - 해당 상점의 아이템을 놓습니다.
/dpas price [상점이름] - 해당 상점의 아이템 가격을 정합니다.
/dpas point [시간/s/m] [포인트양] - 잠수를 하고 있을시 특정 시간 마다 얼만큼의 포인트를 받게 할 지 정합니다.

플레이스홀더
%dpas-point% 잠수포인트

/dpas wgadd [이름] (월드) - 해당 월드의 월드가드에 잠수포인트 쌓을 수 있도록 추가 (월드 미입력 시 현재 플레이어가 속한 월드로 지정, ALL 입력시 모든 월드 대상)
/dpas wgremove [이름]  (월드) -  해당 잠수포인트 월드가드 제거 (월드 미입력 시 현재 플레이어가 속한 월드로 지정, ALL 입력시 모든 월드 대상)
/dpas page [상점이름] [page] - 최대 페이지 설정 ( 기본 값 : 0, 0부터 시작 )
 */
public class DPASCommand {
    public static void init() {
        final CommandBuilder builder = new CommandBuilder(AFKShop.getInstance());
        builder.beginSubCommand("create", "/dpas create <name> - Create a new AFK Point Shop")
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING)
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.createShop(p, name);
                    return true;
                });
        builder.beginSubCommand("items", "/dpas items <shop_name> - Edit items of an existing AFK Point Shop")
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.data.keySet())
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.editShopItems(p, name);
                    return true;
                });
        builder.beginSubCommand("price", "/dpas price <shop_name> - Edit item prices of an existing AFK Point Shop")
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.data.keySet())
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.editShopPrice(p, name);
                    return true;
                });
        builder.beginSubCommand("maxpage", "/dpas maxpage <shop_name> <maxpage> - Set maximum page for a specific AFK Point Shop")
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.data.keySet())
                .withArgument(ArgumentIndex.ARG_1, ArgumentType.INTEGER)
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    int page = args.getInteger(ArgumentIndex.ARG_1);
                    DPASFunction.setMaxPage(p, name, page);
                    return true;
                });
        builder.beginSubCommand("pointset", "/dpas pointset <sec> <point_amount> - Set the amount of points to be received at specific time intervals while AFK")
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING)
                .withArgument(ArgumentIndex.ARG_1, ArgumentType.INTEGER)
                .executesPlayer((p, args) -> {
                    String time = args.getString(ArgumentIndex.ARG_0);
                    int pointAmount = args.getInteger(ArgumentIndex.ARG_1);
                    DPASFunction.setAfkPointPerInterval(p, time, pointAmount);
                    return true;
                });

        builder.beginSubCommand("wgadd", "/dpas wgadd <world> <region> - Add a world to the AFK Point WG list")
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.WORLD)
                .withArgument(ArgumentIndex.ARG_1, ArgumentType.STRING, (p, args) -> WorldGuardAPI.getAllRegions(Bukkit.getWorld(args[1])))
                .executesPlayer((p, args) -> {
                    World world = args.getWorld(ArgumentIndex.ARG_0);
                    String name = args.getString(ArgumentIndex.ARG_1);
                    DPASFunction.addWorldGuardWorld(p, name, world);
                    return true;
                });

        builder.beginSubCommand("open", "/dpas open <shop_name> - Open a specific AFK Point Shop")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.data.keySet())
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.openShop(p, name);
                    return true;
                });
        builder.build("dpas");
    }
}
