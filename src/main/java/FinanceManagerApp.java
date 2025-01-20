import java.util.Scanner;

public class FinanceManagerApp {
    private static final UserManager userManager = new UserManager();
    private static final Scanner scanner = new Scanner(System.in);
    private static final NotificationService notificationService = new NotificationService();
    private static final String DATA_FILE = "users.dat";

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        loadData();

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Регистрация");
            System.out.println("2. Вход");
            System.out.println("3. Выход");

            int command = getCommand();

            try {
                switch (command) {
                    case 1:
                        handleRegistration();
                        break;
                    case 2:
                        handleLogin();
                        break;
                    case 3:
                        saveData();
                        System.out.println("Завершение программы.");
                        return;
                    default:
                        System.out.println("Неверная команда. Попробуйте еще раз.");
                }
            } catch (Exception e) {
                notificationService.notify("Ошибка: " + e.getMessage());
            }
        }
    }

    private static int getCommand() {
        while (true) {
            try {
                System.out.print("Введите номер команды: ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                notificationService.notify("Ошибка ввода. Введите число.");
            }
        }
    }

    private static void handleRegistration() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine().trim();

        if (userManager.registerUser(username, password)) {
            notificationService.notify("Пользователь успешно зарегистрирован.");
        }
    }

    private static void handleLogin() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine().trim();

        User user = userManager.authenticate(username, password);
        if (user != null) {
            notificationService.notify("Добро пожаловать, " + user.getUsername());
            handleUserSession(user);
        }
    }

    private static void handleUserSession(User user) {
        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Установить бюджет по категории");
            System.out.println("4. Просмотр отчёта");
            System.out.println("5. Перевод средств");
            System.out.println("6. Сохранение отчёта в файл");
            System.out.println("7. Выход");

            int command = getCommand();

            try {
                switch (command) {
                    case 1:
                        handleIncome(user);
                        break;
                    case 2:
                        handleExpense(user);
                        break;
                    case 3:
                        handleBudget(user);
                        break;
                    case 4:
                        printReport(user);
                        break;
                    case 5:
                        handleTransfer(user);
                        break;
                    case 6:
                        saveReportToFile(user);
                        break;
                    case 7:
                        System.out.println("Выход из аккаунта.");
                        return;
                    default:
                        notificationService.notify("Неверная команда. Попробуйте еще раз.");
                }
            } catch (Exception e) {
                notificationService.notify("Ошибка: " + e.getMessage());
            }
        }
    }

    private static void handleIncome(User user) {
        try {
            System.out.print("Введите сумму дохода: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Введите категорию: ");
            String category = scanner.nextLine().trim();
            user.getWallet().addIncome(amount, category);
            notificationService.notify("Доход добавлен.");
        } catch (NumberFormatException e) {
            notificationService.notify("Некорректный ввод суммы.");
        }
    }

    private static void handleExpense(User user) {
        try {
            System.out.print("Введите сумму расхода: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Введите категорию: ");
            String category = scanner.nextLine().trim();
            user.getWallet().addExpense(amount, category);

            if (user.getWallet().getCategories().get(category).isOverBudget()) {
                notificationService.notifyBudgetExceed(category);
            } else {
                notificationService.notify("Расход добавлен.");
            }
        } catch (NumberFormatException e) {
            notificationService.notify("Некорректный ввод суммы.");
        }
    }

    private static void handleBudget(User user) {
        try {
            System.out.print("Введите категорию: ");
            String category = scanner.nextLine().trim();
            System.out.print("Введите бюджет: ");
            double budget = Double.parseDouble(scanner.nextLine().trim());
            user.getWallet().setCategoryBudget(category, budget);
            notificationService.notify("Бюджет установлен.");
        } catch (NumberFormatException e) {
            notificationService.notify("Некорректный ввод бюджета.");
        }
    }

    private static void printReport(User user) {
        user.getWallet().printReport();
    }

    private static void handleTransfer(User user) {
        try {
            System.out.print("Введите имя пользователя получателя: ");
            String recipientUsername = scanner.nextLine().trim();
            User recipient = userManager.getUser(recipientUsername);

            if (recipient == null) {
                notificationService.notify("Пользователь не найден.");
                return;
            }

            System.out.print("Введите сумму перевода: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            if (amount <= 0 || amount > user.getWallet().getBalance()) {
                notificationService.notify("Некорректная сумма перевода.");
                return;
            }

            user.getWallet().addExpense(amount, "Перевод: " + recipientUsername);
            recipient.getWallet().addIncome(amount, "Перевод от: " + user.getUsername());

            notificationService.notify("Перевод выполнен.");
        } catch (NumberFormatException e) {
            notificationService.notify("Некорректный ввод суммы.");
        }
    }

    private static void saveReportToFile(User user) {
        System.out.print("Введите имя файла для сохранения отчета: ");
        String filename = scanner.nextLine().trim();
        FileReportWriter.writeReportToFile(filename, user.getWallet());
        notificationService.notify("Отчет сохранен в файл: " + filename);
    }

    private static void loadData() {
        try {
            userManager.loadData(DATA_FILE);
            notificationService.notify("Данные загружены.");
        } catch (Exception e) {
            notificationService.notify("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private static void saveData() {
        try {
            userManager.saveData(DATA_FILE);
            notificationService.notify("Данные сохранены.");
        } catch (Exception e) {
            notificationService.notify("Ошибка при сохранении данных: " + e.getMessage());
        }
    }
}
