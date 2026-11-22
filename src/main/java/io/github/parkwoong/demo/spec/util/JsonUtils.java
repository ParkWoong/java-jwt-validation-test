package io.github.parkwoong.demo.spec.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;

import java.util.Map;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJsonString(Object object, boolean isPretty) {
        String jsonString = null;

        try {
            if (isPretty) {
                jsonString = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                jsonString = mapper.writeValueAsString(object);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return jsonString;
    }

    public static String toPrettyJsonString(Object object) {
        return toJsonString(object, true);
    }

    public static JSONObject getJson(String body) {

        JSONObject json = null;
        try {

            json = new JSONObject(body);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
        }
        return json;
    }

    public static String getValueFromJsonString(String body, String key) {

        String value = null;
        try {

            value = (String) new JSONObject(body).get(key);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
        }
        return value;
    }

    public static Map<String, Object> getDataMapFromJsonString(String body) {

        Map<String, Object> map = null;
        try {

            map = new ObjectMapper().readValue(body, Map.class);

        } catch (Exception e) {

            log.error(e.getMessage(), e);
        }
        return map;
    }
}
