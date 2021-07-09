package budget;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
    private String name;
    private double price;
    private ProductType productType;

    public Product() {
    }

    public Product(String name, double price, ProductType productType) {
        this.name = name;
        this.price = price;
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return name + " $" + new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
    }
}
