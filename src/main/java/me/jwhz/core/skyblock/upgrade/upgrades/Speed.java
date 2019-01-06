package me.jwhz.core.skyblock.upgrade.upgrades;

import me.jwhz.core.Core;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.upgrade.Upgrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@Upgrade.Info(
        name = "Speed",
        costs = {75000},
        isTierBased = false,
        numberOfTiers =  1
)
public class Speed extends Upgrade {

    @Override
    public double getCost(Player player, Island island) {

        if(island.upgrades.hasPurchased(this))
            return 0;

        return getAnnotionInfo().costs()[0];

    }

    @Override
    public boolean hasEnoughMoney(Player player, Island island) {

        return !island.upgrades.hasPurchased(this) && Core.economy.getBalance(player) >= getCost(player, island);

    }

    @Override
    public ItemStack getItem(Player player, Island island) {

        ItemStack item = new ItemStack(Material.SUGAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Track Shoes");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(ChatColor.GRAY + "Gives speed 2 while");
        lore.add(ChatColor.GRAY + "you are on the island.");
        lore.add("");
        if (island.upgrades.hasPurchased(new Speed()))
            lore.add(ChatColor.GREEN + "Purchased.");
        else {

            lore.add(ChatColor.GRAY + "Cost: " + (hasEnoughMoney(player, island) ? ChatColor.GREEN : ChatColor.RED) + "$" + df.format(getCost(player, island)));
            lore.add(ChatColor.GRAY + "Gives speed 2 while on island");
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

}
