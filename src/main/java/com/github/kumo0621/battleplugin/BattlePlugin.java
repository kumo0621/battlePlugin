package com.github.kumo0621.battleplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class BattlePlugin extends JavaPlugin implements org.bukkit.event.Listener {

    String name = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    Map<Player, String> mapList = new HashMap<>();


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player kill = event.getEntity().getKiller();
        String name = event.getEntity().getName();
        if (battleList.contains(name)) {
            player.sendMessage("あなたは負けました。");

            if (kill != null) {
                Objects.requireNonNull(kill).sendMessage("あなたは勝ちました。");
            }
        }
    }

    List<String> battleList = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("battlePlugin")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    sender.sendMessage("戦い相手を指定してください。");
                } else {
                    String set = sender.getName();
                    if (!set.equals(args[0])) {
                        sender.sendMessage(args[0] + "さんに戦いを申し込んだ！！");
                        mapList.put((org.bukkit.entity.Player) sender, args[0]);
                        name = args[0];
                        Player name = Bukkit.getServer().getPlayer(args[0]);
                        Objects.requireNonNull(name).sendMessage(sender.getName() + "さんに戦いを申し込まれた");
                        Objects.requireNonNull(name).sendMessage("参加する場合は参加とチャットにお書きください。");
                        Objects.requireNonNull(name).sendMessage("拒否する場合は拒否とチャットにお書きください。");
                    } else {
                        sender.sendMessage("自分とは戦えません");
                    }
                }
            }
        } else if (command.getName().equals("startBattle")) {
            if (sender instanceof Player) {
                for (Map.Entry<Player, String> entry : mapList.entrySet()) {
                    if (entry.getValue().startsWith(name)) {
                        Player set = entry.getKey();
                        Player set2 = Objects.requireNonNull(Bukkit.getServer().getPlayer(entry.getValue()));
                        battleList.add(String.valueOf(set));
                        battleList.add(String.valueOf(set2));
                        PlayerInventory inventory = Objects.requireNonNull(set).getInventory();
                        PlayerInventory inventory2 = Objects.requireNonNull(set2).getInventory();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 3; i > 0; i--) {
                                    set.sendTitle(i + "", "", 10, 70, 20);
                                    set2.sendTitle(i + "", "", 10, 70, 20);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                set.sendTitle("START", "", 10, 70, 20);
                                set2.sendTitle("START", "", 10, 70, 20);
                                inventory.addItem(new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.BOW, 1), new ItemStack(Material.COOKED_BEEF, 64), new ItemStack(Material.ARROW, 10));
                                inventory2.addItem(new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.BOW, 1), new ItemStack(Material.COOKED_BEEF, 64), new ItemStack(Material.ARROW, 10));
                            }
                        }).start();
                        mapList.remove(entry.getKey(), entry.getValue());
                    }
                }
            }
        } else if (command.getName().equals("startBattle")) {
            if (sender instanceof Player) {
                sender.sendMessage("拒否しました。");
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}

