import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager implements Serializable {
    private Map<String, User> users = new HashMap<>();

    public boolean registerUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Имя пользователя и пароль не должны быть пустыми .");
            return false;
        }

        if (users.containsKey(username)) {
            System.out.println("Пользователь с таким именем уже существует.");
            return false;
        }

        users.put(username, new User(username, password));
        return true;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.validatePassword(password)) {
            return user;
        }
        System.out.println("Неверное имя пользователя или пароль.");
        return null;
    }

    public void saveData(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    public void loadData(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }
    }
    public User getUser(String username) {
        return users.get(username);
    }
}
