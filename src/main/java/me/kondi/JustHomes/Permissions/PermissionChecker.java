package me.kondi.JustHomes.Permissions;

import me.kondi.JustHomes.JustHomes;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PermissionChecker {
    private JustHomes plugin;
    private static int maxAmount;
    private static int teleportationDelay;
    public PermissionChecker(JustHomes plugin) {
        this.plugin = plugin;
        this.maxAmount = plugin.homesMaxAmount;
        this.teleportationDelay = plugin.teleportationDelay;
    }


    public static int checkHomesMaxAmount(Player p) {
        for (PermissionAttachmentInfo permissions : p.getEffectivePermissions())
            if (permissions.getPermission().contains("justhomes.maxhomes."))
                return Integer.parseInt(permissions.getPermission().split("\\.")[2]);

        return maxAmount;
    }

    public static int checkDelay(Player p) {
        for (PermissionAttachmentInfo permissions : p.getEffectivePermissions())
            if (permissions.getPermission().contains("justhomes.teleportationdelay."))
                return Integer.parseInt(permissions.getPermission().split("\\.")[2]);

        return teleportationDelay;
    }
}
