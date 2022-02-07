package me.kondi.JustHomes.Home;

import java.util.HashMap;
import java.util.UUID;

public class HomeNames {
    public HashMap<String, String> getHomeNames() {
        return homeNames;
    }



    public static void addHomeName(String uuid, String homeName){
        homeNames.put(uuid, homeName);
    }

    public static String getHomeName(String uuid){
        if(homeNames.containsKey(uuid)){
            String homeName = homeNames.get(uuid);
            homeNames.remove(uuid);
            return homeName;
        }else return "";
    }



    private static HashMap<String, String> homeNames = new HashMap<>();
}
