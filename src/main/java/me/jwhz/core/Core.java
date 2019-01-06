package me.jwhz.core;

import me.jwhz.core.command.CommandManager;
import me.jwhz.core.listener.EventManager;
import me.jwhz.core.scoreboard.Scoreboard;
import me.jwhz.core.scoreboard.ScoreboardManager;
import me.jwhz.core.skyblock.SkyblockManager;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.leveling.LevelSystem;
import me.jwhz.core.skyblock.schematics.SchematicsManager;
import me.jwhz.core.tpqueue.TPQueue;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Core extends JavaPlugin {

    private static Core instance;
    public static Core pl;
    public static Economy economy;
    public static Chat chat;

    public EventManager eventManager;
    public CommandManager commandManager;
    public SkyblockManager skyblockManager;
    public SchematicsManager schematicsManager;
    public LevelSystem levelSystem;
    public TPQueue tpQueue;
    public ScoreboardManager scoreboardManager;

    public boolean usingPlaceholderAPI;
    @Override
    public void onEnable() {

        instance = this;
        pl = this;

        saveDefaultConfig();

        if(!setupEconomy() || !setupChat()){

            System.out.println("No proper economy or chat/permission plugin setup! Disabling plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return;

        }

        usingPlaceholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        ConfigurationSerialization.registerClass(Island.class, "Island");

        tpQueue = new TPQueue();

        eventManager = new EventManager();

        commandManager = new CommandManager();

        skyblockManager = new SkyblockManager();

        schematicsManager = new SchematicsManager();

        levelSystem = new LevelSystem();

        scoreboardManager = new ScoreboardManager();

        if (scoreboardManager.getYamlConfiguration().getBoolean("scoreboard-enabled"))
            for (Player player : Bukkit.getOnlinePlayers())
                scoreboardManager.getList().add(new Scoreboard(player));

        new BukkitRunnable() {

            @Override
            public void run() {

                levelSystem.reloadTopTen();

            }

        }.runTaskTimer(this, 0, 20 * 60);

        new BukkitRunnable() {

            @Override
            public void run() {

                for (Scoreboard scoreboard : scoreboardManager.getList())
                    scoreboard.update();

            }

        }.runTaskTimer(this, 0, 1);

        new BukkitRunnable() {

            @Override
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers())
                    if (player.getWorld().getName().equalsIgnoreCase("skyblock"))
                        skyblockManager.sendWorldBorder(player);

            }

        }.runTaskTimer(this, 0, 20);


    }

    public static Core getInstance() {

        return instance;

    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if(rsp == null)
            return false;
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

}
