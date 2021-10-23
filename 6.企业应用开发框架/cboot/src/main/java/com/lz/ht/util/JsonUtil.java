package com.lz.ht.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class JsonUtil {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        mapper.setDateFormat(new SimpleDateFormat("yyyyMMddHHmmss"));
    }

    public static <T> T json2ObjectByTr(String str, TypeReference<T> tr)
            throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(str, tr);
    }

    public static String Object2Json(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static Map toMap(String jsonString) {
        HashMap hashMap = null;
        try {
            hashMap = mapper.readValue(jsonString, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static Gson getGson(){
        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson;
    }

    public static Map fromJson(String json){
        return getGson().fromJson(json,new TypeToken<HashMap<String, Object>>(){}.getType());

    }

    public static String toJson(Object object){
        return getGson().toJson(object);
    }

}