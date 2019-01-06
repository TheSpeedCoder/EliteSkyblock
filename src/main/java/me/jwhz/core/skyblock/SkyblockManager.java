package me.jwhz.core.skyblock;

import me.jwhz.core.worldborders.WorldBorderAPI;
import me.jwhz.core.Core;
import me.jwhz.core.manager.Manager;
import me.jwhz.core.skyblock.islands.EmptyGenerator;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.schematics.Schematic;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static me.jwhz.core.Core.pl;

public class SkyblockManager extends Manager<Island> implements ISkyblockManager {

    public File baseFolder = new File(Core.getInstance().getDataFolder() + "/islands");
    public World world = Bukkit.getWorld("skyblock");

    public SkyblockManager() {

        super("islands");

        if (!getYamlConfiguration().isSet("island count")) {
            getYamlConfiguration().set("island count", 0);

            try {
                getYamlConfiguration().save(getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (world == null)
            world = Bukkit.createWorld(new WorldCreator("skyblock").generator(new EmptyGenerator()));

        if (!baseFolder.exists())
            baseFolder.mkdirs();

        if (baseFolder.listFiles() != null)
            for (File file : baseFolder.listFiles()) {

                list.add((Island) YamlConfiguration.loadConfiguration(file).get("Info"));

            }
    }

    public void upIslandCount() {

        getYamlConfiguration().set("island count", getIslandCount() + 1);

        try {
            getYamlConfiguration().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public int getIslandCount() {

        return getYamlConfiguration().getInt("island count", 0);

    }

    public void sendWorldBorder(Player player) {

        if (isWithinAnIsland(player)) {

            for (Island island : list) {

                if (island.isWithin(player.getLocation())) {

                    int radius = island.max.x - island.min.x;

                    WorldBorderAPI.inst().setBorder(player, radius, island.spawn);
                    break;

                }

            }

        }

    }

    @Override
    public boolean hasIsland(UUID uuid) {

        if (list.size() > 0)
            for (Island island : list)
                if (island.owner.equals(uuid) || island.coop.contains(uuid.toString()))
                    return true;

        return false;

    }

    @Override
    public Island getIsland(UUID uuid) {

        if (list.size() > 0)
            for (Island island : list)
                if (island.owner.equals(uuid) || island.coop.contains(uuid.toString()))
                    return island;

        return null;
    }

    @Override
    public Island getIsland(Location location) {

        return getIsland(UUID.fromString(location.getWorld().getName().replace("-island", "")));

    }

    public Island getInteractIsland(Location location) {

        for (Island island : list)
            if (island.isWithin(location))
                return island;

        return null;

    }

    @Override
    public boolean isWithinAnIsland(Player player) {

        for (Island island : list) {

            if (island.isWithin(player.getLocation()))
                return true;

        }

        return false;

    }

    @Override
    public boolean isWithinAnIsland(Location location) {

        for (Island island : list)
            if (island.isWithin(location))
                return true;

        return false;

    }

    @Override
    public boolean isOwner(UUID uuid) {

        return hasIsland(uuid) && getIsland(uuid).owner.equals(uuid);

    }

    @Override
    public Island createIsland(Player player, Schematic schematic) {

        Island island = new Island(player, new Location(world, getIslandCount() * 500, 80, 0), schematic);

        upIslandCount();

        list.add(island);

        core.levelSystem.levels.put(island, 0);

        return island;

    }

    @Override
    public void restartIsland(Player player, Schematic schematic) {

        new BukkitRunnable() {
            @Override
            public void run() {

                Island island = getIsland(player.getUniqueId());

                core.levelSystem.levels.remove(island);
                core.levelSystem.cooldowns.remove(island);

                core.levelSystem.getYamlConfiguration().set(island.getIdentifier(), null);
                try {
                    core.levelSystem.getYamlConfiguration().save(core.levelSystem.getFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Location loc = (Location) core.commandManager.getYamlConfiguration().get("Spawn Location");

                for (Player p : Bukkit.getOnlinePlayers())
                    if (island.isWithin(p.getLocation()))
                        p.teleport(loc);

                for (int x = island.min.x; x <= island.max.x; x++)
                    for (int z = island.min.y; z <= island.max.y; z++)
                        for (int y = 0; y < 256; y++)
                            island.spawn.getWorld().getBlockAt(x, y, z).setType(Material.AIR);

                list.remove(island);

                island = new Island(player, new Location(world, island.spawn.getX() - 100, 80, 0), schematic);

                list.add(island);

                core.levelSystem.levels.put(island, 0);

            }
        }.runTaskLater(Core.getInstance(), 1);

    }

    public void deleteIsland(OfflinePlayer offlinePlayer) {

        Island island = getIsland(offlinePlayer.getUniqueId());

        core.levelSystem.levels.remove(island);
        core.levelSystem.cooldowns.remove(island);
        core.levelSystem.removeTopIsland(island);

        core.levelSystem.getYamlConfiguration().set(island.getIdentifier(), null);
        try {
            core.levelSystem.getYamlConfiguration().save(core.levelSystem.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        getList().remove(island);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = island.min.x; x <= island.max.x; x++)
                    for (int z = island.min.y; z <= island.max.y; z++)
                        for (int y = 0; y < 256; y++)
                            island.spawn.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (island.isWithin(p.getLocation())) {
                        try {
                            p.teleport(new Location(
                                    Bukkit.getWorld(pl.getConfig().getString("Island.delete.spawn_location.world")),
                                    pl.getConfig().getInt("Island.delete.spawn_location.x"),
                                    pl.getConfig().getInt("Island.delete.spawn_location.y"),
                                    pl.getConfig().getInt("Island.delete.spawn_location.z")));
                        } catch (IllegalArgumentException ex) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.isOp()) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            "&c&l(!) &c&lERROR: &CNO SPAWN LOCATION SET! Please set it in the config.yml"));
                                    p.teleport(new Location(Bukkit.getWorld("world"), 1, 50, 1));
                                }
                            }
                        }
                    }
                }
                new File(baseFolder, island.getIdentifier() + ".yml").delete();

            }
        }.runTaskLater(Core.getInstance(), 1);


    }

    @Override
    public void deleteIsland(Player player) {

        deleteIsland((OfflinePlayer) player);

    }
}
