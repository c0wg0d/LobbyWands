package com.sandlotminecraft.lobbywands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SpellBooks
        implements Listener {
    public static ItemStack getFireballBook(Integer level) {
        ItemStack wand = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta im = wand.getItemMeta();
        String lev = level.intValue() == 1 ? "I" : "II";

        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Spellbook: " + ChatColor.RED + "Fireball " + lev);
        im.setLore(Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Using this book will cast", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "a Level " + level + " Fireball.", ChatColor.WHITE + "" + ChatColor.ITALIC + "Uses Remaining: " + ChatColor.GREEN + "20"}));


        wand.setItemMeta(im);
        return wand;
    }

    public static ItemStack getLightningBook(Integer level) {
        ItemStack wand = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta im = wand.getItemMeta();
        String lev = level.intValue() == 1 ? "I" : "II";

        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Spellbook: " + ChatColor.WHITE + "Lightning " + lev);
        im.setLore(Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Using this book will cast", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "a Level " + level + " Lightning Bolt.", ChatColor.WHITE + "" + ChatColor.ITALIC + "Uses Remaining: " + ChatColor.GREEN + "20"}));


        wand.setItemMeta(im);
        return wand;
    }

    @EventHandler
    public void onSpellbookUse(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            ItemStack book = event.getPlayer().getItemInHand();
            Player p = event.getPlayer();
            if ((book.hasItemMeta()) && (book.getItemMeta().hasDisplayName()) && (book.getItemMeta().getDisplayName().contains("Fireball"))) {
                Vector dir = p.getEyeLocation().getDirection().multiply(2);
                Fireball fb = (Fireball) p.launchProjectile(Fireball.class, dir);
                float damage = 0.0F;
                if (book.getItemMeta().getDisplayName().contains("II")) {
                    damage = 3.0F;
                } else if (book.getItemMeta().getDisplayName().contains("I")) {
                    damage = 2.0F;
                }
                fb = (Fireball) p.getWorld().spawn(p.getEyeLocation().add(dir.getX(), dir.getY(), dir.getZ()), Fireball.class);
                fb.setShooter(p);
                fb.setYield(damage);
                fb.setBounce(false);
                fb.setIsIncendiary(false);
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 10.0F, 10.0F);

                decrementUses(p, book);
            }
            if ((book.hasItemMeta()) && (book.getItemMeta().hasDisplayName()) && (book.getItemMeta().getDisplayName().contains("Lightning"))) {
                float damage = 0.0F;
                int radius = 0;
                if (book.getItemMeta().getDisplayName().contains("II")) {
                    damage = 15.0F;
                    radius = 6;
                } else if (book.getItemMeta().getDisplayName().contains("I")) {
                    damage = 10.0F;
                    radius = 3;
                }
                Location loc = p.getTargetBlock(new HashSet<Byte>(), 100).getLocation();
                p.getWorld().strikeLightningEffect(loc);
                for (LivingEntity e : loc.getWorld().getLivingEntities()) {
                    if ((e.getLocation().distance(loc) <= radius) && (!(e instanceof Player))) {
                        e.damage(damage);
                    }
                }
                decrementUses(p, book);
            }
        }
    }

    private void decrementUses(Player player, ItemStack book) {
        ItemMeta im = book.getItemMeta();
        List<String> lore = im.getLore();

        String[] tmp = ChatColor.stripColor((String) lore.get(2)).split("\\s+");
        int uses = Integer.parseInt(tmp[2]);

        uses--;
        lore.set(2, ChatColor.WHITE + "" + ChatColor.ITALIC + "Uses Remaining: " + ChatColor.GREEN + uses);
        im.setLore(lore);
        book.setItemMeta(im);
        player.setExp(uses / 20.0F);
        if (uses == 0) {
            player.getInventory().remove(book);
            player.sendMessage(ChatColor.DARK_PURPLE + "Your Spellbook has been used up.");
        }
    }

    @EventHandler
    public void onInventoryEvent(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        ItemStack book = p.getInventory().getItem(event.getNewSlot());
        if ((book != null) && (book.hasItemMeta()) && (book.getItemMeta().hasDisplayName()) && (book.getItemMeta().getDisplayName().contains("Spellbook"))) {
            String[] tmp = ChatColor.stripColor((String) book.getItemMeta().getLore().get(2)).split("\\s+");
            int remaining = Integer.parseInt(tmp[2]);
            p.setExp(remaining / 20.0F);
        }
    }
}
