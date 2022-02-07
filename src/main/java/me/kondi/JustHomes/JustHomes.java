package me.kondi.JustHomes;

import me.kondi.JustHomes.Commands.*;
import me.kondi.JustHomes.Data.Database;
import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.Home.HomeNames;
import me.kondi.JustHomes.Listeners.Events;
import me.kondi.JustHomes.Permissions.PermissionChecker;
import me.kondi.JustHomes.Teleportation.TeleportPlayer;
import me.kondi.JustHomes.Utils.ConfigManager;
import me.kondi.JustHomes.Utils.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class JustHomes extends JavaPlugin {

    public FileConfiguration config;
    public String prefix = "JHomes >> ";
    public ConfigManager cfgManager = new ConfigManager(this);
    public HashMap<String, String> messages = new HashMap<>();
    public boolean simpleProtection;
    public int homesMaxAmount;

    //Classes
    public Events events;
    public Database db;
    public PlayerData playerData;
    public Commands commands;
    public TeleportPlayer teleportPlayer;
    public PermissionChecker permissionChecker;
    public HomeNames homeNames;
    //Homes commands
    public SetHomeCommand setHome;
    public HomeCommand homeCommand;
    public ListHomeCommand listHome;
    public DeleteHomeCommand deleteHome;
    private static JustHomes instance;


    @Override
    public void onEnable() {
        instance = this;
        setupConfig();
        loadConfig();
        loadClasses();
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GRAY + "Working");
        loadCommands();
        getServer().getPluginManager().registerEvents(events, this);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(prefix + ChatColor.GRAY + "Not Working");
        loadConfig();
        db.stopDatabaseConnection();

    }

    public void loadConfig() {
        cfgManager.setup(); //create folder
        saveResource();

    }

    public void loadClasses() {
        events = new Events(this);
        playerData = new PlayerData(this);
        commands = new Commands(this);
        teleportPlayer = new TeleportPlayer(this);
        setHome = new SetHomeCommand(this);
        homeCommand = new HomeCommand(this);
        listHome = new ListHomeCommand(this);
        deleteHome = new DeleteHomeCommand(this);
        permissionChecker = new PermissionChecker(this);
        db = new Database(this);
        homeNames = new HomeNames();
        new Placeholder().register();
    }

    public static JustHomes getInstance(){
        return instance;
    }


    //Load all commands
    public void loadCommands() {
        getCommand("sethome").setExecutor(commands);
        getCommand("home").setExecutor(commands);
        getCommand("listhome").setExecutor(commands);
        getCommand("home").setTabCompleter(commands);
        getCommand("delhome").setExecutor(commands);
        getCommand("delhome").setTabCompleter(commands);
        getCommand("loadlanguage").setExecutor(commands);
    }


    //Setting up config file
    public void setupConfig() {
        saveDefaultConfig();
        this.config = getConfig();
        saveResource();
        simpleProtection = config.getBoolean("SimpleProtection");
        homesMaxAmount = config.getInt("HomesMaxAmount");

    }

    //Saving language files
    public void saveResource() {
        reloadConfig();
        this.config = getConfig();
        String lang = config.getString("Language") + ".yml";
        this.saveResource("Languages/" + lang, true);
        this.messages = cfgManager.loadLanguage(lang);

    }
}
