package nl.kqcreations.cityrp.banking;

import nl.kqcreations.cityrp.api.banking.Currency;

public enum Currencies implements Currency {

    CENTRAL("$", "Central Dollar", "Central Dollars");


    private final String identifier, name, pluralName;

    Currencies(String identifier, String name, String pluralName) {
        this.identifier = identifier;
        this.name = name;
        this.pluralName = pluralName;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPluralName() {
        return pluralName;
    }
}
