package com.sandlotminecraft.lobbywands.Wands;

import com.sandlotminecraft.lobbywands.LobbyWands;
import com.sandlotminecraft.lobbywands.WandExperience;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Random;

public class MagicWand
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getMagicWand(boolean isMaxLevel) {
        ItemStack wand = new ItemStack(Material.STICK, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Magic Wand");
        String lore1 = ChatColor.translateAlternateColorCodes('&', "&9&oMakes Magic Happen :D");
        String lore2 = ChatColor.DARK_AQUA + "0/100 XP";
        String lore3 = ChatColor.DARK_AQUA + "Level 1 Wand";
        int enchantmentLevel = 1;
        if(isMaxLevel) {
            lore2 = ChatColor.DARK_AQUA + "0/1000 XP";
            lore3 = ChatColor.DARK_AQUA + "Max Level Wand";
            enchantmentLevel = 5;
        }
        im.setLore(Arrays.asList(new String[]{lore1, lore2, lore3}));
        im.addEnchant(Enchantment.DAMAGE_ALL, enchantmentLevel, true);
        wand.setItemMeta(im);
        return wand;
    }

    @EventHandler
    public void onUseMagicWand(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if ((!p.getInventory().getItemInMainHand().getType().equals(Material.STICK)) || (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) || (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Magic Wand"))) {
            return;
        }
        if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            int dir = LobbyWands.getCardinalDirection(p).intValue();
            Location loc = p.getLocation();
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(100);
            if (chanceGoBoom == 1) {
                p.getWorld().createExplosion(loc, 0.0F, false);
                p.setVelocity(p.getVelocity().setY(rand.nextInt(4) + 3));
                p.sendMessage(ChatColor.DARK_PURPLE + "Oops...");
                return;
            }
            switch (dir) {
                case 1:
                    loc = new Location(p.getWorld(), loc.getX(), loc.getY() + 1.0D, loc.getZ() - 0.75D, loc.getYaw(), loc.getPitch());
                    break;
                case 2:
                    loc = new Location(p.getWorld(), loc.getX() + 0.5D, loc.getY() + 1.0D, loc.getZ() - 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 5:
                    loc = new Location(p.getWorld(), loc.getX() + 0.75D, loc.getY() + 1.0D, loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 8:
                    loc = new Location(p.getWorld(), loc.getX() + 0.5D, loc.getY() + 1.0D, loc.getZ() + 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 7:
                    loc = new Location(p.getWorld(), loc.getX(), loc.getY() + 1.0D, loc.getZ() + 0.75D, loc.getYaw(), loc.getPitch());
                    break;
                case 6:
                    loc = new Location(p.getWorld(), loc.getX() - 0.5D, loc.getY() + 1.0D, loc.getZ() + 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 3:
                    loc = new Location(p.getWorld(), loc.getX() - 0.75D, loc.getY() + 1.0D, loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 0:
                    loc = new Location(p.getWorld(), loc.getX() - 0.5D, loc.getY() + 1.0D, loc.getZ() - 0.5D, loc.getYaw(), loc.getPitch());
            }
            //loc.getWorld().playEffect(loc, Effect.VILLAGER_PLANT_GROW, dir, 10);
            loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 1);
            loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 3.0F, 10.0F);
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            long cooldown = 90000 - (WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2)) - 1) * 6000;
            long timesince = System.currentTimeMillis() - LobbyWands.getCooldown(p.getName(), "magic");
            if (timesince < cooldown) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging. You can use it again in " + (cooldown - timesince) / 1000L + " seconds.");
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0));
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3.0F, 10.0F);
                p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Invisibilia!");
                LobbyWands.setCooldown(p.getName(), "magic");
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "You've reappeared!");
                        }
                        if (p.isOnline()) {
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3.0F, 10.0F);
                        }
                    }
                }, 300L);


                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "Your Magic Wand is now recharged.");
                        }
                    }
                }, cooldown / 50L);
            }
        }
    }
}
