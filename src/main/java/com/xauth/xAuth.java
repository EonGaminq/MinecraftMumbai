package com.xauth;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.xauth.listeners.onGUIClose;
import com.xauth.listeners.onLogoutCommand;
import com.xauth.listeners.onUnregisterCommand;
import com.xauth.utils.GUIUtils;
import com.xauth.gui.PinGUI;
import com.xauth.gui.LoginGUI;
import com.xauth.gui.RegisterGUI;
import com.xauth.listeners.onPinClick;
import com.xauth.listeners.onPlayerJoinListener;
import com.xauth.commands.OnCommand;

public class xAuth extends JavaPlugin implements CommandExecutor, Listener {

    private GUIUtils guiUtils;
    private PinGUI pinGUI;
    private LoginGUI loginGUI;
    private RegisterGUI registerGUI;


    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        guiUtils = new GUIUtils();
        guiUtils.loadTitles(getConfig()); // Load titles from the configuration file
        pinGUI = new PinGUI(guiUtils); // Initialize the pinGUI object
        loginGUI = new LoginGUI(guiUtils);
        registerGUI = new RegisterGUI(guiUtils);

        getCommand("xauth").setExecutor(new OnCommand(this));

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new onPlayerJoinListener(this, guiUtils, pinGUI, registerGUI, loginGUI), this);
        Bukkit.getPluginManager().registerEvents(new onGUIClose(this, guiUtils, pinGUI, loginGUI, registerGUI), this);
        Bukkit.getPluginManager().registerEvents(new onLogoutCommand(this, guiUtils, pinGUI, loginGUI), this);
        Bukkit.getPluginManager().registerEvents(new onUnregisterCommand(this, guiUtils, pinGUI, registerGUI), this);
        Bukkit.getPluginManager().registerEvents(new onPinClick(guiUtils), this);

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
