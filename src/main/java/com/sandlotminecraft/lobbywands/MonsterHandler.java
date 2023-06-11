package com.sandlotminecraft.lobbywands;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Random;

public class MonsterHandler
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");
    private boolean lightning = false;

    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }
        if (this.lightning) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        final Player p = ((Player) event.getDamager()).getPlayer();

        if(!p.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("options.world-name"))) {
            return;
        }

        Entity monster = event.getEntity();
        // Magic Wand
        if ((p.getInventory().getItemInMainHand().hasItemMeta()) && (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                && (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Magic"))) {
            Random rand = new Random();
            int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
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
                ItemStack potion = new ItemStack(Material.SPLASH_POTION, 1);
                PotionMeta meta = (PotionMeta) potion.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, wandlevel > 4 ? true : false));
                potion.setItemMeta(meta);
                ThrownPotion thrownPotion = (ThrownPotion) p.launchProjectile(ThrownPotion.class);
                thrownPotion.setShooter(p);
                thrownPotion.setItem(potion);
                p.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Stupefy Regio!");
            }
        }

        // Blaze Wand
        else if ((p.getInventory().getItemInMainHand().hasItemMeta()) && (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                && (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Blaze"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
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
                monster.setFireTicks(100 + wandlevel * 5);
                for (Entity setfire : monster.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
                    if ((setfire.getType() == EntityType.SPIDER) || (setfire.getType() == EntityType.CAVE_SPIDER)) {
                        setfire.setFireTicks(100 + wandlevel * 5);
                    }
                }
                p.getLocation().getWorld().playSound(monster.getLocation(), Sound.ENTITY_GHAST_SHOOT, 10.0F, 10.0F);
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incendio!");
            }
        }

        // Spacetime Wand
        else if ((p.getInventory().getItemInMainHand().hasItemMeta()) && (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                && (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Spacetime"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
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
                Entity[] warpMonsters = new Monster[16];
                int monsterCount = 1;
                warpMonsters[0] = monster;
                p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Alarte Ascendare!");
                for (Entity nearbyMonster : monster.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
                    if ((nearbyMonster instanceof Monster) && (monsterCount < 15)) {
                        warpMonsters[monsterCount] = nearbyMonster;
                        monsterCount++;
                    }
                }
                for (int i = 0; i <= monsterCount; i++) {
                    if (warpMonsters[i] != null) {
                        Location loc = warpMonsters[i].getLocation();
                        for (int j = 1; j < 6 + wandlevel; j++) {
                            loc = loc.add(0.0D, 1.0D, 0.0D);
                            if (loc.getBlock().getType() != Material.AIR) {
                                loc = loc.subtract(0.0D, 1.0D, 0.0D);
                                break;
                            }
                        }
                        warpMonsters[i].getLocation().getWorld().playEffect(warpMonsters[i].getLocation(), Effect.ENDER_SIGNAL, 5);
                        warpMonsters[i].getLocation().getWorld().playSound(warpMonsters[i].getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10.0F, 10.0F);
                        warpMonsters[i].teleport(loc);
                    }
                }
            }
        }

        // Spider Wand
        else if ((p.getInventory().getItemInMainHand().hasItemMeta()) && (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                && (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Spider"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            final int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
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
                spiderstrike[0] = monster;

                int count = 1;
                for (Entity near : monster.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
                    if ((near.getType() == EntityType.SPIDER) || (near.getType() == EntityType.CAVE_SPIDER)) {
                        spiderstrike[count] = near;
                        count++;
                        if (count > strikes) {
                            break;
                        }
                    }
                }
                count = 1;
                monster.getWorld().strikeLightningEffect(monster.getLocation());
                for (Entity dspider : monster.getNearbyEntities(2.0D, 2.0D, 2.0D)) {
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
                                        MonsterHandler.this.lightning = true;
                                        ((LivingEntity) dspider).damage(wandlevel >= 4 ? 3.0D : 2.0D, p);
                                        MonsterHandler.this.lightning = false;
                                    }
                                }
                            }
                        }, 5L * count + rand.nextInt(3));
                    }
                }
            }
        }

        // Flower Wand
        else if ((p.getInventory().getItemInMainHand().hasItemMeta()) && (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                && (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Flower"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            final int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
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
                if ((monster instanceof LivingEntity)) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Mutatio Flora!");
                    event.setDamage(0.0D);
                    ItemStack rosebush = new ItemStack(Material.ROSE_BUSH, 1);
                    Item flower = monster.getWorld().dropItem(monster.getLocation(), rosebush);
                    flower.setPickupDelay(1000);
                    monster.addPassenger(flower);
                    final LivingEntity flowerspider = (LivingEntity) monster;
                    final Item sflower = flower;
                    p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 10.0F, 10.0F);
                    monster.getLocation().getWorld().spawnParticle(Particle.HEART, monster.getLocation(), 5);
                    this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                        public void run() {
                            if (!flowerspider.isDead()) {
                                if (flowerspider.getPassengers().contains(sflower)) {
                                    sflower.remove();
                                }
                                flowerspider.getLocation().getWorld().createExplosion(flowerspider.getLocation().getX(), flowerspider.getLocation().getY(), flowerspider.getLocation().getZ(), 0.5F + wandlevel * 0.25F, false, false);

                                WandExperience.CausedByPlayer = true;
                                WandExperience.PlayerToCredit = p;
                                MonsterHandler.this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(MonsterHandler.this.plugin, new Runnable() {
                                    public void run() {
                                        WandExperience.CausedByPlayer = false;
                                    }
                                }, 10L);
                            }
                        }
                    }, 50L + rand.nextInt(20));
                }
            }
        }

        // Levitation Wand
        else if ((p.getInventory().getItemInMainHand().hasItemMeta()) && (p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                && (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Levitation"))) {
            Random rand = new Random();
            int chanceGoBoom = rand.nextInt(20);
            int wandlevel = WandExperience.getLevel((String) p.getInventory().getItemInMainHand().getItemMeta().getLore().get(2));
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

                PotionEffect effect = new PotionEffect(PotionEffectType.LEVITATION, 60, 1);
                effect.apply((LivingEntity) monster);

                for (Entity nearbyMonster : monster.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
                    if(nearbyMonster instanceof Monster) {
                        effect.apply((LivingEntity) nearbyMonster);
                    }
                }
                p.getLocation().getWorld().playSound(monster.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 10.0F, 10.0F);
                p.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Wingardium Leviosa!");
            }
        }
    }
}
