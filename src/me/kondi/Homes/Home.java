package me.kondi.Homes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.bukkit.entity.Player;








public class Home implements CommandExecutor, TabCompleter {
	private Main plugin = Main.getPlugin(Main.class);
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		
		if(cmd.getName().equalsIgnoreCase("jhome"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("[JHomes] " + ChatColor.RED + "You are machine!");
				return true;
			}
			Player p = (Player) sender;
			
			if(!plugin.cfgManager.getHomes().contains("Users." + p.getUniqueId() + "."))
			{
				p.sendMessage("[JHomes] " + ChatColor.RED + "You dont have any homes!");
				return true;
			}
			
			if(args.length == 0 )
				p.sendMessage("[JHomes] " + ChatColor.RED + "You must specify a home name!");
			
			else
			{
				
				Set<String> keys = plugin.cfgManager.getHomes().getConfigurationSection("Users." + p.getUniqueId()).getKeys(false);
				for(int i = 0; i<keys.size(); i++)
				{
					if(args[0].equalsIgnoreCase(keys.toArray()[i].toString()))
					{
						String wconfig = plugin.cfgManager.getHomes().getString("Users." + p.getUniqueId() +  "." + args[0] + ".World");
						World world = Bukkit.getWorld(wconfig);
						double x = plugin.cfgManager.getHomes().getDouble("Users." + p.getUniqueId() +  "." + args[0] + ".X");
						double y = plugin.cfgManager.getHomes().getDouble("Users." + p.getUniqueId() +  "." + args[0] + ".Y");
						double z = plugin.cfgManager.getHomes().getDouble("Users." + p.getUniqueId() +  "." + args[0] + ".Z");
						float pitch =  Float.parseFloat(plugin.cfgManager.getHomes().getString("Users." + p.getUniqueId() +  "." + args[0] + ".Pitch"));
						float yaw =	Float.parseFloat(plugin.cfgManager.getHomes().getString("Users." + p.getUniqueId() +  "." + args[0] + ".Yaw"));
						Location loc = new Location(world, x, y, z, yaw, pitch);
						Location loc1 = new Location(world, x, y + 1, z, yaw, pitch);
						Location loc2= new Location(world, x, y + 2, z, yaw, pitch);
						Material mat = loc.getBlock().getRelative(BlockFace.DOWN).getType();
						Material mat1 = loc1.getBlock().getRelative(BlockFace.DOWN).getType();
						Material mat2 = loc2.getBlock().getRelative(BlockFace.DOWN).getType();
						
					    if(mat == Material.AIR)
					    {
					    	p.sendMessage("[JHomes] " + ChatColor.RED + "Your home is corrupted! Maybe it's in the air! Check X=" + (int) x + " Y=" + (int) y + " Z=" + (int) z);
					    	return true;
					    }
					    if(mat1 != Material.AIR || mat2 != Material.AIR)
					    {
					    	p.sendMessage("[JHomes] " + ChatColor.RED + "Your home is corrupted! Maybe there is a block! Check X=" + (int) x + " Y=" + (int) y + " Z=" + (int) z);
					    	return true;
					    }
						p.teleport(loc);
						p.sendMessage("[JHomes] " + ChatColor.GREEN + "You were succesfully teleported to " + ChatColor.GOLD + args[0]+ ChatColor.GREEN + "!");
						
						return true;
					}
					
					
						
					
				}
					p.sendMessage("[JHomes] " + ChatColor.RED + "Unknown home name!");
			}
			
				
				
				
			
		}
		
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("jhome"))
		{
			
			
			if(args.length == 1)
			{
				
				
				ArrayList<String> homes = new ArrayList<String>();
				
				if(!(sender instanceof Player))
				{
					sender.sendMessage("[JHomes] " + ChatColor.RED + "You are machine!");
					return homes;
				}
				Player p = (Player) sender;
				
				if(!plugin.cfgManager.getHomes().contains("Users." + p.getUniqueId()))
				{
					return homes;
				}
				if(!args[0].equals(""))
				{
					Set<String> keys = plugin.cfgManager.getHomes().getConfigurationSection("Users." + p.getUniqueId()).getKeys(false);
					for(String home : keys)
					{
						
						homes.add(home);
					}
				}
				else
				{
					Set<String> keys = plugin.cfgManager.getHomes().getConfigurationSection("Users." + p.getUniqueId()).getKeys(false);
					for(String home : keys)
					{
						homes.add(home);
					}
				}
				return homes;
			}
			
			
			
			
			
			
		}
			return null;
			
	}
	

	
}
