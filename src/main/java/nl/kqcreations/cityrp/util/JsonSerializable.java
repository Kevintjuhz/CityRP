package nl.kqcreations.cityrp.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

public interface JsonSerializable {

	final Type BukkitSerialMapType = new TypeToken<Map<String, Object>>() {
	}.getType();


	Gson gson = new Gson();

	String toJson();
}
