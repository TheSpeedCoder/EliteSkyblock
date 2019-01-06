package me.jwhz.core.skyblock.islands;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import me.jwhz.core.Core;
import me.jwhz.core.manager.ManagerObject;
import me.jwhz.core.skyblock.schematics.Schematic;
import me.jwhz.core.skyblock.upgrade.Upgrades;
import me.jwhz.core.skyblock.upgrade.upgrades.ExpandWorldBorder;
import me.jwhz.core.skyblock.upgrade.upgrades.FasterSpawner;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

@SerializableAs("Island")
public class Island extends ManagerObject<String> implements ConfigurationSerializable {

    public UUID owner;
    public List<String> coop = new ArrayList<String>();
    public List<String> banned = new ArrayList<String>();
    public Location spawn = null;
    public String chosenSchematic;
    public Point min, max;
    public Settings settings;
    public Upgrades upgrades;

    public Island(Player player, Location location, Schematic schematic) {

        min = new Point(location.getBlockX(), location.getBlockZ());
        max = new Point(location.getBlockX() + 150, location.getBlockZ() + 150);

        owner = player.getUniqueId();

        settings = new Settings();

        upgrades = new Upgrades();

        chosenSchematic = schematic.getName();

        spawn = new Location(location.getWorld(), location.getBlockX() + 75, 80, location.getBlockZ() + 75);

        EditSession es = WorldEdit.getInstance().getEditSessionFactory()
                .getEditSession(new BukkitWorld(spawn.getWorld()), Integer.MAX_VALUE);
        SchematicFormat format = SchematicFormat.getFormat(schematic.file);
        CuboidClipboard cc = null;
        try {
            cc = format.load(schematic.file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataException e) {
            e.printStackTrace();
        }

        try {
            cc.paste(es, new Vector(spawn.getX(), spawn.getY(), spawn.getZ()), false);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }

        save();

    }

    public Island() {
    }

    public void save() {

        File file = new File(Core.getInstance().skyblockManager.baseFolder, owner.toString() + ".yml");

        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.set("Info", this);

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isWithin(Location location) {

        return location.getBlockX() >= min.x && location.getBlockX() <= max.x &&
                location.getBlockZ() >= min.y && location.getBlockZ() <= max.y &&
                location.getWorld().getName().equalsIgnoreCase(spawn.getWorld().getName());

    }

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> items = new HashMap<>();

        items.put("Owner", owner.toString());
        items.put("Coop", coop);
        items.put("Banned", banned);
        items.put("Spawn", spawn.serialize());
        items.put("Schematic", chosenSchematic);
        items.put("Upgrades", upgrades.serialize());
        items.put("Settings", settings.serialize());

        return items;
    }

    public static Island deserialize(Map items) {

        Island island = new Island();

        island.owner = UUID.fromString((String) items.get("Owner"));
        island.coop = (List<String>) items.get("Coop");
        island.banned = (List<String>) items.get("Banned");
        island.spawn = Location.deserialize((Map) items.get("Spawn"));
        island.chosenSchematic = (String) items.get("Schematic");
        island.upgrades = Upgrades.deserialize((Map) items.get("Upgrades"));
        island.settings = Settings.deserialize((Map) items.get("Settings"));

        int expansion = (island.upgrades.getTier(new ExpandWorldBorder()) - 1) * 25;

        island.min = new Point(island.spawn.getBlockX() - 75 - expansion, island.spawn.getBlockZ() - 75 - expansion);
        island.max = new Point(island.spawn.getBlockX() + 75 + expansion, island.spawn.getBlockZ() + 75 + expansion);

        if (island.coop == null)
            island.coop = new ArrayList<>();

        return island;

    }

    @Override
    public String getIdentifier() {

        return owner.toString();

    }
}
