package com.cloud.shopping.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON Utility Class
 * Provides methods for JSON serialization and deserialization using Jackson
 * Includes error handling and common conversion patterns
 */
@Slf4j
public class JsonUtils {

    /**
     * Thread-safe ObjectMapper instance for JSON operations
     * Jackson's ObjectMapper is thread-safe after configuration
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convert object to JSON string
     *
     * @param obj Object to serialize
     * @return JSON string representation, or null if serialization fails
     */
    @Nullable
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON serialization failed: ", e);
            return null;
        }
    }

    /**
     * Convert JSON string to specified object type
     *
     * @param json JSON string to deserialize
     * @param tClass Target class type
     * @param <T> Generic type parameter
     * @return Instance of specified type, or null if deserialization fails
     */
    @Nullable
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            log.error("JSON deserialization failed: ", e);
            return null;
        }
    }

    /**
     * Convert JSON string to List of specified type
     *
     * @param json JSON string to deserialize
     * @param eClass Element class type
     * @param <E> Generic type parameter for list elements
     * @return List of specified type, or null if deserialization fails
     */
    @Nullable
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            log.error("JSON to List conversion failed: ", e);
            return null;
        }
    }

    /**
     * Convert JSON string to Map with specified key and value types
     *
     * @param json JSON string to deserialize
     * @param kClass Key class type
     * @param vClass Value class type
     * @param <K> Generic type parameter for map keys
     * @param <V> Generic type parameter for map values
     * @return Map of specified types, or null if deserialization fails
     */
    @Nullable
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            log.error("JSON to Map conversion failed: ", e);
            return null;
        }
    }

    /**
     * Convert JSON string to object using TypeReference
     * Useful for complex generic types
     *
     * @param json JSON string to deserialize
     * @param type TypeReference describing the target type
     * @param <T> Generic type parameter
     * @return Object of specified type, or null if deserialization fails
     */
    @Nullable
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log.error("JSON native read failed: ", e);
            return null;
        }
    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    static class User{
//        String name;
//        Integer age;
//    }
//
//    public static void main(String[] args) {
//       User user=new User("Jack",21);
//        //toString
//        String json=toString(user);
//        System.out.println("json:"+json);
//    }
}
