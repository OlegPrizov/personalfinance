import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// класс кошелька
public class Wallet implements Serializable {
    private double balance;
    private List<Transaction> transactions = new ArrayList<>();
    private Map<String, Category> categories = new HashMap<>();

    // добавление статьи доходов
    public void addIncome(double amount, String category) {
        balance += amount;
        transactions.add(new Transaction(amount, category, true));
        Category cat = categories.computeIfAbsent(category, k -> new Category(category, 0));
        cat.addIncome(amount);
    }

    // добавление статьи расходов
    public void addExpense(double amount, String category) {
        balance -= amount;
        transactions.add(new Transaction(amount, category, false));
        Category cat = categories.computeIfAbsent(category, k -> new Category(category, 0));
        cat.addSpent(amount);
        if (cat.isOverBudget()) {
            System.out.println("Предупреждение: превышен бюджет по категории " + category);
        }
    }

    // установить бюджет в определенной категории
    public void setCategoryBudget(String category, double budget) {
        Category cat = categories.computeIfAbsent(category, k -> new Category(category, budget));
        cat.setBudget(budget);
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Map<String, Category> getCategories() {
        return categories;
    }

    // печать отчета в терминал
    public void printReport() {
        double totalIncome = transactions.stream().filter(Transaction::isIncome).mapToDouble(Transaction::getAmount).sum();
        double totalExpenses = transactions.stream().filter(t -> !t.isIncome()).mapToDouble(Transaction::getAmount).sum();

        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Доходы по категориям:");
        categories.forEach((name, cat) -> {
            if (cat.getIncome() > 0) { // Выводим только ненулевые категории дохода
                System.out.println(name + ": " + cat.getIncome());
            }
        });

        System.out.println("Общие расходы: " + totalExpenses);
        System.out.println("Бюджет по категориям:");
        categories.forEach((name, cat) -> {
            double remainingBudget = cat.getRemainingBudget();
            if (cat.getBudget() > 0 || remainingBudget != cat.getBudget()) { // Выводим только значимые категории бюджета
                System.out.println(name + ": " + cat.getBudget() + ", Оставшийся бюджет: " + remainingBudget);
            }
        });
    }
}
