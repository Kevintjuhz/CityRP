package nl.kqcreations.cityrp.event;

import nl.kqcreations.cityrp.city.CityCache;
import nl.kqcreations.cityrp.city.PlayerCity;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final World world = player.getWorld();
        final String worldName = world.getName();

        /*
         *  Checks if the player is op and if there are any cities registered
         *  if not it will send a message to the op and ask if he want to register a new city
         * */
        if (player.isOp()) {
            Common.logFramed("Player is op");
            CityCache.getCityCache(worldName);
            if (CityCache.getCityCache(worldName).getCities().size() == 0) {
                Common.tell(player,
                        "&3" + Common.chatLineSmooth(),
                        "   ",
                        "&3You have not yet setup any world to be an cityrp world! do:",
                        "&b/city add {name} [wg_region] &3- To setup a city",
                        "   ",
                        "&3" + Common.chatLineSmooth()
                );
            }
        }

        /*
         *  This checks if the player is currently in a city on join
         *  and if he is it will send him a welcome message from that city
         * */
        CityCache cache = CityCache.getCityCache(worldName);

        for (CityCache.City city : cache.getCities()) {
            boolean inRegion = false;
            for (String region : HookManager.getRegions(player.getLocation())) {
                if (city.getWgRegion().equals(region)) {
                    inRegion = true;
                    break;
                }
            }
            if (CityCache.getDefaultCity(worldName) != null)
                inRegion = true;

            if (inRegion) {
                PlayerCity playerCity = PlayerCity.getInstance();
                playerCity.sendPlayerCityTitle(player, city);
                playerCity.setPlayerCity(player.getUniqueId(), city);
            }
        }

    }

}
