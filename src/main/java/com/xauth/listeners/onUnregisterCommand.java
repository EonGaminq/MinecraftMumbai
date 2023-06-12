package com.xauth.listeners;

import com.xauth.gui.RegisterGUI;
import com.xauth.xAuth;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class onUnregisterCommand implements Listener {

    private final xAuth plugin;
    private final RegisterGUI registerGUI;

    public onUnregisterCommand(xAuth plugin, RegisterGUI registerGUI) {
        this.plugin = plugin;
        this.registerGUI = registerGUI;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();

        if (command.startsWith("/authme unregister ")) {
            String[] args = command.split(" ");
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                if (args.length == 3) {
                    // Admin unregistering another player
                    String targetPlayerName = args[2];
                    Player targetPlayer = plugin.getServer().getPlayerExact(targetPlayerName);
                    if (targetPlayer != null && targetPlayer.isOnline()) {
                        registerGUI.open(player);
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
                        registerGUI.open(player);
                    }
                }
            }, 6);
        }
    }

}