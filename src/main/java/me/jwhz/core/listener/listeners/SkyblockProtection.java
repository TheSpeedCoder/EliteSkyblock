package me.jwhz.core.listener.listeners;

import me.jwhz.core.listener.EventClass;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.islands.Settings;
import me.jwhz.core.skyblock.upgrade.upgrades.FasterSpawner;
import me.jwhz.core.skyblock.upgrade.upgrades.Jump;
import me.jwhz.core.skyblock.upgrade.upgrades.Speed;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.jwhz.core.Core.pl;

public class SkyblockProtection extends EventClass {

    private FasterSpawner fasterSpawner = new FasterSpawner();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {


        if (e.getPlayer().hasPermission("Skyblock.admin"))
            return;

        if (core.skyblockManager.isWithinAnIsland(e.getPlayer()) && core.skyblockManager.getInteractIsland(e.getPlayer().getLocation()) != core.skyblockManager.getIsland(e.getPlayer().getUniqueId()))
            if (e.getClickedBlock() == null)
                return;
            else
                e.setCancelled(true);


    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player && core.skyblockManager.isWithinAnIsland((Player) e.getEntity()) &&
                (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FALL ||
                        e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || e.getCause() == EntityDamageEvent.DamageCause.DROWNING ||
                        e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            e.setCancelled(true);

    }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Monster)
                && core.skyblockManager.isWithinAnIsland((Player) e.getEntity()) &&
                !core.skyblockManager.getIsland(e.getDamager().getUniqueId()).equals(core.skyblockManager.getInteractIsland(e.getDamager().getLocation())))
            e.setCancelled(true);

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if (e.getPlayer().hasPermission("Skyblock.admin"))
            return;

        if (core.skyblockManager.isWithinAnIsland(e.getPlayer()) && core.skyblockManager.getInteractIsland(e.getPlayer().getLocation()) != core.skyblockManager.getIsland(e.getPlayer().getUniqueId()))
            e.setCancelled(true);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        if (e.getBlockPlaced().getType() == Material.MOB_SPAWNER && core.skyblockManager.getInteractIsland(e.getBlockPlaced().getLocation()) != null &&
                core.skyblockManager.getInteractIsland(e.getBlockPlaced().getLocation()).upgrades.hasPurchased(fasterSpawner))
            fasterSpawner.apply(core.skyblockManager.getInteractIsland(e.getBlockPlaced().getLocation()), e.getBlockPlaced());

        if (e.getPlayer().hasPermission("Skyblock.admin"))
            return;

        if (core.skyblockManager.isWithinAnIsland(e.getPlayer()) && core.skyblockManager.getInteractIsland(e.getPlayer().getLocation()) != core.skyblockManager.getIsland(e.getPlayer().getUniqueId()))
            e.setCancelled(true);

    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent e) {

        if (e.getPlayer().hasPermission("Skyblock.admin"))
            return;

        if (core.skyblockManager.isWithinAnIsland(e.getPlayer()) &&
                core.skyblockManager.getInteractIsland(e.getPlayer().getLocation()) != core.skyblockManager.getIsland(e.getPlayer().getUniqueId()))
            e.setCancelled(true);

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (core.skyblockManager == null)
            return;

        if (core.skyblockManager.isWithinAnIsland(e.getFrom()) && !core.skyblockManager.isWithinAnIsland(e.getTo()))
            e.setCancelled(true);

        if (e.getTo().getY() <= 1) {

            if (core.skyblockManager.isWithinAnIsland(e.getTo())) {

                e.getPlayer().teleport(core.skyblockManager.getInteractIsland(e.getTo()).spawn);
                return;

            } else {
                Player p = e.getPlayer();
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

        if (core.skyblockManager.isWithinAnIsland(e.getTo()) && (e.getTo().getX() != e.getFrom().getX() ||
                e.getTo().getY() != e.getFrom().getY() || e.getTo().getZ() != e.getFrom().getZ())) {

            Island island = core.skyblockManager.getInteractIsland(e.getTo());

            if (island.upgrades.hasPurchased(new Speed()) && island.settings.getValue(Settings.Value.SPEED)) {

                e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3 * 20, 2));

            }

            if (island.upgrades.hasPurchased(new Jump()) && island.settings.getValue(Settings.Value.JUMP)) {

                e.getPlayer().removePotionEffect(PotionEffectType.JUMP);
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 3 * 20, 2));

            }

        }


    }

}
