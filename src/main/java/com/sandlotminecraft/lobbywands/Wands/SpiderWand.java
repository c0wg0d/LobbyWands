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

public class SpiderWand
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getSpiderWand() {
        ItemStack wand = new ItemStack(Material.TRIPWIRE_HOOK, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.GRAY + "Spider Wand");
        im.setLore(Arrays.asList(new String[]{ChatColor.translateAlternateColorCodes('&', "&9&oSpecial Extermination Edition"), ChatColor.DARK_AQUA + "0/100 XP", ChatColor.DARK_AQUA + "Level 1 Wand"}));
        im.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 2, true);
        wand.setItemMeta(im);
        return wand;
    }

    @EventHandler
    public void onUseSpiderWand(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if ((!p.getInventory().getItemInMainHand().getType().equals(Material.TRIPWIRE_HOOK)) || (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) || (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Spider"))) {
            return;
        }
        if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            int dir = LobbyWands.getCardinalDirection(p).intValue();
            Location loc = p.getEyeLocation();
            switch (dir) {
                case 1:
                    loc = new Location(p.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 0.75D, loc.getYaw(), loc.getPitch());
                    break;
                case 2:
                    loc = new Location(p.getWorld(), loc.getX() + 0.5D, loc.getY(), loc.getZ() - 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 5:
                    loc = new Location(p.getWorld(), loc.getX() + 0.75D, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 8:
                    loc = new Location(p.getWorld(), loc.getX() + 0.5D, loc.getY(), loc.getZ() + 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 7:
                    loc = new Location(p.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 0.75D, loc.getYaw(), loc.getPitch());
                    break;
                case 6:
                    loc = new Location(p.getWorld(), loc.getX() - 0.5D, loc.getY(), loc.getZ() + 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 3:
                    loc = new Location(p.getWorld(), loc.getX() - 0.75D, loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 0:
                    loc = new Location(p.getWorld(), loc.getX() - 0.5D, loc.getY(), loc.getZ() - 0.5D, loc.getYaw(), loc.getPitch());
            }
            loc.getWorld().playEffect(loc, Effect.SMOKE, dir, 10);
            loc.getWorld().playSound(loc, Sound.BLOCK_SNOW_BREAK, 3.0F, 10.0F);
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            long cooldown = 90000 - (WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2)) - 1) * 6000;
            long timesince = System.currentTimeMillis() - LobbyWands.getCooldown(p.getName(), "spider");
            int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
            if (timesince < cooldown) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging. You can use it again in " + (cooldown - timesince) / 1000L + " seconds.");
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, wandlevel > 4 ? 1 : 0));
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 10.0F, 10.0F);
                p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Engorgio!" + ChatColor.RESET + "" + ChatColor.GRAY + " - You grow stronger!");
                LobbyWands.setCooldown(p.getName(), "spider");
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        p.sendMessage(ChatColor.GRAY + "Your increased strength has faded.");
                    }
                }, 300L);


                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "Your Spider Wand is now recharged.");
                        }
                    }
                }, cooldown / 50L);
            }
        }
    }
}
