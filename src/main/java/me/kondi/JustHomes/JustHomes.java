package me.kondi.JustHomes;

import me.kondi.JustHomes.Commands.*;
import me.kondi.JustHomes.Data.Database;
import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.Home.HomeNames;
import me.kondi.JustHomes.Listeners.Events;
import me.kondi.JustHomes.Permissions.PermissionChecker;
import me.kondi.JustHomes.Teleportation.TeleportPlayer;
import me.kondi.JustHomes.Utils.ConfigManager;
import me.kondi.JustHomes.Utils.Messages;
import me.kondi.JustHomes.Utils.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class JustHomes extends JavaPlugin {

    public FileConfiguration config;
    public String prefix = "JHomes >> ";
    public ConfigManager cfgManager = new ConfigManager(this);

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
    //Home commands
    public SetHomeCommand setHome;
    public HomeCommand homeCommand;
    public ListHomeCommand listHome;
    public DeleteHomeCommand deleteHome;

    private static JustHomes instance;

    public static JustHomes getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;

        setupConfig();
        loadConfig();
        loadClasses();
        getServer().getConsoleSender().sendMessage(prefix + "Working");
        loadCommands();
        getServer().getPluginManager().registerEvents(events, this);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(prefix + "Not Working");
        loadConfig();
        db.stopDatabaseConnection();

    }



    public void loadConfig() {
        cfgManager.setup(); //create folder
        Messages.reload();

    }

    public void loadClasses() {
        db = new Database(this);
        playerData = new PlayerData(this);
        events = new Events(this);
        commands = new Commands(this);
        teleportPlayer = new TeleportPlayer(this);
        setHome = new SetHomeCommand(this);
        homeCommand = new HomeCommand(this);
        listHome = new ListHomeCommand(this);
        deleteHome = new DeleteHomeCommand(this);
        permissionChecker = new PermissionChecker(this);
        homeNames = new HomeNames();
        new Placeholder().register();
    }


    //Load all commands
    public void loadCommands() {

        getCommand("sethome").setExecutor(commands);
        getCommand("home").setExecutor(commands);
        getCommand("home").setTabCompleter(commands);
        getCommand("listhome").setExecutor(commands);
        getCommand("delhome").setExecutor(commands);
        getCommand("delhome").setTabCompleter(commands);
        getCommand("reloadlanguage").setExecutor(commands);
    }


    //Setting up config file
    public void setupConfig() {
        saveDefaultConfig();
        this.config = getConfig();
        Messages.reload();
        prefix = config.getString(ChatColor.translateAlternateColorCodes('&', "Prefix"));
        simpleProtection = config.getBoolean("SimpleProtection");
        homesMaxAmount = config.getInt("HomesMaxAmount");

    }

    //Saving language files


}
