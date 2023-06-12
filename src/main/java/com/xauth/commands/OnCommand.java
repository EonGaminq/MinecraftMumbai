package com.xauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import com.xauth.gui.LoginGUI;
import com.xauth.xAuth;

public class OnCommand implements CommandExecutor {

    private final xAuth plugin;
    private final LoginGUI loginGUI;

    public OnCommand(xAuth plugin, LoginGUI loginGUI) {
        this.plugin = plugin;
        this.loginGUI = loginGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.getGuiUtils().loadTitles(plugin.getConfig()); // Reload the titles from the config
                player.sendMessage("Config reloaded.");
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
                loginGUI.open(player);
                return true;
            }
        } else {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.getGuiUtils().loadTitles(plugin.getConfig()); // Reload the titles from the config
                sender.sendMessage("Config reloaded.");
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
                sender.sendMessage("The debug command can only be used by players.");
                return true;
            }
        }
        return false;
    }
}
