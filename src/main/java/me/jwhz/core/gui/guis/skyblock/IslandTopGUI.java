package me.jwhz.core.gui.guis.skyblock;

import me.jwhz.core.command.commands.IslandCMD;
import me.jwhz.core.gui.GUI;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.islands.Settings;
import me.jwhz.core.skyblock.leveling.LevelSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class IslandTopGUI extends GUI {

    private int[] topSlots = {13, 21, 23, 37, 38, 39, 40, 41, 42, 43};

    private HashMap<ItemStack, Island> items = new HashMap<>();

    public IslandTopGUI(Player player) {

        inventory = Bukkit.createInventory(null, 54, "Top Islands");

        setupGUI(player);

        addDefaultListening(player);

        player.openInventory(inventory);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        if (e.getCurrentItem() != null) {

            for (ItemStack item : items.keySet())
                if (e.getCurrentItem().equals(item)) {

                    String islandOwnerName = Bukkit.getOfflinePlayer(items.get(item).owner).getName();

                    IslandCMD islandCMD = (IslandCMD) core.commandManager.get("island");

                    if (items.get(item).banned.contains(e.getWhoClicked().getUniqueId().toString()))
                        e.getWhoClicked().sendMessage(islandCMD.failedTp);
                    else {
                        if (items.get(item).settings.getValue(Settings.Value.PRIVATE) &&
                                !items.get(item).coop.contains(e.getWhoClicked().getUniqueId().toString()) &&
                                !items.get(item).owner.equals(e.getWhoClicked().getUniqueId()))
                            e.getWhoClicked().sendMessage(islandCMD.privateIsland);
                        else {
                            e.getWhoClicked().teleport(items.get(item).spawn);
                            e.getWhoClicked().sendMessage(islandCMD.teleportedToIsland.replace("$player", islandOwnerName));
                        }
                    }
                    e.getWhoClicked().closeInventory();

                    HandlerList.unregisterAll(defaultListening);

                    break;
                }
        }

    }

    @Override
    public void setupGUI(Player player) {

        for (int i = 0; i < topSlots.length; i++) {

            if (i < core.levelSystem.topIslands.size()) {
                LevelSystem.TopIsland topIsland = core.levelSystem.topIslands.get(i);

                inventory.setItem(topSlots[i], getItem(topIsland.island, i + 1, topIsland.level));

            }
        }

    }

    public ItemStack getItem(Island island, int rank, int level) {

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setOwner(Bukkit.getOfflinePlayer(island.owner).getName());
        skull.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + Bukkit.getOfflinePlayer(island.owner).getName() + "'s Island");

        ArrayList<String> lore = new ArrayList<String>();

        lore.add(ChatColor.GRAY + "Ranked: #" + rank + " (" + level + ")");
        lore.add("");
        if (island.coop != null && island.coop.size() > 0) {

            int totalOnline = 0, total = island.coop.size() + 1;

            for (String id : island.coop)
                if (Bukkit.getPlayer(UUID.fromString(id)) != null && Bukkit.getPlayer(UUID.fromString(id)).isOnline())
                    totalOnline++;

            if (Bukkit.getPlayer(island.owner) != null && Bukkit.getPlayer(island.owner).isOnline())
                totalOnline++;

            lore.add(ChatColor.YELLOW + "Members: " + ChatColor.GRAY + "(" + totalOnline + "/" + total + ")");

            lore.add(" " + ChatColor.WHITE + Bukkit.getOfflinePlayer(island.owner).getName());

            for (String id : island.coop)
                lore.add(" " + ChatColor.WHITE + Bukkit.getOfflinePlayer(UUID.fromString(id)).getName());

            lore.add("");

        }

        int size = island.max.x - island.min.x;

        lore.add(ChatColor.YELLOW + "Border Size: " + ChatColor.WHITE + size + "x" + size);
        lore.add(ChatColor.YELLOW + "Island Type: " + ChatColor.WHITE + island.chosenSchematic);
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click here to visit " + ChatColor.BOLD + Bukkit.getOfflinePlayer(island.owner).getName() + "'s" + ChatColor.YELLOW + " island");

        skull.setLore(lore);

        item.setItemMeta(skull);

        items.put(item, island);

        return item;

    }

}