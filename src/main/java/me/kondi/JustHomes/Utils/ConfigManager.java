package me.kondi.JustHomes.Utils;

import me.kondi.JustHomes.JustHomes;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class ConfigManager {

    public YamlConfiguration folderCfg;
    public File folder;
    public File file;
    public YamlConfiguration fileCfg;
    public HashMap<String, String> messages = new HashMap<>();
    public YamlConfiguration messagesCfg;
    private JustHomes plugin;

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

    public HashMap<String, String> loadLanguage(String lang) {

        file = new File(plugin.getDataFolder() + File.separator + "Languages" + File.separator + lang);
        messagesCfg = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = messagesCfg.getConfigurationSection("").getKeys(false);
        messages.clear();
        for (String key : keys) {
            messages.put(key.toString(), messagesCfg.getString(ChatColor.translateAlternateColorCodes('&',key)));
        }
        return messages;

    }


}
