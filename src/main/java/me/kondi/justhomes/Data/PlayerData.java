package me.kondi.justhomes.Data;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.command.ConsoleCommandSender;

import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Home.Home;

public class PlayerData {

	private final JustHomesPlugin plugin;
	private final ConsoleCommandSender console;
	private final String prefix;
	private final Database db;

	public PlayerData(JustHomesPlugin plugin) {
		this.plugin = plugin;
		this.console = plugin.getServer().getConsoleSender();
		this.prefix = plugin.prefix;
		this.db = plugin.db;
	}

	public int countPlayerHomes(String uuid) {
		return db.getHomesAmount(uuid);
	}

	public List<Home> listOfHomes(String uuid) {
		return db.getCachedListOfHomes(uuid);
	}

	public Home getHome(String uuid, String homeName) {
		try {
			return db.getHome(uuid, homeName);
		} catch (SQLException ex) {
			console.sendMessage(prefix + "ERROR: " + ex);
		}
		return null;
	}

	/**
	 * Saves all homes of a player.
	 * 
	 * @param uuid the player's UUID
	 */
	public void saveHomes(String uuid) {
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
