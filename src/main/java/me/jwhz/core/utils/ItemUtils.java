package me.jwhz.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;

public class ItemUtils {

    public static boolean addToInventory(Player player, ItemStack[] items, boolean armor) {

        if (!armor) {

            boolean droppedSome = false;

            for (ItemStack item : items) {
                if(item == null)
                    continue;
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItem(player.getLocation().add(0, 0.5, 0), item);
                    droppedSome = true;
                } else
                    player.getInventory().addItem(item);
            }
            return droppedSome;

        } else {

            ItemStack[] notAdded = new ItemStack[items.length];
            int index = 0;

            for (ItemStack item : items) {

                if (item == null)
                    continue;

                Material type = item.getType();

                if (type.toString().toLowerCase().contains("helmet") && player.getInventory().getHelmet() == null) {

                    player.getInventory().setHelmet(item);
                    continue;
                } else if (type.toString().toLowerCase().contains("boots") && player.getInventory().getBoots() == null) {

                    player.getInventory().setBoots(item);
                    continue;
                } else if (type.toString().toLowerCase().contains("chestplate") && player.getInventory().getChestplate() == null) {

                    player.getInventory().setChestplate(item);
                    continue;
                } else if (type.toString().toLowerCase().contains("leggings") && player.getInventory().getLeggings() == null) {

                    player.getInventory().setLeggings(item);
                    continue;
                } else {

                    notAdded[index] = item;
                    index++;

                }

                if (index > 0)
                    return addToInventory(player, notAdded, false);
                else
                    return false;

            }

        }
        return false;
    }

    public static String readItemStack(ItemStack itemStack) {

        StringBuilder builder = new StringBuilder();
        ItemMeta meta = itemStack.getItemMeta();

        builder.append("item:" + itemStack.getType().toString() + " ");
        builder.append("amount:" + itemStack.getAmount() + " ");
        if (meta.getDisplayName() != null && meta.getDisplayName().length() > 0)
            builder.append("display:" + meta.getDisplayName().replace('\u00a7', '&').replace(" ", "__") + " ");
        builder.append("data:" + (int) itemStack.getData().getData() + " ");

        if (meta.getLore() != null && meta.getLore().size() > 0)
            for (String lore : meta.getLore())
                builder.append("lore:" + lore.replace('\u00a7', '&').replace(" ", "__") + " ");

        if (itemStack.getEnchantments() != null && itemStack.getEnchantments().size() > 0)
            for (Enchantment ench : itemStack.getEnchantments().keySet())
                builder.append("enchant:" + ench.getName() + ":" + itemStack.getEnchantments().get(ench) + " ");

        return builder.toString().substring(0, builder.length() - 1);

    }

    public static ItemStack getMaterial(String string) {

        String[] args = string.split(":");

        Material material = Material.AIR;
        byte data = 0x0;

        if (args.length < 2)
            try {
                int id = Integer.parseInt(args[0]);
                material = Material.getMaterial(id);
            } catch (Exception e) {
                material = Material.valueOf(args[0]);
            }

        if (args.length >= 2 && args[1] != null)
            data = (byte) Integer.parseInt(args[1]);

        return new ItemStack(material, 1, data);


    }

    public static ItemStack readString(String string) {

        ItemStack item;

        Material material = null;
        int data = 0;

        if (string != null) {
            if (string.contains(" ")) {
                for (String s : string.split(" ")) {
                    if (s.contains("item:")) {
                        try {

                            int id = Integer.parseInt(s.split(":")[1]);
                            material = Material.getMaterial(id);

                        } catch (Exception e) {
                            material = Material.valueOf(s.split(":")[1]);
                        }
                    }
                    if (s.contains("data:"))
                        data = Integer.parseInt(s.split(":")[1]);
                }

                item = new ItemStack(material, 1, (short) data);

                ItemMeta meta = item.getItemMeta();

                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                int r = -1, g = -1, b = -1;

                ArrayList<String> lore = new ArrayList<String>();

                for (String s : string.split(" ")) {

                    if (s.contains("display:")) {
                        meta.setDisplayName(color(s.replace("display:", "").replace("__", " ")));
                        continue;
                    }

                    if (s.contains("amount:")) {
                        item.setAmount(Integer.parseInt(s.split(":")[1]));
                        continue;

                    }

                    if (s.contains("r:")) {
                        r = Integer.parseInt(s.split(":")[1]);
                        continue;
                    }

                    if (s.contains("g:")) {
                        g = Integer.parseInt(s.split(":")[1]);
                        continue;
                    }

                    if (s.contains("b:")) {
                        b = Integer.parseInt(s.split(":")[1]);
                        continue;
                    }

                    if (s.contains("lore:")) {
                        lore.add(color(s.replace("lore:", "").replace("__", " ")));
                        continue;
                    }

                    if (s.contains("enchant:")) {

                        meta.addEnchant(Enchantment.getByName(s.split(":")[1]), Integer.parseInt(s.split(":")[2]), true);

                    }


                }

                meta.setLore(lore);
                meta.spigot().setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

                if (r != -1 || g != -1 || b != -1) {

                    LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                    armorMeta.setColor(Color.fromRGB(

                            Math.max(0, r),
                            Math.max(0, g),
                            Math.max(0, b)

                    ));

                    item.setItemMeta(armorMeta);

                } else

                    item.setItemMeta(meta);
                return item;
            } else if (string.startsWith("item:")) {

                return getMaterial(string.substring(5));

            }
        }

        return null;
    }

    private static String color(String string) {

        return ChatColor.translateAlternateColorCodes('&', string);

    }


}
