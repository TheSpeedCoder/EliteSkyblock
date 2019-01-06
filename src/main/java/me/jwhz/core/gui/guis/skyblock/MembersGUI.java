package me.jwhz.core.gui.guis.skyblock;


import me.jwhz.core.ChatConfirming;
import me.jwhz.core.Core;
import me.jwhz.core.command.commands.IslandCMD;
import me.jwhz.core.gui.GUI;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.utils.TimeUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class MembersGUI extends GUI {

    private int[] memberSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
    private boolean isOwner;
    private Island island;
    private HashMap<ItemStack, UUID> players = new HashMap<>();

    public MembersGUI(Player player) {

        island = core.skyblockManager.getIsland(player.getUniqueId());

        isOwner = core.skyblockManager.isOwner(player.getUniqueId());

        inventory = Bukkit.createInventory(null, 36, "Island Members");

        setupGUI(player);

        addDefaultListening(player);

        player.openInventory(inventory);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);

        if (e.getSlot() == 0) {

            //e.getWhoClicked().closeInventory();
            HandlerList.unregisterAll(defaultListening);
            new IslandGUI((Player) e.getWhoClicked());
            return;

        }

        if (e.getCurrentItem() != null && isOwner && (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT)) {

            for (Map.Entry<ItemStack, UUID> entry : players.entrySet()) {

                if (island.coop.contains(entry.getValue().toString()) && entry.getKey().getItemMeta().getDisplayName().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName())) {

                    e.getWhoClicked().sendMessage(ChatColor.YELLOW + "Are you sure you want to kick " + Bukkit.getOfflinePlayer(entry.getValue()).getName() + "?");

                    TextComponent confirm = new TextComponent(ChatColor.YELLOW + "Click the following: ");

                    TextComponent yes = new TextComponent(ChatColor.GREEN + "" + ChatColor.BOLD + "YES");
                    yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "1m0asdm;l10-"));
                    yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatColor.GREEN + "Click here to confirm.")}));

                    TextComponent no = new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "NO");
                    no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "m1psa=-d[1"));
                    no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatColor.RED + "Click here to cancel.")}));

                    confirm.addExtra(yes);
                    confirm.addExtra(" ");
                    confirm.addExtra(no);

                    ((Player) e.getWhoClicked()).spigot().sendMessage(confirm);

                    //e.getWhoClicked().closeInventory();

                    OfflinePlayer target = Bukkit.getOfflinePlayer(entry.getValue());

                    new ChatConfirming((Player) e.getWhoClicked(), new ChatConfirming.Callbacks() {
                        @Override
                        public void onSuccessConfirm() {

                            IslandCMD islandCMD = ((IslandCMD) core.commandManager.get("island"));

                            e.getWhoClicked().sendMessage(islandCMD.youveKicked.replace("$player", target.getName()));

                            island.coop.remove(target.getUniqueId().toString());
                            island.save();

                            if (target.isOnline()) {

                                target.getPlayer().sendMessage(islandCMD.kicked);
                                Location loc = (Location) core.commandManager.getYamlConfiguration().get("Spawn Location");

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {

                                        target.getPlayer().teleport(loc);
                                    }
                                }.runTaskLater(Core.getInstance(), 1);
                            }

                        }

                        @Override
                        public void onFailConfirm() {

                            e.getWhoClicked().sendMessage(ChatColor.RED + "You did not kick " + target.getName() + ".");

                        }

                        @Override
                        public void onCancelled() {

                            e.getWhoClicked().sendMessage(ChatColor.RED + "You did not kick " + target.getName() + ".");

                        }
                    });

                    break;

                }

            }

        }

    }

    @Override
    public void setupGUI(Player player) {

        inventory.setItem(0, fastItem(Material.BED, "&cBack"));

        List<String> members = new ArrayList<>();

        members.add(island.owner.toString());

        if (island.coop != null)
            for (String id : island.coop)
                members.add(id);

        for (int i = 0; i < members.size(); i++) {

            inventory.setItem(memberSlots[i], getItem(UUID.fromString(members.get(i)), player.getWorld()));

        }

    }

    public ItemStack getItem(UUID id, World world) {

        OfflinePlayer player = Bukkit.getOfflinePlayer(id);

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setOwner(player.getName());
        meta.setDisplayName(ChatColor.YELLOW + player.getName());

        ArrayList<String> lore = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("###,###.##");

        lore.add("");
        lore.add(ChatColor.GRAY + "Rank: " + ChatColor.WHITE + getGroup(world, player));
        lore.add(ChatColor.GRAY + "Playtime: " + ChatColor.WHITE + TimeUtils.formatSeconds(getOnlineTime(player) * 1000));
        lore.add(ChatColor.GRAY + "Kills: " + ChatColor.WHITE + getKills(player));
        lore.add(ChatColor.GRAY + "Deaths: " + ChatColor.WHITE + getDeaths(player));
        lore.add(ChatColor.GRAY + "Balance: " + ChatColor.WHITE + "$" + df.format(Core.economy.getBalance(player)));

        if (isOwner && !player.getUniqueId().equals(island.owner)) {

            lore.add("");
            lore.add(ChatColor.YELLOW + "Shift + Click here to kick " + ChatColor.BOLD + player.getName() + ChatColor.YELLOW + ".");

        }

        meta.setLore(lore);

        item.setItemMeta(meta);

        players.put(item, player.getUniqueId());

        return item;
    }

    public long getDeaths(OfflinePlayer player) {

        if (player.isOnline()) {

            return Bukkit.getPlayer(player.getName()).getStatistic(Statistic.DEATHS);

        }

        JSONParser parser = new JSONParser();

        try {

            JSONObject array = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/world/stats/" + player.getUniqueId() + ".json"));

            return array.containsKey("stat.deaths") ? (long) array.get("stat.deaths") : 0;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }


    public long getKills(OfflinePlayer player) {

        if (player.isOnline()) {

            return Bukkit.getPlayer(player.getName()).getStatistic(Statistic.PLAYER_KILLS);

        }

        JSONParser parser = new JSONParser();

        try {

            JSONObject array = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/world/stats/" + player.getUniqueId() + ".json"));

            return array.containsKey("stat.playerKills") ? (long) array.get("stat.playerKills") : 0;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public long getOnlineTime(OfflinePlayer player) {

        if (player.isOnline()) {

            return Bukkit.getPlayer(player.getName()).getStatistic(Statistic.PLAY_ONE_TICK) / 20;

        }

        JSONParser parser = new JSONParser();

        try {

            JSONObject array = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/world/stats/" + player.getUniqueId() + ".json"));

            return array.containsKey("stat.playOneMinute") ? (long) array.get("stat.playOneMinute") / 20 : 0;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public String getGroup(World world, OfflinePlayer player) {

        if (Core.chat.getPrimaryGroup(world.getName(), player) == null)
            return "Default";

        return Core.chat.getPrimaryGroup(world.getName(), player);

    }

}
