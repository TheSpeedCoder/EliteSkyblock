package me.jwhz.core.skyblock.upgrade;

import me.jwhz.core.skyblock.islands.Island;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("Upgrades")
public class Upgrades implements ConfigurationSerializable {

    public HashMap<String, Integer> upgrades = new HashMap<>();

    public boolean hasPurchased(Upgrade upgrade) {

        return upgrades.containsKey(upgrade.getAnnotionInfo().name());

    }

    public boolean canUpgrade(Upgrade upgrade) {


        return !hasPurchased(upgrade) || getTier(upgrade) - 1 < upgrade.getAnnotionInfo().numberOfTiers();

    }

    public int getTier(Upgrade upgrade) {

        if (upgrade.getAnnotionInfo().isTierBased() && !hasPurchased(upgrade))
            return 1;
        else if (upgrade.getAnnotionInfo().isTierBased() && hasPurchased(upgrade))
            return upgrades.get(upgrade.getAnnotionInfo().name()) + 1;


        return upgrades.get(upgrade.getAnnotionInfo().name());

    }

    public void purchase(Island island, Upgrade upgrade){

        if(hasPurchased(upgrade)){

            int tier = upgrades.get(upgrade.getAnnotionInfo().name());

            upgrades.remove(upgrade.getAnnotionInfo().name());
            upgrades.put(upgrade.getAnnotionInfo().name(), tier + 1);

        }else
            upgrades.put(upgrade.getAnnotionInfo().name(), 1);

        island.save();

    }

    @Override
    public Map<String, Object> serialize() {

        return (Map<String, Object>) (Map<?, ?>) upgrades;

    }

    public static Upgrades deserialize(Map items) {

        Upgrades upgrades = new Upgrades();

        upgrades.upgrades = (HashMap<String, Integer>) items;

        return upgrades;

    }

}
