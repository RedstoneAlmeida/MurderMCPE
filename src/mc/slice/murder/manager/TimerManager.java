package mc.slice.murder.manager;

import cn.nukkit.Player;
import cn.nukkit.item.ItemSwordDiamond;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.sound.ClickSound;
import cn.nukkit.scheduler.Task;
import mc.slice.murder.Loader;
import mc.slice.murder.arena.MurderArena;
import mc.slice.murder.utils.ArenaState;

/**
 * Created by ASUS on 07/02/2018.
 */
public class TimerManager extends Manager {

    public TimerManager(Loader plugin) {
        super(plugin);
    }

    public void run(){
        for(MurderArena arena : getPlugin().getArenas()){
            getPlugin().getServer().getScheduler().scheduleDelayedRepeatingTask(null, new Task() {
                @Override
                public void onRun(int i) {
                    if(arena.getState() == ArenaState.GAME_RUNNING){
                        if(arena.getSwordCountdown() > 1) arena.setSwordCountdown(arena.getSwordCountdown() - 1);
                        if(arena.getSwordCountdown() == 1){
                            ItemSwordDiamond sword = new ItemSwordDiamond();
                            sword.addEnchantment(Enchantment.get(Enchantment.ID_DAMAGE_ALL).setLevel(15, false));
                            arena.getMurder().getPlayer().getInventory().setItem(0, sword);
                        }
                        getPlugin().getArenaManager().checkWin(arena);
                    }
                    if(arena.getState() == ArenaState.GAME_COUNTDOWN){
                        if(arena.getPlayers().size() >= 2){
                            arena.setCountdown(arena.getCountdown() - 1);
                            if(arena.getCountdown() <= 1){
                                getPlugin().getArenaManager().start(arena);
                                return;
                            }
                            for(Player p : arena.getPlayers()){
                                if(arena.getCountdown() <= 10){
                                    p.sendTitle("§e" + arena.getCountdown());
                                    p.getLevel().addSound(new ClickSound(p), p);
                                } else {
                                    p.sendTip("§aJogo começará em:§6 " + arena.getCountdown() + " §asegundos");
                                }
                            }
                        } else {
                            for (Player player : arena.getPlayers()) {
                                player.sendTip("§bEsperando Jogadores");
                            }
                        }
                    }
                }
            }, 20, 20);
        }
    }
}
