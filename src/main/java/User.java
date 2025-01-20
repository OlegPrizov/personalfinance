import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String passwordHash;
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = password;  // Implement proper password hashing in real cases
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
