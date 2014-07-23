package com.sandlotminecraft.lobbywands;

import com.sandlotminecraft.lobbywands.Wands.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created by cheracc on 7/18/2014.
 */
public class LobbyWands extends JavaPlugin implements Listener {

    public static HashMap<String, Long> invisCooldown = new HashMap<>();

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new MagicWand(), this);
        getServer().getPluginManager().registerEvents(new BlazeWand(), this);
        getServer().getPluginManager().registerEvents(new SpiderHandler(), this);
        getServer().getPluginManager().registerEvents(new SpacetimeWand(), this);
        getServer().getPluginManager().registerEvents(new SpiderWand(), this);
        getServer().getPluginManager().registerEvents(new FlowerWand(), this);
        getServer().getPluginManager().registerEvents(new WandExperience(), this);
        getCommand("wands").setExecutor(this);
    }

    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.getInventory().addItem(MagicWand.getMagicWand());
            p.getInventory().addItem(BlazeWand.getBlazeWand());
            p.getInventory().addItem(SpacetimeWand.getSpacetimeWand());
            p.getInventory().addItem(SpiderWand.getSpiderWand());
            p.getInventory().addItem(FlowerWand.getFlowerWand());
            p.sendMessage("You've been given a set of wands.");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        boolean hasWand = false;

        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            if (item != null)
                if (item.getType() == Material.STICK)
                    if (item.hasItemMeta())
                        if (item.getItemMeta().hasDisplayName())
                            if (item.getItemMeta().getDisplayName().contains("Magic"))
                                hasWand = true;
        }

        if (!hasWand) {
            event.getPlayer().getInventory().addItem(MagicWand.getMagicWand());
            event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "You've been given a Magic Wand!");
        }
    }

    public static Integer getCardinalDirection(Player player) {
        double rotation = player.getLocation().getYaw();

        if (rotation < 0)
            rotation += 360;

        if (292.5 <= rotation && rotation < 337.5) //SE
            return 8;
        else if (157.5 <= rotation && rotation <= 202.5) //N
            return 1;
        else if (22.5 <= rotation && rotation <= 67.5) //SW
            return 6;
        else if (67.5 <= rotation && rotation <= 112.5) //W
            return 3;
        else if (112.5 <= rotation && rotation <= 157.5) //NW
            return 0;
        else if (337.5 <= rotation && rotation <= 360) //S
            return 7;
        else if (0 <= rotation && rotation <= 22.5) //S
            return 7;
        else if (202.5 <= rotation && rotation <= 247.5) //NE
            return 2;
        else if (247.5 <= rotation && rotation <= 292.5) //E
            return 5;
        else
            return -1;
    }

    public static long getInvisCooldown(String pname) {
        if (invisCooldown.containsKey(pname))
                return invisCooldown.get(pname);
        else return 0;
    }
}
