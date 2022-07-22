package me.kondi.justhomes.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Home.Home;
import me.kondi.justhomes.Utils.Messages;

public class Commands implements CommandExecutor, TabCompleter {
	private final JustHomesPlugin plugin;
	private final String prefix;

	public Commands(JustHomesPlugin plugin) {
		this.plugin = plugin;
		this.prefix = plugin.prefix;

	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String arg2, String[] args) {

		if (cmd.getName().equalsIgnoreCase("reloadlanguage")) {
			if (sender.isOp() || sender.hasPermission("justhomes.loadlanguage")) {
				Messages.reload();
				return true;
			}
		}

		if (!(sender instanceof Player p)) {
			sender.sendMessage(prefix + Messages.get("NotHumanException"));
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("homes") && p.hasPermission("justhomes.homes")) {
			plugin.homesCommand.executeHomesCommand(p);
		}
		if (cmd.getName().equalsIgnoreCase("delhome") && p.hasPermission("justhomes.delhome")) {
			plugin.deleteHome.executeDelhomeCommand(p, args);
		}
		if (cmd.getName().equalsIgnoreCase("sethome") && p.hasPermission("justhomes.sethome")) {
			plugin.setHomeCommand.executeSethomeCommand(p, args);
		}
		if (cmd.getName().equalsIgnoreCase("home") && p.hasPermission("justhomes.home")) {
			plugin.homeCommand.executeHomeCommand(p, args);
		}
		if (cmd.getName().equalsIgnoreCase("spawn") && p.hasPermission("justhomes.spawn")) {
			plugin.spawnCommand.executeSpawnCommand(p);
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("delhome") || cmd.getName().equalsIgnoreCase("home")) {

			if (args.length == 1) {

				List<String> homeNames = new ArrayList<>();
				if (!(sender instanceof Player p)) {
					sender.sendMessage(prefix + Messages.get("NotPlayerException"));
					return homeNames;
				}
				String uuid = p.getUniqueId().toString();
//				if (plugin.playerData.countPlayerHomes(uuid) == 0) {
//					return homes;
//				}

				List<Home> homes = plugin.playerData.listOfHomes(uuid);
				for (Home home : homes) {
					if (home.getHomeName().toLowerCase().startsWith(args[0].toLowerCase())) {
						homeNames.add(home.getHomeName());
					}
				}
				for (String spawnName : Arrays.asList(Messages.get("WorldSpawn"), Messages.get("Bed"))) {
					if (spawnName.toLowerCase().startsWith(args[0].toLowerCase())) {
						homeNames.add(spawnName);
					}
				}

				return homeNames;

			}
		}
		return null;
	}
}
