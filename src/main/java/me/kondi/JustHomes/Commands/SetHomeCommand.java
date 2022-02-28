package me.kondi.JustHomes.Commands;


import me.clip.placeholderapi.PlaceholderAPI;
import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.Home.Home;
import me.kondi.JustHomes.Home.HomeNames;
import me.kondi.JustHomes.JustHomes;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SetHomeCommand {

    private JustHomes plugin;
    private String prefix;
    private HashMap<String, String> messages = new HashMap<>();
    private PlayerData playerData;
    private Material[] damageBlocksMaterials = {Material.CACTUS, Material.FIRE, Material.CAMPFIRE, Material.SOUL_FIRE, Material.SOUL_CAMPFIRE, Material.MAGMA_BLOCK,
            Material.SWEET_BERRY_BUSH, Material.WITHER_ROSE, Material.LAVA, Material.POWDER_SNOW};
    private List<Material> damageBlocks = Arrays.asList(damageBlocksMaterials);

    public SetHomeCommand(JustHomes plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;
        this.messages = plugin.messages;
        this.playerData = plugin.playerData;
    }

    public SetHomeCommand() {
    }

    public void set(Player p, String[] args) {

        String uuid = p.getUniqueId().toString();
        Material mat = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();

        if (plugin.simpleProtection) {
            Material middle = p.getLocation().getBlock().getType();
            Material below = p.getWorld().getBlockAt(p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ()).getType();
            if (damageBlocks.contains(below) || damageBlocks.contains(middle) || middle == Material.NETHER_PORTAL) {
                p.sendMessage(prefix + messages.get("SetOnlyOnGroundException"));
                return;
            }

        }


        if (args.length == 0) {
            p.sendMessage(prefix + messages.get("SpecifyHomeNameException"));
            return;
        }
        int playerHomes = playerData.countPlayerHomes(uuid);
        if (playerHomes == 0) {

            saveLoc(p, args[0]);
            p.sendMessage(prefix + PlaceholderAPI.setPlaceholders(p, messages.get("CreatedHome")));
        } else {


            for (Home home : playerData.listOfHomes(uuid)) {
                if (home.getHomeName().equals(args[0])) {
                    saveLoc(p, args[0]);
                    p.sendMessage(prefix + PlaceholderAPI.setPlaceholders(p, messages.get("EditedHome")));
                    return;
                }
            }
            if (playerHomes >= plugin.permissionChecker.checkHomesMaxAmount(p)) {
                p.sendMessage(prefix + messages.get("TooMuchHomesException"));
                return;
            }


            saveLoc(p, args[0]);
            p.sendMessage(prefix + PlaceholderAPI.setPlaceholders(p, messages.get("CreatedHome")));


        }


    }


    public void saveLoc(Player p, String homeName) {
        String uuid = p.getUniqueId().toString();
        HomeNames.addHomeName(uuid, homeName);
        Home home = new Home(uuid, homeName, p.getLocation().getWorld().getName(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
        playerData.addHome(home);
    }

}
