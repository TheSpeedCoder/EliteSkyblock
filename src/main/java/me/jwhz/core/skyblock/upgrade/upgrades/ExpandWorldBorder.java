package me.jwhz.core.skyblock.upgrade.upgrades;

import me.jwhz.core.Core;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.upgrade.Upgrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;

@Upgrade.Info(
        name = "Expand World Border",
        costs = {500000, 5000000}
)
public class ExpandWorldBorder extends Upgrade {

    @Override
    public double getCost(Player player, Island island) {

        if (island.upgrades.getTier(this) == 3)
            return 0;

        return getAnnotionInfo().costs()[island.upgrades.getTier(this) - 1];

    }

    @Override
    public boolean hasEnoughMoney(Player player, Island island) {

        return island.upgrades.getTier(this) != 3 && Core.economy.getBalance(player) >= getCost(player, island);

    }

    @Override
    public ItemStack getItem(Player player, Island island) {

        ItemStack item = new ItemStack(Material.FENCE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Breakin' Out");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(ChatColor.GRAY + "Expands your island's");
        lore.add(ChatColor.GRAY + "border in all directions.");
        lore.add("");
        if (island.upgrades.getTier(new ExpandWorldBorder()) == 3) {

            lore.add(ChatColor.GRAY + "Current Tier: " + ChatColor.BLUE + "Tier 3 " + ChatColor.GRAY + "(250x250)");
            lore.add("");
            lore.add(ChatColor.GREEN + "Maxed out.");
        } else {

            lore.add(ChatColor.GRAY + "Cost: " + (hasEnoughMoney(player, island) ? ChatColor.GREEN : ChatColor.RED) + "$" + df.format(getCost(player, island)));
            lore.add(ChatColor.GRAY + "Expands border to " + (island.upgrades.getTier(this) == 1 ? "200x200" : "250x250") + ".");
            lore.add("");
            lore.add(ChatColor.GRAY + "Current Tier: " + ChatColor.BLUE + "Tier " + island.upgrades.getTier(this) + " "
                    + ChatColor.GRAY + "" + (island.upgrades.getTier(this) == 1 ? "150x150" : "200x200") + "");
            lore.add("");
            lore.add(hasEnoughMoney(player, island) ?
                    ChatColor.YELLOW + "Click here to " + ChatColor.BOLD + "upgrade" + ChatColor.YELLOW + "." :
                    ChatColor.RED + "You do not have enough money."
            );

        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;

    }

    public void applyUpgrade(Island island) {

        island.min = new Point(island.min.x - 25, island.min.y - 25);
        island.max = new Point(island.max.x + 25, island.max.y + 25);


    }


}
