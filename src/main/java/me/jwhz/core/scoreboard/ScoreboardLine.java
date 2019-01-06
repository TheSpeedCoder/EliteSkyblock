package me.jwhz.core.scoreboard;

import me.jwhz.core.Core;
import me.jwhz.core.events.ScoreboardVariableReplaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreboardLine {

    private Core core = Core.getInstance();
    private YamlConfiguration config;
    private Scoreboard scoreboard;

    private int lineIndex;
    private int index = 0;
    private int lineSpot = 0;
    private int animationDelay = 0;

    private boolean title = false;
    private boolean animation = false;
    private List<String> lines = null;
    private long lastUpdate = 0;

    private String path;
    public String entryName;

    public ScoreboardLine(Scoreboard scoreboard, String path, int index) {

        this.path = path;
        this.scoreboard = scoreboard;
        this.lineIndex = index;

        if (path.equalsIgnoreCase("Scoreboard.title")) {
            this.entryName = "skip";
            title = true;
        } else
            this.entryName = path.replace(".", ",").split(",")[2];

        config = core.scoreboardManager.yamlConfiguration;

        animation = config.getBoolean(path + ".animation enabled", false);

        ArrayList<String> defaultLines = new ArrayList<String>();
        defaultLines.addAll(Arrays.asList("Edit in config"));

        lines = (List<String>) config.getList(path + ".animation", defaultLines);
        animationDelay = config.getInt(path + ".update delay", 20);

        if (!title) {
            lineSpot = config.getInt(path + ".line spot", 0);

            register();

        }

        update();

    }

    public boolean canUpdate() {

        if (lastUpdate == 0) {
            lastUpdate = System.currentTimeMillis();
            return true;
        } else if (lastUpdate + animationDelay * 50 <= System.currentTimeMillis()) {
            lastUpdate = System.currentTimeMillis();
            return true;
        }

        return false;

    }

    public void update() {

        if (animation)
            index = (index == lines.size() - 1 ? 0 : index + 1);

        if (title) {

            String line = lines.get(index);

            ScoreboardVariableReplaceEvent svr = new ScoreboardVariableReplaceEvent(line, scoreboard.uuid, true);
            Bukkit.getPluginManager().callEvent(svr);

            scoreboard.objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', svr.getLine()));

        } else {

            Team team = scoreboard.scoreboard.getTeam(entryName);

            String line = lines.get(index);

            ScoreboardVariableReplaceEvent svr = new ScoreboardVariableReplaceEvent(line, scoreboard.uuid, false);
            Bukkit.getPluginManager().callEvent(svr);

            line = svr.getLine();

            String prefix = ChatColor.translateAlternateColorCodes('&', line.length() > 16 ? line.substring(0, 16) : line);

            String suffix = "";
            String extra = ChatColor.getLastColors(prefix);
            if (line.length() > 16)
                suffix = extra + line.substring(16, line.length() + extra.length() > 32 ? 32 - extra.length() : line.length());

            team.setPrefix(prefix);
            team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));

        }


    }

    public void register() {

        Team team = scoreboard.scoreboard.registerNewTeam(entryName);
        team.addEntry(ChatColor.values()[lineIndex].toString());

        String line = lines.get(index);

        String prefix = line.length() > 16 ? line.substring(0, 16) : line;
        String suffix = null;
        if (line.length() > 16)
            suffix = ChatColor.getLastColors(prefix) + line.substring(16, line.length() > 32 ? 32 : line.length());

        team.setPrefix(prefix);
        if (suffix != null)
            team.setSuffix(suffix);

        scoreboard.objective.getScore(ChatColor.values()[lineIndex].toString()).setScore(lineSpot);
        update();

    }

    public void unregister(Team team) {
        if (team == null)
            return;
        scoreboard.objective.getScoreboard().resetScores(ChatColor.values()[lineIndex].toString());
        team.unregister();

    }


}
