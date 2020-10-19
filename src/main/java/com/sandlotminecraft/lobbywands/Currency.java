package com.sandlotminecraft.lobbywands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class Currency
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getGalleons(Integer amount) {
        ItemStack galleon = new ItemStack(Material.SUNFLOWER, amount.intValue());
        ItemMeta gim = galleon.getItemMeta();
        gim.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        gim.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gold Galleon");
        gim.setLore(Arrays.asList(new String[]{ChatColor.GOLD + "" + ChatColor.ITALIC + "A Gold Galleon is the", ChatColor.GOLD + "" + ChatColor.ITALIC + "most valuable of all", ChatColor.GOLD + "" + ChatColor.ITALIC + "the Wizarding Coins.", ChatColor.GOLD + "" + ChatColor.ITALIC + "One is worth 17 Sickles."}));


        galleon.setItemMeta(gim);
        return galleon;
    }

    public static ItemStack getSickles(Integer amount) {
        ItemStack galleon = new ItemStack(Material.NETHER_STAR, amount.intValue());
        ItemMeta gim = galleon.getItemMeta();
        gim.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        gim.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Silver Sickle");
        gim.setLore(Arrays.asList(new String[]{ChatColor.GOLD + "" + ChatColor.ITALIC + "A Silver Sickle is a", ChatColor.GOLD + "" + ChatColor.ITALIC + "valuable coin worth", ChatColor.GOLD + "" + ChatColor.ITALIC + "29 Bronze Knuts."}));


        galleon.setItemMeta(gim);
        return galleon;
    }

    public static ItemStack getKnuts(Integer amount) {
        ItemStack galleon = new ItemStack(Material.ORANGE_DYE, amount.intValue());
        ItemMeta gim = galleon.getItemMeta();
        gim.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1, true);
        gim.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bronze Knut");
        gim.setLore(Arrays.asList(new String[]{ChatColor.GOLD + "" + ChatColor.ITALIC + "A Bronze Knut is the", ChatColor.GOLD + "" + ChatColor.ITALIC + "basic currency of", ChatColor.GOLD + "" + ChatColor.ITALIC + "the Wizarding Coins."}));


        galleon.setItemMeta(gim);
        return galleon;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        if (((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) &&
                (event.getPlayer().getItemInHand().hasItemMeta()) && (event.getPlayer().getItemInHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS))) {
            event.setCancelled(true);
        }
    }
}
