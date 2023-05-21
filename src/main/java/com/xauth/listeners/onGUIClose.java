package com.xauth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitScheduler;

import fr.xephi.authme.api.v3.AuthMeApi;
import com.xauth.xAuth;

public class onGUIClose implements Listener {

    private final xAuth plugin;
    private final String guiTitle; // Add a field to store the GUI title

    public onGUIClose(xAuth plugin, String guiTitle) {
        this.plugin = plugin;
        this.guiTitle = guiTitle; // Assign the GUI title from the constructor parameter
    }

    // If the PIN GUI is closed and the player is not authenticated, reopen the GUI with a delay of 1 tick
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(guiTitle)) {
            Player player = (Player) event.getPlayer();
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                if (!plugin.getClickedSlots().isEmpty()) {
                    plugin.getClickedSlots().clear(); // Clear the recorded PIN digits using the plugin's clickedSlots list
                }
                if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                    plugin.openLoginGUI(player);
                }
            }, 6);
        }
    }
}
