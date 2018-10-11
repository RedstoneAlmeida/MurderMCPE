package mc.slice.murder.manager;

import mc.slice.murder.Loader;

public class Manager {

    private Loader plugin;

    public Manager(Loader plugin){
        this.plugin = plugin;
    }

    public Loader getPlugin() {
        return plugin;
    }
}
