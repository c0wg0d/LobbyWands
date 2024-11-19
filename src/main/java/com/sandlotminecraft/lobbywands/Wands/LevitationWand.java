package com.sandlotminecraft.lobbywands.Wands;

import com.sandlotminecraft.lobbywands.LobbyWands;
import com.sandlotminecraft.lobbywands.WandExperience;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
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

import static com.sandlotminecraft.lobbywands.Targeter.getTargetEntity;

public class LevitationWand
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getLevitationWand(boolean isMaxLevel) {
        ItemStack wand = new ItemStack(Material.FEATHER, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.DARK_AQUA + "Levitation Wand");
        String lore1 = ChatColor.translateAlternateColorCodes('&', "&9&oIt's LeviOsa, not LeviosA");
        String lore2 = ChatColor.DARK_AQUA + "0/100 XP";
        String lore3 = ChatColor.DARK_AQUA + "Level 1 Wand";
        int arthropodsLevel = 1;
        if(isMaxLevel) {
            lore2 = ChatColor.DARK_AQUA + "0/1000 XP";
            lore3 = ChatColor.DARK_AQUA + "Max Level Wand";
            arthropodsLevel = 4;
        }
        im.setLore(Arrays.asList(new String[]{lore1, lore2, lore3}));
        im.addEnchant(Enchantment.DAMAGE_ARTHROPODS, arthropodsLevel, true);
        wand.setItemMeta(im);
        return wand;
    }

    @EventHandler
    public void onUseLevitationWand(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if ((!p.getInventory().getItemInMainHand().getType().equals(Material.FEATHER))
                || (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                || (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Levitation Wand"))) {
            return;
        }
        int wandLevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
        if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            Location loc = p.getEyeLocation();
            loc.getWorld().spawnParticle(Particle.GLOW, loc, 1);
            loc.getWorld().playSound(loc, Sound.ENTITY_SHULKER_OPEN, 3.0F, 10.0F);

            // Shoot a shulker bullet at the targeted entity
            Entity e = getTargetEntity(p);
            if(e != null && e instanceof Monster) {
                Random rand = new Random();
                int chanceGoBoom = rand.nextInt(20);
                int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
                if (wandlevel >= 3) {
                    chanceGoBoom = rand.nextInt(15);
                }
                if (wandlevel >= 6) {
                    chanceGoBoom = rand.nextInt(10);
                }
                if (wandlevel >= 9) {
                    chanceGoBoom = rand.nextInt(5);
                }
                if (chanceGoBoom == 1) {
                    loc.getWorld().playSound(loc, Sound.ENTITY_SHULKER_SHOOT, 3.0F, 10.0F);
                    ShulkerBullet bullet = p.launchProjectile(ShulkerBullet.class);
                    bullet.setShooter(p);
                    bullet.setTarget(e);

                    WandExperience.CausedByPlayer = true;
                    WandExperience.PlayerToCredit = p;
                    LevitationWand.this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(LevitationWand.this.plugin, new Runnable() {
                        public void run() {
                            WandExperience.CausedByPlayer = false;
                        }
                    }, 200L);
                }
            }
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            long cooldown = 90000 - (WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2)) - 1) * 6000;
            long timesince = System.currentTimeMillis() - LobbyWands.getCooldown(p.getName(), "levitation");
            if (p.hasPotionEffect(PotionEffectType.LEVITATION)) {
                p.removePotionEffect(PotionEffectType.LEVITATION);
            }
            if (timesince < cooldown) {
                p.sendMessage(ChatColor.DARK_AQUA + "Your wand is still recharging. You can use it again in " + (cooldown - timesince) / 1000L + " seconds.");
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, wandLevel * 60, 0));
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 3.0F, 10.0F);
                p.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Wingardium Leviosa!");
                LobbyWands.setCooldown(p.getName(), "levitation");
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "Your Levitation Wand is now recharged.");
                        }
                    }
                }, cooldown / 50L);
            }
        }
    }
}
