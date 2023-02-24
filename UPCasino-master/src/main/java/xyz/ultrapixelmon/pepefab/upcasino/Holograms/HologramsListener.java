package xyz.ultrapixelmon.pepefab.upcasino.Holograms;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HologramsListener {

    public static Map<Location, Hologram> hologramsMap = new HashMap<>(); // Map Hologram principal
    public static Map<Location, Long> hologramsTimeMap = new HashMap<>(); // Map Hologram qui gère le temps
    public static Map<Location, String> hologramsUserMap = new HashMap<>(); // Map Hologram qui check le joueur qui a posé
    

    public static void createHologram(Location location, double emeraude, double redstone) {
        Location finalLoc = location.clone().add(0.5, 2.50, 0.5);
        
        long currentTime = System.currentTimeMillis();

        // Arrondi les pourcentages avec une décimale
        double chanceEmeraudeArrondi = Math.round((emeraude / (emeraude + redstone)) * 1000) / 10.0;
        double chanceRedstoneArrondi = Math.round((redstone / (emeraude + redstone)) * 1000) / 10.0;


        if (hologramsMap.containsKey(location)) { // S'il existe on le met juste à jour
            Hologram hologram = hologramsMap.get(location);
            hologram.clearLines();
            hologram.appendItemLine(new ItemStack(Material.EMERALD));
            hologram.appendTextLine("§aGagner: " + chanceEmeraudeArrondi + "§a%");
            hologram.appendTextLine("§cPerdre: " + chanceRedstoneArrondi + "§c%");
            hologram.teleport(finalLoc);

            hologramsTimeMap.put(location, currentTime);
        } else {
            Hologram hologram = HologramsAPI.createHologram(plugin, finalLoc);
            hologram.appendItemLine(new ItemStack(Material.EMERALD));
            hologram.appendTextLine("§aGagner: " + chanceEmeraudeArrondi + "§a%");
            hologram.appendTextLine("§cPerdre: " + chanceRedstoneArrondi + "§c%");
            hologram.teleport(finalLoc);

            hologramsMap.put(location, hologram);
            hologramsTimeMap.put(location, currentTime);
        }
    }

    public static void removeHologram(Location location){
        Hologram hologram = hologramsMap.remove(location);
        if(hologram != null){
            hologram.delete();
        }
        hologramsTimeMap.remove(location);
        hologramsUserMap.remove(location);
    }

}