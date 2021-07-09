package budget;

import java.io.IOException;
import java.util.*;

public class UserInterface {
    private Scanner scanner;
    private BudgetManager budgetManager;
    private DataSaverLoader dataSaverLoader;


    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
        this.budgetManager = new BudgetManager();
        this.dataSaverLoader = new DataSaverLoader();
    }

    public void start() {
        label:
        while (true) {
            printMenu();
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    addIncome();
                    break;
                case "2":
                    addPurchase();
                    break;
                case "3":
                    printPurchased();
                    break;
                case "4":
                    System.out.println();
                    printBalance();
                    break;
                case "5":
                    saveToFile();
                    break;
                case "6":
                    loadFromFile();
                    break;
                case "7":
                    analyze();
                    break;
                case "0":
                    System.out.println("\nBye!");
                    break label;
                default:
                    System.out.println("\nWrong input\n");
            }
        }
    }

    private void analyze() {
        label:
        while (true) {
            printAnalyzeMenu();
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    printPurchasedList(budgetManager.getPurchasedProductsSortedByPrice(), null);
                    break;
                case "2":
                    printProductsSortedByType();
                    break;
                case "3":
                    printCertainType();
                    break;
                case "4":
                    break label;
            }
        }
    }

    private void printCertainType() {
        printAllProductTypes();
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        ProductType productType = ProductType.values()[index];
        List<Product> products = budgetManager.getPurchasedProducts(productType);
        products.sort(Comparator.comparing(Product::getPrice).reversed());
        printPurchasedList(products, productType);
    }

    private void printAnalyzeMenu() {
        System.out.println("\nHow do you want to sort?\n" +
                "1) Sort all purchases\n" +
                "2) Sort by type\n" +
                "3) Sort certain type\n" +
                "4) Back");
    }

    private void printPurchased() {
        while (true) {
            printAllProductTypes();
            System.out.println("5) All\n6) Back");
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index == 5) {
                break;
            }
            if (index >= 0 && index < 4) {
                ProductType productType = ProductType.values()[index];
                List<Product> products = budgetManager.getPurchasedProducts(productType);
                printPurchasedList(products, productType);
            } else if (index == 4) {
                List<Product> products = budgetManager.getPurchasedProducts();
                printPurchasedList(products, null);
            } else {
                System.out.println("\nWrong input");
            }
        }
    }

    private void printProductsSortedByType() {
        System.out.println("\nTypes:");
        Map<Double, String> map = new HashMap<>();
        for (ProductType productType : ProductType.values()) {
            List<Product> list = budgetManager.getPurchasedProducts(productType);
            map.put(list.stream().mapToDouble(Product::getPrice).sum() , productType.getName());
        }
        List<Double> sums = new ArrayList<>(map.keySet());
        sums.sort(Collections.reverseOrder());
        for(double sum : sums){
            System.out.printf(map.get(sum) + " - $%.2f\n" , sum);
        }

        printTotalSum(budgetManager.getPurchasedProducts());
    }

    private void printPurchasedList(List<Product> products, ProductType productType) {
        System.out.println("\n" + (productType == null ? "All:" : productType.getName() + ":"));
        if (!products.isEmpty()) {
            products.forEach(System.out::println);
        } else {
            System.out.println("The purchase list is empty");
        }
        printTotalSum(products);
    }

    private void printTotalSum(List<Product> products) {
        System.out.printf("Total sum: $%.2f\n", products.stream().mapToDouble(Product::getPrice).sum());
    }

    private void printBalance() {
        System.out.printf("Balance: $%.2f\n", budgetManager.getBalance());
    }

    private void addPurchase() {
        while (true) {
            printAllProductTypes();
            System.out.println("5) Back");
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index == 4) {
                break;
            }
            if (index >= 0 && index < 4) {
                System.out.println("\nEnter purchase name:");
                String name = scanner.nextLine();
                System.out.println("Enter its price:");
                double price = Double.parseDouble(scanner.nextLine());
                budgetManager.addBoughtProduct(new Product(name, price, ProductType.values()[index]));
                System.out.println("Purchase was added!");
            } else {
                System.out.println("\nWrong input");
            }
        }
    }


    private void printAllProductTypes() {
        System.out.println("\nChoose the type of purchase");
        ProductType[] productTypeList = ProductType.values();
        for (int i = 0; i < productTypeList.length; i++) {
            System.out.println((i + 1) + ") " + productTypeList[i].getName());
        }
    }

    private void addIncome() {
        System.out.println("\nEnter income:");
        budgetManager.addIncome(Double.parseDouble(scanner.nextLine()));
        System.out.println("Income was added!");
    }

    private void printMenu() {
        System.out.println("\nChoose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit");
    }

    public void saveToFile() {
        try {
            dataSaverLoader.SaveToFile(budgetManager);
            System.out.println("\nPurchases were saved!");
        } catch (Exception e) {

            System.out.println("\nCan't access file");
        }

    }

    public void loadFromFile() {
        try {
            budgetManager = dataSaverLoader.loadFromFile();
            System.out.println("\nPurchases were loaded!");
        } catch (IOException e) {
            System.out.println("\nFile not found");
            budgetManager = new BudgetManager();
        }
    }
}
