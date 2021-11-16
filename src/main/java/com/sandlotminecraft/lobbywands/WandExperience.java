package com.sandlotminecraft.lobbywands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WandExperience
        implements Listener {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("LobbyWands");
    public static Player pSetExplosion;
    public static boolean flowerplaced = false;

    @EventHandler
    public void onSpiderDeath(EntityDeathEvent event) {
        if ((event.getEntityType() != EntityType.SPIDER) && (event.getEntityType() != EntityType.CAVE_SPIDER)) {
            return;
        }

        if(event.getEntity().getWorld().getName() != plugin.getConfig().getString("options.world-name")) {
            return;
        }

        if ((event.getEntity().getPassenger() instanceof Item)) {
            event.getEntity().getPassenger().remove();
        }

        event.setDroppedExp(0);
        if (event.getDrops() != null) {
            event.getDrops().clear();
        }
        Player p = null;
        if (event.getEntity().getKiller() == null) {
            EntityDamageEvent ede = event.getEntity().getLastDamageCause();
            if ((ede instanceof EntityDamageByEntityEvent)) {
                Entity lastDamager = ((EntityDamageByEntityEvent) ede).getDamager();
                if (((lastDamager instanceof Fireball)) &&
                        ((((Fireball) lastDamager).getShooter() instanceof Player))) {
                    p = (Player) ((Fireball) lastDamager).getShooter();
                    if ((!p.getInventory().getItemInMainHand().hasItemMeta()) || (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) || (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Blaze"))) {
                        return;
                    }
                }
            } else if (flowerplaced) {
                p = pSetExplosion;
                if ((!p.getInventory().getItemInMainHand().hasItemMeta()) || (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) || (!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Flower"))) {
                    return;
                }
            }
            if (p == null) {
                return;
            }
        } else {
            p = event.getEntity().getKiller();
            flowerplaced = false;
        }
        if (!p.getInventory().getItemInMainHand().hasItemMeta()) {
            return;
        }
        ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();

        String wandType = LobbyWands.getWandName(im);
        int coinsDropped = 0;
        int knutsDropped = 0;
        int sicklesDropped = 0;
        Random rand = new Random();
        switch (wandType) {
            case "magic":
                int[] chancem = {0, 0, 0, 0, 0, 1, 1, 1, 2};
                coinsDropped = chancem[rand.nextInt(9)];
                break;
            case "spider":
                coinsDropped = rand.nextInt(7);
                break;
            case "spacetime":
                coinsDropped = rand.nextInt(17);
                break;
            case "blaze":
                coinsDropped = rand.nextInt(31);
                break;
            case "flower":
                coinsDropped = rand.nextInt(67);
                break;
            case "spellbook":
                coinsDropped = rand.nextInt(10);
        }
        if (coinsDropped >= 29) {
            sicklesDropped = coinsDropped / 29;
            knutsDropped = coinsDropped % 29;
        } else {
            knutsDropped = coinsDropped;
        }
        if (sicklesDropped > 0) {
            ItemStack sickles = Currency.getSickles(Integer.valueOf(sicklesDropped));
            Item spiderdrop = event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), sickles);
            spiderdrop.setMetadata("killer", new FixedMetadataValue(this.plugin, p.getName()));
            spiderdrop.setMetadata("timedropped", new FixedMetadataValue(this.plugin, Long.valueOf(System.currentTimeMillis())));
        }
        if (knutsDropped > 0) {
            ItemStack knuts = Currency.getKnuts(Integer.valueOf(knutsDropped));
            Item spiderdrop = event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), knuts);
            spiderdrop.setMetadata("killer", new FixedMetadataValue(this.plugin, p.getName()));
            spiderdrop.setMetadata("timedropped", new FixedMetadataValue(this.plugin, Long.valueOf(System.currentTimeMillis())));
        }
        if (wandType.equals("spellbook")) {
            return;
        }

        List<String> lore = im.getLore();

        String lore1 = "";
        String lore2 = "";
        String lore3 = "";

        if (lore != null && lore.size() >= 1) {
            lore1 = lore.get(0);
        }
        if (lore.size() >= 2) {
            lore2 = lore.get(1);
        }
        if (lore.size() >= 3) {
            lore3 = lore.get(2);
        }

        if (getLevel(ChatColor.stripColor(lore3)) == 10) {
            return;
        }
        int[] xp = getXP(ChatColor.stripColor(lore2));
        xp[0] += 1;
        if (xp[0] >= xp[1]) {
            int newlevel = getLevel(ChatColor.stripColor(lore3)) + 1;
            p.sendMessage(ChatColor.YELLOW + "Your wand is now level " + newlevel + "!");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 10.0F);
            ItemMeta wand = p.getInventory().getItemInMainHand().getItemMeta();
            xp[0] = 0;
            xp[1] = (newlevel * 100);
            lore3 = ChatColor.DARK_AQUA + "Level " + newlevel + " Wand";
            p.sendMessage(ChatColor.GOLD + " - Cooldown Time is Now " + (90 - newlevel * 6) + " Seconds!");
            if ((newlevel == 4) || (newlevel == 7) || (newlevel == 10)) {
                int enchantLevel = im.getEnchantLevel(Enchantment.DAMAGE_ARTHROPODS);
                im.removeEnchant(Enchantment.DAMAGE_ARTHROPODS);
                im.addEnchant(Enchantment.DAMAGE_ARTHROPODS, enchantLevel + 1, true);
                p.sendMessage(ChatColor.GOLD + " - Enchantment Level has Increased!");
            }
            if ((newlevel == 5) && (wand.getDisplayName().contains("Spider"))) {
                p.sendMessage(ChatColor.GOLD + " - Your Strength effect is now more powerful!");
            }
            if (wand.getDisplayName().contains("Spacetime")) {
                p.sendMessage(ChatColor.GOLD + " - Your teleportation distance has increased.");
            }
            if ((newlevel == 3) || (newlevel == 6) || (newlevel == 9)) {
                p.sendMessage(ChatColor.GOLD + " - Chance of Special Attack Increased!");
            }
            if (newlevel == 10) {
                p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Your wand has reached its maximum power!");
                lore3 = ChatColor.DARK_AQUA + "Max Level Wand";
            }
//            p.setLevel(newlevel);
//            p.setExp(xp[0] / xp[1]);
        }
        lore2 = ChatColor.DARK_AQUA + "" + xp[0] + "/" + xp[1] + " XP";
        im.setLore(Arrays.asList(new String[]{lore1, lore2, lore3}));
        p.getInventory().getItemInMainHand().setItemMeta(im);
        //p.setExp(xp[0] / xp[1]);
    }

    private int[] getXP(String lore) {
        int[] xp = new int[2];
        String[] xpstr = lore.split("/");
        String have = xpstr[0];
        xpstr = xpstr[1].split("\\s+");
        String need = xpstr[0];

        xp[0] = Integer.parseInt(have);
        xp[1] = Integer.parseInt(need);

        return xp;
    }

    public static int getLevel(String lore) {
        if (lore.contains("Max")) {
            return 10;
        }
        String[] levstr = lore.split("\\s+");
        int level = Integer.parseInt(levstr[1]);
        return level;
    }

//    @EventHandler
//    public void onInventoryEvent(PlayerItemHeldEvent event) {
//        Player p = event.getPlayer();
//        ItemStack wand = p.getInventory().getItem(event.getNewSlot());
//        if ((wand != null) && (wand.hasItemMeta()) && (wand.getItemMeta().hasDisplayName()) && (wand.getItemMeta().getDisplayName().contains("Wand"))) {
//            int wandlevel = getLevel((String) wand.getItemMeta().getLore().get(2));
//            int[] xp = getXP(ChatColor.stripColor((String) wand.getItemMeta().getLore().get(1)));
//            p.setExp(xp[0] / xp[1]);
//            p.setLevel(wandlevel);
//        } else {
//            p.setExp(0.0F);
//            p.setLevel(0);
//        }
//    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        List<MetadataValue> pnames = event.getItem().getMetadata("killer");
        List<MetadataValue> times = event.getItem().getMetadata("timedropped");
        for (MetadataValue pname : pnames) {
            if (pname.value() != event.getPlayer().getName()) {
                for (MetadataValue time : times) {
                    if (System.currentTimeMillis() - ((Long) time.value()).longValue() < 30000L) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
