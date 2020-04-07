package nl.kqcreations.cityrp.command.city;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class CityCommandGroup extends SimpleCommandGroup {

    @Override
    protected void registerSubcommands() {
        registerSubcommand(new AddCityCommand(this));
    }
}
