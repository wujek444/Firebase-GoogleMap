package pl.jwojcik.gascompanion.models;

public class CurrentUserService {

    public static CurrentUserService shared = new CurrentUserService();

    private User user;

    public CurrentUserService() {}

    public static void login(User user) {
        shared.user = user;
    }

    public static void logout() {
        shared.user = null;
    }

    public static User getLoggedUser(){
        return shared.user;
    }

    public User getUser() {
        return user;
    }
}
