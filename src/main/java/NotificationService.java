// класс для работы с уведомлениями
public class NotificationService {

    public void notify(String message) {
        System.out.println("Уведомление: " + message);
    }

    public void notifyBudgetExceed(String category) {
        System.out.println("Предупреждение: бюджет по категории '" + category + "' превышен!");
    }
}
