import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private double budget;
    private double spent;
    private double income;  // доходы

    public Category(String name, double budget) {
        this.name = name;
        this.budget = budget;
        this.spent = 0;
        this.income = 0;
    }

    public String getName() {
        return name;
    }

    public void addIncome(double amount) {
        this.income += amount;
    }

    public void addSpent(double amount) {
        this.spent += amount;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getSpent() {
        return spent;
    }

    public boolean isOverBudget() {
        return spent > budget;
    }

    public double getRemainingBudget() {
        return budget - spent;
    }

    public double getIncome() {
        return income;
    }

    @Override
    public String toString() {
        return String.format("%s: бюджет %.2f, израсходовано %.2f, доход %.2f", name, budget, spent, income);
    }
}