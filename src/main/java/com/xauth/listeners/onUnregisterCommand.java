package com.xauth.listeners;

import com.xauth.xAuth;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class onUnregisterCommand implements Listener {

    private final xAuth plugin;

    public onUnregisterCommand(xAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();

        if (command.startsWith("/authme unregister ")) {
            String[] args = command.split(" ");
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
            if (args.length == 2) {
                // Admin unregistering another player
                String targetPlayerName = args[2];
                Player targetPlayer = plugin.getServer().getPlayerExact(targetPlayerName);
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    player.sendMessage("You've been unregistered by an Admin" + targetPlayerName);
                    plugin.openDispenserGUI(targetPlayer);
                    }
                }
            }, 6);

        } else if (command.startsWith("/unregister ")) {
            String[] args = command.split(" ");
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
            if (args.length == 2) {
                // Player unregistering themselves
                if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                    plugin.openDispenserGUI(player);
                }
              }
            }, 2);
        }
    }

}
