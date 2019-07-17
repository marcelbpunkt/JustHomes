package me.kondi.Homes;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	
	private Main plugin = Main.getPlugin(Main.class);
	
	public FileConfiguration homesCfg;
	public File homesFile;
	
	public FileConfiguration optionsCfg;
	public File optionsFile;
	
	public void setup()
	{
		if(!plugin.getDataFolder().exists())
		{
			plugin.getDataFolder().mkdir();
			
		}
		
		homesFile = new File(plugin.getDataFolder(), "Homes.yml");
		optionsFile = new File(plugin.getDataFolder(), "Options.yml");
		
		exceptions(homesFile);
		exceptions(optionsFile);
		
		optionsCfg = YamlConfiguration.loadConfiguration(optionsFile);
		homesCfg = YamlConfiguration.loadConfiguration(homesFile);
		
		Bukkit.getServer().getConsoleSender().sendMessage("[JHomes] " + ChatColor.GREEN + "Files have been successfully created!");
		
	}
	
	public void exceptions(File file)
	{
		
		if(!file.exists())
		{
			try {
				file.createNewFile();
			}
			catch(IOException e){
				Bukkit.getServer().getConsoleSender().sendMessage("[JHomes] " + ChatColor.RED + "Could not create the " + file.getName() + " file!");
			}
		}
	}
	
	public FileConfiguration getHomes()
	{
		return homesCfg;
	}
	
	public void saveHomes()
	{
		try {
			homesCfg.save(homesFile);
		}
		catch(IOException e){
			Bukkit.getServer().getConsoleSender().sendMessage("[JHomes] " + ChatColor.RED + "Could not save the Homes.yml file!");
		}
	}
	
	public void reloadHomes() {
		homesCfg = YamlConfiguration.loadConfiguration(homesFile);
	}
	
	public FileConfiguration getOptions()
	{
		
		return optionsCfg;
		
	}
	
	public void reloadOptions() {
		optionsCfg = YamlConfiguration.loadConfiguration(optionsFile);
	}
	
	public void saveOptions()
	{
		try {
			optionsCfg.save(optionsFile);
		}
		catch(IOException e){
			Bukkit.getServer().getConsoleSender().sendMessage("[JHomes] " + ChatColor.RED + "Could not save the Options.yml file!");
		}
	}

}
