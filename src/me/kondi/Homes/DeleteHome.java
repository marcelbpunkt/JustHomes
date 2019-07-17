package me.kondi.Homes;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class DeleteHome implements CommandExecutor, TabCompleter {
	private Main plugin = Main.getPlugin(Main.class);
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("jdelhome"))
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
			
			if(args.length == 0)
				p.sendMessage("[JHomes] " + ChatColor.RED + "You must specify a home name!");
			else
			{
				Set<String> keys = plugin.cfgManager.getHomes().getConfigurationSection("Users." + p.getUniqueId()).getKeys(false);
				for(int i = 0; i<keys.size(); i++)
				{
					if(args[0].equalsIgnoreCase(keys.toArray()[i].toString()))
					{
						plugin.cfgManager.getHomes().set("Users." + p.getUniqueId() +  "." + args[0], null );
						plugin.cfgManager.saveHomes();
						p.sendMessage("[JHomes] " + ChatColor.GREEN + "Home " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been successfully deleted!");
						return true;
					}
				}
				p.sendMessage("[JHomes] " + ChatColor.RED + "Unknown home name!");
			}
			
		}
		
		return true;
	}
	
	@Override
	public ArrayList<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("jdelhome"))
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
