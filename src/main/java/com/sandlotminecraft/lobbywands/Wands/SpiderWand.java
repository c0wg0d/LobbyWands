package com.sandlotminecraft.lobbywands.Wands;

import com.sandlotminecraft.lobbywands.LobbyWands;
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

/**
 * Created by Shawn on 7/21/2014.
 */
public class SpiderWand implements Listener {

    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getSpiderWand() {
        ItemStack wand = new ItemStack(Material.TRIPWIRE_HOOK, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.GRAY + "Spider Wand");
        im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&9&oSpecial Extermination Edition")));
        im.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 2, true);
        wand.setItemMeta(im);
        return wand;
    }


    @EventHandler
    public void onUseSpiderWand (PlayerInteractEvent event) {
        final Player p = event.getPlayer();

        if (!p.getItemInHand().getType().equals(Material.TRIPWIRE_HOOK) || !p.getItemInHand().getItemMeta().hasDisplayName() || !p.getItemInHand().getItemMeta().getDisplayName().contains("Spider")) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            int dir = LobbyWands.getCardinalDirection(p);
            Location loc = p.getLocation();

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

            if (System.currentTimeMillis() - LobbyWands.getInvisCooldown(p.getName()) < 45000) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging...");
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 0));
                p.getLocation().getWorld().playSound(p.getLocation(),Sound.BLAZE_BREATH, 10, 10);
                p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Engorgio!" + ChatColor.RESET + "" + ChatColor.GRAY + " - You grow stronger!");
                LobbyWands.invisCooldown.put(p.getName(), System.currentTimeMillis());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is now recharged.");
                    }
                }, 900L);
            }
        }
    }


}
