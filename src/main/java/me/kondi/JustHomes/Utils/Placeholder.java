package me.kondi.JustHomes.Utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.kondi.JustHomes.Home.HomeNames;
import me.kondi.JustHomes.JustHomes;
import me.kondi.JustHomes.Teleportation.TeleportPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Placeholder extends PlaceholderExpansion {
public static JustHomes plugin = JustHomes.getInstance();
    @Override
    public @NotNull String getIdentifier() {
        return null;
    }

    @Override
    public @NotNull String getAuthor() {
        return null;
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String params) {
        if (p == null) {
            return "";

        }
        if(params.equals("homename")){
            return HomeNames.getHomeName(p.getUniqueId().toString());
        }
        if(params.equals("teleportationtime")){
            return TeleportPlayer.tpCooldown.get(p.getUniqueId().toString()).toString();
        }

        return null;
    }
}
