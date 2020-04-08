package nl.kqcreations.cityrp.api.banking;

import nl.kqcreations.cityrp.util.JsonSerializable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultATM implements ATM, JsonSerializable {

    private final Location location;
    private transient Map<UUID, Menu> menuMap = new HashMap<>();

    DefaultATM(Location location) {
        this.location = location;
    }

    DefaultATM(String json) {
        Map<String, Object> rawSerial = JsonSerializable.gson.fromJson(json, JsonSerializable.BukkitSerialMapType);
        this.location = Location.deserialize(rawSerial);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void accessAs(Player player) {
        menuMap.computeIfAbsent(player.getUniqueId(), (key) -> {
            ATMMenu menu = new ATMMenu();
            menu.displayTo(player);
            return menu;
        });
    }

    @Override
    public String toJson() {
        return JsonSerializable.gson.toJson(location.serialize(), JsonSerializable.BukkitSerialMapType);
    }

    private static final class ATMMenu extends Menu {


    }
}
