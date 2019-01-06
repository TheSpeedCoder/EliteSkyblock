package me.jwhz.core.listener;

import me.jwhz.core.manager.ManagerObject;
import org.bukkit.event.Listener;

public class EventClass extends ManagerObject<String> implements Listener {

    public String getIdentifier() {

        return getClass().getName();

    }
}
