package com.xauth.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class GUIUtils {

    private String loginTitle;
    private String registerTitle;
    private final List<Integer> clickedSlots;
    private String dynamicTitle;
    private static final Logger logger = Logger.getLogger(GUIUtils.class.getName());

    // Map to associate integers with symbols
    private static final Map<Integer, String> symbolMap = new HashMap<>();

    static {
        symbolMap.put(1, "섏");
        symbolMap.put(2, "섐");
        symbolMap.put(3, "섑");
        symbolMap.put(4, "섒");
        symbolMap.put(5, "섓");
        symbolMap.put(6, "섔");
        symbolMap.put(7, "섕");
        symbolMap.put(8, "섖");
        symbolMap.put(9, "섗");
        // Add more mappings as needed
    }

    private static final Map<Integer, Integer> slotConversionMap = new HashMap<>();

    static {
        slotConversionMap.put(3, 1);
        slotConversionMap.put(4, 2);
        slotConversionMap.put(5, 3);
        slotConversionMap.put(12, 4);
        slotConversionMap.put(13, 5);
        slotConversionMap.put(14, 6);
        slotConversionMap.put(21, 7);
        slotConversionMap.put(22, 8);
        slotConversionMap.put(23, 9);
    }

    public void loadTitles(FileConfiguration config) {
        loginTitle = config.getString("LoginTitle");
        registerTitle = config.getString("RegisterTitle");
        updateDynamicTitle();
    }

    public int convertRawSlot(int slot) {
        return slotConversionMap.getOrDefault(slot, -1);
    }

    public String getLoginTitle() {
        return loginTitle;
    }

    public String getRegisterTitle() {
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

    public GUIUtils() {
        clickedSlots = new ArrayList<>();
        dynamicTitle = "";
    }

    public void addClickedSlot(int slot) {
        clickedSlots.add(slot);
        updateDynamicTitle();
    }

    public List<Integer> getClickedSlots() {
        return clickedSlots;
    }

    public String buildPIN() {
        StringBuilder pinBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pinBuilder.append(clickedSlots.get(i));
        }
        String pin = pinBuilder.toString();
        clickedSlots.clear();
        updateDynamicTitle();
        return pin;
    }

    public String getDynamicTitle() {
        return dynamicTitle;
    }

    public String getSymbolForSlot(int slot) {
        return symbolMap.getOrDefault(slot, String.valueOf(slot));
    }

    private void updateDynamicTitle() {
        StringBuilder titleBuilder = new StringBuilder(loginTitle);
        String dynamicTitle = titleBuilder.toString();

        for (int i = 0; i < clickedSlots.size(); i++) {
            String symbol = symbolMap.getOrDefault(clickedSlots.get(i), String.valueOf(clickedSlots.get(i)));
            dynamicTitle = dynamicTitle.replaceFirst("\\섎", symbol);
        }
        this.dynamicTitle = dynamicTitle;
        logger.info("Dynamic Title updated to: " + dynamicTitle); // Print dynamic title using logger
    }
}