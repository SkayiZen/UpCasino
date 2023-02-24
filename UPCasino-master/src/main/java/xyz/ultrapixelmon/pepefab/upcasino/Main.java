package xyz.ultrapixelmon.pepefab.upcasino;

import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.ultrapixelmon.pepefab.upcasino.Holograms.HologramsListener;
import xyz.ultrapixelmon.pepefab.upcasino.Listeners.DropperListener;

import java.util.Iterator;
import java.util.Map;


public final class Main extends JavaPlugin{

    public static Main instance;

    public static Main getInstance() {

        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new DropperListener(), this);

        // Scheduler suppresion d'hologram inactif
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            System.out.println("§c" + HologramsListener.hologramsMap);
            System.out.println("§a" + HologramsListener.hologramsTimeMap);
            System.out.println("§f" + HologramsListener.hologramsUserMap);
            long currentTimeSchudle = System.currentTimeMillis();
            for (Iterator<Map.Entry<Location, Long>> iterator = HologramsListener.hologramsTimeMap.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<Location, Long> entry = iterator.next();
                Location locationSchudle = entry.getKey();
                long hologramTime = entry.getValue();
                if (currentTimeSchudle  - hologramTime > 120 * 1000) { // Si le temps écoulé depuis la création est supérieur
                    CMIHologram hologram = HologramsListener.hologramsMap.get(locationSchudle);
                    if (hologram != null) {
                        hologram.remove();
                    }
                    iterator.remove(); // Supprime l'entrée de la map de temps
                    HologramsListener.hologramsMap.remove(locationSchudle); // Supprime l'entrée de la map d'hologrammes
                    HologramsListener.hologramsUserMap.remove(locationSchudle);
                }
            }
        }, 20 * 20, 20 * 20); // Period en tick (20 ticks = 1 seconde)

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
