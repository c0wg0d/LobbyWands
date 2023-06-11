package com.sandlotminecraft.lobbywands.Wands;

import com.sandlotminecraft.lobbywands.LobbyWands;
import com.sandlotminecraft.lobbywands.ParticleEffect;
import com.sandlotminecraft.lobbywands.WandExperience;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class BlazeWand
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getBlazeWand(boolean isMaxLevel) {
        ItemStack wand = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.RED + "Blaze Wand");
        String lore1 = ChatColor.translateAlternateColorCodes('&', "&9&oA Magical Fire Wand");
        String lore2 = ChatColor.DARK_AQUA + "0/100 XP";
        String lore3 = ChatColor.DARK_AQUA + "Level 1 Wand";
        int enchantmentLevel = 1;
        if(isMaxLevel) {
            lore2 = ChatColor.DARK_AQUA + "0/1000 XP";
            lore3 = ChatColor.DARK_AQUA + "Max Level Wand";
            enchantmentLevel = 2;
        }
        im.setLore(Arrays.asList(new String[]{lore1, lore2, lore3}));
        im.addEnchant(Enchantment.FIRE_ASPECT, enchantmentLevel, true);
        wand.setItemMeta(im);
        return wand;
    }

    @EventHandler
    public void onUseBlazeWand(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if ((!p.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)) || (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) || (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Blaze Wand"))) {
            return;
        }
        if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            int dir = LobbyWands.getCardinalDirection(p).intValue();
            Location loc = p.getEyeLocation();
            switch (dir) {
                case 1:
                    loc = new Location(p.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1.0D, loc.getYaw(), loc.getPitch());
                    break;
                case 2:
                    loc = new Location(p.getWorld(), loc.getX() + 0.5D, loc.getY(), loc.getZ() - 1.0D, loc.getYaw(), loc.getPitch());
                    break;
                case 5:
                    loc = new Location(p.getWorld(), loc.getX() + 1.0D, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 8:
                    loc = new Location(p.getWorld(), loc.getX() + 1.0D, loc.getY(), loc.getZ() + 1.0D, loc.getYaw(), loc.getPitch());
                    break;
                case 7:
                    loc = new Location(p.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1.0D, loc.getYaw(), loc.getPitch());
                    break;
                case 6:
                    loc = new Location(p.getWorld(), loc.getX() - 1.0D, loc.getY(), loc.getZ() + 1.0D, loc.getYaw(), loc.getPitch());
                    break;
                case 3:
                    loc = new Location(p.getWorld(), loc.getX() - 1.0D, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 0:
                    loc = new Location(p.getWorld(), loc.getX() - 1.0D, loc.getY(), loc.getZ() - 1.0D, loc.getYaw(), loc.getPitch());
            }
            loc.getWorld().spawnParticle(Particle.LAVA, loc, 1);
            loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 10.0F, 10.0F);
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            long cooldown = 90000 - (WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2)) - 1) * 8000;
            long timesince = System.currentTimeMillis() - LobbyWands.getCooldown(p.getName(), "blaze");
            if (timesince < cooldown) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging. You can use it again in " + (cooldown - timesince) / 1000L + " seconds.");
            } else {
                Vector dir = p.getEyeLocation().getDirection().multiply(2);
                Fireball fb = (Fireball) p.launchProjectile(Fireball.class, dir);
                fb = (Fireball) p.getWorld().spawn(p.getEyeLocation().add(dir.getX(), dir.getY(), dir.getZ()), Fireball.class);
                fb.setShooter(p);
                fb.setYield(1.5F + WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2)) / 4);
                fb.setBounce(false);

                fb.setIsIncendiary(false);
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 10.0F, 10.0F);
                LobbyWands.setCooldown(p.getName(), "blaze");
                p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Flagrante!");

                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "Your Blaze Wand is now recharged.");
                        }
                    }
                }, cooldown / 50L);
            }
        }
    }

    @EventHandler
    public void onFireballExplode(EntityExplodeEvent event) {
        if ((event.getEntity() instanceof Fireball)) {
            event.setCancelled(true);
        }
    }
}
