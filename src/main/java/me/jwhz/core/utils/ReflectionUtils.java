package me.jwhz.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: Jake Honea
 * On: 8/12/2017
 */

public class ReflectionUtils {

    public static String VERSION;

    static {

        VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    }

    private static HashMap<String, Class> NMS_CACHE = new HashMap<String, Class>();
    private static HashMap<String, Class> CRAFTBUKKIT_CACHE = new HashMap<String, Class>();

    public static Class getNMSClass(String clazz) {

        if (NMS_CACHE.containsKey(clazz))
            return NMS_CACHE.get(clazz);

        try {

            Class foundClass = Class.forName(
                    "net.minecraft.server."
                            + VERSION
                            + "." + clazz
            );

            NMS_CACHE.put(clazz, foundClass);
            return foundClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Class getCraftbukkitClass(String clazz) {

        if (CRAFTBUKKIT_CACHE.containsKey(clazz))
            return CRAFTBUKKIT_CACHE.get(clazz);


        try {
            Class foundClass = Class.forName(
                    "org.bukkit.craftbukkit."
                            + VERSION
                            + "." + clazz
            );
            CRAFTBUKKIT_CACHE.put(clazz, foundClass);
            return foundClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Object getHandle(Player player) {

        try {
            return player.getClass().getMethod("getHandle").invoke(player);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void sendPackets(Player player, Object... packets) {

        if (packets == null)
            return;

        try {
            Object handler = getHandle(player);
            Object connection = handler.getClass().getField("playerConnection").get(handler);

            for (Object packet : packets) {

                connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setField(Object clazz, String fieldName, Object value) {

        try {
            Field field = clazz.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(clazz, value);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

