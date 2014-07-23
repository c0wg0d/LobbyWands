package com.sandlotminecraft.lobbywands;

import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

/**
 * Created by Shawn on 7/21/2014.
 */
public class SpiderHandler implements Listener {

    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    @EventHandler
    public void onSpiderExplosionDamage (EntityDamageEvent event) {
        // Only cares about spiders taking damage
        if ((event.getEntityType() != EntityType.SPIDER && event.getEntityType() != EntityType.CAVE_SPIDER)) {
            event.setCancelled(true);
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            Bukkit.broadcastMessage("Spider damaged by explosion.");
            Player damager = null;
            for (Entity e : event.getEntity().getNearbyEntities(2,2,2)) {
                if (e instanceof Player) {
                    damager = (Player)e;
                    Bukkit.broadcastMessage("Found player " + damager.getName());
                    break;
                }
            }
            if (damager != null) {
                Bukkit.getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent(damager, event.getEntity(), EntityDamageEvent.DamageCause.CUSTOM, event.getDamage()));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSpiderDamageByPlayer (EntityDamageByEntityEvent event) {

        // Only cares about spiders taking damage
        if ((event.getEntityType() != EntityType.SPIDER && event.getEntityType() != EntityType.CAVE_SPIDER)) {
            return;
        }
        // Only cares if damage is done by a player


        //figure out how to give player credit for spider dying from explosion

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            // We made this event, so ignore it
            return;
        }

        Player p = ((Player) event.getDamager()).getPlayer();

        Entity spider = event.getEntity();

        // Player is using a Magic Wand
        if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().contains("Magic")) {
            Random rand = new Random();
            int wandlevel = WandExperience.getLevel(p.getItemInHand().getItemMeta().getLore().get(2));
            int chanceGoBoom = rand.nextInt(25);
            if (wandlevel >= 3) chanceGoBoom = rand.nextInt(20);
            if (wandlevel >= 6) chanceGoBoom = rand.nextInt(15);
            if (wandlevel >= 9) chanceGoBoom = rand.nextInt(10);

            chanceGoBoom = 1; //test

            if (chanceGoBoom == 1) {
                p.getWorld().createExplosion(spider.getLocation().getX(), spider.getLocation().getY(), spider.getLocation().getZ(), 2F, false, false);
                //p.getWorld().createExplosion(spider.getLocation(), 0.0F + wandlevel/5, false);
                spider.setVelocity(p.getLocation().getDirection().multiply(1).setY(2));
                p.setVelocity(p.getLocation().getDirection().multiply(-0.5).setY(0.5));
                p.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Expulso!");
            }
        }

        //Player is using a Blaze Wand
        if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().contains("Blaze")) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);

            if (chanceGoBoom == 1) {
                spider.setFireTicks(100);
                p.getLocation().getWorld().playSound(spider.getLocation(), Sound.GHAST_FIREBALL, 10, 10);
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incendio!");
            }
        }

        //Player is using a Spider Wand
        if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().contains("Spider")) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(5);

            if (chanceGoBoom == 1) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Immobulus!");
                if (spider instanceof LivingEntity)
                    ((LivingEntity) spider).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 8), true);
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 10, 10);

            }
        }

        //Player is using a Flower Wand
        if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().contains("Flower")) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);

            if (chanceGoBoom == 1) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Mutatio Flora!");
                if (spider instanceof LivingEntity)
                    spider.remove();
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.FUSE, 10, 10);

                final Location loc = spider.getLocation();
                Material mat = loc.getBlock().getType();
                if (mat == Material.AIR) {
                    Location blockBelow = new Location(p.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
                    mat = blockBelow.getBlock().getType();
                    if (mat != Material.AIR && mat.isSolid()) {
                        BlockState bs = loc.getBlock().getState();
                        bs.setType(Material.RED_ROSE);
                        bs.setRawData((byte) (rand.nextInt(7) + 1));
                        bs.update(true);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                loc.getWorld().createExplosion(loc.getBlockX(), loc.getY(), loc.getZ(), 1.5F, false, false);
                                loc.getBlock().setType(Material.AIR);
                            }
                        }, 50L + rand.nextInt(20));
                    }
                } else {
                    final Location newloc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
                    mat = newloc.getBlock().getType();
                    if (mat == Material.AIR) {
                        Location blockBelow = new Location(p.getWorld(), newloc.getX(), newloc.getY() - 1, newloc.getZ());
                        mat = blockBelow.getBlock().getType();
                        if (mat != Material.AIR && mat.isSolid()) {
                            BlockState bs = newloc.getBlock().getState();
                            bs.setType(Material.RED_ROSE);
                            bs.setRawData((byte) (rand.nextInt(7) + 1));
                            bs.update(true);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run() {
                                    newloc.getWorld().createExplosion(newloc.getBlockX(), newloc.getY(), newloc.getZ(), 1.5F, false, false);
                                    newloc.getBlock().setType(Material.AIR);
                                }
                            }, 50L + rand.nextInt(20));
                        }
                    }
                }
            }
        }
    }
}
