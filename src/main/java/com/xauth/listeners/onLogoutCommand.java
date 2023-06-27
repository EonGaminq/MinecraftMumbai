package com.xauth.listeners;

import com.xauth.gui.PinGUI;
import com.xauth.gui.LoginGUI;
import com.xauth.utils.GUIUtils;
import com.xauth.xAuth;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class onLogoutCommand implements Listener {

    private final xAuth plugin;
    private final GUIUtils guiUtils;
    private final PinGUI pinGUI;
    private final LoginGUI loginGUI;

    public onLogoutCommand(xAuth plugin, GUIUtils guiUtils, PinGUI pinGUI, LoginGUI loginGUI) {
        this.plugin = plugin;
        this.guiUtils = guiUtils;
        this.pinGUI = pinGUI;
        this.loginGUI = loginGUI;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        if (command.startsWith("/logout")) {
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                    guiUtils.setPinGUITitle(guiUtils.getLoginTitle());
                    loginGUI.open(player);
                }
            }, 6);
        }
    }
}
