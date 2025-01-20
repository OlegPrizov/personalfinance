import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

// класс для работы с транзакциями
public class Transaction implements Serializable {
    private final double amount;
    private final String category;
    private final boolean isIncome;
    private final Date date;

    public Transaction(double amount, String category, boolean isIncome) {
        this.amount = amount;
        this.category = category;
        this.isIncome = isIncome;
        this.date = new Date();
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    @Override
    public String toString() { // переделываю вывод
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String type = isIncome ? "Доход" : "Расход";
        return String.format("%s: %.2f, Категория: %s, Дата: %s", type, amount, category, dateFormat.format(date));
    }
}
