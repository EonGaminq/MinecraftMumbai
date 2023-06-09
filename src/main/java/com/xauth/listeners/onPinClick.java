package com.xauth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.xauth.utils.GUIUtils;
import com.xauth.utils.InventoryUpdate;

import fr.xephi.authme.api.v3.AuthMeApi;

import java.util.Map;

public class onPinClick implements Listener {

    private final GUIUtils guiUtils;
    private final Map<Player, StringBuilder> pinMap;

    public onPinClick(GUIUtils guiUtils) {
        this.guiUtils = guiUtils;
        this.pinMap = guiUtils.getPinMap();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        boolean isLeftClick = event.isLeftClick();

        String inventoryTitle = event.getView().getTitle();
        if (!inventoryTitle.contains(":offset_-16::ui_login:") && !inventoryTitle.contains(":offset_-16::ui_regis:")) {
            return; // Ignore non-relevant inventories
        }
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return; // Ignore empty slots
        }
        int slot = event.getRawSlot();
        int convertedSlot = guiUtils.convertRawSlot(slot);
        if (convertedSlot == -1) {
            return; // Ignore non-clickable area
        }
        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
        if (isLeftClick) {
            StringBuilder pinBuilder = pinMap.computeIfAbsent(player, k -> new StringBuilder());
            pinBuilder.append(convertedSlot);
            guiUtils.addClickedSlot(convertedSlot, player);
            // Check if the PIN length is 4 after appending the digit
            if (pinBuilder.length() >= 4) {
                String pin = pinBuilder.toString();
                pinMap.remove(player);
                player.closeInventory(); // Close inventory once 4-digit PIN has been typed out.
                executeAuthCommand(player, pin);
            }
        }
        if (AuthMeApi.getInstance().isRegistered(player.getName())) {
            InventoryUpdate.updateInventory(player, guiUtils.getLoginDynamicTitle());
        } else {
            InventoryUpdate.updateInventory(player, guiUtils.getRegisterDynamicTitle());
        }
    }

    private void executeAuthCommand(Player player, String pin) {
        if (AuthMeApi.getInstance().isRegistered(player.getName())) {
            Bukkit.dispatchCommand(player, "login " + pin);
        } else {
            Bukkit.dispatchCommand(player, "register " + pin + " " + pin);
        }
    }
}