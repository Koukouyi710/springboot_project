package com.neuedu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * 通用的Json与Java对象互相转换通用类
 */

public class JsonUtils {
   /* private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        //对象中所有的字段序列化
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认timestamp格式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS,false);

    }*/
}
