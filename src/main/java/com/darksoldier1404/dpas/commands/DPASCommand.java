package com.darksoldier1404.dpas.commands;

import com.darksoldier1404.dpas.AFKShop;
import com.darksoldier1404.dpas.functions.DPASFunction;
import com.darksoldier1404.dppc.builder.command.ArgumentIndex;
import com.darksoldier1404.dppc.builder.command.ArgumentType;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;

public class DPASCommand {
    public static void init() {
        final CommandBuilder builder = new CommandBuilder(AFKShop.getInstance());
        builder.beginSubCommand("create", AFKShop.getInstance().getLang().get("command_create_desc"))
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING)
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.createShop(p, name);
                    return true;
                });
        builder.beginSubCommand("items", AFKShop.getInstance().getLang().get("command_items_desc"))
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.data.keySet())
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.editShopItems(p, name);
                    return true;
                });
        builder.beginSubCommand("price", AFKShop.getInstance().getLang().get("command_price_desc"))
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.data.keySet())
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.editShopPrice(p, name);
                    return true;
                });
        builder.beginSubCommand("maxpage", AFKShop.getInstance().getLang().get("command_maxpage_desc"))
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.data.keySet())
                .withArgument(ArgumentIndex.ARG_1, ArgumentType.INTEGER)
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    int page = args.getInteger(ArgumentIndex.ARG_1);
                    DPASFunction.setMaxPage(p, name, page);
                    return true;
                });
        builder.beginSubCommand("pointset", AFKShop.getInstance().getLang().get("command_pointset_desc"))
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING)
                .withArgument(ArgumentIndex.ARG_1, ArgumentType.INTEGER)
                .executesPlayer((p, args) -> {
                    String time = args.getString(ArgumentIndex.ARG_0);
                    int pointAmount = args.getInteger(ArgumentIndex.ARG_1);
                    DPASFunction.setAfkPointPerInterval(p, time, pointAmount);
                    return true;
                });

        builder.beginSubCommand("areaadd", AFKShop.getInstance().getLang().get("command_areaadd_desc"))
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING)
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.addArea(p, name);
                    return true;
                });

        builder.beginSubCommand("arearemove", AFKShop.getInstance().getLang().get("command_arearemove_desc"))
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.areas.keySet())
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.deleteArea(p, name);
                    return true;
                });

        builder.beginSubCommand("areamode", AFKShop.getInstance().getLang().get("command_areamode_desc"))
                .withPermission("dpas.admin")
                .executesPlayer((p, args) -> {
                    DPASFunction.switchAreaSetMode(p);
                    return true;
                });

        builder.beginSubCommand("areaset", AFKShop.getInstance().getLang().get("command_areaset_desc"))
                .withPermission("dpas.admin")
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.areas.keySet())
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.setAreaLocation(p, name);
                    return true;
                });

        builder.beginSubCommand("open", AFKShop.getInstance().getLang().get("command_open_desc"))
                .withArgument(ArgumentIndex.ARG_0, ArgumentType.STRING, AFKShop.data.keySet())
                .executesPlayer((p, args) -> {
                    String name = args.getString(ArgumentIndex.ARG_0);
                    DPASFunction.openShop(p, name);
                    return true;
                });
        builder.build("dpas");
    }
}
