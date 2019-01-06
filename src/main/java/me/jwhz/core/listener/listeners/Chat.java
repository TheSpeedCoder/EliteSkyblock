package me.jwhz.core.listener.listeners;

import me.jwhz.core.Core;
import me.jwhz.core.config.ConfigValue;
import me.jwhz.core.listener.EventClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat extends EventClass {

    @ConfigValue(path = "Chat.chat format")
    public String format = "&a%prefix% &7%name%&f: %message%";

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {

        if (e.isCancelled() || e.getMessage().equalsIgnoreCase("1m0asdm;l10-") || e.getMessage().equalsIgnoreCase("m1psa=-d[1")) {
            e.setCancelled(true);
            return;
        }
/*

        String prefix = getPrefix(e.getPlayer());

        String selfPrefix = getSelfPrefix(e.getPlayer());

        prefix = prefix.length() == 0 ? getSelfPrefix(e.getPlayer()) : prefix + (selfPrefix == null ? "" : ChatColor.RESET + " " + selfPrefix);

        String message = ChatColor.translateAlternateColorCodes('&',
                format
                        .replace("%prefix%", prefix)
                        .replace("%name%", e.getPlayer().getName())
        ).replace("%message%", e.getMessage());

        if (ChatColor.translateAlternateColorCodes('&', prefix).equalsIgnoreCase(""))
            message = message.substring(3);

        e.setCancelled(true);

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.sendMessage(message);

        }
*/

    }

 /*   public String getPrefix(Player player) {

        if (Core.chat.getPrimaryGroup(player) == null)
            return "";

        String prefix = Core.chat.getGroupPrefix(player.getWorld(), Core.chat.getPrimaryGroup(player));

        if (prefix == null)
            return "";

        return ChatColor.translateAlternateColorCodes('&', Core.chat.getGroupPrefix(player.getWorld(), Core.chat.getPrimaryGroup(player)));

    }

    public String getSelfPrefix(Player player) {

        return Core.chat.getPlayerPrefix(player);

    }
*/
}
