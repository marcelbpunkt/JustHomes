package me.kondi.JustHomes.Commands;


import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.Home.Home;
import me.kondi.JustHomes.JustHomes;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ListHomeCommand {

    private JustHomes plugin;
    private String prefix;
    private HashMap<String, String> messages;
    private PlayerData playerData;

    public ListHomeCommand(JustHomes plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;
        this.messages = plugin.messages;
        this.playerData = plugin.playerData;
    }

    public void getList(Player p) {
        String uuid = p.getUniqueId().toString();
        if (playerData.countPlayerHomes(uuid) == 0) {
            p.sendMessage(prefix + messages.get("UserHasNoHomes"));
            return;
        }

        List<Home> keys = playerData.listOfHomes(uuid);
        int maxHomesAmount = plugin.permissionChecker.checkHomesMaxAmount(p);
        if (keys.size() < maxHomesAmount) maxHomesAmount = keys.size();

        if (keys.size() == 0) {
            p.sendMessage(prefix + messages.get("UserHasNoHomes"));
            return;
        }


        p.sendMessage(messages.get("ListHomesTitle"));

        for (int i = 1; i < maxHomesAmount + 1; i++) {
            Home home = keys.get(i-1);
            TextComponent indexText = new TextComponent(messages.get("ListColorOfIndexNumber") + i + ". " );
            TextComponent homeText = new TextComponent(messages.get("ListColorOfIndexName") + home.getHomeName());
            homeText.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text( "World= " + Bukkit.getWorld(home.getWorldName()).getEnvironment().name() +"\n"+
                    "X= " + (int) home.getX() +"\n"+
                    "Y= " + (int) home.getY() +"\n"+
                    "Z= " + (int) home.getZ()
                    )));
            indexText.addExtra(homeText);
            p.spigot().sendMessage(indexText);
        }


    }

}
