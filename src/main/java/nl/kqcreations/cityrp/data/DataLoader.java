package nl.kqcreations.cityrp.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.kqcreations.cityrp.data.mongo_data.bank.BankAccountData;
import nl.kqcreations.cityrp.data.yml_data.CityData;
import nl.kqcreations.cityrp.data.yml_data.WorldData;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataLoader extends YamlConfig {

	@Getter
	private static final DataLoader instance = new DataLoader();

	public void load() {
		loadWorlds();
		loadCities();
		loadBankAccounts();
	}

	private void loadBankAccounts() {
		Common.runLaterAsync(20, BankAccountData::LoadBankAccounts);
	}

	private void loadWorlds() {
		loadConfiguration(NO_DEFAULT, "data" + File.separator + "WorldData.yml");

		Common.log("Started to load worlds");

		for (final String worldName : getMap("").keySet()) {
			Common.log("World: " + worldName);
			WorldData cache = WorldData.getOrCreateWorldCache(worldName);
			Common.log(cache.getTitle(), cache.getColor());

		}

		Common.log("Worlds are loaded here is the data");
	}

	private void loadCities() {
		loadConfiguration(NO_DEFAULT, "data" + File.separator + "cities.yml");

		Common.log("Started to load cities");

		for (final String region : getMap("").keySet()) {
			Common.log("CityRegion: " + region);
			CityData cache = CityData.getOrCreateCityCache(region);
			Common.log(cache.getTitle(), cache.getColor());
		}

		Common.log("Cities are loaded here is the data");

	}

}
