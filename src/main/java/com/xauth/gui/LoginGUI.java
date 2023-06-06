package com.xauth.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.inventory.InventoryType;

import com.xauth.utils.GUIUtils;

public class LoginGUI {
    private String loginTitle;
    private GUIUtils guiUtils;

    public LoginGUI(String loginTitle, GUIUtils guiUtils) {
        this.loginTitle = loginTitle;
        this.guiUtils = guiUtils;
    }

    public void open(Player player) {
        Inventory loginGUI = Bukkit.createInventory(null, InventoryType.DROPPER, loginTitle);
        for (int i = 0; i < loginGUI.getSize(); i++) {
            ItemStack fillItemClone = guiUtils.createFillItem();
            ItemMeta itemMeta = fillItemClone.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RESET + String.valueOf(i + 1));
            fillItemClone.setItemMeta(itemMeta);
            loginGUI.setItem(i, fillItemClone);
        }
        player.openInventory(loginGUI);

    }
}
