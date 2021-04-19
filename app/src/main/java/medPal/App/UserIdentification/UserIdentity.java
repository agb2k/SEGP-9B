package medPal.App.UserIdentification;

/**
 * Handle user identification.
 */
public class UserIdentity {

    public static UserIdentity userIdentification = new UserIdentity();
    private boolean loggedIn = false;
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
     * @param email
     */
    public void setInformation(String email) {
        loggedIn = true;
        this.email = email;
    }

    public String getEmail() { return email; }

    public boolean loggedIn() {
        return loggedIn;
    }



}
