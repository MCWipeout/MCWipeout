package org.stormdev.mcwipeout.utils.helpers;
/*
  Created by Stormbits at 9/11/2023
*/


import org.bukkit.Material;
import org.stormdev.mcwipeout.Wipeout;
import org.stormdev.mcwipeout.frame.obstacles.GenericLocationSet;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.JsonPlatformSection;
import org.stormdev.mcwipeout.frame.obstacles.platforms.helpers.PlatformSettings;
import org.stormdev.mcwipeout.utils.WLocation;
import org.stormdev.shade.gson.TypeAdapter;
import org.stormdev.shade.gson.internal.LinkedTreeMap;
import org.stormdev.shade.gson.reflect.TypeToken;
import org.stormdev.shade.gson.stream.JsonReader;
import org.stormdev.shade.gson.stream.JsonToken;
import org.stormdev.shade.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GenericLocationTypeAdapter extends TypeAdapter<GenericLocationSet> {

    private static Type seriType = new TypeToken<Map<String, Object>>() {
    }.getType();

    @Override
    public void write(JsonWriter jsonWriter, GenericLocationSet jsonPlatformSection) throws IOException {
        if (jsonPlatformSection == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.value(getRaw(jsonPlatformSection));
    }

    @Override
    public GenericLocationSet read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        return fromRaw(jsonReader.nextString());
    }

    private String getRaw(GenericLocationSet movingSection) {
        Map<String, Object> serial = new HashMap<>();

        StringBuilder builder = new StringBuilder();

        for (Map.Entry<WLocation, Material> map : movingSection.getMap().entrySet()) {
            builder.append(map.getKey().asStringLocation()).append(":").append(map.getValue().name()).append(",");
        }

        serial.put("locations", builder.toString());

        return Wipeout.getGson().toJson(serial);
    }

    private GenericLocationSet fromRaw(String raw) {
        Map<String, Object> keys = Wipeout.getGson().fromJson(raw, seriType);

        Map<WLocation, Material> map = new HashMap<>();

        String longString = (String) keys.get("locations");

        String[] splitString = longString.split(",");

        for (String s : splitString) {
            String[] coords = s.split(":");
            map.put(WLocation.from(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2])), Material.valueOf(coords[3]));
        }

        return new GenericLocationSet(map);
    }
}
