package xyz.ultrapixelmon.pepefab.upcasino.SchedulerTasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.ultrapixelmon.pepefab.upcasino.Holograms.HologramsListener;

import java.util.HashMap;
import java.util.Map;

import static com.Zrips.CMI.Modules.Economy.Economy.plugin;

public class SchedulersInventory {

    private static Map<Location, BukkitTask> taskMap = new HashMap<>();
    private static int timer = 0;

    public static final HashMap<Location, Integer> inventoryTaskMap = new HashMap<>();

    public static void startTimer(Player player, Location location) {
        BukkitTask task = new BukkitRunnable() {
            int cooldown = 60; // Temps avant de fermer l'inventaire du joueur

            @Override
            public void run() {
                if (!player.isOnline()) { // Coupe s'il n'est plus en ligne
                    stopTimer(player, location);
                    return;
                }
                if (timer == cooldown) { // Si le temps est écoulé, ferme l'inventaire et arrête le timer
                    player.closeInventory();
                    stopTimer(player, location);
                    player.sendMessage("§cVous êtes trop long pour utiliser ce dropper !");
                } else {
                    timer+=5;
                    System.out.println(player.getName() + " : " + timer);
                }
            }
        }.runTaskTimer(plugin, 100L, 100L); // Executé sur le thread principal toutes les 5 secondes
        taskMap.put(location, task);
    }

    public static void stopTimer(Player player, Location location) {
        if (taskMap.containsKey(location)) {
            BukkitTask task = taskMap.get(location);
            task.cancel();
            timer = 0;
            taskMap.remove(location);
        }
    }

    public static void startCheckDropper(Location location, Inventory inventory){
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int emeraldCount = 0;
            int redstoneCount = 0;
            @Override
            public void run() {
                redstoneCount = 0;
                emeraldCount = 0;
                for(int i = 0; i < 9; i++){
                    ItemStack itemStack = inventory.getItem(i);
                    if(itemStack != null){
                        if(itemStack.getType().equals(Material.EMERALD)){
                            emeraldCount += itemStack.getAmount() - itemStack.getAmount() +1;
                        } else if (itemStack.getType().equals(Material.REDSTONE)){
                            redstoneCount += itemStack.getAmount() - itemStack.getAmount() +1;
                        }
                    }
                }
                HologramsListener.createHologram(location, emeraldCount, redstoneCount);
            }
        }, 0L, 20L);
        inventoryTaskMap.put(location, taskId);
    }
}
