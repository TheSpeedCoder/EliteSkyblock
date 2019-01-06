package me.jwhz.core.gui.guis.skyblock;

import me.jwhz.core.gui.GUI;
import me.jwhz.core.skyblock.islands.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static me.jwhz.core.Core.pl;
import static org.bukkit.ChatColor.YELLOW;

public class IslandGUI extends GUI {

    private Island island;
    private boolean isOwner;

    public IslandGUI(Player player) {

        island = core.skyblockManager.getIsland(player.getUniqueId());

        isOwner  = island.owner.equals(player.getUniqueId());

        inventory = Bukkit.createInventory(null, 54, Bukkit.getOfflinePlayer(island.owner).getName() + "'s Island");

        setupGUI(player);

        addDefaultListening(player);

        player.openInventory(inventory);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        e.setCancelled(true);

        if (e.getSlot() == 19 && isOwner) {

            HandlerList.unregisterAll(defaultListening);
            new MembersGUI((Player) e.getWhoClicked());
            return;

        }

        if (e.getSlot() == 22) {

            HandlerList.unregisterAll(defaultListening);
            new StatisticsGUI((Player) e.getWhoClicked());
            return;

        }

        if (e.getSlot() == 25 && isOwner) {

            HandlerList.unregisterAll(defaultListening);
            new SettingsGUI((Player) e.getWhoClicked());
            return;

        }

        if (e.getSlot() == 38) {

            HandlerList.unregisterAll(defaultListening);
            new UpgradesGUI((Player) e.getWhoClicked());
            return;

        }

        if (e.getSlot() == 40 && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.EYE_OF_ENDER) {

            e.getWhoClicked().sendMessage(YELLOW + "You have been teleported to your island.");
            e.getWhoClicked().teleport(island.spawn);
            HandlerList.unregisterAll(defaultListening);
            return;
        }

        if (e.getSlot() == 42 && (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) && isOwner) {

            core.skyblockManager.deleteIsland((Player) e.getWhoClicked());
            e.getWhoClicked().sendMessage(ChatColor.GREEN + "You have successfully deleted your island.");
            e.getWhoClicked().closeInventory();

        }


    }

    @Override
    public void setupGUI(Player player) {

        boolean isOwner = island.owner.equals(player.getUniqueId());

        ItemStack stats = new ItemStack(Material.BED);
        ItemMeta statsMeta = stats.getItemMeta();
        statsMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + Bukkit.getOfflinePlayer(island.owner).getName() + "'s Island");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(ChatColor.YELLOW + "Owner: " + ChatColor.WHITE + Bukkit.getOfflinePlayer(island.owner).getName());
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

        statsMeta.setLore(lore);
        stats.setItemMeta(statsMeta);

        inventory.setItem(4, stats);

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta skull = (SkullMeta) head.getItemMeta();
        skull.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Members");

        skull.setLore(Arrays.asList(
                ChatColor.GRAY + "View & Manage the island members.",
                "",
                (isOwner ? ChatColor.YELLOW + "Click to view the island " + ChatColor.BOLD + "members" + ChatColor.YELLOW + "." :
                        ChatColor.RED + "You need the " + ChatColor.BOLD + "owner" + ChatColor.RED + " role for this.")
        ));

        skull.setOwner(Bukkit.getOfflinePlayer(island.owner).getName());

        head.setItemMeta(skull);

        inventory.setItem(19, head);

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();

        bookMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Statistics");
        bookMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "View all of the island statistics.",
                "",
                ChatColor.YELLOW + "Click to view the island " + ChatColor.BOLD + "statistics" + ChatColor.YELLOW + "."
        ));

        book.setItemMeta(bookMeta);

        inventory.setItem(22, book);

        ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR);
        ItemMeta settingsMeta = book.getItemMeta();

        settingsMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Settings");
        settingsMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Edit all of the island settings.",
                "",
                (isOwner ? ChatColor.YELLOW + "Click to edit the island " + ChatColor.BOLD + "settings" + ChatColor.YELLOW + "." :
                        ChatColor.RED + "You need the " + ChatColor.BOLD + "owner" + ChatColor.RED + " role for this.")
        ));

        settings.setItemMeta(settingsMeta);

        inventory.setItem(25, settings);

        ItemStack upgrades = new ItemStack(Material.CHEST);
        ItemMeta upgradesMeta = book.getItemMeta();

        upgradesMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Upgrades");
        upgradesMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "View all of the island upgrade.",
                "",
                ChatColor.YELLOW + "Click to edit the island " + ChatColor.BOLD + "upgrade" + ChatColor.YELLOW + "."
        ));

        upgrades.setItemMeta(upgradesMeta);

        inventory.setItem(38, upgrades);

        ItemStack tpHome = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta tpmeta = tpHome.getItemMeta();

        tpmeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Go to island");

        tpmeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Teleport to your island.",
                "",
                ChatColor.YELLOW + "Click here to " + ChatColor.BOLD + "teleport" + ChatColor.YELLOW + "."
        ));

        tpHome.setItemMeta(tpmeta);

        if (core.skyblockManager.getInteractIsland(player.getLocation()) == null || !core.skyblockManager.getInteractIsland(player.getLocation()).equals(island))
            inventory.setItem(40, tpHome);

        ItemStack delete = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = book.getItemMeta();

        deleteMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Delete Island");

        ArrayList<String> deleteLore = new ArrayList<>();

        deleteLore.add(ChatColor.GRAY + "Delete this island completely.");
        deleteLore.add("");

        if (isOwner) {

            deleteLore.add(ChatColor.RED + "To delete this island, you");
            deleteLore.add(ChatColor.RED + "must " + ChatColor.GOLD + "" + ChatColor.BOLD + "SHIFT + Click" + ChatColor.RED + " here");
            deleteLore.add(ChatColor.RED + "to permanently delete.");

        } else
            deleteLore.add(ChatColor.RED + "You need the " + ChatColor.BOLD + "owner" + ChatColor.RED + " role for this.");


        deleteMeta.setLore(deleteLore);

        delete.setItemMeta(deleteMeta);

        inventory.setItem(42, delete);

    }
}
