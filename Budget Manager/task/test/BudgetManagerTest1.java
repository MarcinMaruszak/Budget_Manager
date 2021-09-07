import budget.BudgetManager;
import budget.DataSaverLoader;
import budget.Product;
import budget.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BudgetManagerTest1 {

    BudgetManager budgetManager;
    static List<Product> products;

    @BeforeAll
    static void listInit() {
        products = List.of(new Product("juice", 2.2d, ProductType.FOOD),
                new Product("bread", 1d, ProductType.FOOD),
                new Product("t-shirt", 20d, ProductType.CLOTHES),
                new Product("game", 5.5d, ProductType.ENTERTAINMENT),
                new Product("fish", 2d, ProductType.OTHER));
    }

    @BeforeEach
    void beforeEach() {
        budgetManager = new BudgetManager();
    }

    @Test
    void testTotalAfterCreation() {
        assertEquals(0, budgetManager.getBalance());
        assertEquals(0, budgetManager.getTotal());
    }

    @Test
    void testAddIncome() {
        budgetManager.addIncome(400);
        assertEquals(400, budgetManager.getBalance());
    }

    @Test
    void testProductPurchase() {
        Product product = new Product("juice", 10d, ProductType.FOOD);
        budgetManager.addBoughtProduct(product);
        assertEquals(1, budgetManager.getPurchasedProductsByType(ProductType.FOOD).size());
        assertEquals(10, budgetManager.getTotal());
    }

    @ParameterizedTest
    @MethodSource
    void testVariousProductsPurchasesTotals(List<Product> products, double expected) {
        budgetManager.addIncome(500);
        for (Product product : products) {
            budgetManager.addBoughtProduct(product);
        }
        assertEquals(expected, budgetManager.getTotal());
        assertEquals(500 - expected, budgetManager.getBalance());
    }

    static Stream<Arguments> testVariousProductsPurchasesTotals() {
        return Stream.of(
                Arguments.of(List.of(new Product("juice", 2.2d, ProductType.FOOD),
                                new Product("fan", 1d, ProductType.OTHER))
                        , 3.2d),

                Arguments.of(List.of(new Product("juice", 2.2d, ProductType.FOOD),
                                new Product("t-shirt", 20d, ProductType.CLOTHES))
                        , 22.2d),
                Arguments.of(products, 30.7d));
    }

    @ParameterizedTest
    @MethodSource
    void testProductsByType(List<Product> products, ProductType type, int expected) {
        for (Product product : products) {
            budgetManager.addBoughtProduct(product);
        }
        assertEquals(expected, budgetManager.getPurchasedProductsByType(type).size());
    }

    static Stream<Arguments> testProductsByType() {
        return Stream.of(
                Arguments.of(
                        products, ProductType.FOOD, 2),
                Arguments.of(List.of(
                                new Product("juice", 2.2d, ProductType.FOOD),
                                new Product("bread", 1d, ProductType.FOOD),
                                new Product("book", 20d, ProductType.OTHER),
                                new Product("door", 50.5d, ProductType.OTHER),
                                new Product("fish", 2d, ProductType.OTHER))
                        , ProductType.OTHER, 3),
                Arguments.of(products,ProductType.CLOTHES, 1),
                Arguments.of(products, ProductType.ENTERTAINMENT, 1)
        );
    }

    @Nested
    class FilesTest {
        DataSaverLoader dataSaverLoader;

        @BeforeEach
        void init() {
            dataSaverLoader = new DataSaverLoader();
        }

        @AfterEach
        void deleteFile() {
            File file = new File("purchases.txt");
            file.delete();
        }

        @Test
        void testFileSaving() throws IOException {
            for (Product product : products){
                budgetManager.addBoughtProduct(product);
            }
            budgetManager.setBalance(134.65);
            dataSaverLoader.SaveToFile(budgetManager);

            File file = new File("purchases.txt");
            assertTrue(file.exists());

            ObjectMapper objectMapper = new JsonMapper();
            budgetManager = objectMapper.readValue(new FileInputStream("purchases.txt"), BudgetManager.class);

            assertEquals(134.65, budgetManager.getBalance());
            assertEquals(5, budgetManager.getPurchasedProducts().size());
            assertEquals(30.7d, budgetManager.getTotal());
        }


        @Test
        void testFileLoading() throws IOException {
            for (Product product : products){
                budgetManager.addBoughtProduct(product);
            }
            budgetManager.setBalance(785.64);
            dataSaverLoader.SaveToFile(budgetManager);

            budgetManager = dataSaverLoader.loadFromFile();
            assertEquals(785.64, budgetManager.getBalance());
            assertEquals(5, budgetManager.getPurchasedProducts().size());
            assertEquals(30.7d, budgetManager.getTotal());
        }
    }
}
