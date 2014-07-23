package com.sandlotminecraft.lobbywands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shawn on 7/22/2014.
 */
public class WandExperience implements Listener {

    @EventHandler
    public void onSpiderDeath (EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.SPIDER &&
                event.getEntityType() != EntityType.CAVE_SPIDER) {
            return;
        }

        event.getDrops().clear();
        if (!(event.getEntity().getKiller() instanceof Player)) {
            return;
        }

        event.setDroppedExp(0);
        Player p = event.getEntity().getKiller();

        if (!p.getItemInHand().hasItemMeta()) {
            return;
        }

        ItemMeta im = p.getItemInHand().getItemMeta();
        List<String> lore = im.getLore();

        String lore1 = lore.get(0);
        String lore2 = lore.get(1);
        String lore3 = lore.get(2);

        int xp[] = getXP(ChatColor.stripColor(lore2));
        xp[0]++;

        // Check for level up
        if (xp[0] >= xp[1]) {
            int newlevel = getLevel(ChatColor.stripColor(lore3)) + 1;
            p.sendMessage(ChatColor.YELLOW + "Your wand is now level " + newlevel + "!");
            xp[0] = 0;
            xp[1] = newlevel * 10;
            lore3 = ChatColor.DARK_AQUA + "Level " + newlevel + " Wand";
            p.sendMessage(ChatColor.GOLD + " - Cooldown Time is Now " + (90 - newlevel*6) + " Seconds!");

            //bump up enchant level if level 4, 7, or 10
            if (newlevel == 4 || newlevel == 7 || newlevel == 10) {
                int enchantLevel = im.getEnchantLevel(Enchantment.DAMAGE_ARTHROPODS);
                im.removeEnchant(Enchantment.DAMAGE_ARTHROPODS);
                im.addEnchant(Enchantment.DAMAGE_ARTHROPODS,enchantLevel + 1,true);
                p.sendMessage(ChatColor.GOLD + " - Enchantment Level has Increased!");
            }

            if (newlevel == 3 || newlevel == 6 || newlevel == 9)
                p.sendMessage(ChatColor.GOLD + " - Chance of Special Attack Increased!");
        }

        // Set the new lore
        lore2 = ChatColor.DARK_AQUA + "" + xp[0] + "/" + xp[1] + " XP";
        im.setLore(Arrays.asList(lore1, lore2, lore3));
        p.getItemInHand().setItemMeta(im);
    }

    private int[] getXP (String lore) {
        int[] xp = new int[2];
        String[] xpstr = lore.split("/");
        String have = xpstr[0];
        xpstr = xpstr[1].split("\\s+");
        String need = xpstr[0];

        xp[0] = Integer.parseInt(have);
        xp[1] = Integer.parseInt(need);

        return xp;
    }

    public static int getLevel (String lore) {
        String[] levstr = lore.split("\\s+");
        int level = Integer.parseInt(levstr[1]);
        return level;
    }
}
