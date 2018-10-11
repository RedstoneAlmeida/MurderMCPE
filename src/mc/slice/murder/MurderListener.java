package mc.slice.murder;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.scheduler.Task;
import mc.slice.murder.arena.MurderArena;

public class MurderListener implements Listener {

    private Loader plugin;

    public MurderListener(Loader plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void itemPickup(InventoryPickupItemEvent event){
        if(event.getInventory().getHolder() instanceof Player) {
            Player player = (Player) event.getInventory().getHolder();
            if(plugin.getPlayerArena().containsKey(player)){
                MurderArena arena = plugin.getPlayerArena().get(player);
                if(arena != null){
                    if(arena.getMurder().getPlayer().equals(player) && event.getItem().getId() == Item.BOW){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.setDeathMessage("");
        Player player = event.getEntity();
        plugin.getServer().getScheduler().scheduleDelayedTask(null, new Task() {
            @Override
            public void onRun(int i) {
                RespawnPacket packet = new RespawnPacket();
                player.dataPacket(packet);
                if(plugin.getPlayerArena().containsKey(player)) {
                    MurderArena arena = plugin.getPlayerArena().get(player);
                    if (arena != null) {
                        arena.sendMessageToAll(String.format("§c%s §6morreu", player.getName()));
                        arena.removePlayer(player);
                        if(arena.getMurder().getPlayer().equals(player)){
                            arena.getMurder().setDead(true);
                        }
                        arena.addSpec(player);
                        player.setGamemode(Player.SPECTATOR);
                        player.setNameTagAlwaysVisible(true);
                    }
                }
            }
        }, 15);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        if(!p.isOp()){
            plugin.setBuild(p, false);
        }
    }

}
