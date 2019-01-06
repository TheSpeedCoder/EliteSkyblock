package me.jwhz.core.skyblock.upgrade;

import me.jwhz.core.skyblock.islands.Island;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;

public abstract class Upgrade {

    public DecimalFormat df = new DecimalFormat("###,###.##");

    public abstract double getCost(Player player, Island island);

    public abstract boolean hasEnoughMoney(Player player, Island island);

    public abstract ItemStack getItem(Player player, Island island);

    public Info getAnnotionInfo(){

        return getClass().getAnnotation(Info.class);

    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {

        String name();

        boolean isTierBased() default true;

        int numberOfTiers() default 2;

        double[] costs();

    }

}
