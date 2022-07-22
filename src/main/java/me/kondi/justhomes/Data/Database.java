package me.kondi.justhomes.Data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Home.Home;

public class Database {

	private HashMap<String, List<Home>> cachedHomes = new HashMap<>();

	// Database objects and connection data
	private Connection con;
	private Statement st;
	private String host;
	private String database;
	private String username;
	private String password;
	private String databaseType;

	// Config and plugin objects
	private FileConfiguration config;
	private JustHomesPlugin plugin;
	private String prefix;
	private ConsoleCommandSender console;

	/**
	 * Initializes the database and establishes a connection.
	 * 
	 * @param plugin the plugin
	 */
	public Database(JustHomesPlugin plugin) {
		this.plugin = plugin;
		this.prefix = plugin.prefix;
		this.config = plugin.config;
		this.databaseType = config.getString("DatabaseType");
		this.console = plugin.getServer().getConsoleSender();

		try {
			if (databaseType.equalsIgnoreCase("MYSQL")) {
				this.host = config.getString("Host");
				this.database = config.getString("DatabaseName");
				this.username = config.getString("Username");
				this.password = config.getString("Password");
				con = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "", username, password);
			} else if (databaseType.equalsIgnoreCase("SQLITE")) {
				con = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/playerdata/homeData.db");
			}

			st = con.createStatement();
			createTable();
		} catch (Exception ex) {

			console.sendMessage(prefix + "ERROR: " + ex);

		}

	}

	/**
	 * Saves all homes of a player to the database, not including world and player
	 * spawns.
	 * 
	 * @param uuid the player's UUID
	 */
	public void saveHomes(String uuid) {
		cachedHomes.get(uuid).forEach(home -> {
			try {
				setHome(home);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Saves all cached homes of all players to the database, not including world
	 * and player spawns.
	 */
	public void saveAllHomes() {
		cachedHomes.keySet().forEach(this::saveHomes);
	}

	/**
	 * Inserts a specified home into the database.
	 * 
	 * @param home the home that is to be inserted
	 * @throws SQLException if any SQL-related error occurs
	 */
	public void setHome(Home home) throws SQLException {
		try {
			String exist = "SELECT * FROM HOMES WHERE UUID = ? AND HomeName = ?";
			PreparedStatement preparedStmt = con.prepareStatement(exist);
			preparedStmt.setString(1, home.getOwner());
			preparedStmt.setString(2, home.getHomeName());

			ResultSet results = preparedStmt.executeQuery();
			if (!results.next()) {
				String query = "INSERT INTO HOMES (UUID, WorldName, HomeName, X, Y, Z, Pitch, Yaw)"
						+ "VALUES(?, ?, ?,?, ?, ?, ?, ?)";
				PreparedStatement preparedStmtInsert = con.prepareStatement(query);
				preparedStmtInsert.setString(1, home.getOwner());
				preparedStmtInsert.setString(2, home.getWorldName());
				preparedStmtInsert.setString(3, home.getHomeName());
				preparedStmtInsert.setDouble(4, home.getX());
				preparedStmtInsert.setDouble(5, home.getY());
				preparedStmtInsert.setDouble(6, home.getZ());
				preparedStmtInsert.setFloat(7, home.getPitch());
				preparedStmtInsert.setFloat(8, home.getYaw());

				preparedStmtInsert.execute();
			} else {
				String query = "UPDATE HOMES SET WorldName=?, X=?, Y=?, Z=?, Pitch=?, Yaw=? WHERE UUID = ? AND HomeName=?";
				PreparedStatement preparedStmtUpdate = con.prepareStatement(query);
				preparedStmtUpdate.setString(1, home.getWorldName());
				preparedStmtUpdate.setDouble(2, home.getX());
				preparedStmtUpdate.setDouble(3, home.getY());
				preparedStmtUpdate.setDouble(4, home.getZ());
				preparedStmtUpdate.setFloat(5, home.getPitch());
				preparedStmtUpdate.setFloat(6, home.getYaw());
				preparedStmtUpdate.setString(7, home.getOwner());
				preparedStmtUpdate.setString(8, home.getHomeName());
				preparedStmtUpdate.execute();
			}

		} catch (Exception ex) {

			console.sendMessage(prefix + "ERROR:" + ex);

		}

	}

	/**
	 * Returns the number of homes a specified player has, including the world and
	 * player spawn point homes.
	 * 
	 * @param uuid the player's UUID
	 * @return the number of homes of the specified player, including the world and
	 *         player spawn point homes
	 */
	public int getHomesAmount(String uuid) {
		return cachedHomes.get(uuid).size();
	}

	public List<Home> getCachedListOfHomes(String uuid) {
		return cachedHomes.get(uuid);
	}

	public Home getHome(String uuid, String homeName) throws SQLException {
		Optional<Home> home = cachedHomes.get(uuid).stream().filter(h -> h.getHomeName().equalsIgnoreCase(homeName))
				.findFirst();

		return home.orElse(null);
	}

	/**
	 * Deletes a specified home from the database.
	 * 
	 * @param home the home that is to be deleted
	 * @throws SQLException if any SQL-related error occurs
	 */
	public void deleteHome(Home home) throws SQLException {
		try {
			cachedHomes.get(home.getOwner()).remove(home);

			String query = "DELETE FROM HOMES WHERE UUID = ? AND HomeName = ?";
			PreparedStatement preparedStmtInsert = con.prepareStatement(query);
			preparedStmtInsert.setString(1, home.getOwner());
			preparedStmtInsert.setString(2, home.getHomeName());
			preparedStmtInsert.execute();

		} catch (Exception ex) {
			console.sendMessage(prefix + "ERROR: " + ex);

		}
	}

	/**
	 * Creates the homes table in the database.
	 */
	public void createTable() {
		try {
			DatabaseMetaData meta = con.getMetaData();
			ResultSet tables = meta.getTables(null, null, "HOMES", null);

			while (!tables.next()) {
				String playerDataTable = "CREATE TABLE HOMES" + "(UUID VARCHAR(255) NOT NULL, "
						+ "HomeName VARCHAR(255) NOT NULL, " + "WorldName VARCHAR(255), " + "X DOUBLE, " + "Y DOUBLE, "
						+ "Z DOUBLE, " + "Pitch FLOAT, " + "Yaw FLOAT," + "PRIMARY KEY (UUID, HomeName))";

				st.executeUpdate(playerDataTable);
				console.sendMessage(prefix + "Table [HOMES] Created!");
				break;
			}

		} catch (Exception ex) {

			console.sendMessage(prefix + "ERROR: " + ex);
		}
	}

	/**
	 * Closes the connection to the database.
	 */
	public void stopDatabaseConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the homes of a player, not including the world and player spawn points.
	 * 
	 * @param uuid the player's UUID
	 */
	public void loadPlayerData(String uuid) {
		try {

			String query = "SELECT HomeName, WorldName, X, Y, Z, Pitch, Yaw FROM HOMES WHERE UUID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, uuid);
			ResultSet results = preparedStmt.executeQuery();
			List<Home> listOfHomes = new ArrayList<>();

			while (results.next()) {
				Home home = new Home(uuid, results.getString("HomeName"), results.getString("WorldName"),
						results.getDouble("X"), results.getDouble("Y"), results.getDouble("Z"),
						results.getFloat("Pitch"), results.getFloat("Yaw"));
				listOfHomes.add(home);
			}

			cachedHomes.put(uuid, listOfHomes);

		} catch (SQLException ex) {

			console.sendMessage(prefix + "ERROR: " + ex);

		}

	}

	/**
	 * Adds a home to a player's cached homes.
	 * 
	 * @param home the home to add
	 */
	public void addHomeToCache(Home home) {

		cachedHomes.get(home.getOwner()).add(home);

	}

	/**
	 * Replaces a specified home by another specified home in the cache.
	 * 
	 * @param home    the home that is to be replaced
	 * @param newHome the home that is to replace {@code home}
	 */
	public void replaceHomeInCache(Home home, Home newHome) {
		List<Home> homeList = cachedHomes.get(home.getOwner());
		homeList.set(homeList.indexOf(home), newHome);
		cachedHomes.replace(home.getOwner(), homeList);
	}

}
