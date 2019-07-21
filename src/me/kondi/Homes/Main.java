package me.kondi.Homes;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public ConfigManager cfgManager;
	
	@Override
	public void onEnable() {
		loadConfig();
		getServer().getConsoleSender().sendMessage("[JHomes] " + ChatColor.GREEN + "Working");
		getCommand("jsethome").setExecutor(new Sethome());
		getCommand("jhome").setExecutor(new Home());
		getCommand("jhome").setTabCompleter(new Home());
		getCommand("jlisthome").setExecutor(new ListHome());
		getCommand("jdelhome").setExecutor(new DeleteHome());
		getCommand("jdelhome").setTabCompleter(new DeleteHome());
	}
	
	@Override
    public void onDisable() {
		getServer().getConsoleSender().sendMessage("[JHomes] " + ChatColor.RED + "Not Working");
		loadConfig();
	}
	
	public void loadConfig()
	{
		cfgManager = new ConfigManager();
		cfgManager.setup();
		cfgManager.saveHomes();
		cfgManager.reloadHomes();
		cfgManager.reloadOptions();
		cfgManager.getOptions().addDefault("HomesMaxAmount", 5);
		cfgManager.getOptions().options().copyDefaults(true);
		cfgManager.saveOptions();
	}

}
