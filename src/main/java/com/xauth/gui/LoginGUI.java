package com.xauth.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.xauth.utils.GUIUtils;


public class LoginGUI {
    private final GUIUtils guiUtils;

    public LoginGUI(GUIUtils guiUtils) {
        this.guiUtils = guiUtils;
    }

    public void open(Player player) {
        Inventory loginGui = Bukkit.createInventory(null, 27, guiUtils.getLoginTitle());
        for (int i = 0; i < loginGui.getSize(); i++) {
            ItemStack fillItemClone;
            if (isWithinClickableArea(i) || isFillItemSlot(i)) {
                fillItemClone = guiUtils.createFillItem();
                ItemMeta itemMeta = fillItemClone.getItemMeta();
                int convertedSlot = guiUtils.convertRawSlot(i);
                String symbol = Integer.toString(convertedSlot);
                String displayName = ChatColor.RESET + symbol;
                assert itemMeta != null;
                itemMeta.setDisplayName(displayName);
                fillItemClone.setItemMeta(itemMeta);
            } else {
                fillItemClone = null; // Empty slot outside clickable area
            }
            loginGui.setItem(i, fillItemClone);
        }
        player.openInventory(loginGui);
    }

    private boolean isWithinClickableArea(int slot) {
        int row = slot / 9;
        int col = slot % 9;
        return row >= 1 && row <= 3 && col >= 3 && col <= 5;
    }

    private boolean isFillItemSlot(int slot) {
        return slot == 3 || slot == 4 || slot == 5;
    }
}

