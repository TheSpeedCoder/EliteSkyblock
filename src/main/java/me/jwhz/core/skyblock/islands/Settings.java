package me.jwhz.core.skyblock.islands;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    private HashMap<String, Object> settings = new HashMap<>();

    public boolean getValue(Value value) {

        if (!settings.containsKey(value.name))
            return !value.equals(Value.PRIVATE);

        return (boolean) settings.get(value.name);

    }

    public void setValue(Value value, boolean bool, Island island) {

        settings.remove(value.name);
        settings.put(value.name, bool);

        island.save();

    }

    public static Settings deserialize(Map items) {

        Settings settings = new Settings();

        settings.settings = (HashMap<String, Object>) items;

        return settings;

    }

    public Map<String, Object> serialize() {

        return settings;

    }

    public enum Value {

        SPEED("speed"),
        PRIVATE("private"),
        JUMP("jump");

        private String name;

        Value(String name) {

            this.name = name;

        }

        @Override
        public String toString() {

            return name;

        }
    }

}
