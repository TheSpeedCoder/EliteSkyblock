package me.jwhz.core.skyblock.schematics;

import me.jwhz.core.Core;
import me.jwhz.core.manager.Manager;

import java.io.File;
import java.util.ArrayList;

public class SchematicsManager extends Manager<Schematic> {

    public File dataFolder = new File(Core.getInstance().getDataFolder() + "/island schematics/");

    public ArrayList<Schematic> schematics = new ArrayList<>();

    public SchematicsManager() {

        super("islands");

        if(!dataFolder.exists())
            dataFolder.mkdirs();

        if (dataFolder.listFiles() != null)
            for (File file : dataFolder.listFiles())
                if (file.getName().endsWith(".schematic"))
                    schematics.add(new Schematic(file));


    }

}
