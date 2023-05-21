package com.xauth.listeners;

import com.xauth.xAuth;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class onLogoutCommand implements Listener {

    private final xAuth plugin;

    public onLogoutCommand(xAuth plugin) { this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        if (command.startsWith("/logout")) {
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
            if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                plugin.openLoginGUI(player); }
            }, 6);
        }
    }
}
