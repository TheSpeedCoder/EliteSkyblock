package me.jwhz.core.skyblock;

import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.schematics.Schematic;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface ISkyblockManager {

    boolean hasIsland(UUID uuid);

    boolean isOwner(UUID uuid);

    boolean isWithinAnIsland(Player player);

    boolean isWithinAnIsland(Location location);

    Island getIsland(UUID uuid);

    Island getIsland(Location location);

    Island createIsland(Player player, Schematic schematic);

    void restartIsland(Player player, Schematic schematic);

    void deleteIsland(Player player);


}
