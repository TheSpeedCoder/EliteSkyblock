package me.jwhz.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Created by Jake on 6/21/2017.
 */
public class ScoreboardVariableReplaceEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private String line;
    private boolean hide;
    private boolean title;
    private UUID uuid;

    public ScoreboardVariableReplaceEvent(String line, UUID uuid, boolean title){

        this.line = line;
        this.hide = false;
        this.title = title;
        this.uuid = uuid;

    }

    public UUID getUUID(){

        return uuid;

    }

    public String getLine(){

        return line;

    }

    public void setLine(String line){

        this.line = line;

    }

    public boolean isTitle(){

        return title;

    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){

        return handlers;

    }

}
