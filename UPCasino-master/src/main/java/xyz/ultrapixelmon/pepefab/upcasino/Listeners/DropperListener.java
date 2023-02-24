package xyz.ultrapixelmon.pepefab.upcasino.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.ultrapixelmon.pepefab.upcasino.Holograms.HologramsListener;
import xyz.ultrapixelmon.pepefab.upcasino.SchedulerTasks.SchedulersInventory;

import static xyz.ultrapixelmon.pepefab.upcasino.Holograms.HologramsListener.hologramsMap;

public class DropperListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getInventory().getType() == InventoryType.DROPPER && event.getInventory().getName().equalsIgnoreCase("§c§lMachine à casino"))) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null || currentItem.getType() == Material.AIR) {
            return;
        }

        Material material = currentItem.getType();

        if (!material.equals(Material.REDSTONE) && !material.equals(Material.EMERALD)) {
            player.sendMessage("§cVous ne pouvez mettre que des émeraudes ou redstone dans une machine à casino.");
            player.closeInventory();
            player.updateInventory();
            event.setCancelled(true);
        } else if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || event.getAction() == InventoryAction.HOTBAR_SWAP) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerOpenInventory(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        if (inventory.getType() == InventoryType.DROPPER && inventory.getName().equalsIgnoreCase("§c§lMachine à casino")) {
            Location location = event.getInventory().getLocation();
            if (!player.hasPermission("upcasino.admin")) {
                SchedulersInventory.startTimer(player, location);
                SchedulersInventory.startCheckDropper(location, inventory);
            } else {
                HologramsListener.createHologram(location, emeraldCount(0, inventory), redstoneCount(0, inventory));
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void playerCloseInventory(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        if(inventory.getType() != InventoryType.DROPPER || !inventory.getName().equalsIgnoreCase("§c§lMachine à casino")){
            return;
        }

        Location location = inventory.getLocation();
        if(!player.hasPermission("upcasino.admin")){
            SchedulersInventory.stopTimer(player, location); // Permet de lancer le schedulers pour kick le joueur après 60 secondes dans l'inv

            int taskID = SchedulersInventory.inventoryTaskMap.getOrDefault(location, -1); // Permet de ne pas check la mise à jour automatique du dropper
            if(taskID != -1){
                Bukkit.getScheduler().cancelTask(taskID);
            }
        }

        HologramsListener.createHologram(location, emeraldCount(0, inventory), redstoneCount(0, inventory));
    }

    @EventHandler
    public void onPlayerBreakEvent(BlockBreakEvent event){
        Location location = event.getBlock().getLocation();
        if(hologramsMap.containsKey(location)){
            HologramsListener.removeHologram(location);
        }
    }

    @EventHandler
    public void onPlayerOpenDropper(PlayerInteractEvent event){
        Block clickedBlock = event.getClickedBlock();
        if (!(clickedBlock.getState() instanceof Dropper)) return;

        Dropper dropper = (Dropper) clickedBlock.getState();
        String inventoryName = dropper.getInventory().getName();
        if (!inventoryName.equals("§c§lMachine à casino")) return;

        Location dropperLocation = clickedBlock.getLocation();
        String checkPlayer = HologramsListener.hologramsUserMap.get(dropperLocation);
        if (checkPlayer == null) {
            HologramsListener.hologramsUserMap.put(dropperLocation, event.getPlayer().getName());
        } else if (!checkPlayer.equals(event.getPlayer().getName())) {
            if (event.getPlayer().hasPermission("upcasino.admin")) return;
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cCe dropper casino appartient à " + checkPlayer + " vous ne pouvez pas intéragir avec pour le moment.");
        }
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();
            BlockState state = block.getState();
            if (state instanceof Dropper) {
                Dropper dropper = (Dropper) state;
                String inventoryName = dropper.getInventory().getName();
                if (inventoryName.equals("§c§lMachine à casino")) {
                    Inventory inventory = dropper.getInventory();
                    HologramsListener.createHologram(event.getBlock().getLocation(), emeraldCount(0, inventory), redstoneCount(0, inventory));
                }
            }
    }



    private static int emeraldCount(int emerald, Inventory inventory){
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == Material.EMERALD) {
                emerald += itemStack.getAmount();
            }
        }
        return emerald;
    }


    private static int redstoneCount(int redstone, Inventory inventory){
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == Material.EMERALD) {
                redstone += itemStack.getAmount();
            }
        }
        return redstone;
    }

}

