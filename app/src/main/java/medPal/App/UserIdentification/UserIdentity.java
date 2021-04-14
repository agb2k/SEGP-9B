package medPal.App.UserIdentification;

/**
 * Handle user identification.
 */
public class UserIdentity {

    public static UserIdentity userIdentification = new UserIdentity();
    private boolean loggedIn = false;
    private int userId;
    private String username;
    private String email;

    /**
     * Private constructor, to avoid new initialization
     */
    private UserIdentity() {}

    public static UserIdentity getInstance() {
        return userIdentification;
    }

    /**
     * Set information
     * @param userId
     * @param username
     * @param email
     */
    public void setInformation(int userId, String username, String email) {
        loggedIn = true;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public boolean loggedIn() {
        return loggedIn;
    }



}
