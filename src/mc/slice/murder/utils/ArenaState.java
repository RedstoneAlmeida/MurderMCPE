package mc.slice.murder.utils;

public enum  ArenaState {

    GAME_COUNTDOWN(0), GAME_RUNNING(1), GAME_RESET(2);

    private int id;

    ArenaState(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
