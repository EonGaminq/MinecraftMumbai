package com.xauth;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class xAuth extends JavaPlugin implements CommandExecutor, Listener {

    private List<Integer> clickedSlots;

    @Override
    public void onEnable() {
        getLogger().info("xAuth plugin has been enabled.");
        getCommand("auth").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        clickedSlots = new ArrayList<>();
    }

    @Override
    public void onDisable() {
        getLogger().info("xAuth plugin has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("auth")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Delay opening the dispenser GUI for 1 tick to ensure it is properly rendered
                Bukkit.getScheduler().runTaskLater(this, () -> openDispenserGUI(player), 1);
            }
            return true;
        }
        return false;
    }

    private void openDispenserGUI(Player player) {
        Inventory dispenserGUI = Bukkit.createInventory(null, InventoryType.DISPENSER, "PIN GUI");

        ItemStack diamond = new ItemStack(Material.DIAMOND);

        for (int i = 0; i < dispenserGUI.getSize(); i++) {
            dispenserGUI.setItem(i, diamond);
        }

        player.openInventory(dispenserGUI);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("PIN GUI")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.DIAMOND) {
                int slot = event.getRawSlot() + 1;
                Player player = (Player) event.getWhoClicked();
                clickedSlots.add(slot);
                player.sendMessage("Clicked on slot: " + slot);

                if (clickedSlots.size() >= 4) {
                    StringBuilder pinBuilder = new StringBuilder("Your PIN is ");
                    for (int i = 0; i < 4; i++) {
                        pinBuilder.append(clickedSlots.get(i));
                    }
                    player.sendMessage(pinBuilder.toString());
                    clickedSlots.clear();
                }
            }
        }
    }
}
