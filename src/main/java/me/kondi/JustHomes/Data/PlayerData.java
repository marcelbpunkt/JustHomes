package me.kondi.JustHomes.Data;

import me.kondi.JustHomes.Home.Home;
import me.kondi.JustHomes.JustHomes;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Set;

public class PlayerData {

    private JustHomes plugin;
    private ConsoleCommandSender console;

    public PlayerData(JustHomes plugin) {
        this.plugin = plugin;
        this.console = plugin.getServer().getConsoleSender();
    }

    public int countPlayerHomes(String uuid) {


        try {
            return plugin.db.getHomesAmount(uuid);
        } catch (SQLException ex) {
            ex.printStackTrace();
            plugin.getServer().getConsoleSender().sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);
        }
        return 0;


    }


    public Set<String> listOfHomes(String uuid) {

        try {
            return plugin.db.getHomesList(uuid);
        } catch (SQLException ex) {
            ex.printStackTrace();
            console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);
        }
        return null;
    }

    public Set<String> listOfHomesToTabCompleter(String uuid) {
        return plugin.db.getCachedListOfHomes(uuid);
    }


    public void saveHome(Home home) {
        try {
            plugin.db.setHome(home);
        } catch (SQLException ex) {
            console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);
        }


    }


    public Home getHome(Player p, String homeName) {
        String uuid = p.getUniqueId().toString();
        int homesMaxAmount = plugin.permissionChecker.checkHomesMaxAmount(p);
        boolean foundHome = false;
        Set<String> keys = listOfHomes(uuid);
        if (keys.size() > homesMaxAmount) {
            for (int i = 0; i < homesMaxAmount; i++) {
                if (homeName.equalsIgnoreCase(keys.toArray()[i].toString()))
                    foundHome = true;
            }
        } else foundHome = true;

        if (foundHome) {

            try {
                return plugin.db.getHome(uuid, homeName);
            } catch (SQLException ex) {

                console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);
            }
        } else {
            p.sendMessage(plugin.prefix + ChatColor.RED + plugin.messages.get("UnavailableHome"));
        }


        return null;
    }

    public void deleteHome(String uuid, String homeName) {

        try {
            plugin.db.deleteHome(uuid, homeName);
        } catch (SQLException ex) {
            ex.printStackTrace();
            console.sendMessage("[JHomes] ERROR:" + ChatColor.RED + ex);
        }


    }

}
