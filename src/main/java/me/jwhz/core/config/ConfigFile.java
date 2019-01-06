package me.jwhz.core.config;

import me.jwhz.core.Core;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigFile {

    public String fileName;
    public File file;
    public YamlConfiguration yamlConfiguration;

    public ConfigFile(String fileName) {

        this.fileName = fileName;

        if(!Core.getInstance().getDataFolder().exists())
            Core.getInstance().getDataFolder().mkdir();

        file = new File(Core.getInstance().getDataFolder() + "/" + fileName + ".yml");

        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);

    }

    public File getFile() {

        return file;

    }

    public YamlConfiguration getYamlConfiguration() {

        return yamlConfiguration != null ? yamlConfiguration : YamlConfiguration.loadConfiguration(file);

    }


}
