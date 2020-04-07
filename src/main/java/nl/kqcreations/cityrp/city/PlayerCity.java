package nl.kqcreations.cityrp.city;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.Remain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerCity {

    @Getter
    private final static PlayerCity instance = new PlayerCity();

    private Map<UUID, CityCache.City> playersCurrentCity = new HashMap<>();

    public CityCache.City getPlayerCity(UUID uuid) {
        return playersCurrentCity.get(uuid);
    }

    public void setPlayerCity(UUID uuid, CityCache.City city) {
        Valid.checkNotNull(uuid, "The uuid cannot be null");
        Valid.checkNotNull(city, "The city cannot be null");

        if (playersCurrentCity.get(uuid) == null)
            playersCurrentCity.put(uuid, city);
        else
            playersCurrentCity.replace(uuid, city);
    }

    public void sendPlayerCityTitle(Player player, CityCache.City city) {
        ChatColor color = city.getColor() != null ? city.getColor() : ChatColor.WHITE;
        Remain.sendTitle(player, Common.colorize("&7Welcome in"), Common.colorize(color + city.getName()).toUpperCase());
    }
}
