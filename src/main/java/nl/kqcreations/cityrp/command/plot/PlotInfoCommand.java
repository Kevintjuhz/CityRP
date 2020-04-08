package nl.kqcreations.cityrp.command.plot;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import nl.kqcreations.cityrp.cache.CityCache;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class PlotInfoCommand extends SimpleSubCommand {
	protected PlotInfoCommand(SimpleCommandGroup parent) {
		super(parent, "info|i");
		setMinArguments(0);
		setDescription("Returns plot info on the plot the player is standing on");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = getPlayer();
		Location location = player.getLocation();
		CityCache cityCache = CityCache.getCityCache(player.getWorld().getName());

		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet regionSet = query.getApplicableRegions(BukkitAdapter.adapt(location));

		if (regionSet == null)
			return;

//		CityCache.City city = cityCach;
//
//		for (ProtectedRegion region : regionSet) {
//			String regionName = region.getId();
//			PlotCache.Plot plot = city.getPlot(regionName);
//
//			if (plot != null) {
//				String[] message = new String[]{
//						"&3============= &b" + cityName + " " + regionName + " &3============="
//				};
//				String[] secondMessage;
//
//				if (region.getOwners() == null) {
//					secondMessage = new String[]{
//							"&3Owner: &b" + region.getOwners(),
//							"&3Members: &b" + region.getMembers(),
//					};
//
//				} else {
//					secondMessage = new String[]{
//							"&3Price: &b$" + plot.GetPlotPrice(),
//							"&3Type &b/plot buy &3to buy this plot!"
//					};
//				}
//
//				message = (String[]) ArrayUtils.addAll(message, secondMessage);
//				return message;
//			}
//		}

	}
}
