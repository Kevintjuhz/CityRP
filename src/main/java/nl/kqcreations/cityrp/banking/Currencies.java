package nl.kqcreations.cityrp.banking;

import lombok.AccessLevel;
import lombok.Getter;
import nl.kqcreations.cityrp.api.banking.Currency;

/**
 * Enumerated representation of all hard-coded currencies.
 */
public enum Currencies implements Currency {

    /**
     * Represents a the central "universal" currency used to integrate wih vault,
     * and to pack-against when trading with other currencies.
     */
    CENTRAL("$", "Central Dollar", "Central Dollars");


    @Getter(AccessLevel.PUBLIC)
    private final String identifier, name, pluralName;

    Currencies(String identifier, String name, String pluralName) {
        this.identifier = identifier;
        this.name = name;
        this.pluralName = pluralName;
    }

}
