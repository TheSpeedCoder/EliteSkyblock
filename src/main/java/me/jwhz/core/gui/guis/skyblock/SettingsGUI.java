package me.jwhz.core.gui.guis.skyblock;

import me.jwhz.core.gui.GUI;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.islands.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;

public class SettingsGUI extends GUI {

    private Island island;

    public SettingsGUI(Player player) {

        inventory = Bukkit.createInventory(null, 36, "Island Settings");

        island = core.skyblockManager.getIsland(player.getUniqueId());

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

        if (e.getSlot() == 25) {

            island.settings.setValue(Settings.Value.JUMP, !island.settings.getValue(Settings.Value.JUMP), island);
            island.save();

            setupGUI((Player) e.getWhoClicked());

        }

        if (e.getSlot() == 22) {

            island.settings.setValue(Settings.Value.PRIVATE, !island.settings.getValue(Settings.Value.PRIVATE), island);
            island.save();

            setupGUI((Player) e.getWhoClicked());

        }

        if (e.getSlot() == 19) {

            island.settings.setValue(Settings.Value.SPEED, !island.settings.getValue(Settings.Value.SPEED), island);
            island.save();

            setupGUI((Player) e.getWhoClicked());

        }

    }

    @Override
    public void setupGUI(Player player) {

        inventory.setItem(0, fastItem(Material.BED, "&cBack"));

        inventory.setItem(13, fastItem(
                Material.EYE_OF_ENDER,
                "&e&lPrivacy",
                "",
                "&7Allows other users to visit your",
                "&7island whenever they want. Other",
                "&7players cannot edit your island no",
                "&7matter what, even if it's disabled",
                "",
                "&fCurrently: " + (island.settings.getValue(Settings.Value.PRIVATE) ? "&a&lENABLED" : "&c&lDISABLED")
        ));

        inventory.setItem(22, fastItem(
                Material.INK_SACK,
                (island.settings.getValue(Settings.Value.PRIVATE) ? "&a&lEnabled" : "&c&lDisabled"),
                (island.settings.getValue(Settings.Value.PRIVATE) ? (byte) 10 : (byte) 8),
                (island.settings.getValue(Settings.Value.PRIVATE) ? "&7Players cannot visit your island." : "&7Players can visit your island."),
                "",
                (island.settings.getValue(Settings.Value.PRIVATE) ? "&eClick here to &ldisable&e." : "&eClick here to &lenable&e.")
        ));

        inventory.setItem(16, fastItem(
                Material.FEATHER,
                "&e&lJump Boost",
                "",
                "&7Enables/Disables the jump boost",
                "&7on your island. You must have",
                "&7bought the jump boost upgrade",
                "&7for this to work.",
                "",
                "&fCurrently: " + (island.settings.getValue(Settings.Value.JUMP) ? "&a&lENABLED" : "&c&lDISABLED")
        ));

        inventory.setItem(25, fastItem(
                Material.INK_SACK,
                (island.settings.getValue(Settings.Value.JUMP) ? "&a&lEnabled" : "&c&lDisabled"),
                (island.settings.getValue(Settings.Value.JUMP) ? (byte) 10 : (byte) 8),
                (island.settings.getValue(Settings.Value.JUMP) ? "&7Jump boost disabled." : "&7Jump boost enabled"),
                "",
                (island.settings.getValue(Settings.Value.JUMP) ? "&eClick here to &ldisable&e." : "&eClick here to &lenable&e.")
        ));

        inventory.setItem(10, fastItem(
                Material.SUGAR,
                "&e&lSpeed Boost",
                "",
                "&7Enables/Disables the speed boost",
                "&7on your island. You must have",
                "&7bought the speed boost upgrade",
                "&7for this to work.",
                "",
                "&fCurrently: " + (island.settings.getValue(Settings.Value.JUMP) ? "&a&lENABLED" : "&c&lDISABLED")
        ));

        inventory.setItem(19, fastItem(
                Material.INK_SACK,
                (island.settings.getValue(Settings.Value.SPEED) ? "&a&lEnabled" : "&c&lDisabled"),
                (island.settings.getValue(Settings.Value.SPEED) ? (byte) 10 : (byte) 8),
                (island.settings.getValue(Settings.Value.SPEED) ? "&7Speed boost disabled." : "&7Speed boost enabled"),
                "",
                (island.settings.getValue(Settings.Value.SPEED) ? "&eClick here to &ldisable&e." : "&eClick here to &lenable&e.")
        ));

    }

    public ItemStack fastItem(Material mat, String displayName, byte data, String... lore) {

        ItemStack item = new ItemStack(mat, 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        ArrayList<String> updatedLore = new ArrayList<String>();
        for (String s : lore)
            updatedLore.add(ChatColor.translateAlternateColorCodes('&', s));

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.setLore(updatedLore);
        item.setItemMeta(meta);

        return item;

    }

}
