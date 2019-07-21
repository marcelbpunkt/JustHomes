package me.kondi.Homes;




import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sethome implements CommandExecutor {
	private Main plugin = Main.getPlugin(Main.class);
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("jsethome"))
		{
			
			if(!(sender instanceof Player))
			{
				sender.sendMessage("[JHomes] " + ChatColor.RED + "You are machine!");
				return true;
			}
			Player p = (Player) sender;
			
			Material mat = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
		    if(mat == Material.AIR || mat == Material.LAVA)
		    {
		    	p.sendMessage("[JHomes] " + ChatColor.RED + "You must be on the ground!");
		    	return true;
		    }
		    	
			if(args.length == 0)
			{
				p.sendMessage("[JHomes] " + ChatColor.RED + "You must specify a home name!");
				return true;
			}
				
			else {
				if(!plugin.cfgManager.getHomes().contains("Users." + p.getUniqueId()))
				{
						saveLoc(p, args[0]);
						plugin.cfgManager.saveHomes();
						p.sendMessage("[JHomes] " + ChatColor.GREEN + "Home " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been successfully saved!");
				}
				else {
					
					Set<String> keys = plugin.cfgManager.getHomes().getConfigurationSection("Users." + p.getUniqueId()).getKeys(false);
					
					if(keys.size() >=  plugin.cfgManager.getOptions().getInt("HomesMaxAmount"))
						p.sendMessage("[JHomes] " + ChatColor.RED + "You have too much homes! Try deleting any of them.");
					else {
						
						saveLoc(p, args[0]);
						plugin.cfgManager.saveHomes();
						p.sendMessage("[JHomes] " + ChatColor.GREEN + "Home " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been successfully saved!");
			
					}
				}
			}
		}
		return true;
	}
	
	public void saveLoc(Player p, String s)
	{
		plugin.cfgManager.getHomes().set("Users." + p.getUniqueId() +  "." + s + ".Username" , p.getDisplayName());
		plugin.cfgManager.getHomes().set("Users." + p.getUniqueId() +  "." + s + ".World", p.getLocation().getWorld().getName());
		plugin.cfgManager.getHomes().set("Users." + p.getUniqueId() +  "." + s + ".X", p.getLocation().getX());
		plugin.cfgManager.getHomes().set("Users." + p.getUniqueId() +  "." + s + ".Y", p.getLocation().getY());
		plugin.cfgManager.getHomes().set("Users." + p.getUniqueId() +  "." + s + ".Z", p.getLocation().getZ());
		plugin.cfgManager.getHomes().set("Users." + p.getUniqueId() +  "." + s + ".Pitch", p.getLocation().getPitch());
		plugin.cfgManager.getHomes().set("Users." + p.getUniqueId() +  "." + s + ".Yaw", p.getLocation().getYaw());
	}

}
