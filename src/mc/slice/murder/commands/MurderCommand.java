package mc.slice.murder.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import mc.slice.murder.Loader;
import mc.slice.murder.arena.MurderArena;
import mc.slice.murder.utils.ArenaState;

public class MurderCommand extends Command {

    private Loader plugin;

    public MurderCommand(Loader plugin) {
        super("murder");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            if (args.length >= 1) {
                if (args[0].equals("add")) {
                    if (args.length >= 2) {
                        String name = args[1];
                        int slots = Integer.parseInt(args[2]);
                        MurderArena arena = new MurderArena(plugin, name, ((Player) sender).getLevel().getName(), slots);
                        arena.setSpawn(((Player) sender).getPosition());
                        plugin.addArena(arena);
                        return false;
                    }
                    return false;
                } else if(args[0].equals("join")){
                    String name = args[1];
                    for(MurderArena arena : plugin.getArenas()){
                        if(arena.getArenaName().equalsIgnoreCase(name)){
                            if(arena.getState() == ArenaState.GAME_COUNTDOWN){
                                plugin.getArenaManager().join((Player) sender, arena);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
