package program.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class StringToMap {

    public Map<String, String> createMap(String response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map
                = objectMapper.readValue(response, new TypeReference<Map<String, String>>(){});
        return map;
    }
}
