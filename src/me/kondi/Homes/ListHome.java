package me.kondi.Homes;



import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListHome implements CommandExecutor {
	private Main plugin = Main.getPlugin(Main.class);
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("jlisthome"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("[JHomes] " + ChatColor.RED + "You are machine!");
				return true;
			}
			Player p = (Player) sender;
			
			if(!plugin.cfgManager.getHomes().contains("Users." + p.getUniqueId()))
			{
				p.sendMessage("[JHomes] " + ChatColor.RED + "You dont have any homes!");
			}
			else {
				Set<String> keys = plugin.cfgManager.getHomes().getConfigurationSection("Users." + p.getUniqueId()).getKeys(false);
				int max = plugin.cfgManager.getOptions().getInt("HomesMaxAmount");
				int iter = keys.size();
				if(keys.size() >= max)
				{
					iter = max;
				}
				if(keys.size() == 0)
				{
					p.sendMessage("[JHomes] " + ChatColor.RED + "You dont have any homes!");
				}
				else {
					p.sendMessage("[JHomes] " + ChatColor.GREEN + "[List of your homes]");
					for(int i = 0; i<iter; i++)
					{
						
						
						
						p.sendMessage(i + ". " + ChatColor.GOLD + keys.toArray()[i].toString());
						
						
					}
					
				}
				
				
			}
			
			
			
				
		
		}
		
	
		return true;
	
	}
}
