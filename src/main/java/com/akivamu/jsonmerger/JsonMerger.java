package com.akivamu.jsonmerger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class JsonMerger {
    public JsonObject merge(JsonObject originJsonObject, JsonObject updateJsonObject) {
        // TODO validation

        JsonObject resultJsonObject = originJsonObject.deepCopy();

        // Resolve conflicts
        for (Map.Entry<String, JsonElement> originEntry : originJsonObject.entrySet()) {

            String key = originEntry.getKey();

            // Conflict
            if (updateJsonObject.has(key)) {
                // Case Primitive || JsonArray: overwrite
                if (originEntry.getValue().isJsonPrimitive() || originEntry.getValue().isJsonArray()) {
                    resultJsonObject.add(key, updateJsonObject.get(key));
                }

                // Case JsonObject: recursive merge
                if (originEntry.getValue().isJsonObject()) {
                    resultJsonObject.add(key, merge(originEntry.getValue().getAsJsonObject(), updateJsonObject.get(key).getAsJsonObject()));
                }
            }
        }

        // Add new entry
        for (Map.Entry<String, JsonElement> updateEntry : updateJsonObject.entrySet()) {

            String key = updateEntry.getKey();

            // New
            if (!originJsonObject.has(key)) {
                resultJsonObject.add(key, updateJsonObject.get(key));
            }
        }

        return resultJsonObject;
    }
}
