package me.kondi.JustHomes.Utils;

import me.kondi.JustHomes.JustHomes;

import java.util.HashMap;

public class Messages {
    private static JustHomes plugin = JustHomes.getInstance();
    private static HashMap<String, String> messages = new HashMap<>();
    public static String get(String id){
        return messages.get(id);
    }

    public static void reload() {
        plugin.reloadConfig();
        plugin.config = plugin.getConfig();
        String lang = plugin.config.getString("Language") + ".yml";
        plugin.saveResource("Languages/" + lang, true);
        plugin.cfgManager.loadLanguage(lang);

    }
    public static void clear(){
        messages.clear();
    }

    public static void put(String key, String message){
        messages.put(key, message);
    }

}
