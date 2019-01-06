package me.jwhz.core.scoreboard;

import me.jwhz.core.manager.Manager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;

public class ScoreboardManager extends Manager<Scoreboard> implements Listener {

    public ScoreboardManager() {

        super("scoreboard");

        if (!getYamlConfiguration().isSet("Scoreboard.title")) {
            core.saveResource("scoreboard.yml", true);

            yamlConfiguration = YamlConfiguration.loadConfiguration(new File(core.getDataFolder(), "scoreboard.yml"));

        }

        if(!getYamlConfiguration().getBoolean("scoreboard-enabled")){

            return;

        }

        core.getServer().getPluginManager().registerEvents(this, core);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        list.add(new Scoreboard(e.getPlayer()));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        remove(e.getPlayer().getUniqueId().toString());

    }

}
