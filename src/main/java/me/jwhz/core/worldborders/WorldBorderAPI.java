package me.jwhz.core.worldborders;

import me.jwhz.core.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class WorldBorderAPI {

    private static WorldBorderAPI inst = new WorldBorderAPI();

    public static WorldBorderAPI inst() {
        return inst;
    }

    public void setBorder(Player player, double radius, Location location) {

        try {
            Object border = ReflectionUtils.getNMSClass("WorldBorder").newInstance();

            if(ReflectionUtils.VERSION.contains("1_12")){

                Object handler = ReflectionUtils.getHandle(player);

                border.getClass().getField("world").set(border, handler.getClass().getField("world").get(handler));

            }

            border.getClass().getDeclaredMethod("setCenter", double.class, double.class).invoke(border, location.getX(), location.getY());
            border.getClass().getDeclaredMethod("setSize", double.class).invoke(border, radius);

            Constructor constructor = ReflectionUtils.getNMSClass("PacketPlayOutWorldBorder").getConstructor(
                    ReflectionUtils.getNMSClass("WorldBorder"),
                    ReflectionUtils.getNMSClass("PacketPlayOutWorldBorder$EnumWorldBorderAction")
            );

            ReflectionUtils.sendPackets(player, constructor.newInstance(border,
                    ReflectionUtils.getNMSClass("PacketPlayOutWorldBorder$EnumWorldBorderAction").getEnumConstants()[0]));
            ReflectionUtils.sendPackets(player, constructor.newInstance(border,
                    ReflectionUtils.getNMSClass("PacketPlayOutWorldBorder$EnumWorldBorderAction").getEnumConstants()[2]));


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
