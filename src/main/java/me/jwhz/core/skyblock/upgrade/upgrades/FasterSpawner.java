package me.jwhz.core.skyblock.upgrade.upgrades;

import me.jwhz.core.Core;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.upgrade.Upgrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

@Upgrade.Info(
        name = "Spawner Booster",
        costs = {150000, 1500000}
)
public class FasterSpawner extends Upgrade {

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

        ItemStack item = new ItemStack(Material.MOB_SPAWNER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Faster Spawners");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(ChatColor.GRAY + "Increases mob spawners");
        lore.add(ChatColor.GRAY + "spawn speed.");
        lore.add("");
        if (island.upgrades.getTier(new FasterSpawner()) == 3) {

            lore.add(ChatColor.GRAY + "Current Tier: " + ChatColor.BLUE + "Tier 3 " + ChatColor.GRAY + "(2x faster)");
            lore.add("");
            lore.add(ChatColor.GREEN + "Maxed out.");

        } else {

            lore.add(ChatColor.GRAY + "Cost: " + (hasEnoughMoney(player, island) ? ChatColor.GREEN : ChatColor.RED) + "$" + df.format(getCost(player, island)));
            lore.add(ChatColor.GRAY + "Makes spawners spawn " + (island.upgrades.getTier(this) == 1 ? "1.5" : "2") + "x faster");
            lore.add("");
            lore.add(ChatColor.GRAY + "Current Tier: " + ChatColor.BLUE + "Tier " + island.upgrades.getTier(this) + " "
                    + ChatColor.GRAY + "(" + (island.upgrades.getTier(this) == 1 ? "1" : "1.5") + "x faster)");
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

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = island.min.x; x <= island.max.x; x++)
                    for (int z = island.min.y; z <= island.max.y; z++)
                        for (int y = 0; y < 256; y++) {

                            Block block = island.spawn.getWorld().getBlockAt(x, y, z);

                            if (block != null && block.getType() == Material.MOB_SPAWNER)
                                apply(island, block);

                        }
            }
        }.runTaskLater(Core.getInstance(), 1);

    }

    public void apply(Island island, Block block){

        double multiplier = island.upgrades.getTier(this) == 2 ? 0.75 : 0.5;

        ((CreatureSpawner) block.getState()).setDelay((int) (((CreatureSpawner)block.getState()).getDelay() * multiplier));

    }

}
