package com.xauth.listeners;

import com.xauth.gui.PinGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitScheduler;

import fr.xephi.authme.api.v3.AuthMeApi;
import com.xauth.utils.GUIUtils;
import com.xauth.gui.LoginGUI;
import com.xauth.gui.RegisterGUI;
import com.xauth.xAuth;

public class onGUIClose implements Listener {

    private final xAuth plugin;
    private final GUIUtils guiUtils;
    private final PinGUI pinGUI;
    private final LoginGUI loginGUI;
    private final RegisterGUI registerGUI;

    public onGUIClose(xAuth plugin, GUIUtils guiUtils, PinGUI pinGUI, LoginGUI loginGUI, RegisterGUI registerGUI) {
        this.plugin = plugin;
        this.guiUtils = guiUtils;
        this.pinGUI = pinGUI;
        this.loginGUI = loginGUI;
        this.registerGUI = registerGUI;
    }

    // If the PIN GUI is closed and the player is not authenticated, reopen the GUI with a delay of 1 tick
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String inventoryTitle = event.getView().getTitle();
        if (inventoryTitle.contains(":offset_-16::ui_login:") || inventoryTitle.contains(":offset_-16::ui_regis:")) {
            Player player = (Player) event.getPlayer();
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                guiUtils.clearPinMap(player); // Clear the recorded PIN digits for the specific player in pinMap
                if (!guiUtils.getClickedSlots().isEmpty()) {
                    guiUtils.getClickedSlots().clear(); // Clear the recorded PIN digits using the GUIUtils' clickedSlots list
                }
                if (!AuthMeApi.getInstance().isRegistered(player.getName())) {
                    registerGUI.open(player);
                } else if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                    loginGUI.open(player);
                }
            }, 6);
        }
    }
}
