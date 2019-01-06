package me.jwhz.core.gui.guis.skyblock;

import me.jwhz.core.Core;
import me.jwhz.core.gui.GUI;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.upgrade.upgrades.ExpandWorldBorder;
import me.jwhz.core.skyblock.upgrade.upgrades.FasterSpawner;
import me.jwhz.core.skyblock.upgrade.upgrades.Jump;
import me.jwhz.core.skyblock.upgrade.upgrades.Speed;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UpgradesGUI extends GUI {

    private Island island;
    private Speed speed = new Speed();
    private Jump jump = new Jump();
    private ExpandWorldBorder worldBorder = new ExpandWorldBorder();
    private FasterSpawner fasterSpawner = new FasterSpawner();

    public UpgradesGUI(Player player) {

        island = core.skyblockManager.getIsland(player.getUniqueId());

        inventory = Bukkit.createInventory(null, 45, "Island Upgrades");

        setupGUI(player);

        addDefaultListening(player);

        player.openInventory(inventory);

    }

    @Override
    public void onClick(InventoryClickEvent e) {

        if (e.getSlot() == 0) {

            HandlerList.unregisterAll(defaultListening);
            new IslandGUI((Player) e.getWhoClicked());
            return;

        }

        if (e.getSlot() == 11) {

            if (island.upgrades.canUpgrade(speed) && speed.hasEnoughMoney((Player) e.getWhoClicked(), island)) {

                Core.economy.withdrawPlayer((OfflinePlayer) e.getWhoClicked(), speed.getCost((Player) e.getWhoClicked(), island));
                island.upgrades.purchase(island, speed);

                setupGUI((Player) e.getWhoClicked());

            }

            return;

        }

        if (e.getSlot() == 15) {

            if (island.upgrades.canUpgrade(jump) && jump.hasEnoughMoney((Player) e.getWhoClicked(), island)) {

                Core.economy.withdrawPlayer((OfflinePlayer) e.getWhoClicked(), jump.getCost((Player) e.getWhoClicked(), island));
                island.upgrades.purchase(island, jump);

                setupGUI((Player) e.getWhoClicked());

            }

            return;

        }

        if (e.getSlot() == 29) {

            if (island.upgrades.canUpgrade(worldBorder) && worldBorder.hasEnoughMoney((Player) e.getWhoClicked(), island)) {

                Core.economy.withdrawPlayer((OfflinePlayer) e.getWhoClicked(), worldBorder.getCost((Player) e.getWhoClicked(), island));
                island.upgrades.purchase(island, worldBorder);

                worldBorder.applyUpgrade(island);

                setupGUI((Player) e.getWhoClicked());

            }
            return;
        }

        if (e.getSlot() == 33) {

            if (island.upgrades.canUpgrade(fasterSpawner) && fasterSpawner.hasEnoughMoney((Player) e.getWhoClicked(), island)) {

                Core.economy.withdrawPlayer((OfflinePlayer) e.getWhoClicked(), fasterSpawner.getCost((Player) e.getWhoClicked(), island));
                island.upgrades.purchase(island, fasterSpawner);

                fasterSpawner.applyUpgrade(island);

                setupGUI((Player) e.getWhoClicked());

            }

        }


    }

    @Override
    public void setupGUI(Player player) {

        inventory.setItem(0, fastItem(Material.BED, "&cBack"));

        inventory.setItem(11, speed.getItem(player, island));

        inventory.setItem(15, jump.getItem(player, island));

        inventory.setItem(29, worldBorder.getItem(player, island));

        inventory.setItem(33, fasterSpawner.getItem(player, island));

    }

}
