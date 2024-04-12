package main.java.com.drgnfireyellow.dragonicroyale;

import java.util.ArrayList;
import java.io.File;
// import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
// import org.apache.commons.io.FileUtils;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

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

    ArrayList<String> players = new ArrayList<String>();
    MultiverseCore mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    MVWorldManager worldManager = mvCore.getMVWorldManager();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if ((players.contains(player.getName())) & (player.getWorld().getName().equals("dragonicroyale"))) {
            player.sendMessage("You lost...");
            players.remove(player.getName());
            player.spigot().respawn();
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            if (players.size() == 1) {
                Player winner = Bukkit.getPlayer(players.get(0));
                winner.showTitle(Title.title(Component.text("You Win!"), Component.text("")));
                winner.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                players.remove(winner.getName());
                worldManager.regenWorld("dragonicroyale", true, true, null);
                // File worldFolder = Bukkit.getWorld("dragonicroyale").getWorldFolder();
                // for (Player p : Bukkit.getWorld("dragonicroyale").getPlayers()) {
                //     player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                // }
                // System.out.println(Bukkit.unloadWorld(Bukkit.getWorld("dragonicroyale"), false));
                // try {
                //     FileUtils.deleteDirectory(worldFolder);
                // }
                // catch(IOException exception) {
                //     Bukkit.getLogger().warning("Unable to delete world folder!");
                // }
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("battleroyale")) {
                if (!players.contains(sender.getName())) {
                    players.add(((Player) sender).getName());
                    if (players.size() == 1) {
                        sender.sendMessage("You are the host and can decide when to start the game.");
                        if (worldManager.getMVWorld("dragonicroyale") == null) {
                            worldManager.addWorld("dragonicroyale", World.Environment.NORMAL, null, WorldType.NORMAL, false, null);
                            worldManager.getMVWorld("dragonicroyale").setHidden(true);
                            Bukkit.getWorld("dragonicroyale").getWorldBorder().setSize(100);
                            Bukkit.getWorld("dragonicroyale").setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
                        }
                    }
                    else {
                        sender.sendMessage("Please wait for the host to start the game...");
                    }
                }
                else {
                    sender.sendMessage("You are already in Battle Royale.");
                }
                return false;
            }
            if (command.getName().equals("startbattleroyale")) {
                if (sender.getName().equals(players.get(0))) {
                    if (players.size() >= 2) {
                        for (String playerName : players) {
                            Player player = Bukkit.getPlayer(playerName);
                            player.getInventory().clear();
                            ItemStack[] armor = {new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)};
                            player.getInventory().setArmorContents(armor);
                            player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                            player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
                            player.teleport(Bukkit.getWorld("dragonicroyale").getSpawnLocation());
                        }
                    }
                    else {
                        sender.sendMessage("You need at least 2 people to play!");
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
