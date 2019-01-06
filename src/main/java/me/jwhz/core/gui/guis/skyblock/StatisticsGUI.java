package me.jwhz.core.gui.guis.skyblock;

import me.jwhz.core.Core;
import me.jwhz.core.gui.GUI;
import me.jwhz.core.skyblock.islands.Island;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;

import static me.jwhz.core.utils.TimeUtils.formatSeconds;

public class StatisticsGUI extends GUI {

    private Island island;

    public StatisticsGUI(Player player) {

        island = core.skyblockManager.getIsland(player.getUniqueId());

        inventory = Bukkit.createInventory(null, 36, "Island Statistics");

        setupGUI(player);

        addDefaultListening(player);

        player.openInventory(inventory);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        if (e.getSlot() == 0) {

            HandlerList.unregisterAll(defaultListening);
            new IslandGUI((Player) e.getWhoClicked());

        }

    }

    @Override
    public void setupGUI(Player player) {

        inventory.setItem(0, fastItem(Material.BED, "&cBack"));

        long totalKills = 0, totalDeaths = 0, totalPlaytime = 0, totalMoney = 0;
        double userKills = 0, userDeaths = 0, userPlaytime = 0;
        double userMoney = 0;

        if (island.coop != null)
            for (String id : island.coop) {

                UUID uuid = UUID.fromString(id);

                userKills = getKills(uuid);
                userDeaths = getDeaths(uuid);
                userPlaytime = getOnlineTime(uuid);
                userMoney = Core.economy.getBalance(Bukkit.getOfflinePlayer(uuid));

                totalKills += userKills;
                totalDeaths += userDeaths;
                totalPlaytime += userPlaytime;
                totalMoney += userMoney;

            }

        userKills = getKills(island.owner);
        userDeaths = getDeaths(island.owner);
        userPlaytime = getOnlineTime(island.owner);
        userMoney = Core.economy.getBalance(Bukkit.getOfflinePlayer(island.owner));

        totalKills += userKills;
        totalDeaths += userDeaths;
        totalPlaytime += userPlaytime;
        totalMoney += userMoney;

        userKills = getKills(player.getUniqueId());
        userDeaths = getDeaths(player.getUniqueId());
        userPlaytime = getOnlineTime(player.getUniqueId());
        userMoney = Core.economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId()));

        DecimalFormat df = new DecimalFormat("#.#");

        inventory.setItem(11, fastItem(
                Material.DIAMOND_SWORD,
                "&e&lIsland Kills",
                "",
                "&7Total Kills: &f" + totalKills,
                "&7Your Contribution: &f" + (totalKills == 0 ? 0 : df.format((userKills / totalKills) * 100)) + "%",
                "",
                "&cThis statistic is calculated by",
                "&cadding all island member kills."
        ));

        inventory.setItem(20, fastItem(
                Material.LAVA_BUCKET,
                "&e&lIsland Deaths",
                "",
                "&7Total Deaths: &f" + totalDeaths,
                "&7Your Contribution: &f" + (totalDeaths == 0 ? 0 : df.format((userDeaths / totalDeaths) * 100)) + "%",
                "",
                "&cThis statistic is calculated by",
                "&cadding all island member deaths."
        ));

        inventory.setItem(13, fastItem(
                Material.WATCH,
                "&e&lIsland Playtime",
                "",
                "&7Total Playtime: &f" + formatSeconds(totalPlaytime * 1000),
                "&7Your Playtime: &f" + formatSeconds((long) userPlaytime * 1000),
                "",
                "&cThis statistic is calculated by",
                "&cadding all island member playtime."
        ));

        inventory.setItem(22, fastItem(
                Material.GOLD_BLOCK,
                "&e&lLevel",
                "",
                "&7Island Level: &f" + core.levelSystem.levels.get(island)
        ));

        inventory.setItem(15, fastItem(
                Material.EMERALD,
                "&e&lIsland Balance",
                "",
                "&7Total Balance: &f" + new DecimalFormat("###,###.##").format(totalMoney),
                "&7Your Contribution: &f" + (totalMoney == 0 ? 0 : df.format((userMoney / totalMoney) * 100)) + "%",
                "",
                "&cThis statistic is calculated by",
                "&cadding all island member balances."
        ));

        int size = island.max.x - island.min.x;

        inventory.setItem(24, fastItem(
                Material.REDSTONE_TORCH_ON,
                "&e&lBorder",
                "",
                "&7Border Size: &f" + size + "x" + size
        ));


    }

    public long getDeaths(UUID uuid) {

        if (Bukkit.getPlayer(uuid) != null) {

            return Bukkit.getPlayer(uuid).getStatistic(Statistic.DEATHS);

        }

        JSONParser parser = new JSONParser();

        try {

            JSONObject array = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/world/stats/" + uuid + ".json"));

            return array.containsKey("stat.deaths") ? (long) array.get("stat.deaths") : 0;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }


    public long getKills(UUID uuid) {

        if (Bukkit.getPlayer(uuid) != null) {

            return Bukkit.getPlayer(uuid).getStatistic(Statistic.PLAYER_KILLS);

        }

        JSONParser parser = new JSONParser();

        try {

            JSONObject array = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/world/stats/" + uuid + ".json"));

            return array.containsKey("stat.playerKills") ? (long) array.get("stat.playerKills") : 0;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public long getOnlineTime(UUID uuid) {

        if (Bukkit.getPlayer(uuid) != null) {

            return Bukkit.getPlayer(uuid).getStatistic(Statistic.PLAY_ONE_TICK) / 20;

        }

        JSONParser parser = new JSONParser();

        try {

            JSONObject array = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/world/stats/" + uuid + ".json"));

            return array.containsKey("stat.playOneMinute") ? (long) array.get("stat.playOneMinute") / 20 : 0;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }
}
