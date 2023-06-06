package com.xauth;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

import fr.xephi.authme.api.v3.AuthMeApi;
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

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        clickedSlots = new ArrayList<>();

        guiUtils = new GUIUtils();
        guiUtils.loadTitles(getConfig()); // Load titles from the configuration file

        loginGUI = new LoginGUI(guiUtils.getLoginTitle(), guiUtils);
        registerGUI = new RegisterGUI(guiUtils.getRegisterTitle(), guiUtils);
        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new onPlayerJoinListener(this, guiUtils, loginGUI, registerGUI), this);
        Bukkit.getPluginManager().registerEvents(new onGUIClose(this, loginGUI, registerGUI), this);
        Bukkit.getPluginManager().registerEvents(new onLogoutCommand(this, loginGUI), this);
        Bukkit.getPluginManager().registerEvents(new onUnregisterCommand(this, registerGUI), this);
        Bukkit.getPluginManager().registerEvents(new onPinClick(this, loginGUI, registerGUI, clickedSlots), this);
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
