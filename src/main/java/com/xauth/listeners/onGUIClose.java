package com.xauth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitScheduler;

import fr.xephi.authme.api.v3.AuthMeApi;
import com.xauth.gui.LoginGUI;
import com.xauth.gui.RegisterGUI;
import com.xauth.utils.GUIUtils;
import com.xauth.xAuth;

public class onGUIClose implements Listener {

    private final xAuth plugin;
    private final RegisterGUI registerGUI;
    private final LoginGUI loginGUI;
    private final GUIUtils guiUtils;



    public onGUIClose(xAuth plugin, LoginGUI loginGUI, RegisterGUI registerGUI, GUIUtils guiUtils) {
        this.plugin = plugin;
        this.loginGUI = loginGUI;
        this.registerGUI = registerGUI;
        this.guiUtils = guiUtils;
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
                    registerGUI.open(player);
                } else if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                    loginGUI.open(player);
                } else {
                    return;
                }
            }, 6);
        }
    }
}
