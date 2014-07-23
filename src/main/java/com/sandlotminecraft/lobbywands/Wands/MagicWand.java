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

/**
 * Created by Shawn on 7/21/2014.
 */
public class MagicWand implements Listener {

    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getMagicWand() {
        ItemStack wand = new ItemStack(Material.STICK, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Magic Wand");
        im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&9&oMakes Magic Happen :D"), ChatColor.DARK_AQUA + "0/10 XP", ChatColor.DARK_AQUA + "Level 1 Wand"));
        im.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 1, true);
        wand.setItemMeta(im);
        return wand;
    }


    @EventHandler
    public void onUseMagicWand (PlayerInteractEvent event) {
        final Player p = event.getPlayer();

        if (!p.getItemInHand().getType().equals(Material.STICK) || !p.getItemInHand().getItemMeta().hasDisplayName() || !p.getItemInHand().getItemMeta().getDisplayName().contains("Magic Wand")) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            int dir = LobbyWands.getCardinalDirection(p);
            Location loc = p.getLocation();
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(100);

            if (chanceGoBoom == 1) {
                //loc.getWorld().playSound(loc, Sound.EXPLODE, 20, 10);
                p.getWorld().createExplosion(loc, 0.0F, false);
                p.setVelocity(p.getVelocity().setY(rand.nextInt(4) + 3));
                p.sendMessage(ChatColor.DARK_PURPLE + "Oops...");
                return;
            }

            switch(dir) {
                case 1 : loc = new Location(p.getWorld(), loc.getX(), loc.getY()+1, loc.getZ() - 0.75, loc.getYaw(), loc.getPitch());
                    break;
                case 2 : loc = new Location(p.getWorld(), loc.getX() + 0.5, loc.getY()+1, loc.getZ() - 0.5, loc.getYaw(), loc.getPitch());
                    break;
                case 5 : loc = new Location(p.getWorld(), loc.getX() + 0.75, loc.getY()+1, loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 8 : loc = new Location(p.getWorld(), loc.getX() + 0.5, loc.getY()+1, loc.getZ() + 0.5, loc.getYaw(), loc.getPitch());
                    break;
                case 7 : loc = new Location(p.getWorld(), loc.getX(), loc.getY()+1, loc.getZ() + 0.75, loc.getYaw(), loc.getPitch());
                    break;
                case 6 : loc = new Location(p.getWorld(), loc.getX() - 0.5, loc.getY()+1, loc.getZ() + 0.5, loc.getYaw(), loc.getPitch());
                    break;
                case 3 : loc = new Location(p.getWorld(), loc.getX() - 0.75, loc.getY()+1, loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 0 : loc = new Location(p.getWorld(), loc.getX() - 0.5, loc.getY()+1, loc.getZ() - 0.5, loc.getYaw(), loc.getPitch());
                    break;
            }

            loc.getWorld().playEffect(loc, Effect.SMOKE, dir, 10);
            loc.getWorld().playSound(loc, Sound.FIZZ, 3, 10);
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            long cooldown = 90000 - (WandExperience.getLevel(p.getItemInHand().getItemMeta().getLore().get(2)) * 6000);
            long timesince = System.currentTimeMillis() - LobbyWands.getInvisCooldown(p.getName());
            if (timesince < cooldown) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging. You can use it again in " + ((cooldown - timesince) / 1000) + " seconds.");
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0));
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 3, 10);
                p.sendMessage(ChatColor.DARK_PURPLE + "You've vanished!");
                LobbyWands.invisCooldown.put(p.getName(), System.currentTimeMillis());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) p.sendMessage(ChatColor.DARK_PURPLE + "You've reappeared!");
                        if (p.isOnline()) p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 3, 10);
                    }
                }, 300L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is now recharged.");
                    }
                }, 900L);
            }
        }
    }


}
