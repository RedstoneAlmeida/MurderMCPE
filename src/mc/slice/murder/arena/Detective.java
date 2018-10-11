package mc.slice.murder.arena;

import cn.nukkit.Player;

public class Detective {

    private MurderArena arena;
    private Player player;

    public Detective(Player player, MurderArena arena){
        this.player = player;
        this.arena = arena;
    }

    public MurderArena getArena() {
        return arena;
    }

    public Player getPlayer() {
        return player;
    }

}
