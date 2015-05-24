package com.sandlotminecraft.lobbywands;

import com.dsh105.echopet.api.EchoPetAPI;
import com.dsh105.echopet.compat.api.entity.PetType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PetEggs
        implements Listener {
    public EchoPetAPI getEchoPetAPI() {
        return EchoPetAPI.getAPI();
    }

    public static ItemStack getBatEgg() {
        ItemStack wand = new ItemStack(383, 1, (short) 0, Byte.valueOf((byte) 65));
        ItemMeta im = wand.getItemMeta();

        im.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "A Pet Bat Egg");
        im.setLore(Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Calls a pet bat to follow", "you. Left click to call your", "pet, Right click to store it."}));


        wand.setItemMeta(im);
        return wand;
    }

    public static ItemStack getCatEgg() {
        ItemStack wand = new ItemStack(383, 1, (short) 0, Byte.valueOf((byte) 98));
        ItemMeta im = wand.getItemMeta();

        im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "A Pet Cat Egg");
        im.setLore(Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Calls a pet cat to follow", "you. Left click to call your", "pet, Right click to store it."}));


        wand.setItemMeta(im);
        return wand;
    }

    public static ItemStack getBugEgg() {
        ItemStack wand = new ItemStack(383, 1, (short) 0, Byte.valueOf((byte) 60));
        ItemMeta im = wand.getItemMeta();

        im.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "A Pet Bug Egg");
        im.setLore(Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Calls a pet bug to follow", "you. Left click to call your", "pet, Right click to store it."}));


        wand.setItemMeta(im);
        return wand;
    }

    @EventHandler
    public void onEggUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack egg = p.getItemInHand();
        if ((egg != null) && (egg.hasItemMeta()) && (egg.getItemMeta().hasDisplayName()) && (egg.getItemMeta().getDisplayName().contains("Pet"))) {
            if (egg.getItemMeta().getDisplayName().contains("Bat")) {
                if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
                    if (!getEchoPetAPI().hasPet(p)) {
                        getEchoPetAPI().givePet(p, PetType.BAT, false);
                        getEchoPetAPI().getPet(p).setPetName("" + p.getName() + "'s Pet Bat", false);
                        p.sendMessage(ChatColor.YELLOW + "Your Pet Bat now follows closely behind you.");
                    }
                } else if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_AIR)) {
                    if (getEchoPetAPI().hasPet(p)) {
                        getEchoPetAPI().removePet(p, false, false);
                    }
                    event.setCancelled(true);
                }
            } else if (egg.getItemMeta().getDisplayName().contains("Cat")) {
                if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
                    if (!getEchoPetAPI().hasPet(p)) {
                        getEchoPetAPI().givePet(p, PetType.OCELOT, false);
                        getEchoPetAPI().getPet(p).setPetName("" + p.getName() + "'s Pet Cat", false);
                        p.sendMessage(ChatColor.YELLOW + "Your Pet Cat now follows closely behind you.");
                    }
                } else if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_AIR)) {
                    if (getEchoPetAPI().hasPet(p)) {
                        getEchoPetAPI().removePet(p, false, false);
                    }
                    event.setCancelled(true);
                }
            } else if (egg.getItemMeta().getDisplayName().contains("Bug")) {
                if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
                    if (!getEchoPetAPI().hasPet(p)) {
                        getEchoPetAPI().givePet(p, PetType.SILVERFISH, false);
                        getEchoPetAPI().getPet(p).setPetName("" + p.getName() + "'s Pet Bug", false);
                        p.sendMessage(ChatColor.YELLOW + "Your Pet Bug now follows closely behind you.");
                    }
                } else if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_AIR)) {
                    if (getEchoPetAPI().hasPet(p)) {
                        getEchoPetAPI().removePet(p, false, false);
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
