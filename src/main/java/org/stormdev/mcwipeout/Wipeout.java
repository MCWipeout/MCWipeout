package org.stormdev.mcwipeout;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.stormdev.StormPlugin;
import org.stormdev.commands.StormCommand;
import org.stormdev.commands.registry.CommandRegistry;
import org.stormdev.mcwipeout.commands.PingCommand;
import org.stormdev.mcwipeout.commands.StuckCommand;
import org.stormdev.mcwipeout.commands.TogglePlayersCmd;
import org.stormdev.mcwipeout.commands.WipeoutCommand;
import org.stormdev.mcwipeout.frame.game.GameManager;
import org.stormdev.mcwipeout.frame.game.MapManager;
import org.stormdev.mcwipeout.frame.obstacles.GenericLocationSet;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.mcwipeout.frame.team.TeamManager;
import org.stormdev.mcwipeout.frame.team.WipeoutPlayer;
import org.stormdev.mcwipeout.listeners.ObstacleEvents;
import org.stormdev.mcwipeout.listeners.PlayerEvents;
import org.stormdev.mcwipeout.listeners.RegionEvents;
import org.stormdev.mcwipeout.utils.WipeoutPlaceholderExpansion;
import org.stormdev.mcwipeout.utils.helpers.GenericLocationTypeAdapter;
import org.stormdev.mcwipeout.utils.helpers.MovingSectionTypeAdapter;
import org.stormdev.mcwipeout.utils.worldguardhook.SimpleWorldGuardAPI;
import org.stormdev.mcwipeout.utils.worldguardhook.WgPlayer;
import org.stormdev.mcwipeout.utils.worldguardhook.WgRegionListener;
import org.stormdev.shade.gson.Gson;
import org.stormdev.shade.gson.GsonBuilder;
import org.stormdev.utils.Color;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public final class Wipeout extends StormPlugin<Wipeout> {

    @Getter
    private BukkitAudiences adventure;

    private static Wipeout plugin;

    public static Wipeout get() {
        return plugin;
    }

    @Getter
    private TeamManager teamManager;

    @Getter
    private MapManager mapManager;

    @Getter
    private GameManager gameManager;

    @Getter
    private HashMap<UUID, WgPlayer> playerCache;
    @Getter
    private SimpleWorldGuardAPI simpleWorldGuardAPI;

    @Getter
    private World world;

    private List<StormCommand> commandList;

    private static Gson gson;

    public static Gson getGson() {
        return gson;
    }

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.broadcast(Color.colorize("&eEnabling MCWipeout..."), "mcwipeout.*");
        long time = System.currentTimeMillis();

        this.adventure = BukkitAudiences.create(this);

        playerCache = new HashMap<>();
        simpleWorldGuardAPI = new SimpleWorldGuardAPI();
        Bukkit.getPluginManager().registerEvents(new WgRegionListener(this), this);

        init();

        new WipeoutAPI(this);

        loadData();

        world = Bukkit.getWorld("maps");

        registerListeners();
        registerCommands();

        Bukkit.broadcast(Color.colorize("&eEnabled in " + (System.currentTimeMillis() - time) + " ms."), "mcwipeout.*");
    }

    @Override
    public void onDisable() {
        saveData();

        playerCache.clear();

        if (gameManager.getActiveMap() != null) {
            for (Obstacle obstacle : gameManager.getActiveMap().getObstacles()) {
                obstacle.setEnabled(false);
                obstacle.reset();
            }
            if (gameManager.getBossBar() != null) {
                gameManager.getBossBar().removeAll();
            }

            gameManager.setActiveMap(null);
        }

        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }

        for (StormCommand stormCommand : commandList) {
            stormCommand.unregister();
        }

        Bukkit.broadcast(Color.colorize("&eDisabling MCWipeout..."), "mcwipeout.*");

        CommandRegistry.syncCommand();
    }

    private void registerListeners() {
        new ObstacleEvents(this);
        new PlayerEvents(this);
        new RegionEvents(this);
    }

    private void registerCommands() {
        getLogger().info("Registering commands!");

        WipeoutCommand wipeoutCommand = new WipeoutCommand(this);
        wipeoutCommand.register();

        StuckCommand stuckCommand = new StuckCommand(this);
        stuckCommand.register();

        TogglePlayersCmd togglePlayersCmd = new TogglePlayersCmd(this);
        togglePlayersCmd.register();

        PingCommand pingCommand = new PingCommand(this);
        pingCommand.register();

        commandList.addAll(Arrays.asList(wipeoutCommand, stuckCommand, togglePlayersCmd, pingCommand));

        CommandRegistry.syncCommand();
    }

    private void init() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WipeoutPlaceholderExpansion().register();
        }

        gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(GenericLocationSet.class, new GenericLocationTypeAdapter()).registerTypeAdapter(JsonPlatformSection.class, new MovingSectionTypeAdapter()).disableHtmlEscaping().create();

        getLogger().info("Initializing managers!");
        teamManager = new TeamManager(this);
        gameManager = new GameManager(this);
        mapManager = new MapManager(this);
        commandList = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerCache.remove(player.getUniqueId());
            playerCache.put(player.getUniqueId(), new WgPlayer(player));

            teamManager.getWipeoutPlayers().add(new WipeoutPlayer(player.getUniqueId(), false));

            player.getInventory().remove(Material.EMERALD);

            for (Player pl : Bukkit.getOnlinePlayers()) {
                player.showPlayer(this, pl);
            }
        }
    }

    @SneakyThrows
    private void loadData() {
        File file = new File(getDataFolder(), "data.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            saveResource("data.json", false);
            return;
        }

        Team[] teams = gson.fromJson(new FileReader(file), Team[].class);

        if (teams == null) return;

        for (Team fromList : teams) {
            Team newTeam = new Team(fromList.getId(), fromList.getMembers());
            newTeam.setColor(fromList.getColor());
            newTeam.setCheckPointMap(new HashMap<>());

            teamManager.addTeam(newTeam);
        }

    }

    @SneakyThrows
    private void saveData() {

        File file = new File(getDataFolder(), "data.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            saveResource("data.json", false);
        }

        try (final Writer writer = new FileWriter(file)) {
            gson.toJson(teamManager.getTeamList(), writer);
            writer.flush();
        }

    }

    public WgPlayer getPlayer(UUID uuid) {
        return playerCache.get(uuid);
    }

    public static boolean isGameRunning() {
        return plugin.getGameManager().getActiveMap() != null;
    }
}
