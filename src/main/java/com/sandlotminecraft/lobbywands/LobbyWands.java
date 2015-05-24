package com.sandlotminecraft.lobbywands;

import com.sandlotminecraft.lobbywands.Wands.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Random;

public class LobbyWands
        extends JavaPlugin
        implements Listener {
    public static HashMap<String, HashMap<String, Long>> wandCooldown = new HashMap();

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new MagicWand(), this);
        getServer().getPluginManager().registerEvents(new BlazeWand(), this);
        getServer().getPluginManager().registerEvents(new SpiderHandler(), this);
        getServer().getPluginManager().registerEvents(new SpacetimeWand(), this);
        getServer().getPluginManager().registerEvents(new SpiderWand(), this);
        getServer().getPluginManager().registerEvents(new FlowerWand(), this);
        getServer().getPluginManager().registerEvents(new WandExperience(), this);
        getServer().getPluginManager().registerEvents(new Currency(), this);
        getServer().getPluginManager().registerEvents(new SpellBooks(), this);
        getServer().getPluginManager().registerEvents(new PetEggs(), this);
        getCommand("wands").setExecutor(this);


        getServer().clearRecipes();

        getServer().getWorld("lobby").setMonsterSpawnLimit(100);
        getServer().getWorld("lobby").setTicksPerAnimalSpawns(100);
        getServer().getWorld("lobby").setTicksPerMonsterSpawns(20);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                LobbyWands.this.getServer().getWorld("lobby").setTime(18000L);
            }
        }, 200L, 200L);
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            p.getInventory().addItem(new ItemStack[]{MagicWand.getMagicWand()});
            p.getInventory().addItem(new ItemStack[]{BlazeWand.getBlazeWand()});
            p.getInventory().addItem(new ItemStack[]{SpacetimeWand.getSpacetimeWand()});
            p.getInventory().addItem(new ItemStack[]{SpiderWand.getSpiderWand()});
            p.getInventory().addItem(new ItemStack[]{FlowerWand.getFlowerWand()});
            p.getInventory().addItem(new ItemStack[]{SpellBooks.getFireballBook(Integer.valueOf(1))});
            p.getInventory().addItem(new ItemStack[]{SpellBooks.getLightningBook(Integer.valueOf(1))});
            p.getInventory().addItem(new ItemStack[]{SpellBooks.getFireballBook(Integer.valueOf(2))});
            p.getInventory().addItem(new ItemStack[]{SpellBooks.getLightningBook(Integer.valueOf(2))});
            p.getInventory().addItem(new ItemStack[]{PetEggs.getBatEgg()});
            p.getInventory().addItem(new ItemStack[]{PetEggs.getCatEgg()});
            p.getInventory().addItem(new ItemStack[]{PetEggs.getBugEgg()});

            p.getInventory().addItem(new ItemStack[]{Currency.getGalleons(Integer.valueOf(64))});
            p.getInventory().addItem(new ItemStack[]{Currency.getSickles(Integer.valueOf(64))});
            p.getInventory().addItem(new ItemStack[]{Currency.getKnuts(Integer.valueOf(64))});
            p.sendMessage("You've been given a bunch of random garbage.");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        boolean hasWand = false;
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            if ((item != null) &&
                    (item.getType() == Material.STICK) &&
                    (item.hasItemMeta()) &&
                    (item.getItemMeta().hasDisplayName()) &&
                    (item.getItemMeta().getDisplayName().contains("Magic"))) {
                hasWand = true;
            }
        }
        if (!hasWand) {
            event.getPlayer().getInventory().addItem(new ItemStack[]{MagicWand.getMagicWand()});
            event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "You've been given a Magic Wand!");
        }
    }

    public static Integer getCardinalDirection(Player player) {
        double rotation = player.getLocation().getYaw();
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return Integer.valueOf(8);
        }
        if ((157.5D <= rotation) && (rotation <= 202.5D)) {
            return Integer.valueOf(1);
        }
        if ((22.5D <= rotation) && (rotation <= 67.5D)) {
            return Integer.valueOf(6);
        }
        if ((67.5D <= rotation) && (rotation <= 112.5D)) {
            return Integer.valueOf(3);
        }
        if ((112.5D <= rotation) && (rotation <= 157.5D)) {
            return Integer.valueOf(0);
        }
        if ((337.5D <= rotation) && (rotation <= 360.0D)) {
            return Integer.valueOf(7);
        }
        if ((0.0D <= rotation) && (rotation <= 22.5D)) {
            return Integer.valueOf(7);
        }
        if ((202.5D <= rotation) && (rotation <= 247.5D)) {
            return Integer.valueOf(2);
        }
        if ((247.5D <= rotation) && (rotation <= 292.5D)) {
            return Integer.valueOf(5);
        }
        return Integer.valueOf(-1);
    }

    public static long getCooldown(String pname, String wand) {
        if ((wandCooldown.containsKey(pname)) && (((HashMap) wandCooldown.get(pname)).containsKey(wand))) {
            return ((Long) ((HashMap) wandCooldown.get(pname)).get(wand)).longValue();
        }
        return 0L;
    }

    public static void setCooldown(String player, String wand) {
        HashMap<String, Long> wcd = new HashMap();
        if (wandCooldown.containsKey(player)) {
            wcd = (HashMap) wandCooldown.get(player);
        }
        wcd.put(wand, Long.valueOf(System.currentTimeMillis()));
        wandCooldown.put(player, wcd);
    }

    public static String getWandName(ItemMeta im) {
        if ((im.hasDisplayName()) && (im.getDisplayName().contains("Wand"))) {
            String itemName = im.getDisplayName();
            if (itemName.contains("Magic")) {
                return "magic";
            }
            if (itemName.contains("Blaze")) {
                return "blaze";
            }
            if (itemName.contains("Spider")) {
                return "spider";
            }
            if (itemName.contains("Spacetime")) {
                return "spacetime";
            }
            if (itemName.contains("Flower")) {
                return "flower";
            }
            return "null";
        }
        return "spellbook";
    }

    @EventHandler
    public void onWandDrop(PlayerDropItemEvent event) {
        if ((event.getItemDrop().getItemStack().hasItemMeta()) && (event.getItemDrop().getItemStack().getItemMeta().hasDisplayName()) && (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("Wand"))) {
            event.setCancelled(true);
        } else if ((event.getItemDrop().getItemStack().hasItemMeta()) && (event.getItemDrop().getItemStack().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player p = event.getEntity().getPlayer();

        final ItemStack[] inventory = p.getInventory().getContents();
        final ItemStack[] armor = p.getInventory().getArmorContents();

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                p.getInventory().setContents(inventory);
                p.getInventory().setArmorContents(armor);
            }
        });
        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if ((event.getPlayer() != null) && (event.getPlayer().getItemInHand().hasItemMeta()) && (event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) && (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Wand"))) {
            event.setCancelled(true);
        } else if ((event.getPlayer() != null) && (event.getPlayer().getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        Location loc = event.getEntity().getLocation();
        World world = event.getEntity().getWorld();
        Random rand = new Random();
        if (event.getEntityType() == EntityType.VILLAGER) {
            return;
        }
        if (event.getEntityType() == EntityType.SQUID) {
            event.setCancelled(true);
            return;
        }
        if ((event.getEntityType() != EntityType.SPIDER) && (event.getEntityType() != EntityType.CAVE_SPIDER) && ((event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) || (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER))) {
            world.spawnEntity(loc, rand.nextInt(2) == 0 ? EntityType.SPIDER : EntityType.CAVE_SPIDER);
            event.setCancelled(true);
        }
    }
}
