package budget;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.*;

public class DataSaverLoader {

    private ObjectMapper objectMapper;

    public DataSaverLoader() {
        objectMapper = JsonMapper.builder()
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .visibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
                .visibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.NONE)
                .build();
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
