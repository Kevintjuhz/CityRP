package nl.kqcreations.cityrp.api.banking;

import java.util.UUID;

/**
 * Represents an object which is responsible for issuing {@link CreditCard}s.
 */
public interface ICreditService {

    /**
     * Get the existing or create a new credit card.
     *
     * @param name  The name of the card.
     * @param owner The UUID of the owner.
     * @return Returns the existing or newly created credit card.
     */
    CreditCard getOrCreateCardFor(String name, UUID owner);

}
