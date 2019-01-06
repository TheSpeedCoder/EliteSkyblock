package me.jwhz.core.listener.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jwhz.core.events.ScoreboardVariableReplaceEvent;
import me.jwhz.core.listener.EventClass;
import me.jwhz.core.skyblock.islands.Island;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class ScoreboardListener extends EventClass {

    @EventHandler
    public void onScoreboardUpdate(ScoreboardVariableReplaceEvent e) {

        Player player = Bukkit.getPlayer(e.getUUID());
        Island island = core.skyblockManager.getIsland(player.getUniqueId());

        if (e.getLine().contains("{island_level}"))
            e.setLine(e.getLine().replace("{island_level}", (island == null ? "0" : core.levelSystem.levels.get(island) + "")));

        if (e.getLine().contains("{island_rank}"))
            e.setLine(e.getLine().replace("{island_rank}",
                    core.levelSystem.contains(island) ?
                            core.levelSystem.getSoftLevel(island) + "" :
                            "Ranking..."
                    )
            );

        if (e.getLine().contains("{island_online}")) {

            if (island == null)
                e.setLine(e.getLine().replace("{island_online}", "N"));
            else {

                int online = Bukkit.getPlayer(island.owner) != null ? 1 : 0;

                for (String id : island.coop)
                    online += Bukkit.getPlayer(UUID.fromString(id)) != null ? 1 : 0;

                e.setLine(e.getLine().replace("{island_online}", online + ""));
            }

        }

        if (e.getLine().contains("{island_coop_size}"))
            e.setLine(e.getLine().replace("{island_coop_size}", (island == null ? "A" : island.coop.size() + 1) + ""));

        if (core.usingPlaceholderAPI)
            e.setLine(PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(e.getUUID()), e.getLine()));


    }

}
