package budget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetManager {
    private List<Product> purchasedProducts;
    private double balance;


    public BudgetManager() {
        this.purchasedProducts = new ArrayList<>();
        this.balance = 0;
    }

    public List<Product> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(List<Product> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Product> getPurchasedProductsByType(ProductType productType) {
        if(productType == null){
            return purchasedProducts;
        }
        return purchasedProducts.stream()
                .filter(product -> product.getProductType().equals(productType))
                .collect(Collectors.toList());
    }

    public void addBoughtProduct(Product product){
        balance -= product.getPrice();
        this.purchasedProducts.add(product);
    }


    public double getTotal(){
        double sum = purchasedProducts.stream().mapToDouble(Product::getPrice).sum();

        return new BigDecimal(sum).setScale(2 , RoundingMode.HALF_UP).doubleValue();
    }

    public void addIncome(double amount){
        this.balance += amount;
    }

    public List<Product> getPurchasedProductsSortedByPrice() {
        List<Product> list = new ArrayList<>(purchasedProducts);
        list.sort(Comparator.comparing(Product::getPrice).reversed());
        return list;
    }
}
