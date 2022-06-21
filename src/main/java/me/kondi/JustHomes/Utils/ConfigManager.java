package me.kondi.JustHomes.Utils;

import me.kondi.JustHomes.JustHomes;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class ConfigManager {

    private YamlConfiguration folderCfg;
    private File folder;
    private File file;
    
    private YamlConfiguration messagesCfg;
    private final JustHomes plugin;

    public ConfigManager(JustHomes plugin) {
        this.plugin = plugin;

    }

    public void setup() {
        plugin.getDataFolder().mkdir();
        createFileDataFolder("playerdata");
        createFileDataFolder("Languages");

    }

    public void createFileDataFolder(String name) {
        folder = new File(plugin.getDataFolder(), name);
        folder.mkdir();

        folderCfg = YamlConfiguration.loadConfiguration(folder);
    }

    public void updateConfig() {
        plugin.config = plugin.getConfig();
        FileConfiguration oldConfig = plugin.config;
        File cfgFile  =  new File(plugin.getDataFolder() + File.separator + "config.yml");
        cfgFile.delete();
        plugin.saveResource("config.yml", false);
        updateConfigVariable();

        //Updating new config.yml
        Set<String> sections = oldConfig.getConfigurationSection("").getKeys(false);
        for (String key : sections) {
            if(plugin.config.contains(key))
                plugin.config.set(key, oldConfig.get(key));
        }
        plugin.saveConfig();
        updateConfigVariable();
    }

    private void updateConfigVariable(){
        plugin.reloadConfig();
        plugin.config = plugin.getConfig();
    }

    public void loadLanguage(String lang) {

        file = new File(plugin.getDataFolder() + File.separator + "Languages" + File.separator + lang);
        messagesCfg = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = messagesCfg.getConfigurationSection("").getKeys(false);
        Messages.clear();
        for (String key : keys) {
            Messages.put(key, ChatColor.translateAlternateColorCodes('&',messagesCfg.getString(key)));
        }


    }


}
