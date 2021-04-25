package program.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

/**
 * Интерфейс для конвертации в Json
 */
public interface JSONSerialize {

    /**
     * Метод для конвертации в Json-строку
     * @return
     * @throws JsonProcessingException
     * @throws JSONException
     */
    String toJson() throws JsonProcessingException, JSONException;
}
