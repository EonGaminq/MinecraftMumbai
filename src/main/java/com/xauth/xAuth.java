package com.xauth;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import com.xauth.listeners.onGUIClose;
import com.xauth.listeners.onLogoutCommand;
import com.xauth.listeners.onUnregisterCommand;
import com.xauth.utils.GUIUtils;
import com.xauth.gui.PinGUI;
import com.xauth.listeners.onPinClick;
import com.xauth.listeners.onPlayerJoinListener;
import com.xauth.commands.OnCommand;

public class xAuth extends JavaPlugin implements CommandExecutor, Listener {

    private GUIUtils guiUtils;
    private PinGUI pinGUI;


    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        guiUtils = new GUIUtils();
        guiUtils.loadTitles(getConfig()); // Load titles from the configuration file
        guiUtils.setPinGUITitle(guiUtils.getRegisterTitle());
        pinGUI = new PinGUI(guiUtils); // Initialize the pinGUI object
        getCommand("xauth").setExecutor(new OnCommand(this));

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new onPlayerJoinListener(this, guiUtils, pinGUI), this);
        Bukkit.getPluginManager().registerEvents(new onGUIClose(this, guiUtils, pinGUI), this);
        Bukkit.getPluginManager().registerEvents(new onLogoutCommand(this, guiUtils, pinGUI), this);
        Bukkit.getPluginManager().registerEvents(new onUnregisterCommand(this, guiUtils, pinGUI), this);
        Bukkit.getPluginManager().registerEvents(new onPinClick(this, guiUtils), this);

        getLogger().info("xAuth plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("xAuth plugin has been disabled.");
    }
    public GUIUtils getGuiUtils() {
        return guiUtils;
    }
}
