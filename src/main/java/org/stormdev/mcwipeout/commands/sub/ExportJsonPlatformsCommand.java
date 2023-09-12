package org.stormdev.mcwipeout.commands.sub;
/*
  Created by Stormbits at 2/12/2023
*/

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.stormdev.commands.CommandContext;
import org.stormdev.commands.StormSubCommand;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.PlatformSettings;
import org.stormdev.mcwipeout.utils.WLocation;
import org.stormdev.mcwipeout.utils.helpers.BlockFaceHelper;
import org.stormdev.utils.Color;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class ExportJsonPlatformsCommand extends StormSubCommand {
    private Wipeout plugin;
    private Set<String> aliases;

    private List<JsonPlatformSection> exports;

    public ExportJsonPlatformsCommand(Wipeout plugin) {
        super(0, "&cInvalid Args!");
        this.plugin = plugin;
        this.aliases = new HashSet<>();
        aliases.add("exportasjsonplatform");
        exports = new ArrayList<>();
    }


    @Override
    public Set<String> aliases() {
        return aliases;
    }

    @SneakyThrows
    @Override
    public void execute(CommandContext<?> commandContext) {
        String[] args = commandContext.args();
        CommandSender sender = commandContext.sender();

        if (!sender.hasPermission("wipeout.admin")) {
            sender.sendMessage(ChatColor.RED + "No permissions!");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/wipeout exportasjsonplatform (x, y, z)");
            sender.sendMessage(ChatColor.RED + "/wipeout exportasjsonplatform (fileId)");
            return;
        } else if (args.length == 1) {
            String fileId = args[0];

            File file = new File(Wipeout.get().getDataFolder() + "/exported/", fileId + ".json");
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
                //Wipeout.get().saveResource("exported/" + fileId + ".json", false);
            }

            try (final Writer writer = new FileWriter(file)) {
                Wipeout.getGson().toJson(exports, writer);
                writer.flush();
            }

            Bukkit.broadcast(Color.colorize("&eExported: " + exports.size() + " JSON objects to /exported/" + fileId + ".json"), "wipeout.*");

            exports.clear();

            return;

        } else if (args.length == 3) {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);

            Set<Block> list = BlockFaceHelper.getConnection(WLocation.from(x, y, z).asBlock());

            Map<WLocation, Material> map = new HashMap<>();

            list.forEach(block -> map.put(WLocation.from(block), block.getType()));

            JsonPlatformSection jsonPlatformSection = new JsonPlatformSection(map, PlatformSettings.builder().build());

            Bukkit.broadcast(Color.colorize("&eExported a JSON Object, Block list: " + list.size()), "wipeout.*");

            exports.add(jsonPlatformSection);
        }
    }
}
