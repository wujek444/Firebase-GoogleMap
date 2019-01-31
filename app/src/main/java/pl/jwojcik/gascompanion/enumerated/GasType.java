package pl.jwojcik.gascompanion.enumerated;

public enum GasType {

    PB95("pb95"),
    PB98("pb98"),
    ON("on"),
    LPG("lpg")
    ;
    private String name;

    GasType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
