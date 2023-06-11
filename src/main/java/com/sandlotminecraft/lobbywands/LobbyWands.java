package com.sandlotminecraft.lobbywands;

import com.sandlotminecraft.lobbywands.Wands.*;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class LobbyWands
        extends JavaPlugin
        implements Listener {
    public static HashMap<String, HashMap<String, Long>> wandCooldown = new HashMap();

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new MagicWand(), this);
        getServer().getPluginManager().registerEvents(new BlazeWand(), this);
        getServer().getPluginManager().registerEvents(new MonsterHandler(), this);
        getServer().getPluginManager().registerEvents(new SpacetimeWand(), this);
        getServer().getPluginManager().registerEvents(new SpiderWand(), this);
        getServer().getPluginManager().registerEvents(new FlowerWand(), this);
        getServer().getPluginManager().registerEvents(new LevitationWand(), this);
        getServer().getPluginManager().registerEvents(new WandExperience(), this);
        getServer().getPluginManager().registerEvents(new Currency(), this);
        getServer().getPluginManager().registerEvents(new SpellBooks(), this);
        //getServer().getPluginManager().registerEvents(new PetEggs(), this);
        getCommand("wands").setExecutor(this);

        String optionsWorldName = getConfig().getString("options.world-name");
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if(p.hasPermission("lobbywands.wands")) {
                p.getInventory().addItem(MagicWand.getMagicWand(false));
                p.getInventory().addItem(MagicWand.getMagicWand(true));
                p.getInventory().addItem(BlazeWand.getBlazeWand(false));
                p.getInventory().addItem(BlazeWand.getBlazeWand(true));
                p.getInventory().addItem(SpacetimeWand.getSpacetimeWand(false));
                p.getInventory().addItem(SpacetimeWand.getSpacetimeWand(true));
                p.getInventory().addItem(SpiderWand.getSpiderWand(false));
                p.getInventory().addItem(SpiderWand.getSpiderWand(true));
                p.getInventory().addItem(FlowerWand.getFlowerWand(false));
                p.getInventory().addItem(FlowerWand.getFlowerWand(true));
                p.getInventory().addItem(LevitationWand.getLevitationWand(false));
                p.getInventory().addItem(LevitationWand.getLevitationWand(true));
                p.getInventory().addItem(SpellBooks.getFireballBook(1));
                p.getInventory().addItem(SpellBooks.getLightningBook(1));
                p.getInventory().addItem(SpellBooks.getTntBook(1));
                p.getInventory().addItem(SpellBooks.getFireballBook(2));
                p.getInventory().addItem(SpellBooks.getLightningBook(2));
                p.getInventory().addItem(SpellBooks.getTntBook(2));
//                p.getInventory().addItem(PetEggs.getBatEgg());
//                p.getInventory().addItem(PetEggs.getCatEgg());
//                p.getInventory().addItem(PetEggs.getBugEgg());

                p.getInventory().addItem(Currency.getGalleons(64));
                p.getInventory().addItem(Currency.getSickles(64));
                p.getInventory().addItem(Currency.getKnuts(64));
                p.sendMessage("You've been given a bunch of random garbage.");
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String optionsWorldName = getConfig().getString("options.world-name");

        if(!optionsWorldName.equals(event.getPlayer().getWorld().getName())) {
            // Don't give them a magic wand if they aren't in Hogwarts world
            return;
        }

        boolean hasWand = false;
        Player p = event.getPlayer();
        for (ItemStack item : p.getInventory().getContents()) {
            if ((item != null) &&
                    (item.getType() == Material.STICK) &&
                    (item.hasItemMeta()) &&
                    (item.getItemMeta().hasDisplayName()) &&
                    (item.getItemMeta().getDisplayName().contains("Magic"))) {
                hasWand = true;
            }

            // Upgrade to new wand enchantments
            // Magic Wand, upgrade from Bane of Arthropods to Sharpness
            if ((item != null) &&
                    (item.getType() == Material.STICK) &&
                    (item.hasItemMeta()) &&
                    (item.getItemMeta().hasDisplayName()) &&
                    (item.getItemMeta().getDisplayName().contains("Magic"))) {
                int wandlevel = WandExperience.getLevel((String) item.getItemMeta().getLore().get(2));
                if (item.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
                    item.removeEnchantment(Enchantment.DAMAGE_ARTHROPODS);
                    if (wandlevel >= 1 && wandlevel <= 3) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                    } else if (wandlevel >= 4 && wandlevel <= 5) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
                    } else if (wandlevel >= 6 && wandlevel <= 7) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
                    } else if (wandlevel >= 8 && wandlevel <= 9) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
                    } else if (wandlevel == 10) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
                    }
                    p.sendMessage(ChatColor.GOLD + "Your Magic Wand has been upgraded with the Sharpness enchantment!");
                }
            }

            // Blaze Wand, upgrade from Bane of Arthropods to Fire Aspect
            if ((item != null) &&
                    (item.getType() == Material.BLAZE_ROD) &&
                    (item.hasItemMeta()) &&
                    (item.getItemMeta().hasDisplayName()) &&
                    (item.getItemMeta().getDisplayName().contains("Blaze"))) {
                int wandlevel = WandExperience.getLevel((String) item.getItemMeta().getLore().get(2));
                if (item.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
                    item.removeEnchantment(Enchantment.DAMAGE_ARTHROPODS);
                    if (wandlevel >= 1 && wandlevel <= 5) {
                        item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
                    } else if (wandlevel >= 5) {
                        item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
                    }
                    p.sendMessage(ChatColor.GOLD + "Your Blaze Wand has been upgraded with the Fire Aspect enchantment!");
                }
            }

            // Spacetime Wand, upgrade from Bane of Arthropods to Fire Aspect
            if ((item != null) &&
                    (item.getType() == Material.REDSTONE_TORCH) &&
                    (item.hasItemMeta()) &&
                    (item.getItemMeta().hasDisplayName()) &&
                    (item.getItemMeta().getDisplayName().contains("Spacetime"))) {
                int wandlevel = WandExperience.getLevel((String) item.getItemMeta().getLore().get(2));
                if (item.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
                    item.removeEnchantment(Enchantment.DAMAGE_ARTHROPODS);
                    if (wandlevel >= 1 && wandlevel <= 5) {
                        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
                    } else if (wandlevel >= 5) {
                        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
                    }
                    p.sendMessage(ChatColor.GOLD + "Your Spacetime Wand has been upgraded with the Knockback enchantment!");
                }
            }

            // Flower Wand, upgrade from Bane of Arthropods to Sharpness
            if ((item != null) &&
                    (item.getType() == Material.ALLIUM) &&
                    (item.hasItemMeta()) &&
                    (item.getItemMeta().hasDisplayName()) &&
                    (item.getItemMeta().getDisplayName().contains("Flower"))) {
                int wandlevel = WandExperience.getLevel((String) item.getItemMeta().getLore().get(2));
                if (item.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
                    item.removeEnchantment(Enchantment.DAMAGE_ARTHROPODS);
                    if (wandlevel >= 1 && wandlevel <= 3) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 1);
                    } else if (wandlevel >= 4 && wandlevel <= 5) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 2);
                    } else if (wandlevel >= 6 && wandlevel <= 7) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 3);
                    } else if (wandlevel >= 8 && wandlevel <= 9) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 4);
                    } else if (wandlevel == 10) {
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
                    }
                    p.sendMessage(ChatColor.GOLD + "Your Flower Wand has been upgraded with the Sharpness enchantment!");
                }
            }
        }

        if (!hasWand) {
            p.getInventory().addItem(MagicWand.getMagicWand(false));
            p.sendMessage(ChatColor.LIGHT_PURPLE + "You've been given a Magic Wand!");
        }
    }

    public static Integer getCardinalDirection(Player player) {
        double rotation = player.getLocation().getYaw();
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return 8;
        }
        if ((157.5D <= rotation) && (rotation <= 202.5D)) {
            return 1;
        }
        if ((22.5D <= rotation) && (rotation <= 67.5D)) {
            return 6;
        }
        if ((67.5D <= rotation) && (rotation <= 112.5D)) {
            return 3;
        }
        if ((112.5D <= rotation) && (rotation <= 157.5D)) {
            return 0;
        }
        if ((337.5D <= rotation) && (rotation <= 360.0D)) {
            return 7;
        }
        if ((0.0D <= rotation) && (rotation <= 22.5D)) {
            return 7;
        }
        if ((202.5D <= rotation) && (rotation <= 247.5D)) {
            return 2;
        }
        if ((247.5D <= rotation) && (rotation <= 292.5D)) {
            return 5;
        }
        return -1;
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
            if (itemName.contains("Levitation")) {
                return "levitation";
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
        if ((event.getPlayer() != null) && (event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
                && (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
                && (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Wand"))) {
            event.setCancelled(true);
        } else if ((event.getPlayer() != null) && (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))) {
            event.setCancelled(true);
        }
    }
}
