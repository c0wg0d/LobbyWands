package com.sandlotminecraft.lobbywands.Wands;

import com.sandlotminecraft.lobbywands.LobbyWands;
import com.sandlotminecraft.lobbywands.ParticleEffect;
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
import java.util.HashSet;
import java.util.Random;

public class SpacetimeWand
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getSpacetimeWand(boolean isMaxLevel) {
        ItemStack wand = new ItemStack(Material.REDSTONE_TORCH, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.AQUA + "Spacetime Wand");
        String lore1 = ChatColor.translateAlternateColorCodes('&', "&9&oBends Time and Space :o");
        String lore2 = ChatColor.DARK_AQUA + "0/100 XP";
        String lore3 = ChatColor.DARK_AQUA + "Level 1 Wand";
        int enchantmentLevel = 1;
        if(isMaxLevel) {
            lore2 = ChatColor.DARK_AQUA + "0/1000 XP";
            lore3 = ChatColor.DARK_AQUA + "Max Level Wand";
            enchantmentLevel = 2;
        }
        im.setLore(Arrays.asList(new String[]{lore1, lore2, lore3}));
        im.addEnchant(Enchantment.KNOCKBACK, enchantmentLevel, true);
        wand.setItemMeta(im);
        return wand;
    }

    @EventHandler
    public void onUseSpacetimeWand(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if ((!p.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE_TORCH)) || (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) || (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Spacetime"))) {
            return;
        }
        if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            int dir = LobbyWands.getCardinalDirection(p).intValue();
            Location loc = p.getEyeLocation();
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(50);
            if (chanceGoBoom == 1) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 1));
                p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Wingardium Propero!" + ChatColor.RESET + "" + ChatColor.GREEN + " - You now move with great speed!");
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_GALLOP, 10.0F, 10.0F);
                return;
            }
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
            loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, dir, 10);
            loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HAT, 3.0F, 10.0F);
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            long cooldown = 90000 - (WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2)) - 1) * 6000;
            long timesince = System.currentTimeMillis() - LobbyWands.getCooldown(p.getName(), "spacetime");
            int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
            if (timesince < cooldown) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging. You can use it again in " + (cooldown - timesince) / 1000L + " seconds.");
            } else {
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10.0F, 10.0F);

                Location loc = p.getLocation();
                float yaw = loc.getYaw();
                float pitch = loc.getPitch();
                Location destination = p.getTargetBlock(null, 20 + wandlevel * 2).getLocation();
                destination.setYaw(yaw);
                destination.setPitch(pitch);
                if(destination.getBlock().getType() != Material.AIR) {
                    destination.setY(destination.getY() + 1);
                }
                p.teleport(destination);

                p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Teleporto!");
                LobbyWands.setCooldown(p.getName(), "spacetime");
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "Your Spacetime Wand is now recharged.");
                        }
                    }
                }, cooldown / 50L);
            }
        }
    }
}
