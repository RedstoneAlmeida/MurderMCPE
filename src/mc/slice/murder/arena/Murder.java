package mc.slice.murder.arena;

import cn.nukkit.Player;

public class Murder {

    private MurderArena arena;
    private Player player;
    private boolean isDead = false;

    public Murder(Player player, MurderArena arena){
        this.player = player;
        this.arena = arena;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    public MurderArena getArena() {
        return arena;
    }

    public Player getPlayer() {
        return player;
    }
}
