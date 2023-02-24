package xyz.ultrapixelmon.pepefab.upcasino.SchedulerTasks;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.ultrapixelmon.pepefab.upcasino.Holograms.HologramsListener;

import java.util.Iterator;
import java.util.Map;

public class SchedulersHolograms {

    public static void ScheduleCheckHologramInactive(JavaPlugin plugin){

        int cooldown = 120; // Temps avant de supprimer les holograms inactif en seconde

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            long currentTimeSchudle = System.currentTimeMillis();
            for (Iterator<Map.Entry<Location, Long>> iterator = HologramsListener.hologramsTimeMap.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<Location, Long> entry = iterator.next();
                Location locationSchudle = entry.getKey();
                long hologramTime = entry.getValue();
                if (currentTimeSchudle  - hologramTime > cooldown * 1000) { // Si le temps écoulé depuis la création est supérieur
                    Hologram hologram = HologramsListener.hologramsMap.get(locationSchudle);
                    if(hologram != null){
                        hologram.delete();
                    }
                    iterator.remove(); // Supprime l'entrée de la map de temps
                    HologramsListener.hologramsMap.remove(locationSchudle); // Supprime l'entrée de la map d'hologrammes
                    HologramsListener.hologramsUserMap.remove(locationSchudle);
                }
            }
        }, 20 * 20, 20 * 20); // Period en tick (20 ticks = 1 seconde)

    }

}
