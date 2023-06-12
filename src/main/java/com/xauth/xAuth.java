package com.xauth;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import com.xauth.listeners.onGUIClose;
import com.xauth.listeners.onLogoutCommand;
import com.xauth.listeners.onUnregisterCommand;
import com.xauth.utils.GUIUtils;
import com.xauth.gui.LoginGUI;
import com.xauth.gui.RegisterGUI;
import com.xauth.listeners.onPinClick;
import com.xauth.listeners.onPlayerJoinListener;
import com.xauth.commands.OnCommand;

public class xAuth extends JavaPlugin implements CommandExecutor, Listener {

    private List<Integer> clickedSlots;
    private GUIUtils guiUtils;
    private LoginGUI loginGUI;
    private RegisterGUI registerGUI;
    private StringBuilder dynamicTitle;


    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        clickedSlots = new ArrayList<>();

        guiUtils = new GUIUtils();
        guiUtils.loadTitles(getConfig()); // Load titles from the configuration file

        loginGUI = new LoginGUI(guiUtils.getLoginTitle(), guiUtils);
        registerGUI = new RegisterGUI(guiUtils.getRegisterTitle(), guiUtils);
        dynamicTitle = new StringBuilder();

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new onPlayerJoinListener(this, guiUtils, loginGUI, registerGUI), this);
        Bukkit.getPluginManager().registerEvents(new onGUIClose(this, loginGUI, registerGUI, guiUtils), this);
        Bukkit.getPluginManager().registerEvents(new onLogoutCommand(this, loginGUI), this);
        Bukkit.getPluginManager().registerEvents(new onUnregisterCommand(this, registerGUI), this);
        Bukkit.getPluginManager().registerEvents(new onPinClick(this, guiUtils), this);
        // Register the OnCommand as the command executor
        getCommand("xauth").setExecutor(new OnCommand(this, loginGUI));

        getLogger().info("xAuth plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("xAuth plugin has been disabled.");
    }
    public List<Integer> getClickedSlots() {
        return clickedSlots;
    }
    public GUIUtils getGuiUtils() {
        return guiUtils;
    }
}
