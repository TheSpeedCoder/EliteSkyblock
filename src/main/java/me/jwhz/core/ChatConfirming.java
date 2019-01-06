package me.jwhz.core;

import me.jwhz.core.gui.guis.skyblock.IslandSelectorGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class ChatConfirming implements Listener {

    private Listener listener = null;

    public ChatConfirming(Player player, Callbacks callbacks) {

        listener = new Listener() {

            @EventHandler
            public void onPlayerChat(AsyncPlayerChatEvent e) {

                if (e.getPlayer().equals(player)) {
                    if (e.getMessage().equalsIgnoreCase("1m0asdm;l10-")) {
                        callbacks.onSuccessConfirm();
                        HandlerList.unregisterAll(listener);
                        e.setCancelled(true);
                    } else if (e.getMessage().equalsIgnoreCase("m1psa=-d[1")) {
                        callbacks.onFailConfirm();
                        HandlerList.unregisterAll(listener);
                        e.setCancelled(true);
                    }

                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent e) {

                if (e.getPlayer().equals(player)) {
                    HandlerList.unregisterAll(listener);
                    callbacks.onCancelled();

                }
            }

        };

        Bukkit.getServer().getPluginManager().registerEvents(listener, Core.getInstance());

    }

    public interface Callbacks {

        void onSuccessConfirm();

        void onFailConfirm();

        void onCancelled();

    }

}
