package me.jwhz.core.tpqueue;

import me.jwhz.core.Core;
import me.jwhz.core.listener.EventClass;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class TPQueue extends EventClass {

    private Queue<QueueItem> queue = new ArrayDeque<>();

    public void add(QueueItem item, String message) {

        queue.add(item);

        new BukkitRunnable() {

            @Override
            public void run() {

                if (item.isCancelled)
                    return;

                queue.remove(item);

                item.player.teleport(item.location);
                item.player.sendMessage(message);
                item.player = null;
                item.location = null;

            }

        }.runTaskLater(Core.getInstance(), 60);

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (queue.size() > 0) {

            if (e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockZ() != e.getFrom().getBlockZ() || e.getTo().getBlockY() != e.getFrom().getBlockY()) {

                Iterator<QueueItem> iterator = queue.iterator();

                while (iterator.hasNext()) {

                    QueueItem item = iterator.next();

                    if (item.player.equals(e.getPlayer())) {
                        item.player.sendMessage(ChatColor.RED + "You move therefore teleportation has been cancelled.");
                        item.isCancelled = true;
                        item.player = null;
                        item.location = null;
                        iterator.remove();
                    }

                }

            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        if (queue.size() > 0) {

            Iterator<QueueItem> iterator = queue.iterator();

            while (iterator.hasNext()) {

                QueueItem item = iterator.next();

                if (item.player.equals(e.getPlayer())) {
                    item.isCancelled = true;
                    item.player = null;
                    item.location = null;
                    iterator.remove();
                }

            }

        }
    }

    public static class QueueItem {

        public Player player;
        public Location location;
        public boolean isCancelled = false;

        public QueueItem(Player player, Location location) {

            this.player = player;
            this.location = location;

        }

    }

}
