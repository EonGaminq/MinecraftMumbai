package com.xauth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import com.xauth.xAuth;
import com.xauth.utils.GUIUtils;
import com.xauth.gui.PinGUI;
import com.xauth.gui.LoginGUI;
import com.xauth.gui.RegisterGUI;

import fr.xephi.authme.api.v3.AuthMeApi;

public class onPlayerJoinListener implements Listener {

    private final GUIUtils guiUtils;
    private final PinGUI pinGUI;
    private final LoginGUI loginGUI;
    private  final RegisterGUI registerGUI;

    public onPlayerJoinListener(xAuth plugin, GUIUtils guiUtils, PinGUI pinGUI,RegisterGUI registerGUI, LoginGUI loginGUI) {
        this.guiUtils = guiUtils;
        this.pinGUI = pinGUI;
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
        } else if (status == Status.DECLINED || status == Status.FAILED_DOWNLOAD) {
            player.kickPlayer("Failed to load ResourcePack, Please log in again.");
        }
    }
}