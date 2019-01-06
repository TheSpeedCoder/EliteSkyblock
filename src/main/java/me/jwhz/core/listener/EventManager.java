package me.jwhz.core.listener;

import me.jwhz.core.config.ConfigHandler;
import me.jwhz.core.listener.listeners.Chat;
import me.jwhz.core.listener.listeners.ScoreboardListener;
import me.jwhz.core.listener.listeners.SkyblockProtection;
import me.jwhz.core.manager.Manager;
import org.bukkit.event.Listener;

public class EventManager extends Manager<EventClass> {

    public EventManager() {

        super("config");
        add(new Chat());
        add(new SkyblockProtection());
        add(core.tpQueue);
        add(new ScoreboardListener());

    }

    @Override
    public void add(Object listener) {

        core.getServer().getPluginManager().registerEvents((Listener) listener, core);

        ConfigHandler.setPresets(listener, getFile());
        ConfigHandler.reload(listener, getFile());

    }
}
