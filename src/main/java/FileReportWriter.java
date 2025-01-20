import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileReportWriter {
    public static void writeReportToFile(String filename, Wallet wallet) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Баланс: " + wallet.getBalance() + "\n");
            writer.write("Транзакции:\n");
            for (Transaction t : wallet.getTransactions()) {
                writer.write(t.toString() + "\n");
            }
            writer.write("Категории:\n");
            for (Category c : wallet.getCategories().values()) {
                writer.write(c.toString() + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ошибка при записи файла: " + e.getMessage());
        }
    }
}