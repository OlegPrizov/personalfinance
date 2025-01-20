import java.util.Scanner;

public class FinanceManagerApp {
    private static final UserManager userManager = new UserManager();
    private static final Scanner scanner = new Scanner(System.in);
    private static final NotificationService notificationService = new NotificationService();
    private static final String DATA_FILE = "users.dat";

    // команда main запускает команду run, в которой уже описана вся основная логика программы
    public static void main(String[] args) {
        run();
    }

    // основная команда
    public static void run() {
        loadData();

        // главное меню
        while (true) {
            System.out.println("Выберите действие, введя нужный номер команды:");
            System.out.println("1. Зарегистрировать нового пользователя");
            System.out.println("2. Войти в систему");
            System.out.println("3. Выйти из системы");

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

    // получение команды, введенной пользователем в терминал
    private static int getCommand() {
        while (true) {
            try {
                System.out.print("Введите номер команды: ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) { // проверяем, что было введено число
                notificationService.notify("Ошибка ввода. Введите число.");
            }
        }
    }

    // команда регистрации пользователя
    private static void handleRegistration() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine().trim();

        if (userManager.registerUser(username, password)) { // тут регистрируем пользователя
            notificationService.notify("Пользователь успешно зарегистрирован.");
        }
    }

    // команда входа в систему
    private static void handleLogin() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine().trim();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine().trim();

        User user = userManager.authenticate(username, password); // тут производится вход в систему
        if (user != null) {
            notificationService.notify("Добро пожаловать, " + user.getUsername());
            handleUserSession(user);
        }
    }

    private static void handleUserSession(User user) {
        while (true) {
            System.out.println("Выберите действие, введя нужный номер команды:");
            System.out.println("1. Добавить статью доходов");
            System.out.println("2. Добавить статью расходов");
            System.out.println("3. Установить бюджет в нужной категории");
            System.out.println("4. Просмотреть отчет отчёта");
            System.out.println("5. Перевести средства средств");
            System.out.println("6. Сохранить отчёт в файл");
            System.out.println("7. Выйти");

            int command = getCommand();

            // цикл, который проверяет, какая команда была введена, и реализует дальнейшую логику
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

    // Команда для добавления статьи доходов
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

    // Команда для добавления статьи расходов
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

    // команда для установления бюджета в определенной категории
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

    // команда для вывода отчета
    private static void printReport(User user) {
        user.getWallet().printReport();
    }

    // команда для перевода средств между кошельками
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

    // команда для сохранения отчета в файл
    private static void saveReportToFile(User user) {
        System.out.print("Введите имя файла для сохранения отчета: ");
        String filename = scanner.nextLine().trim();
        FileReportWriter.writeReportToFile(filename, user.getWallet());
        notificationService.notify("Отчет сохранен в файл: " + filename);
    }

    // загрузка данных
    private static void loadData() {
        try {
            userManager.loadData(DATA_FILE);
            notificationService.notify("Данные загружены.");
        } catch (Exception e) {
            notificationService.notify("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    // сохранение данных
    private static void saveData() {
        try {
            userManager.saveData(DATA_FILE);
            notificationService.notify("Данные сохранены.");
        } catch (Exception e) {
            notificationService.notify("Ошибка при сохранении данных: " + e.getMessage());
        }
    }
}
