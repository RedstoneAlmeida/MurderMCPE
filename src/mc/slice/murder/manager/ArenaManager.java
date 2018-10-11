package mc.slice.murder.manager;


import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.Task;
import mc.slice.murder.Loader;
import mc.slice.murder.arena.Detective;
import mc.slice.murder.arena.Murder;
import mc.slice.murder.arena.MurderArena;
import mc.slice.murder.utils.ArenaState;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ArenaManager extends Manager{

    private Random random = new Random();

    public ArenaManager(Loader plugin) {
        super(plugin);
    }

    public void join(Player player, MurderArena arena){
        player.getInventory().clearAll();
        player.removeAllEffects();
        arena.teleportPlayer(player);
        player.setGamemode(Player.SURVIVAL);
        player.setHealth(player.getMaxHealth());
        player.getFoodData().setLevel(20);
        player.setNameTagAlwaysVisible(false);
        for(Player p : arena.getPlayers()){
            p.sendMessage(String.format("§a§lMURDER §r§e%s §7entrou na partida §e(%s de %s)", player.getName(), arena.getPlayers().size(), arena.getSlots()));
        }
    }

    public void start(MurderArena arena){
        arena.setState(ArenaState.GAME_RUNNING);
        arena.setCountdown(0);
        Position pos = arena.getSpawn();
        pos.add(0, 1);
        for(Player p : arena.getPlayers()) {
            p.sendTitle("", "§aJogo INICIADO!");
            p.teleport(pos);
        }
        List<Player> players = arena.getPlayers();
        getPlugin().getServer().getScheduler().scheduleAsyncTask(null, new AsyncTask() {
            @Override
            public void onRun() {
                Collections.shuffle(players);
            }
        });
        Player murder = players.get(0);
        Player detetive = players.get(1);
        arena.setMurder(new Murder(murder, arena));
        arena.setDetective(new Detective(detetive, arena));
        murder.sendTitle("§cVocê é o Murder!");
        detetive.sendTip("§eVocê é o Detetive!");
        pos.subtract(0, 1);
    }

    public void stop(MurderArena arena){
        arena.setCountdown(30);
        arena.setSwordCountdown(15);
        arena.setState(ArenaState.GAME_COUNTDOWN);
    }

    public void checkWin(MurderArena arena){
        if(arena.getPlayers().size() == 1 && !arena.getMurder().isDead()){
            Player player = arena.getMurder().getPlayer();
            player.sendTitle("§eVocê matou todos", "§cVocê ganhou");
            arena.removePlayer(player);
            player.teleport(getPlugin().getServer().getDefaultLevel().getSpawnLocation());
        }
        if(arena.getMurder().isDead()){
            for(Player p : arena.getPlayers()){
                if(arena.getMurder().getPlayer().equals(p)) continue;
                p.sendTitle("§eMurder morreu", "§cVocê ganhou");
            }
        }

        getPlugin().getServer().getScheduler().scheduleDelayedTask(null, new Task() {
            @Override
            public void onRun(int i) {
                arena.setState(ArenaState.GAME_RESET);
                stop(arena);
            }
        }, 20 * 8);
    }
}
