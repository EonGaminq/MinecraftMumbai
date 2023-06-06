package com.xauth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import com.xauth.xAuth;
import com.xauth.gui.LoginGUI;
import com.xauth.gui.RegisterGUI;
import com.xauth.utils.GUIUtils;

import fr.xephi.authme.api.v3.AuthMeApi;

public class onPlayerJoinListener implements Listener {

    private xAuth plugin;
    private GUIUtils guiUtils;
    private LoginGUI loginGUI;
    private RegisterGUI registerGUI;

    public onPlayerJoinListener(xAuth plugin, GUIUtils guiUtils, LoginGUI loginGUI, RegisterGUI registerGUI) {
        this.plugin = plugin;
        this.guiUtils = guiUtils;
        this.loginGUI = loginGUI;
        this.registerGUI = registerGUI;
        this.guiUtils.loadTitles(plugin.getConfig()); // Load titles from the configuration file
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        Status status = event.getStatus();
        if (status == Status.SUCCESSFULLY_LOADED) {
            if (!AuthMeApi.getInstance().isRegistered(player.getName())) {
                registerGUI.open(player);
            }
            if (AuthMeApi.getInstance().isRegistered(player.getName()) && !AuthMeApi.getInstance().isAuthenticated(player)) {
                loginGUI.open(player);
            }
            if (AuthMeApi.getInstance().isAuthenticated(player)) {
                return;
            }
        } else if (status == Status.DECLINED || status == Status.FAILED_DOWNLOAD) {
            player.kickPlayer("Failed to load ResourcePack, Please log in again.");
        }
    }
}