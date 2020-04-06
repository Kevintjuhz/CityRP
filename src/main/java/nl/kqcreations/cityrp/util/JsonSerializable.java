package nl.kqcreations.cityrp.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public interface JsonSerializable {

    Gson gson = new Gson();;

    String toJson();
}
