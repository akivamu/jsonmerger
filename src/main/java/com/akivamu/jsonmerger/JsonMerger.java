package com.akivamu.jsonmerger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Setter;

import java.util.Map;

public class JsonMerger {
    @Setter
    private JsonMergerTypeMismatchedStrategy typeMismatchedStrategy = JsonMergerTypeMismatchedStrategy.PREFER_ORIGIN;
    @Setter
    private JsonMergerArrayStrategy arrayStrategy = JsonMergerArrayStrategy.CONCAT;
    @Setter
    private JsonMergerPrimitiveStrategy primitiveStrategy = JsonMergerPrimitiveStrategy.PREFER_UPDATE;

    public JsonObject merge(JsonObject originObject, JsonObject extObject) {
        // Validation
        if (originObject == null) return null;
        JsonObject resultObject = originObject.deepCopy();
        if (extObject == null) return resultObject;

        for (Map.Entry<String, JsonElement> extEntry : extObject.entrySet()) {
            String extKey = extEntry.getKey();
            JsonElement extValue = extEntry.getValue();

            if (!originObject.has(extKey)) {
                // New entry, just add
                resultObject.add(extKey, extValue);
            } else {
                // Both objects have duplicate key
                JsonElement originValue = originObject.get(extKey);

                if (originValue.isJsonObject() && extValue.isJsonObject()) {
                    // Case both JsonObject: recursive merging
                    resultObject.add(extKey, merge(originValue.getAsJsonObject(), extValue.getAsJsonObject()));
                } else if (originValue.isJsonArray() && extValue.isJsonArray()) {
                    // Case both JsonArray:
                    switch (arrayStrategy) {
                        case CONCAT: {
                            JsonArray originArray = resultObject.get(extKey).getAsJsonArray();
                            JsonArray extArray = extValue.getAsJsonArray();
                            for (int i = 0; i < extArray.size(); i++) {
                                originArray.add(extArray.get(i));
                            }
                            break;
                        }
                        case PREFER_ORIGIN:
                            break;
                        case PREFER_UPDATE:
                            resultObject.add(extKey, extValue);
                            break;
                    }
                } else if (originValue.isJsonPrimitive() && extValue.isJsonPrimitive()) {
                    // Case both Primitive:
                    switch (primitiveStrategy) {
                        case PREFER_ORIGIN:
                            break;
                        case PREFER_UPDATE:
                            resultObject.add(extKey, extValue);
                            break;
                    }
                } else {
                    // Case type mismatch
                    switch (typeMismatchedStrategy) {
                        case PREFER_ORIGIN:
                            break;
                        case PREFER_UPDATE:
                            resultObject.add(extKey, extValue);
                            break;
                    }
                }
            }

        }
        return resultObject;
    }
}
