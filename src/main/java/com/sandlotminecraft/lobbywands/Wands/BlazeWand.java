package com.sandlotminecraft.lobbywands.Wands;

import com.sandlotminecraft.lobbywands.LobbyWands;
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
import java.util.Random;

/**
 * Created by Shawn on 7/21/2014.
 */
public class BlazeWand implements Listener {

    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getBlazeWand() {
        ItemStack wand = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.RED + "Blaze Wand");
        im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&9&oA Magical Fire Wand")));
        im.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 1, true);
        wand.setItemMeta(im);
        return wand;
    }


    @EventHandler
    public void onUseBlazeWand (PlayerInteractEvent event) {
        final Player p = event.getPlayer();

        if (!p.getItemInHand().getType().equals(Material.BLAZE_ROD)) {// || !p.getItemInHand().getItemMeta().hasDisplayName() || !p.getItemInHand().getItemMeta().getDisplayName().contains("Blaze Wand")) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            int dir = LobbyWands.getCardinalDirection(p);
            Location loc = p.getLocation();
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(100);

            if (chanceGoBoom == 1) {
                p.setFireTicks(300);
                loc.getWorld().playSound(loc, Sound.FIRE_IGNITE, 10, 10);
                p.sendMessage(ChatColor.DARK_PURPLE + "Oops...");
                return;
            }

            switch(dir) {
                case 1 : loc = new Location(p.getWorld(), loc.getX(), loc.getY()+1, loc.getZ() - 1, loc.getYaw(), loc.getPitch());
                    break;
                case 2 : loc = new Location(p.getWorld(), loc.getX() + .5, loc.getY()+1, loc.getZ() - 1, loc.getYaw(), loc.getPitch());
                    break;
                case 5 : loc = new Location(p.getWorld(), loc.getX() + 1, loc.getY()+1, loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 8 : loc = new Location(p.getWorld(), loc.getX() + 1, loc.getY()+1, loc.getZ() + 1, loc.getYaw(), loc.getPitch());
                    break;
                case 7 : loc = new Location(p.getWorld(), loc.getX(), loc.getY()+1, loc.getZ() + 1, loc.getYaw(), loc.getPitch());
                    break;
                case 6 : loc = new Location(p.getWorld(), loc.getX() - 1, loc.getY()+1, loc.getZ() + 1, loc.getYaw(), loc.getPitch());
                    break;
                case 3 : loc = new Location(p.getWorld(), loc.getX() - 1, loc.getY()+1, loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 0 : loc = new Location(p.getWorld(), loc.getX() - 1, loc.getY()+1, loc.getZ() - 1, loc.getYaw(), loc.getPitch());
                    break;
            }

            loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, dir, 10);
            loc.getWorld().playSound(loc, Sound.FIRE, 10, 10);
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (System.currentTimeMillis() - LobbyWands.getInvisCooldown(p.getName()) < 45000) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging...");
            } else {
                Vector dir = p.getEyeLocation().getDirection().multiply(2);
                Fireball fb = p.getWorld().spawn(p.getEyeLocation().add(dir.getX(), dir.getY(), dir.getZ()), Fireball.class);
                fb.setShooter(p);
                fb.setYield(4.0F);
                fb.setBounce(false);
                //fb.setYield(0.0F);
                fb.setIsIncendiary(false);
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 10, 10);
                LobbyWands.invisCooldown.put(p.getName(), System.currentTimeMillis());

                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is now recharged.");
                    }
                }, 1200L);
            }
        }
    }

    @EventHandler
    public void onFireballExplode (EntityExplodeEvent event) {
        if (event.getEntity() instanceof Fireball) {
            event.setCancelled(true);
        }
    }
}
