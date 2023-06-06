package com.xauth.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIUtils {

    private static String loginTitle;
    private static String registerTitle;

    public static void loadTitles(FileConfiguration config) {
        loginTitle = config.getString("LoginTitle");
        registerTitle = config.getString("RegisterTitle");
    }

    public static String getLoginTitle() {
        return loginTitle;
    }

    public static String getRegisterTitle() {
        return registerTitle;
    }

    public ItemStack createFillItem() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ChatColor.RESET + "");
        item.setItemMeta(itemMeta);
        return item;
    }
}
