package org.stormdev.mcwipeout;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.stormdev.StormPlugin;
import org.stormdev.commands.registry.CommandRegistry;
import org.stormdev.mcwipeout.commands.PingCommand;
import org.stormdev.mcwipeout.commands.StuckCommand;
import org.stormdev.mcwipeout.commands.TogglePlayersCmd;
import org.stormdev.mcwipeout.commands.WipeoutCommand;
import org.stormdev.mcwipeout.frame.game.GameManager;
import org.stormdev.mcwipeout.frame.game.MapManager;
import org.stormdev.mcwipeout.frame.obstacles.Obstacle;
import org.stormdev.mcwipeout.frame.team.Team;
import org.stormdev.mcwipeout.frame.team.TeamManager;
import org.stormdev.mcwipeout.listeners.ObstacleEvents;
import org.stormdev.mcwipeout.utils.WipeoutPlaceholderExpansion;
import org.stormdev.mcwipeout.utils.worldguardhook.SimpleWorldGuardAPI;
import org.stormdev.mcwipeout.utils.worldguardhook.WgPlayer;
import org.stormdev.mcwipeout.utils.worldguardhook.WgRegionListener;
import org.stormdev.shade.gson.Gson;
import org.stormdev.shade.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.UUID;

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

    private static Gson gson;

    public static Gson getGson() {
        return gson;
    }

    @Override
    public void onEnable() {
        plugin = this;

        this.adventure = BukkitAudiences.create(this);

        playerCache = new HashMap<>();
        simpleWorldGuardAPI = new SimpleWorldGuardAPI();
        Bukkit.getPluginManager().registerEvents(new WgRegionListener(this), this);

        init();

        loadData();

        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        saveData();

        if (getGameManager().getActiveMap() != null) {
            getGameManager().getActiveMap().getObstacles().forEach(Obstacle::reset);
        }

        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }

        plugin = null;
    }

    private void registerListeners() {
        new ObstacleEvents(this);
    }

    private void registerCommands() {
        getLogger().info("Registering commands!");
        new WipeoutCommand(this).register();
        new StuckCommand(this).register();
        new TogglePlayersCmd(this).register();
        new PingCommand(this).register();

        CommandRegistry.syncCommand();
    }

    private void init() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WipeoutPlaceholderExpansion().register();
        }

        getLogger().info("Initializing managers!");
        teamManager = new TeamManager(this);
        gameManager = new GameManager(this);
        mapManager = new MapManager(this);
    }

    @SneakyThrows
    private void loadData() {
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

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
}
