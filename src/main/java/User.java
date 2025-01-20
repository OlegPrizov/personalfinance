import java.io.Serializable;

// класс пользователя
public class User implements Serializable {
    private String username;
    private String passwordHash;
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = password;
        this.wallet = new Wallet();
    }

    public String getUsername() {
        return username;
    }

    public boolean validatePassword(String password) {
        return this.passwordHash.equals(password);
    }

    public Wallet getWallet() {
        return wallet;
    }
}
