package me.jwhz.core.gui.guis.skyblock;

import me.jwhz.core.ChatConfirming;
import me.jwhz.core.gui.GUI;
import me.jwhz.core.skyblock.schematics.Schematic;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class IslandSelectorGUI extends GUI {

    private HashMap<ItemStack, Schematic> schematics = new HashMap<>();

    public IslandSelectorGUI(Player player) {

        inventory = Bukkit.createInventory(null, 27, "Select an island type");

        setupGUI(player);

        listener = new Listener() {

            @EventHandler
            public void onInventoryClick(InventoryClickEvent e) {

                if (e.getInventory().equals(inventory) && e.getWhoClicked().equals(player)) {

                    e.setCancelled(true);

                    if (e.getCurrentItem() != null)
                        for (ItemStack item : schematics.keySet())
                            if (item.equals(e.getCurrentItem())) {

                                if(!player.hasPermission("Islands." + schematics.get(item).getName()))
                                    return;

                                player.closeInventory();

                                if (core.skyblockManager.hasIsland(player.getUniqueId())) {
                                    player.sendMessage(ChatColor.YELLOW + "Are you sure you want to leave your current island and create another?");

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

                                    player.spigot().sendMessage(confirm);

                                    new ChatConfirming(player, new ChatConfirming.Callbacks() {
                                        @Override
                                        public void onSuccessConfirm() {

                                            player.sendTitle(ChatColor.DARK_AQUA + "Resetting Island...", ChatColor.YELLOW + "You will be teleported when the island is generated.");

                                            HandlerList.unregisterAll(listener);

                                            long startedTime = System.currentTimeMillis();

                                            core.skyblockManager.restartIsland(player, schematics.get(item));

                                            long finished = System.currentTimeMillis();

                                            new BukkitRunnable() {

                                                @Override
                                                public void run() {

                                                    player.teleport(core.skyblockManager.getIsland(player.getUniqueId()).spawn);

                                                    player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Island created!" + ChatColor.YELLOW + " Use /is help for island related commands.");

                                                }
                                            }.runTaskLater(core, (5 - (finished - startedTime)) * 20);

                                        }

                                        @Override
                                        public void onFailConfirm() {

                                            HandlerList.unregisterAll(listener);
                                            player.sendMessage(ChatColor.RED + "You have stop your island from restarting!");

                                        }

                                        @Override
                                        public void onCancelled() {

                                            HandlerList.unregisterAll(listener);
                                            player.sendMessage(ChatColor.RED + "You have stop your island from restarting!");

                                        }
                                    });

                                } else {

                                    if(!player.hasPermission("Islands." + schematics.get(item).getName()))
                                        return;

                                    player.sendTitle(ChatColor.DARK_AQUA + "Generating Island...", ChatColor.YELLOW + "You will be teleported when the island is generated.");

                                    HandlerList.unregisterAll(listener);

                                    long startedTime = System.currentTimeMillis();

                                    core.skyblockManager.createIsland(player, schematics.get(item));

                                    long finished = System.currentTimeMillis();

                                    new BukkitRunnable() {

                                        @Override
                                        public void run() {

                                            player.teleport(core.skyblockManager.getIsland(player.getUniqueId()).spawn);

                                            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Island created!" + ChatColor.YELLOW + " Use /is help for island related commands.");

                                        }
                                    }.runTaskTimer(core, 0, (5 - (finished - startedTime)) * 20);
                                    break;
                                }
                            }

                }

            }

            @EventHandler
            public void onInventoryInteract(InventoryInteractEvent e) {

                if (e.getInventory().equals(inventory) && e.getWhoClicked().equals(player))
                    e.setCancelled(true);

            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent e) {

                if (e.getInventory().equals(inventory) && e.getPlayer().equals(player))
                    HandlerList.unregisterAll(this);

            }

        }

        ;

        Bukkit.getServer().

                getPluginManager().

                registerEvents(listener, core);

        player.openInventory(inventory);

    }

    @Override
    public void setupGUI(Player player) {

        int i = 10;

        for (Schematic schematic : core.schematicsManager.schematics)
            inventory.setItem(i++, getItem(player.hasPermission("Islands." + schematic.getName()), schematic, player));

    }

    public ItemStack getItem(boolean hasPerm, Schematic schematic, Player player) {

        ItemStack item = new ItemStack(hasPerm ? Material.EMPTY_MAP : Material.PAPER);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&l" + schematic.getName()));

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");

        if (schematic.getLore() != null) {
            for (String s : schematic.getLore())
                lore.add(ChatColor.translateAlternateColorCodes('&', s));

            lore.add("");
        }

        lore.add(
                ChatColor.translateAlternateColorCodes('&',
                        hasPerm ?
                                "&eClick here to select &l" + schematic.getName() + "&e." :
                                "&cYou do not have access to &l" + schematic.getName() + "&c."
                )
        );

        meta.setLore(lore);

        item.setItemMeta(meta);

        schematics.put(item, schematic);

        return item;

    }

}
