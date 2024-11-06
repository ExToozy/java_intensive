package org.example.infrastructure.util;

import org.springframework.stereotype.Component;

@Component
public class MappingUtils {

    public String mapToString(Object value) {
        return value != null ? value.toString() : null;
    }

    public int mapToInt(Object value) {
        return (Integer) value;
    }

}
