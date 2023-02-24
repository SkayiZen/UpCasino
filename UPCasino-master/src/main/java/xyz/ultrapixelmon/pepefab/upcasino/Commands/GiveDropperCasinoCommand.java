package xyz.ultrapixelmon.pepefab.upcasino.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GiveDropperCasinoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        ItemStack dropper = new ItemStack(Material.DROPPER, 1);
        ItemMeta dropperMeta = dropper.getItemMeta();
        dropperMeta.setDisplayName("§c§lMachine à casino");
        dropperMeta.setLore(Arrays.asList("§fGrâce à cette machine vous pouvez créer un casino", "§fSeul les §aÉmeraudes §f(§aWin§f) et §cRedstones §f(§cLoose§f)", "§fpeuvent être placé dans le dropper."));
        dropper.setItemMeta(dropperMeta);

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (Bukkit.getOnlinePlayers().contains(target)) {
                target.getInventory().addItem(dropper);
                target.sendMessage("§aVous venez de recevoir un dropper casino.");
            } else {
                sender.sendMessage("§cAucun joueur ne correspond au pseudo " + args[0]);
            }
        }

        return false;
    }
}
