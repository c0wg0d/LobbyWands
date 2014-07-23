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
import java.util.Random;

/**
 * Created by Shawn on 7/21/2014.
 */
public class SpacetimeWand implements Listener {

    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getSpacetimeWand() {
        ItemStack wand = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.AQUA + "Spacetime Wand");
        im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&9&oBends Time and Space :o")));
        im.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 1, true);
        wand.setItemMeta(im);
        return wand;
    }


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onUseSpacetimeWand (PlayerInteractEvent event) {
        final Player p = event.getPlayer();

        if (!p.getItemInHand().getType().equals(Material.REDSTONE_TORCH_ON) || !p.getItemInHand().getItemMeta().hasDisplayName() || !p.getItemInHand().getItemMeta().getDisplayName().contains("Spacetime")) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            int dir = LobbyWands.getCardinalDirection(p);
            Location loc = p.getLocation();
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(50);

            if (chanceGoBoom == 1) {
                //loc.getWorld().playSound(loc, Sound.EXPLODE, 20, 10);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 1));
                p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Wingardium Propero!" + ChatColor.RESET + "" + ChatColor.GREEN + " - You now move with great speed!");
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.HORSE_GALLOP, 10, 10);
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

            if (System.currentTimeMillis() - LobbyWands.getInvisCooldown(p.getName()) < 45000) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging...");
            } else {
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 10);
                p.teleport(p.getTargetBlock(null, 20).getLocation());
                p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Teleporto!");
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
