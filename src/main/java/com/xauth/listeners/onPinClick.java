package com.xauth.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.xauth.gui.LoginGUI;
import com.xauth.gui.RegisterGUI;
import com.xauth.xAuth;

import java.util.List;
import fr.xephi.authme.api.v3.AuthMeApi;

public class onPinClick implements Listener {

    private final xAuth plugin;
    private final LoginGUI loginGUI;
    private final RegisterGUI registerGUI;
    private final List<Integer> clickedSlots;

    public onPinClick(xAuth plugin, LoginGUI loginGUI, RegisterGUI registerGUI, List<Integer> clickedSlots) {
        this.plugin = plugin;
        this.loginGUI = loginGUI;
        this.registerGUI = registerGUI;
        this.clickedSlots = clickedSlots;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryTitle = event.getView().getTitle();
        if (inventoryTitle.equals(plugin.getGuiUtils().getLoginTitle()) || inventoryTitle.equals(plugin.getGuiUtils().getRegisterTitle())) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                int slot = event.getRawSlot() + 1;
                Player player = (Player) event.getWhoClicked();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                if (slot > 9) {
                    return; // Ignore slots beyond 9
                }
                clickedSlots.add(slot);
                // Start combining all the numbers into a string
                if (clickedSlots.size() >= 4) {
                    StringBuilder pinBuilder = new StringBuilder();
                    for (int i = 0; i < 4; i++) {
                        pinBuilder.append(clickedSlots.get(i));
                    }
                    String pin = pinBuilder.toString();
                    clickedSlots.clear();
                    player.closeInventory(); // Close inventory once 4-digit PIN has been typed out.
                    // If the player is already registered, use /login command; if not, use /register command
                    if (AuthMeApi.getInstance().isRegistered(player.getName())) {
                        Bukkit.dispatchCommand(player, "login " + pin);
                    } else {
                        Bukkit.dispatchCommand(player, "register " + pin + " " + pin);
                    }
                    if (AuthMeApi.getInstance().isAuthenticated(player)) {
                        player.sendMessage("码 Registered Successfully! Your PIN is §x§8§3§d§1§3§0" + pin); // Send PIN Message
                    }
                }
            }
        }
    }
}
