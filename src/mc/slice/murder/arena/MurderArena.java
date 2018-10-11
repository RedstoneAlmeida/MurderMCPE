package mc.slice.murder.arena;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import mc.slice.murder.Loader;
import mc.slice.murder.utils.ArenaState;

import java.util.ArrayList;
import java.util.List;

public class MurderArena {

    private List<Player> players = new ArrayList<>();
    private List<Player> spectators = new ArrayList<>();
    private ArenaState state = ArenaState.GAME_COUNTDOWN;

    private Loader plugin;
    private String arenaName;
    private String levelName;
    private int slots;

    private int countdown = 30;
    private int swordCountdown = 15;

    private Murder murder;
    private Detective detective;

    public Position spawn;

    public MurderArena(Loader plugin, String arenaName, String levelName, int slots){
        this.plugin = plugin;
        this.arenaName = arenaName;
        this.levelName = levelName;
        this.slots = slots;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getArenaName() {
        return arenaName;
    }

    public Position getSpawn() {
        return spawn;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public int getSwordCountdown() {
        return swordCountdown;
    }

    public void setSwordCountdown(int swordCountdown) {
        this.swordCountdown = swordCountdown;
    }

    public int getSlots() {
        return slots;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setSpawn(Position spawn) {
        this.spawn = spawn;
    }

    public void setMurder(Murder murder) {
        this.murder = murder;
    }

    public void setDetective(Detective detective) {
        this.detective = detective;
    }

    public Detective getDetective() {
        return detective;
    }

    public Murder getMurder() {
        return murder;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public void addSpec(Player player){
        if(!spectators.contains(player)){
            spectators.add(player);
        }
    }

    public void removeSpec(Player player){
        if(spectators.contains(player)){
            spectators.remove(player);
        }
    }

    public void addPlayer(Player player){
        if(!players.contains(player)){
            players.add(player);
        }
    }

    public void removePlayer(Player player){
        if(players.contains(player)){
            players.remove(player);
        }
    }

    public int countPlayers(){
        return players.size();
    }

    public void teleportPlayer(Player player){
        if(countPlayers() + 1 > getSlots()) return;
        addPlayer(player);
        player.teleport(getSpawn());
    }

    public void sendMessageToAll(String message){
        for (Player p : players){
            p.sendMessage(message);
        }
    }
}
