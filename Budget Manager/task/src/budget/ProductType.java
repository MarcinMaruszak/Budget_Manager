package budget;

public enum ProductType {
    FOOD("Food"),
    CLOTHES("Clothes"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
