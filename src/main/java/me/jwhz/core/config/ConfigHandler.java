package me.jwhz.core.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class ConfigHandler {

    public static void reload(Object clazz, File file) {

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for (Field f : clazz.getClass().getDeclaredFields()) {

            f.setAccessible(true);

            if (f.isAnnotationPresent(ConfigValue.class)) {

                ConfigValue configAnnotation = f.getAnnotation(ConfigValue.class);

                if (!configuration.isSet(configAnnotation.path()))
                    continue;

                try {

                    Object value = configuration.get(configAnnotation.path());

                    if (value instanceof String)
                        f.set(clazz, ChatColor.translateAlternateColorCodes('&', String.valueOf(value)));
                    else
                        f.set(clazz, value);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public static void setPresets(Object clazz, File file) {

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        for (Field f : clazz.getClass().getDeclaredFields()) {

            f.setAccessible(true);

            if (f.isAnnotationPresent(ConfigValue.class)) {

                ConfigValue configAnnotation = f.getAnnotation(ConfigValue.class);

                try {
                    Object value = f.get(clazz);

                    if (!configuration.isSet(configAnnotation.path()))
                        configuration.set(configAnnotation.path(), value);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void reload(ConfigFile configFile) {

        reload(configFile, configFile.getFile());

    }

    public static void setPresets(ConfigFile configFile) {

        setPresets(configFile, configFile.getFile());

    }

}