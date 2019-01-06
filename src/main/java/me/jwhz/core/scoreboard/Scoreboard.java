package me.jwhz.core.scoreboard;

import me.jwhz.core.manager.ManagerObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.UUID;

public class Scoreboard extends ManagerObject<String> {

    public org.bukkit.scoreboard.Scoreboard scoreboard;
    public Objective objective;
    public YamlConfiguration config;
    public UUID uuid;
    public ScoreboardLine title;
    public ArrayList<ScoreboardLine> lines = new ArrayList<ScoreboardLine>();

    public Scoreboard(Player player) {

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("score", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.uuid = player.getUniqueId();

        title = new ScoreboardLine(this, "Scoreboard.title", -1);

        int index = 0;

        for (String key : core.scoreboardManager.getYamlConfiguration().getConfigurationSection("Scoreboard.lines").getKeys(false))
            lines.add(new ScoreboardLine(this, "Scoreboard.lines." + key, index++));

        update();

        player.setScoreboard(scoreboard);


    }

    public void update() {

        if (title.canUpdate())
            title.update();

        for (ScoreboardLine line : lines)
            if (line.canUpdate())
                line.update();

    }

    @Override
    public String getIdentifier() {

        return uuid.toString();

    }

}
