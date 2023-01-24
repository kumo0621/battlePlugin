package com.github.kumo0621.battleplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BattlePlugin extends JavaPlugin implements org.bukkit.event.Listener {
    String Player;
    String Player2;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    List<String> startList = new ArrayList<>();
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player kill = event.getEntity().getKiller();
        //Player Inventory = Bukkit.getPlayer(Player);
        //Player Inventory2 = Bukkit.getPlayer(Player2);
        if (startList.contains(player.getName())) {
            //死亡したプレイヤーがbattleListに含まれている場合の処理
            player.sendMessage("あなたは負けました。");
            //PlayerInventory inventory = Objects.requireNonNull(Inventory).getInventory();
            //PlayerInventory inventory2 = Objects.requireNonNull(Inventory2).getInventory();
            //inventory.clear();
            //inventory2.clear();
            //inventory.addItem(itemclear.clone());
            //inventory2.addItem(itemclear2.clone());
            startList.remove(Player);
            startList.remove(Player2);
            if(kill!=null) {
                Objects.requireNonNull(kill).sendMessage("あなたは勝ちました。");
            }
        }
    }

    List<String> battleList = new ArrayList<>();
    ItemStack[] itemclear;
    ItemStack[] itemclear2;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("battlePlugin")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    sender.sendMessage("引数を指定してください。");
                } else {
                    switch (args[0]) {
                        case "join":
                            battleList.add(sender.getName());
                            System.out.println(battleList);
                            sender.sendMessage(sender.getName() + "さんの参加を認めました。");
                            break;
                        case "leave":
                            battleList.remove(sender.getName());
                            sender.sendMessage(sender.getName() + "さんの参加を取り下げました。");
                            break;
                        case "help":
                            sender.sendMessage("join:参加する");
                            sender.sendMessage("leave:参加を拒否する");
                            sender.sendMessage("select 名前 :戦う相手を決める");
                        case "select":
                            if (battleList != null) {
                                if (battleList.contains(args[1])) {
                                    Player = sender.getName();
                                    if (!Player.equals(args[1])) {
                                        Player secondPlayer = Bukkit.getServer().getPlayer(args[1]);
                                        Player2 = Objects.requireNonNull(Bukkit.getServer().getPlayer(args[1])).getName();
                                        PlayerInventory inventory = ((Player) sender).getInventory();
                                        PlayerInventory inventory2 = Objects.requireNonNull(secondPlayer).getInventory();
                                        itemclear = ((Player) sender).getInventory().getContents().clone();
                                        itemclear2 = Objects.requireNonNull(Bukkit.getServer().getPlayer(Player2)).getInventory().getContents().clone();
                                        inventory.clear();
                                        inventory2.clear();
                                        startList.add(Player);
                                        startList.add(Player2);
                                        battleList.remove(Player);
                                        battleList.remove(Player2);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (int i = 3; i > 0; i--) {
                                                    ((Player) sender).sendTitle(i + "", "", 10, 70, 20);
                                                    secondPlayer.sendTitle(i + "", "", 10, 70, 20);
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                ((Player) sender).sendTitle("START", "", 10, 70, 20);
                                                secondPlayer.sendTitle("START", "", 10, 70, 20);
                                                inventory.addItem(new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.BOW, 1), new ItemStack(Material.COOKED_BEEF, 64), new ItemStack(Material.ARROW, 10));
                                                inventory2.addItem(new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.BOW, 1), new ItemStack(Material.COOKED_BEEF, 64), new ItemStack(Material.ARROW, 10));

                                            }
                                        }).start();
                                    } else {
                                        sender.sendMessage("自分とは戦えません");
                                    }
                                    break;

                                } else {
                                    sender.sendMessage("そのプレイヤーは参加してません。");
                                }
                            } else {
                                sender.sendMessage("誰も待機してません");
                            }
                    }

                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}

