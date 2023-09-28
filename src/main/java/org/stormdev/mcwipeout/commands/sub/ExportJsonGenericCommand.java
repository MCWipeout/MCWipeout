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
import org.stormdev.mcwipeout.frame.obstacles.GenericLocationSet;
import org.stormdev.mcwipeout.utils.helpers.BlockFaceHelper;
import org.stormdev.mcwipeout.utils.helpers.WLocation;
import org.stormdev.utils.Color;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class ExportJsonGenericCommand extends StormSubCommand {
    private Wipeout plugin;
    private Set<String> aliases;

    private List<GenericLocationSet> exports;

    public ExportJsonGenericCommand(Wipeout plugin) {
        super(0, "&cInvalid Args!");
        this.plugin = plugin;
        this.aliases = new HashSet<>();
        aliases.add("exportasjsongeneric");
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
            sender.sendMessage(ChatColor.RED + "/wipeout exportasjsongeneric (x, y, z) (material)");
            sender.sendMessage(ChatColor.RED + "/wipeout exportasjsongeneric (fileId)");
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

        } else if (args.length == 4) {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            Material material = Material.valueOf(args[3]);

            Set<Block> list = BlockFaceHelper.getConnectionWithType(WLocation.from(x, y, z).asBlock(), Material.valueOf(args[3]));

            Map<WLocation, Material> map = new HashMap<>();

            list.forEach(block -> {
                if (block.getType() == material) {
                    map.put(WLocation.from(block), block.getType());
                }
            });

            GenericLocationSet jsonPlatformSection = new GenericLocationSet(map);

            Bukkit.broadcast(Color.colorize("&eExported a JSON Object, Block list: " + map.size()), "wipeout.*");

            exports.add(jsonPlatformSection);
        }
    }
}
