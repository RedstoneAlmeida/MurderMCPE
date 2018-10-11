package mc.slice.murder;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.ConfigSection;
import mc.slice.murder.arena.MurderArena;
import mc.slice.murder.commands.MurderCommand;
import mc.slice.murder.manager.ArenaManager;
import mc.slice.murder.manager.TimerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Loader extends PluginBase {

    private List<MurderArena> arenas = new ArrayList<>();
    private HashMap<Player, MurderArena> playerArena = new HashMap<>();

    private TimerManager timerManager;
    private ArenaManager arenaManager;

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public TimerManager getTimerManager() {
        return timerManager;
    }

    public HashMap<Player, MurderArena> getPlayerArena() {
        return playerArena;
    }

    public List<MurderArena> getArenas() {
        return arenas;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        for(Level level : getServer().getLevels().values()){
            getServer().loadLevel(level.getName());
        }
        LoadWorld();
        loadArenas();
        arenaManager = new ArenaManager(this);
        timerManager = new TimerManager(this);

        if (getConfig().get("arenas") == null) {
            getConfig().set("arenas", new ConfigSection());
            getConfig().save();
        }

        getServer().getCommandMap().register("Murder", new MurderCommand(this));
        getServer().getPluginManager().registerEvents(new MurderListener(this), this);

        timerManager.run();
    }

    public void LoadWorld() {
        String dir2 = null;
        Level level = this.getServer().getDefaultLevel();
        level.getFolderName();
        File directory = new File("");

        try {
            String var11 = directory.getCanonicalPath();
            dir2 = var11 + "/worlds/";
        } catch (Exception var8) {
            ;
        }

        File var9 = new File(dir2);
        File[] fa = var9.listFiles();

        for(int i = 0; i < fa.length; ++i) {
            File fs = fa[i];
            if(fs.isDirectory() && !this.getServer().isLevelLoaded(fs.getName())) {
                this.getServer().loadLevel(fs.getName());
            }
        }

    }

    public void loadArenas(){
        for(String key : getConfig().getAll().keySet()){
            ConfigSection section = getConfig().getSection(key);

            String arenaName = section.getString("name");
            String levelName = section.getString("level");

            int slots = section.getInt("slots");
            double x = section.getDouble("spawn.x");
            double y = section.getDouble("spawn.y");
            double z = section.getDouble("spawn.z");
            Level level = getServer().getLevelByName(levelName);
            Position position = new Position(x, y, z, level);
            MurderArena arena = new MurderArena(this, arenaName, levelName, slots);
            arena.setSpawn(position);
            getLogger().info(String.format("§aCarregada: §c%s", arena.getArenaName()));

            arenas.add(arena);
        }
    }

    public void addArena(MurderArena arena){
        ConfigSection arenaSection = new ConfigSection();
        arenaSection.put("name", arena.getArenaName());
        arenaSection.put("level", arena.getLevelName());
        arenaSection.put("slots", arena.getSlots());
        arenaSection.put("spawn.x", arena.getSpawn().getX());
        arenaSection.put("spawn.y", arena.getSpawn().getY());
        arenaSection.put("spawn.z", arena.getSpawn().getZ());
        getConfig().set(arena.getArenaName(), arenaSection);
        getConfig().save();
        this.arenas.add(arena);
    }

    public void setBuild(Player p, boolean value){
        AdventureSettings settings = p.getAdventureSettings();
        settings.set(AdventureSettings.Type.BUILD_AND_MINE, value);
        settings.set(AdventureSettings.Type.WORLD_BUILDER, value);
        settings.update();
    }
}
