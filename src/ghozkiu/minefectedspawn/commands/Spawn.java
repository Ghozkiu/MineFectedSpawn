package ghozkiu.minefectedspawn.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ghozkiu.minefectedspawn.MineFectedSpawn;
import ghozkiu.minefectedspawn.classes.SpawnPoint;
import ghozkiu.minefectedspawn.utils.Lang;
import ghozkiu.minefectedspawn.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Spawn implements CommandExecutor {
    public static List<SpawnPoint> locations = new ArrayList<>();
    private final String locationsFile;
    FileConfiguration pluginConfig;

    public Spawn(MineFectedSpawn plugin) {
        this.pluginConfig = plugin.getConfig();
        this.locationsFile = plugin.getLocationsFilePath();
    }

    public static void loadLocations(String locationsFile) throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(locationsFile));
        Type locationType = new TypeToken<ArrayList<SpawnPoint>>() {
        }.getType();
        locations = gson.fromJson(reader, locationType);
        reader.close();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //mfspawn create %locName%
        //mfspawn remove %locName%
        //mfspawn spawn %locName%
        //mfspawn spawn %locName% %player%
        //mfspawn spawn
        //mfspawn list

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location playerLoc = player.getLocation();
            switch (args[0]) {
                //mfspawn create %locName%
                case "create":
                    if (player.hasPermission(Permissions.STAFF.get())) {
                        if (args.length == 2) {
                            String spawnName = args[1].replace('_', ' ');
                            SpawnPoint newSpawnPoint = new SpawnPoint(spawnName, playerLoc.getWorld().getName(), playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), playerLoc.getYaw(), playerLoc.getPitch());
                            if (findSpawnPoint(newSpawnPoint.getName()) == null) {
                                locations.add(newSpawnPoint);
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_ALREADY_EXISTS.get())
                                        .replaceAll("<SPName>", spawnName + "")));
                            }
                            if (saveLocations()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_CREATED.get())
                                        .replaceAll("<SPName>", spawnName + "")));
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_CREATION_ERROR.get())
                                        .replaceAll("<SPName>", spawnName + "")));
                            }

                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_NAME_ERROR.get())));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_PERMISSION_ERROR.get())));
                    }
                    break;
                //mfspawn remove %locName%
                case "remove":
                    if (player.hasPermission(Permissions.STAFF.get())) {
                        if (args.length == 2) {
                            String spawnName = args[1].replace('_', ' ');
                            SpawnPoint spawnToRemove = findSpawnPoint(spawnName);
                            if (spawnToRemove != null) {
                                locations.remove(spawnToRemove);
                                if (saveLocations()) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_REMOVED.get())
                                            .replaceAll("<SPName>", spawnName + "")));
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_REMOVE_ERROR.get())
                                            .replaceAll("<SPName>", spawnName + "")));
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_NAME_ERROR.get())));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_PERMISSION_ERROR.get())));
                    }
                    break;
                case "spawn": {
                    int random = (int) (Math.random() * (locations.size()));
                    switch (args.length) {
                        //mfspawn spawn %locName%
                        case 2: {
                            if (player.hasPermission(Permissions.STAFF.get())) {
                                String spawnName = args[1].replace("_", " ");
                                SpawnPoint spawnPoint = findSpawnPoint(spawnName);
                                if (spawnPoint != null) {
                                    player.teleport(spawnPoint.getLocation());
                                    tpMessage(player, spawnName);
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_NOT_FOUND.get())
                                            .replaceAll("<SPName>", spawnName + "")));
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_PERMISSION_ERROR.get())));
                            }
                            break;
                        }
                        //mfspawn spawn %locName% %player%
                        case 3: {
                            if (player.hasPermission(Permissions.STAFF.get())) {
                                Player playerToTp = Bukkit.getPlayer(args[2]);
                                String spawnName = args[1].replace("_", " ");
                                SpawnPoint spawnPoint = findSpawnPoint(spawnName);
                                if (playerToTp != null && spawnPoint != null) {
                                    playerToTp.teleport(spawnPoint.getLocation());
                                    tpMessage(playerToTp, spawnName);
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_PERMISSION_ERROR.get())));
                            }
                            break;
                        }
                        //mfspawn spawn
                        default:
                            if (player.hasPermission(Permissions.USER.get())) {
                                SpawnPoint spawnPoint = locations.get(random);
                                player.teleport(spawnPoint.getLocation());
                                tpMessage(player, spawnPoint.getName());
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_PERMISSION_ERROR.get())));
                            }
                    }
                    break;
                }
                //mfspawn playerpsawn %player%
                case "playerspawn":
                    if (player.hasPermission(Permissions.STAFF.get()) && args.length == 2) {
                        int random = (int) (Math.random() * (locations.size()));
                        Player playerToTp = Bukkit.getPlayer(args[1]);
                        SpawnPoint spawnPoint = locations.get(random);
                        if (playerToTp != null && spawnPoint != null) {
                            playerToTp.teleport(spawnPoint.getLocation());
                            tpMessage(playerToTp, spawnPoint.getName());
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_PERMISSION_ERROR.get())));
                    }
                    break;
                //mfspawn list
                case "list":
                    if (player.hasPermission(Permissions.STAFF.get())) {
                        String list = "";
                        for (SpawnPoint spawnPoint : locations) {
                            if (list.length() == 0) {
                                list += spawnPoint.getName();
                            }
                            list += ", " + spawnPoint.getName();
                        }
                        player.sendMessage(ChatColor.GREEN + list);
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.SP_PERMISSION_ERROR.get())));
                    }
                    break;
                case "help":
                    //mfspawn create %locName%
                    //mfspawn remove %locName%
                    //mfspawn spawn %locName%
                    //mfspawn spawn %locName% %player%
                    //mfspawn spawn
                    //mfspawn list
                    player.sendMessage(ChatColor.AQUA + "-----[ " + ChatColor.GREEN + "MineFected " + ChatColor.YELLOW + "SpawnPoints" + ChatColor.AQUA + " ]-----");
                    player.sendMessage(ChatColor.YELLOW + "/mfspawn " + ChatColor.WHITE + "create " + ChatColor.AQUA + "[name]");
                    player.sendMessage(ChatColor.YELLOW + "/mfspawn " + ChatColor.WHITE + "remove " + ChatColor.AQUA + "[name]");
                    player.sendMessage(ChatColor.YELLOW + "/mfspawn " + ChatColor.WHITE + "spawn " + ChatColor.AQUA + "[name]");
                    player.sendMessage(ChatColor.YELLOW + "/mfspawn " + ChatColor.WHITE + "spawn " + ChatColor.AQUA + "[name]" + ChatColor.GOLD + " [player]");
                    player.sendMessage(ChatColor.YELLOW + "/mfspawn " + ChatColor.WHITE + "playerspawn " + ChatColor.GOLD + " [player]");
                    player.sendMessage(ChatColor.YELLOW + "/mfspawn " + ChatColor.WHITE + "spawn");
                    player.sendMessage(ChatColor.YELLOW + "/mfspawn " + ChatColor.WHITE + "list");
                    player.sendMessage(ChatColor.YELLOW + "/mfspawn " + ChatColor.WHITE + "help");
                    break;
            }
        }
        return false;
    }

    private SpawnPoint findSpawnPoint(String name) {
        for (SpawnPoint spawnPoint : locations) {
            if (spawnPoint.getName().equals(name)) {
                return spawnPoint;
            }
        }
        return null;
    }

    private void tpMessage(Player player, String spawnName) {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', pluginConfig
                        .getString(Lang.SP_TITLE.get())), ChatColor.translateAlternateColorCodes('&', pluginConfig
                        .getString(Lang.SP_SUBTITLE.get())), 10,
                40, 10);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(Lang.PLAYER_TP.get())
                .replaceAll("<SPName>", spawnName + "")));
    }

    public boolean saveLocations() {
        try {
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter(locationsFile);
            gson.toJson(locations, fileWriter);
            fileWriter.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
