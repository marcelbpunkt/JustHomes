package me.kondi.JustHomes.Commands;

import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.Home.Home;
import me.kondi.JustHomes.Home.HomeNames;
import me.kondi.JustHomes.JustHomes;
import me.kondi.JustHomes.Permissions.PermissionChecker;
import me.kondi.JustHomes.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class HomeCommand {

    private final JustHomes plugin;
    private final String prefix;
    private PlayerData playerData;
    private Material[] damageBlocksMaterials = {Material.CACTUS, Material.FIRE, Material.CAMPFIRE, Material.SOUL_FIRE, Material.SOUL_CAMPFIRE, Material.MAGMA_BLOCK,
            Material.SWEET_BERRY_BUSH, Material.WITHER_ROSE, Material.LAVA, Material.POWDER_SNOW};
    private List<Material> damageBlocks = Arrays.asList(damageBlocksMaterials);
    private int teleportationDelay;
    public HomeCommand(JustHomes plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;

        this.playerData = plugin.playerData;
        this.teleportationDelay = plugin.teleportationDelay;
    }


    public void get(Player p, String[] args) {

        String uuid = p.getUniqueId().toString();
        if (playerData.countPlayerHomes(uuid) == 0) {
            p.sendMessage(prefix + Messages.get("UserHasNoHomes"));
            return;
        }

        if (args.length == 0) {
            p.sendMessage(prefix + Messages.get("SpecifyHomeNameException"));
            return;
        }


        String homeName = args[0];
        Home home = playerData.getHome(uuid, homeName);
        List<Home> homeList = playerData.listOfHomes(uuid);

        if (home == null) {
            p.sendMessage(prefix + Messages.get("UnknownHomeName"));
            return;
        }
        if(homeList.indexOf(home) + 1 > PermissionChecker.checkHomesMaxAmount(p)){
            p.sendMessage(prefix + Messages.get("UnavailableHome"));
            return;
        }

        Location loc = new Location(Bukkit.getWorld(home.getWorldName()), home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch());

        if (plugin.simpleProtection) {
            Material middle = loc.getBlock().getType();
            Material below = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getType();
            if (damageBlocks.contains(below) || damageBlocks.contains(middle)) {
                p.sendMessage(prefix + Messages.get("CorruptedHome"));
                return;
            }
        }

        HomeNames.addHomeName(uuid, homeName);
        plugin.teleportPlayer.teleportPlayer(p, loc, PermissionChecker.checkDelay(p));


    }


}









