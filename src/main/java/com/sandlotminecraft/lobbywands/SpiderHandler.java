package com.sandlotminecraft.lobbywands;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.Random;

public class SpiderHandler
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");
    private boolean lightning = false;

    @EventHandler
    public void onSpiderDamageByPlayer(EntityDamageByEntityEvent event) {
        if ((event.getEntityType() != EntityType.SPIDER) && (event.getEntityType() != EntityType.CAVE_SPIDER)) {
            return;
        }
        if (this.lightning) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        final Player p = ((Player) event.getDamager()).getPlayer();

        Entity spider = event.getEntity();
        if ((p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName()) && (p.getItemInHand().getItemMeta().getDisplayName().contains("Magic"))) {
            Random rand = new Random();
            int wandlevel = WandExperience.getLevel((String) p.getItemInHand().getItemMeta().getLore().get(2));
            int chanceGoBoom = rand.nextInt(20);
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
                Potion potion = new Potion(PotionType.INSTANT_DAMAGE, wandlevel > 4 ? 2 : 1);
                potion.setSplash(true);
                ItemStack itemStack = new ItemStack(Material.POTION);
                potion.apply(itemStack);
                ThrownPotion thrownPotion = (ThrownPotion) p.launchProjectile(ThrownPotion.class);
                thrownPotion.setShooter(p);
                thrownPotion.setItem(itemStack);
                p.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Stupefy Regio!");
            }
        }
        if ((p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName()) && (p.getItemInHand().getItemMeta().getDisplayName().contains("Blaze"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            int wandlevel = WandExperience.getLevel((String) p.getItemInHand().getItemMeta().getLore().get(2));
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
                spider.setFireTicks(100 + wandlevel * 5);
                for (Entity setfire : spider.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
                    if ((setfire.getType() == EntityType.SPIDER) || (setfire.getType() == EntityType.CAVE_SPIDER)) {
                        setfire.setFireTicks(100 + wandlevel * 5);
                    }
                }
                p.getLocation().getWorld().playSound(spider.getLocation(), Sound.GHAST_FIREBALL, 10.0F, 10.0F);
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incendio!");
            }
        }
        if ((p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName()) && (p.getItemInHand().getItemMeta().getDisplayName().contains("Spacetime"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            int wandlevel = WandExperience.getLevel((String) p.getItemInHand().getItemMeta().getLore().get(2));
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
                Entity[] warpspiders = new LivingEntity[16];
                int spidercount = 1;
                warpspiders[0] = spider;
                p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Wingardium Leviosa!");
                for (Entity nearby : spider.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
                    if (((nearby.getType() == EntityType.SPIDER) || (nearby.getType() == EntityType.CAVE_SPIDER)) && (spidercount < 15)) {
                        warpspiders[spidercount] = nearby;
                        spidercount++;
                    }
                }
                for (int i = 0; i <= spidercount; i++) {
                    if ((warpspiders[i] != null) && ((warpspiders[i].getType() == EntityType.SPIDER) || (warpspiders[i].getType() == EntityType.CAVE_SPIDER))) {
                        Location loc = warpspiders[i].getLocation();
                        for (int j = 1; j < 6 + wandlevel; j++) {
                            loc = loc.add(0.0D, 1.0D, 0.0D);
                            if (loc.getBlock().getType() != Material.AIR) {
                                loc = loc.subtract(0.0D, 1.0D, 0.0D);
                                break;
                            }
                        }
                        warpspiders[i].getLocation().getWorld().playEffect(warpspiders[i].getLocation(), Effect.ENDER_SIGNAL, 5);
                        warpspiders[i].getLocation().getWorld().playSound(warpspiders[i].getLocation(), Sound.ENDERMAN_TELEPORT, 10.0F, 10.0F);
                        warpspiders[i].teleport(loc);
                    }
                }
            }
        }
        if ((p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName()) && (p.getItemInHand().getItemMeta().getDisplayName().contains("Spider"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            final int wandlevel = WandExperience.getLevel((String) p.getItemInHand().getItemMeta().getLore().get(2));
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
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Magna Tonitrus!");
                int strikes = 2 + wandlevel / 3;
                Entity[] spiderstrike = new Entity[strikes + 1];
                spiderstrike[0] = spider;

                int count = 1;
                for (Entity near : spider.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
                    if ((near.getType() == EntityType.SPIDER) || (near.getType() == EntityType.CAVE_SPIDER)) {
                        spiderstrike[count] = near;
                        count++;
                        if (count > strikes) {
                            break;
                        }
                    }
                }
                count = 1;
                spider.getWorld().strikeLightningEffect(spider.getLocation());
                for (Entity dspider : spider.getNearbyEntities(2.0D, 2.0D, 2.0D)) {
                    if ((dspider.getType() == EntityType.SPIDER) || (dspider.getType() == EntityType.CAVE_SPIDER)) {
                        this.lightning = true;
                        ((LivingEntity) dspider).damage(wandlevel >= 4 ? 3.0D : 2.0D, p);
                        this.lightning = false;
                    }
                }
                for (Entity aSpiderstrike : spiderstrike) {
                    if (aSpiderstrike != null) {
                        final Entity struck = aSpiderstrike;
                        count++;
                        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                            public void run() {
                                if (struck.isValid()) {
                                    struck.getWorld().strikeLightningEffect(struck.getLocation());
                                }
                                for (Entity dspider : struck.getNearbyEntities(2.0D, 2.0D, 2.0D)) {
                                    if ((dspider.getType() == EntityType.SPIDER) || (dspider.getType() == EntityType.CAVE_SPIDER)) {
                                        SpiderHandler.this.lightning = true;
                                        ((LivingEntity) dspider).damage(wandlevel >= 4 ? 3.0D : 2.0D, p);
                                        SpiderHandler.this.lightning = false;
                                    }
                                }
                            }
                        }, 5L * count + rand.nextInt(3));
                    }
                }
            }
        }
        if ((p.getItemInHand().hasItemMeta()) && (p.getItemInHand().getItemMeta().hasDisplayName()) && (p.getItemInHand().getItemMeta().getDisplayName().contains("Flower"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            final int wandlevel = WandExperience.getLevel((String) p.getItemInHand().getItemMeta().getLore().get(2));
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
                if ((spider instanceof LivingEntity)) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Mutatio Flora!");
                    event.setDamage(0.0D);
                    ItemStack rosebush = new ItemStack(175, 1, (short) 0, Byte.valueOf((byte) 4));
                    Item flower = spider.getWorld().dropItem(spider.getLocation(), rosebush);
                    flower.setPickupDelay(1000);
                    spider.setPassenger(flower);
                    final LivingEntity flowerspider = (LivingEntity) spider;
                    final Item sflower = flower;
                    p.getLocation().getWorld().playSound(p.getLocation(), Sound.WITHER_SHOOT, 10.0F, 10.0F);
                    ParticleEffect.HEART.display(1.0F, 1.0F, 1.0F, 5.0F, 10, spider.getLocation(), 30);
                    this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                        public void run() {
                            if (!flowerspider.isDead()) {
                                if (flowerspider.getPassenger() == sflower) {
                                    sflower.remove();
                                }
                                flowerspider.getLocation().getWorld().createExplosion(flowerspider.getLocation().getX(), flowerspider.getLocation().getY(), flowerspider.getLocation().getZ(), 0.5F + wandlevel * 0.25F, false, false);

                                WandExperience.flowerplaced = true;
                                WandExperience.pSetExplosion = p;
                                SpiderHandler.this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(SpiderHandler.this.plugin, new Runnable() {
                                    public void run() {
                                        WandExperience.flowerplaced = false;
                                    }
                                }, 10L);
                            }
                        }
                    }, 50L + rand.nextInt(20));
                }
            }
        }
    }

    @EventHandler
    public void cancelItemFrameItemRemoval(HangingBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void allowModToEditHangings(HangingBreakByEntityEvent event) {
        if (((event.getRemover() instanceof Player)) &&
                (((Player) event.getRemover()).hasPermission("group.moderator"))) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void stopHangingDamage(EntityDamageEvent event) {
        if ((event.getEntityType() == EntityType.ITEM_FRAME) || (event.getEntityType() == EntityType.PAINTING)) {
            event.setCancelled(true);
        }
    }
}
