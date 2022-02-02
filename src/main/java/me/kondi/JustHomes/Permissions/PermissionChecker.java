package me.kondi.JustHomes.Permissions;

import me.kondi.JustHomes.Main;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PermissionChecker {
    private Main plugin;

    public PermissionChecker(Main plugin) {
        this.plugin = plugin;
    }


    public int checkHomesMaxAmount(Player p){
        int homesMaxAmount = plugin.homesMaxAmount;
        for(PermissionAttachmentInfo permissions : p.getEffectivePermissions()){
            String permission = permissions.getPermission();
            if(permission.contains("justhomes.maxhomes.")){
                int homesMaxAmountFromPermissions = Integer.parseInt(permission.split("\\.")[2]);
                //if(homesMaxAmountFromPermissions > homesMaxAmount){
                    homesMaxAmount = homesMaxAmountFromPermissions;
                //}
            }
        }
        return homesMaxAmount;
    }
}
