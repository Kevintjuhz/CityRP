package nl.kqcreations.cityrp.api.banking;

import java.util.UUID;

public interface ICreditService {

    CreditCard getOrCreateCardFor(String name, UUID owner);

}
