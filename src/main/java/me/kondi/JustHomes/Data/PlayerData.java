package me.kondi.JustHomes.Data;

import me.kondi.JustHomes.Home.Home;
import me.kondi.JustHomes.JustHomes;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class PlayerData {

    private final JustHomes plugin;
    private final ConsoleCommandSender console;
    private final String prefix;

    public PlayerData(JustHomes plugin) {
        this.plugin = plugin;
        this.console = plugin.getServer().getConsoleSender();
        this.prefix = plugin.prefix;
    }

    public int countPlayerHomes(String uuid) {


        try {
            return plugin.db.getHomesAmount(uuid);
        } catch (SQLException ex) {
            ex.printStackTrace();
            console.sendMessage(prefix + "ERROR: " + ex);
        }
        return 0;


    }


    public List<String> listOfHomes(String uuid) {

        try {
            return plugin.db.getHomesList(uuid);
        } catch (SQLException ex) {
            ex.printStackTrace();
            console.sendMessage(prefix + "ERROR: " + ex);
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
            console.sendMessage(prefix + "ERROR: " + ex);
        }


    }


    public Home getHome(Player p, String homeName) {
        String uuid = p.getUniqueId().toString();
        int homesMaxAmount = plugin.permissionChecker.checkHomesMaxAmount(p);
        boolean foundHome = false;
        List<String> keys = listOfHomes(uuid);
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

                console.sendMessage(prefix + "ERROR: " + ex);
            }
        } else {
            p.sendMessage(prefix + plugin.messages.get("UnavailableHome"));
        }


        return null;
    }

    public void deleteHome(String uuid, String homeName) {

        try {
            plugin.db.deleteHome(uuid, homeName);
        } catch (SQLException ex) {
            ex.printStackTrace();
            console.sendMessage(prefix + "ERROR: " + ex);
        }


    }

}
