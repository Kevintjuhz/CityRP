package nl.kqcreations.cityrp.api.banking;

/**
 * Represents a currency object.
 */
public interface Currency {

    /**
     * @return Returns the identifier of this currency such as "$"
     */
    String getIdentifier();

    /**
     * @return Returns the singular name of this currency.
     */
    String getName();

    /**
     * @return Returns the plural name of this currency.
     */
    String getPluralName();

}
