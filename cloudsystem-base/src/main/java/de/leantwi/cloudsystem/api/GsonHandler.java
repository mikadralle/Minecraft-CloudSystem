package de.leantwi.cloudsystem.api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GsonHandler {

    private static final GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(CloudPlayerAPI.class, new PacketSerializer<CloudPlayerAPI>());


    public static Gson getGson() {
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();

    }


    static class PacketSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

        private static final String TYPE = "type";

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String className = jsonObject.get(TYPE).getAsString();
            try {
                return context.deserialize(json, Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            JsonElement jsonElement = context.serialize(src, src.getClass());
            jsonElement.getAsJsonObject().addProperty(TYPE, src.getClass().getCanonicalName());
            return jsonElement;
        }
    }


}
