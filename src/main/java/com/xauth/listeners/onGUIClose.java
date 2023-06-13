package com.xauth.listeners;

import com.xauth.gui.PinGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitScheduler;

import fr.xephi.authme.api.v3.AuthMeApi;
import com.xauth.utils.GUIUtils;
import com.xauth.xAuth;


public class onGUIClose implements Listener {

    private final xAuth plugin;
    private final GUIUtils guiUtils;
    private final PinGUI pinGUI;


    public onGUIClose(xAuth plugin, GUIUtils guiUtils, PinGUI pinGUI) {
        this.plugin = plugin;
        this.guiUtils = guiUtils;
        this.pinGUI = pinGUI;
    }

    // If the PIN GUI is closed and the player is not authenticated, reopen the GUI with a delay of 1 tick
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String inventoryTitle = event.getView().getTitle();
        String loginTitle = guiUtils.getLoginTitle();
        String registerTitle = guiUtils.getRegisterTitle();

        if (inventoryTitle.equals(loginTitle) || inventoryTitle.equals(registerTitle)) {
            Player player = (Player) event.getPlayer();
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                if (!guiUtils.getClickedSlots().isEmpty()) {
                    guiUtils.getClickedSlots().clear(); // Clear the recorded PIN digits using the GUIUtils' clickedSlots list
                }
                if (!AuthMeApi.getInstance().isRegistered(player.getName())) {
                    guiUtils.setPinGUITitle(guiUtils.getRegisterTitle());
                    pinGUI.open(player);
                } else if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                    guiUtils.setPinGUITitle(guiUtils.getLoginTitle());
                    pinGUI.open(player);
                }
            }, 6);
        }
    }
}
