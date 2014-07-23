package com.sandlotminecraft.lobbywands.Wands;

import com.sandlotminecraft.lobbywands.LobbyWands;
import com.sandlotminecraft.lobbywands.ParticleEffect;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by Shawn on 7/21/2014.
 */
public class FlowerWand implements Listener {

    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getFlowerWand() {
        ItemStack wand = new ItemStack(Material.RED_ROSE, 1);
        wand.setDurability((short) 2);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Flower Wand");
        im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&9&oMakes &dF&el&fo&bw&5e&9r&ds &9&oHappen :D")));
        im.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 1, true);
        wand.setItemMeta(im);
        return wand;
    }


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onUseFlowerWand (PlayerInteractEvent event) {
        final Player p = event.getPlayer();

        if (!p.getItemInHand().getType().equals(Material.RED_ROSE) || !p.getItemInHand().getItemMeta().hasDisplayName() || !p.getItemInHand().getItemMeta().getDisplayName().contains("Flower Wand")) {
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

            ParticleEffect.HEART.display(loc,0,0,0,1,1);
            p.getLocation().getWorld().playSound(loc, Sound.SILVERFISH_IDLE,10,10);
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (System.currentTimeMillis() - LobbyWands.getInvisCooldown(p.getName()) < 45000) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging...");
            } else {
                //generate 5 random integers from -4 to 4
                Random rand = new Random();
                int n[] = new int[5];
                n[0] = rand.nextInt(8) - 4;
                n[1] = rand.nextInt(8) - 4;
                n[2] = rand.nextInt(8) - 4;
                n[3] = rand.nextInt(8) - 4;
                n[4] = rand.nextInt(8) - 4;


                // Places blocks on same level as player, or one block higher - checks for a block underneath
                for ( int i = 0; i < 5; i++) {
                    final Location tloc = new Location(p.getWorld(), p.getLocation().getX() + n[i], p.getLocation().getY(), p.getLocation().getZ() + n[(i+1)%4]);
                    Material mat = tloc.getBlock().getType();
                    if (mat == Material.AIR) {
                        Location blockBelow = new Location(p.getWorld(), tloc.getX(),tloc.getY()-1,tloc.getZ());
                        mat = blockBelow.getBlock().getType();
                        if (mat != Material.AIR && mat.isSolid()) {
                            BlockState bs = tloc.getBlock().getState();
                            bs.setType(Material.RED_ROSE);
                            bs.setRawData((byte)(rand.nextInt(7)+1));
                            bs.update(true);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run() {
                                    tloc.getWorld().createExplosion(tloc.getBlockX(),tloc.getY(),tloc.getZ(),1.5F,false,false);
                                    tloc.getBlock().setType(Material.AIR);
                                    for (Entity nearby : getNearbyEntities(tloc, 5)) {
                                        if (nearby.getType() == EntityType.SPIDER || nearby.getType() == EntityType.CAVE_SPIDER) {
                                            nearby.setLastDamageCause(new EntityDamageEvent(nearby, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 0.5D));
                                        }
                                    }
                                }
                            }, 50L + rand.nextInt(20));
                        }
                    } else {
                        final Location blockAbove = new Location(p.getWorld(), tloc.getX(), tloc.getY()+1, tloc.getZ());
                        mat = blockAbove.getBlock().getType();
                        if (mat == Material.AIR && tloc.getBlock().getType().isSolid()) {
                            BlockState bs = blockAbove.getBlock().getState();
                            bs.setType(Material.RED_ROSE);
                            bs.setRawData((byte) (rand.nextInt(7) + 1));
                            bs.update(true);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run() {
                                    blockAbove.getWorld().createExplosion(blockAbove.getX(),blockAbove.getY(),blockAbove.getZ(),1.5F,false,false);
                                    blockAbove.getBlock().setType(Material.AIR);
                                    for (Entity nearby : getNearbyEntities(tloc, 5)) {
                                        if (nearby.getType() == EntityType.SPIDER || nearby.getType() == EntityType.CAVE_SPIDER) {
                                            nearby.setLastDamageCause(new EntityDamageEvent(nearby, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 0.5D));
                                        }
                                    }
                                }
                            }, 50L + rand.nextInt(20));
                        }
                    }
                }
                p.getLocation().getWorld().playSound(p.getLocation(),Sound.FUSE, 10, 10);
                p.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Orchideous!");
                LobbyWands.invisCooldown.put(p.getName(), System.currentTimeMillis());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is now recharged.");
                    }
                }, 900L);
            }
        }
    }

    @EventHandler
    public void onCreateItem (ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getType() == Material.RED_ROSE)
            event.setCancelled(true);
    }

    public static Entity[] getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet <>();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    @EventHandler
    public void onFlowerPlace (BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.RED_ROSE)
            event.setCancelled(true);
    }
}
