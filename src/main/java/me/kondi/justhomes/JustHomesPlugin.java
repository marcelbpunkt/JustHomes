package me.kondi.justhomes;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.kondi.justhomes.Commands.Commands;
import me.kondi.justhomes.Commands.DeleteHomeCommand;
import me.kondi.justhomes.Commands.HomeCommand;
import me.kondi.justhomes.Commands.ListHomeCommand;
import me.kondi.justhomes.Commands.SetHomeCommand;
import me.kondi.justhomes.Commands.SpawnCommand;
import me.kondi.justhomes.Data.Database;
import me.kondi.justhomes.Data.PlayerData;
import me.kondi.justhomes.Home.HomeNames;
import me.kondi.justhomes.Listeners.Events;
import me.kondi.justhomes.Permissions.PermissionChecker;
import me.kondi.justhomes.Teleportation.TeleportPlayer;
import me.kondi.justhomes.Utils.ConfigManager;
import me.kondi.justhomes.Utils.Messages;
import me.kondi.justhomes.Utils.Metrics;
import me.kondi.justhomes.Utils.Placeholder;

/**
 * Contains all necessary data used within this plugin.
 * 
 * @author Kondee3, marcelbpunkt
 *
 */
public class JustHomesPlugin extends JavaPlugin {

	public FileConfiguration config;

	public String prefix = "JHomes >> ";
	public ConfigManager cfgManager = new ConfigManager(this);

	public boolean simpleProtection;
	public int homesMaxAmount;
	public int teleportationDelay;

	// Classes
	public Events events;
	public Database db;
	public PlayerData playerData;
	public Commands commands;
	public TeleportPlayer teleportPlayer;
	public PermissionChecker permissionChecker;
	public HomeNames homeNames;
	// Home commands
	public SetHomeCommand setHomeCommand;
	public HomeCommand homeCommand;
	public ListHomeCommand homesCommand;
	public DeleteHomeCommand deleteHome;
	public SpawnCommand spawnCommand;

	private Metrics metrics;

	private static JustHomesPlugin instance;

	public static JustHomesPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
			getServer().getConsoleSender().sendMessage(String
					.format("[%s] Disabled due to no PlaceholderAPI dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		setupConfig();
		loadConfig();
		loadClasses();
		loadCommands();
		getServer().getPluginManager().registerEvents(events, this);
	}

	@Override
	public void onDisable() {
		loadConfig();
		if (db != null) {
			db.saveAllHomes();
			db.stopDatabaseConnection();
		}
	}

	public void loadConfig() {
		cfgManager.setup(); // create folder
		Messages.reload();

	}

	public void loadClasses() {
		metrics = new Metrics(this, 15508);
		db = new Database(this);
		playerData = new PlayerData(this);
		events = new Events(this);
		commands = new Commands(this);
		teleportPlayer = new TeleportPlayer(this);
		setHomeCommand = new SetHomeCommand(this);
		homeCommand = new HomeCommand(this);
		homesCommand = new ListHomeCommand(this);
		deleteHome = new DeleteHomeCommand(this);
		spawnCommand = new SpawnCommand(this);
		permissionChecker = new PermissionChecker(this);
		homeNames = new HomeNames();
		new Placeholder().register();
	}

	// Load all commands
	public void loadCommands() {

		getCommand("sethome").setExecutor(commands);
		getCommand("home").setExecutor(commands);
		getCommand("home").setTabCompleter(commands);
		getCommand("homes").setExecutor(commands);
		getCommand("delhome").setExecutor(commands);
		getCommand("delhome").setTabCompleter(commands);
		// getCommand("spawn").setExecutor(commands);
		getCommand("reloadlanguage").setExecutor(commands);
	}

	// Setting up config file
	public void setupConfig() {
		saveDefaultConfig();
		cfgManager.updateConfig();
		prefix = config.getString(ChatColor.translateAlternateColorCodes('&', "Prefix"));
		simpleProtection = config.getBoolean("SimpleProtection");
		homesMaxAmount = config.getInt("HomesMaxAmount");
		teleportationDelay = config.getInt("DelayInTeleport");

	}

}
