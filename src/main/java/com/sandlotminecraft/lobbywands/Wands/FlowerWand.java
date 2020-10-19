package com.sandlotminecraft.lobbywands.Wands;

import com.sandlotminecraft.lobbywands.LobbyWands;
import com.sandlotminecraft.lobbywands.ParticleEffect;
import com.sandlotminecraft.lobbywands.WandExperience;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class FlowerWand
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");

    public static ItemStack getFlowerWand(boolean isMaxLevel) {
        ItemStack wand = new ItemStack(Material.ALLIUM, 1);
        ItemMeta im = wand.getItemMeta();
        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Flower Wand");
        String lore1 = ChatColor.translateAlternateColorCodes('&', "&9&oMakes &dF&el&fo&bw&5e&9r&ds &9&oHappen :D");
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
    public void onUseFlowerWand(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if ((!p.getInventory().getItemInMainHand().getType().equals(Material.ALLIUM)) || (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) || (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Flower Wand"))) {
            return;
        }
        if ((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            int dir = LobbyWands.getCardinalDirection(p).intValue();
            Location loc = p.getLocation();
            switch (dir) {
                case 1:
                    loc = new Location(p.getWorld(), loc.getX(), loc.getY() + 1.0D, loc.getZ() - 0.75D, loc.getYaw(), loc.getPitch());
                    break;
                case 2:
                    loc = new Location(p.getWorld(), loc.getX() + 0.5D, loc.getY() + 1.0D, loc.getZ() - 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 5:
                    loc = new Location(p.getWorld(), loc.getX() + 0.75D, loc.getY() + 1.0D, loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 8:
                    loc = new Location(p.getWorld(), loc.getX() + 0.5D, loc.getY() + 1.0D, loc.getZ() + 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 7:
                    loc = new Location(p.getWorld(), loc.getX(), loc.getY() + 1.0D, loc.getZ() + 0.75D, loc.getYaw(), loc.getPitch());
                    break;
                case 6:
                    loc = new Location(p.getWorld(), loc.getX() - 0.5D, loc.getY() + 1.0D, loc.getZ() + 0.5D, loc.getYaw(), loc.getPitch());
                    break;
                case 3:
                    loc = new Location(p.getWorld(), loc.getX() - 0.75D, loc.getY() + 1.0D, loc.getZ(), loc.getYaw(), loc.getPitch());
                    break;
                case 0:
                    loc = new Location(p.getWorld(), loc.getX() - 0.5D, loc.getY() + 1.0D, loc.getZ() - 0.5D, loc.getYaw(), loc.getPitch());
            }
            //ParticleEffect.HEART.display(0.0F, 0.0F, 0.0F, 0.0F, 5, loc, 30);
            loc.getWorld().spawnParticle(Particle.HEART, loc, 1);
            p.getLocation().getWorld().playSound(loc, Sound.ENTITY_SILVERFISH_AMBIENT, 10.0F, 10.0F);
        }
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            long cooldown = 90000 - (WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2)) - 1) * 6000;
            long timesince = System.currentTimeMillis() - LobbyWands.getCooldown(p.getName(), "flower");
            final int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
            if (timesince < cooldown) {
                p.sendMessage(ChatColor.DARK_PURPLE + "Your wand is still recharging. You can use it again in " + (cooldown - timesince) / 1000L + " seconds.");
            } else {
                Random rand = new Random();
                int[] n = new int[10];
                n[0] = (rand.nextInt(8) - 4);
                n[1] = (rand.nextInt(8) - 4);
                n[2] = (rand.nextInt(8) - 4);
                n[3] = (rand.nextInt(8) - 4);
                n[4] = (rand.nextInt(8) - 4);
                n[5] = (rand.nextInt(8) - 4);
                n[6] = (rand.nextInt(8) - 4);
                n[7] = (rand.nextInt(8) - 4);
                n[8] = (rand.nextInt(8) - 4);
                n[9] = (rand.nextInt(8) - 4);


                int flowersPlaced = 0;
                int tried = 0;
                int flowersAllowed = 2 + WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2)) / 3;
                do {
                    final Location tloc = new Location(p.getWorld(), p.getLocation().getX() + n[rand.nextInt(10)], p.getLocation().getY(), p.getLocation().getZ() + n[((rand.nextInt(10) + 1) % 4)]);
                    Material mat = tloc.getBlock().getType();
                    tried++;
                    if (mat == Material.AIR) {
                        Location blockBelow = new Location(p.getWorld(), tloc.getX(), tloc.getY() - 1.0D, tloc.getZ());
                        mat = blockBelow.getBlock().getType();
                        if ((mat != Material.AIR) && (mat.isSolid())) {
                            BlockState bs = tloc.getBlock().getState();
                            bs.setType(Material.ALLIUM);
                            bs.setRawData((byte) (rand.nextInt(7) + 1));
                            bs.update(true);
                            flowersPlaced++;
                            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                                public void run() {
                                    tloc.getWorld().createExplosion(tloc.getBlockX(), tloc.getY(), tloc.getZ(), 0.5F + wandlevel * 0.25F, false, false);

                                    tloc.getBlock().setType(Material.AIR);
                                    WandExperience.flowerplaced = true;
                                    WandExperience.pSetExplosion = p;
                                    FlowerWand.this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(FlowerWand.this.plugin, new Runnable() {
                                        public void run() {
                                            WandExperience.flowerplaced = false;
                                        }
                                    }, 10L);
                                }
                            }, 50L + rand.nextInt(20));
                        }
                    } else {
                        final Location blockAbove = new Location(p.getWorld(), tloc.getX(), tloc.getY() + 1.0D, tloc.getZ());
                        mat = blockAbove.getBlock().getType();
                        if ((mat == Material.AIR) && (tloc.getBlock().getType().isSolid())) {
                            BlockState bs = blockAbove.getBlock().getState();
                            bs.setType(Material.ALLIUM);
                            bs.setRawData((byte) (rand.nextInt(7) + 1));
                            bs.update(true);
                            flowersPlaced++;
                            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                                public void run() {
                                    blockAbove.getWorld().createExplosion(blockAbove.getX(), blockAbove.getY(), blockAbove.getZ(), 0.5F + wandlevel * 0.25F, false, false);

                                    blockAbove.getBlock().setType(Material.AIR);
                                    WandExperience.flowerplaced = true;
                                    WandExperience.pSetExplosion = p;
                                    FlowerWand.this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(FlowerWand.this.plugin, new Runnable() {
                                        public void run() {
                                            WandExperience.flowerplaced = false;
                                        }
                                    }, 10L);
                                }
                            }, 50L + rand.nextInt(20));
                        }
                    }
                } while ((flowersPlaced <= flowersAllowed) && (tried <= 20));
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 10.0F, 10.0F);
                p.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Orchideous!");
                LobbyWands.setCooldown(p.getName(), "flower");
                this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        if (p.isOnline()) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "Your Flower Wand is now recharged.");
                        }
                    }
                }, cooldown / 50L);
            }
        }
    }

    @EventHandler
    public void onCreateItem(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getType() == Material.ALLIUM) {
            event.setCancelled(true);
        }
    }

    public static Entity[] getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - radius % 16) / 16;
        HashSet<Entity> radiusEntities = new HashSet();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX();
                int y = (int) l.getY();
                int z = (int) l.getZ();
                for (Entity e : new Location(l.getWorld(), x + chX * 16, y, z + chZ * 16).getChunk().getEntities()) {
                    if ((e.getLocation().distance(l) <= radius) && (e.getLocation().getBlock() != l.getBlock())) {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return (Entity[]) radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
}
