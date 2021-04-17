package program.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

public interface JSONSerialize {

    String toJson() throws JsonProcessingException, JSONException;
}
