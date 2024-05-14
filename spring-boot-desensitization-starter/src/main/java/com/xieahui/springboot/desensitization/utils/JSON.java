package com.xieahui.springboot.desensitization.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @description: JSON工具类
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 10:26
 */
public class JSON {

    private static final JSON INSTANCE = new JSON();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JSON() {
        // 配置ObjectMapper
        // 忽略未知属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 忽略空Bean转json错误
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // NULL不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static JSON getInstance() {
        return INSTANCE;
    }

    /**
     * 对象转JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJSONString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON string", e);
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T parseToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON string to object", e);
        }
    }

    /**
     * JSON字符串转泛型对象
     *
     * @param json          JSON字符串
     * @param typeReference 泛型类型
     * @param <T>           泛型类型
     * @return 泛型对象
     */
    public static <T> T parseToObject(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON string to generic type object", e);
        }
    }

}
