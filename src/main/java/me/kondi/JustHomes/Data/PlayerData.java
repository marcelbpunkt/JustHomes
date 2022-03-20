package me.kondi.JustHomes.Data;

import me.kondi.JustHomes.Home.Home;
import me.kondi.JustHomes.JustHomes;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.SQLException;
import java.util.List;

public class PlayerData {

    private final JustHomes plugin;
    private final ConsoleCommandSender console;
    private final String prefix;
    private final Database db;

    public PlayerData(JustHomes plugin) {
        this.plugin = plugin;
        this.console = plugin.getServer().getConsoleSender();
        this.prefix = plugin.prefix;
        this.db = plugin.db;
    }

    public int countPlayerHomes(String uuid) {
        try {
            return db.getHomesAmount(uuid);
        } catch (SQLException ex) {
            console.sendMessage(prefix + "ERROR: " + ex);
        }
        return 0;
    }

    public List<Home> listOfHomes(String uuid) {
        return db.getCachedListOfHomes(uuid);
    }

    public Home getHome(String uuid, String homeName){
        try {
            return  db.getHome(uuid, homeName);
        }
        catch (SQLException ex){
            console.sendMessage(prefix + "ERROR: " + ex);
        }
        return null;
    }

    public void saveHome(String uuid) {
        db.saveHomes(uuid);
    }

    public void deleteHome(Home home) {
        try {
            db.deleteHome(home);
        } catch (SQLException ex) {
            console.sendMessage(prefix + "ERROR: " + ex);
        }
    }

    public void loadPlayerData(String uuid) {
        db.loadPlayerData(uuid);
    }

    public void addHome(Home home) {
        db.addHomeToCache(home);
    }

    public void replaceHome(Home home, Home newHome) {
        db.replaceHomeInCache(home, newHome);
    }

}
