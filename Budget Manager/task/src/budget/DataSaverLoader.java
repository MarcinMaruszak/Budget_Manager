package budget;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class DataSaverLoader {

    private ObjectMapper objectMapper;

    public DataSaverLoader() {
        objectMapper = new ObjectMapper();
    }

    public void SaveToFile(BudgetManager budgetManager) throws IOException {
        OutputStream outputStream = new FileOutputStream("purchases.txt");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, budgetManager);
    }

    public BudgetManager loadFromFile() throws IOException {
        InputStream inputStream = new FileInputStream("purchases.txt");
        return objectMapper.readValue(inputStream, BudgetManager.class);
    }
}
