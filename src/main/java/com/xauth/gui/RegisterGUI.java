package com.xauth.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.inventory.InventoryType;

import com.xauth.utils.GUIUtils;

public class RegisterGUI {
    private String registerTitle;
    private GUIUtils guiUtils;

    public RegisterGUI(String registerTitle, GUIUtils guiUtils) {
        this.registerTitle = registerTitle;
        this.guiUtils = guiUtils;
    }

    public void open(Player player) {
        Inventory registerGUI = Bukkit.createInventory(null, InventoryType.DROPPER, registerTitle);
        for (int i = 0; i < registerGUI.getSize(); i++) {
            ItemStack fillItemClone = guiUtils.createFillItem();
            ItemMeta itemMeta = fillItemClone.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RESET + String.valueOf(i + 1));
            fillItemClone.setItemMeta(itemMeta);
            registerGUI.setItem(i, fillItemClone);
        }
        player.openInventory(registerGUI);
    }
}
