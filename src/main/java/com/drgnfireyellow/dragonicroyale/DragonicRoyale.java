package main.java.com.drgnfireyellow.dragonicroyale;

import java.util.ArrayList;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.WorldBorder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.World;


public class DragonicRoyale extends JavaPlugin implements Listener {

    public ArrayList<Player> players = new ArrayList<Player>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (players.contains(player)) {
            player.sendMessage("You lost...");
            players.remove(player);
            if (players.size() == 1) {
                Player winner = players.get(0);
                winner.showTitle(Title.title(Component.text("You Win!"), Component.text("")));
                winner.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                players.remove(winner);
                File worldFolder = Bukkit.getWorld("dragonicroyale").getWorldFolder();
                Bukkit.unloadWorld("dragonicroyale", false);
                worldFolder.delete();
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("battleroyale")) {
                players.add((Player) sender);
                if (players.size() == 1) {
                    sender.sendMessage("You are the host and can decide when to start the game.");
                    WorldCreator mapCreator = new WorldCreator("dragonicroyale");
                    mapCreator.type(WorldType.NORMAL);
                    mapCreator.createWorld();
                    World map = Bukkit.getWorld("dragonicroyale");
                    map.getWorldBorder().setSize(100);
                }
                else {
                    sender.sendMessage("Please wait for the host to start the game...");
                }
                return false;
            }
            if (command.getName().equals("startbattleroyale")) {
                if (sender.equals(players.get(0))) {
                    for (Player player : players) {
                        player.getInventory().clear();
                        ItemStack[] armor = {new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)};
                        player.getInventory().setArmorContents(armor);
                        player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                        player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
                        player.teleport(Bukkit.getWorld("dragonicroyale").getSpawnLocation());
                    }
                }
                else {
                    sender.sendMessage("You must be the host to start the game!");
                }
                return false;
            }
        }
        return true;
    }
}
