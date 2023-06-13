package com.xauth.utils;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIUtils {

    private String loginTitle;
    private String registerTitle;
    private final List<Integer> clickedSlots;
    private final Map<Player, StringBuilder> pinMap;
    private String dynamicTitle;
    private String pinGUITitle;

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
        pinGUITitle = registerTitle;
        for (Player player : pinMap.keySet()) {
            updateDynamicTitle(player);
        }
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
        assert itemMeta != null;
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ChatColor.RESET + "");
        item.setItemMeta(itemMeta);
        return item;
    }

    public GUIUtils() {
        clickedSlots = new ArrayList<>();
        dynamicTitle = "";
        pinMap = new HashMap<>();
    }

    public void addClickedSlot(int slot, Player player) {
        clickedSlots.add(slot);
        updateDynamicTitle(player);
    }


    public List<Integer> getClickedSlots() {
        return clickedSlots;
    }

    public Map<Player, StringBuilder> getPinMap() {
        return pinMap;
    }

    public void clearPinMap(Player player) {
        pinMap.remove(player);
    }

    public String getPinGUITitle() {
        return pinGUITitle;
    }

    public void setPinGUITitle(String pinGUITitle) {
        this.pinGUITitle = pinGUITitle;
    }

    public String getDynamicTitle() {
        return dynamicTitle;
    }

    private void updateDynamicTitle(Player player) {
        String dynamicTitle = pinGUITitle;
        StringBuilder pinBuilder = pinMap.get(player);
        if (pinBuilder != null) {
            for (int i = 0; i < pinBuilder.length(); i++) {
                int digit = Character.getNumericValue(pinBuilder.charAt(i));
                String symbol = symbolMap.getOrDefault(digit, String.valueOf(digit));
                dynamicTitle = dynamicTitle.replaceFirst("섎", symbol);
            }
        }
        this.dynamicTitle = dynamicTitle;
    }


}
