package me.kondi.JustHomes.Commands;

import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.Home.Home;
import me.kondi.JustHomes.JustHomes;
import me.kondi.JustHomes.Teleportation.TeleportPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;


public class HomeCommand {

    private final JustHomes plugin;
    private String prefix;
    private HashMap<String, String> messages = new HashMap<>();
    private PlayerData playerData;

    public HomeCommand(JustHomes plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;
        this.messages = plugin.messages;
        this.playerData = plugin.playerData;
    }


    public void get(Player p, String[] args) {


        String uuid = p.getUniqueId().toString();
        if (playerData.countPlayerHomes(uuid) == 0) {
            p.sendMessage(prefix + ChatColor.RED + messages.get("UserHasNoHomes"));
            return;
        }

        if (args.length == 0) {
            p.sendMessage(prefix + ChatColor.RED + messages.get("SpecifyHomeNameException"));
            return;
        }


        String homeName = args[0];
        Set<String> keys = playerData.listOfHomes(uuid);
        for (String key : keys) {
            if (homeName.equalsIgnoreCase(key)) {
                Home home = playerData.getHome(p, homeName);
                World world = Bukkit.getWorld(home.getWorldName());
                Location loc = new Location(world, home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch());

                int duration = plugin.config.getInt("DelayInTeleport");

                plugin.teleportPlayer.teleportPlayer(p, loc, duration, homeName);
                return;
            }
        }
        p.sendMessage(prefix + ChatColor.RED + messages.get("UnknownHomeName"));

    }


}






