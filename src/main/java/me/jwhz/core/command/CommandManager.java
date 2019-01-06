package me.jwhz.core.command;

import me.jwhz.core.Core;
import me.jwhz.core.command.commands.*;
import me.jwhz.core.config.ConfigHandler;
import me.jwhz.core.config.ConfigValue;
import me.jwhz.core.manager.Manager;

public class CommandManager extends Manager<CommandBase> {

    @ConfigValue(path = "Command.no permission")
    public String noPermission = "&cYou do not have permission to use this command!";

    public CommandManager() {

        super("messages");

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

        add(new IslandCMD());

    }

    @Override
    public void add(Object obj) {

        CommandBase command = (CommandBase) obj;

        Core.getInstance().getCommand(command.getAnnotationInfo().command()).setExecutor(command);
        Core.getInstance().getCommand(command.getAnnotationInfo().command()).setPermission(command.getAnnotationInfo().permission());
        Core.getInstance().getCommand(command.getAnnotationInfo().command()).setPermissionMessage(noPermission);

        ConfigHandler.setPresets(command, getFile());
        ConfigHandler.reload(command, getFile());

        list.add(command);

    }
}
