package me.jwhz.core.skyblock.schematics;

import me.jwhz.core.Core;
import me.jwhz.core.manager.ManagerObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Schematic extends ManagerObject<String> {

    public File file;

    public Schematic(File file) {

        this.file = file;

    }

    public List<String> getLore() {

        if(!core.schematicsManager.yamlConfiguration.isSet("Islands." + getName() + ".island lore")){

            core.schematicsManager.yamlConfiguration.set("Islands." + getName() + ".island lore", Arrays.asList("&7Edit in", "&cConfig"));
            try {
                core.schematicsManager.yamlConfiguration.save(core.schematicsManager.file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return (List<String>) Core.getInstance().schematicsManager.yamlConfiguration.getList("Islands." + getName() + ".island lore");

    }

    public String getName() {

        return getIdentifier();

    }

    @Override
    public String getIdentifier() {

        return file.getName().split("\\.")[0];

    }
}
