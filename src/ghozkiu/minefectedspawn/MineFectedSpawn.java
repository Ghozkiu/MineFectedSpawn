package ghozkiu.minefectedspawn;

import ghozkiu.minefectedspawn.commands.Spawn;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class MineFectedSpawn extends JavaPlugin {
    private final String locationsFilePath = getDataFolder() + "\\locations.json";
    public String configPath;

    public String getLocationsFilePath() {
        return locationsFilePath;
    }

    public void onEnable() {
        getLogger().info(ChatColor.GREEN + "[MineFectedSpawn] Enabled");
        commandsRegister();
        configRegister();

        try {
            dataInitialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        getLogger().info(ChatColor.YELLOW + "[MineFectedSpawn] Disabling...");
    }

    public void commandsRegister() {
        getCommand("mfspawn").setExecutor(new Spawn(this));
    }

    private void dataInitialize() throws IOException {
        File locationsFile = new File(locationsFilePath);
        if (!locationsFile.createNewFile()) {
            Spawn.loadLocations(locationsFilePath);
        }
    }


    public void configRegister() {
        File config = new File(getDataFolder(), "config.yml");
        this.configPath = config.getPath();
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
}
