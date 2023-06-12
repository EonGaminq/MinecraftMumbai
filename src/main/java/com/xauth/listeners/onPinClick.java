package com.xauth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.xauth.xAuth;
import com.xauth.utils.GUIUtils;
import com.xauth.utils.InventoryUpdate;

import fr.xephi.authme.api.v3.AuthMeApi;

public class onPinClick implements Listener {

    private final xAuth plugin;
    private final GUIUtils guiUtils;

    public onPinClick(xAuth plugin, GUIUtils guiUtils) {
        this.plugin = plugin;
        this.guiUtils = guiUtils;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        boolean isLeftClick = event.isLeftClick();
        boolean isRightClick = event.isRightClick();

        String inventoryTitle = event.getView().getTitle();
        if (!inventoryTitle.contains(guiUtils.getLoginTitle()) && !inventoryTitle.contains(guiUtils.getRegisterTitle())) {
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
            guiUtils.addClickedSlot(convertedSlot);
            InventoryUpdate.updateInventory(player, guiUtils.getDynamicTitle());
        }

        if (isRightClick) {
            return;
        }

        if (guiUtils.getClickedSlots().size() >= 4) {
            String pin = guiUtils.buildPIN();
            player.closeInventory(); // Close inventory once 4-digit PIN has been typed out.
            executeAuthCommand(player, pin);
        }
    }

    private void executeAuthCommand(Player player, String pin) {
        if (AuthMeApi.getInstance().isRegistered(player.getName())) {
            Bukkit.dispatchCommand(player, "login " + pin);
        } else {
            Bukkit.dispatchCommand(player, "register " + pin + " " + pin);
        }
        if (AuthMeApi.getInstance().isAuthenticated(player)) {
            player.sendMessage("Registered Successfully! Your PIN is " + pin); // Send PIN Message
        }
    }
}
