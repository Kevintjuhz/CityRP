package nl.kqcreations.cityrp.util;

import com.google.gson.Gson;

public interface JsonSerializable {

    Gson gson = new Gson();
    ;

    String toJson();
}
