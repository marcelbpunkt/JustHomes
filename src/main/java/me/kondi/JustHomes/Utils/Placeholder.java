package me.kondi.JustHomes.Utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.kondi.JustHomes.Home.HomeNames;
import me.kondi.JustHomes.JustHomes;
import me.kondi.JustHomes.Teleportation.TeleportPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Placeholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "justhomes";
    }

    @Override
    public @NotNull String getAuthor() {
        return "kondi";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.18.1";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
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
