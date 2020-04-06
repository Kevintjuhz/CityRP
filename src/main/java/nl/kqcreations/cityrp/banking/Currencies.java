package nl.kqcreations.cityrp.banking;

public enum Currencies implements Currency {

    CENTRAL("$");


    private final String identifier;

    Currencies(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }
}
