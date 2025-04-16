package project.nowruz;

public class Admin extends Account {
    private String userId;

    public Admin(String name, int age, String email, String username, String password, String userId) {
        super(name, age, email, username, password);
        this.userId = userId;
    }

    public String getUserId() { return userId; }
    public String getRule() { return "Admin"; }
}
