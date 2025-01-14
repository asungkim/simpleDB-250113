package com.ll.simpleDb;

import java.lang.reflect.Field;
import java.util.Map;

public class Util {
    public static <T> T mapToObj(Map<String, Object> map, Class<T> cls) {
        try {
            T instance=cls.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String filedName = entry.getKey();
                Object value = entry.getValue();

                Field field = cls.getDeclaredField(filedName);
                field.setAccessible(true);

                field.set(instance,value);
            }

            return instance;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}


