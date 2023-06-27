package com.xauth.commands;

import com.xauth.gui.PinGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xauth.xAuth;

@SuppressWarnings("NullableProblems")
public class OnCommand implements CommandExecutor {

    private final xAuth plugin;

    public OnCommand(xAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.getGuiUtils().loadTitles(plugin.getConfig()); // Reload the titles from the config

            String loginTitle = plugin.getGuiUtils().getLoginTitle();
            String registerTitle = plugin.getGuiUtils().getRegisterTitle();

            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("Config reloaded.");
                player.sendMessage("Updated Login Title: " + loginTitle);
                player.sendMessage("Updated Register Title: " + registerTitle);
            } else {
                sender.sendMessage("Config reloaded.");
                sender.sendMessage("Updated Login Title: " + loginTitle);
                sender.sendMessage("Updated Register Title: " + registerTitle);
            }

            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("debug")) {
            String argument = args[1];

            if (argument.equalsIgnoreCase("login")) {
                PinGUI pinGUI = new PinGUI(plugin.getGuiUtils());

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    pinGUI.open(player);
                }

                return true;
            } else if (argument.equalsIgnoreCase("register")) {
                PinGUI pinGUI = new PinGUI(plugin.getGuiUtils());

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    pinGUI.open(player);
                }

                return true;
            }
        }

        return false;
    }
}
