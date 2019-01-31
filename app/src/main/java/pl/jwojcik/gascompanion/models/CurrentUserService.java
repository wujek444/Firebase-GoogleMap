package pl.jwojcik.gascompanion.models;

import android.graphics.Bitmap;


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

    public static void setProfileImage(Bitmap bitmap) {
        shared.user.setImage(bitmap);
    }

    public static User getLoggedUser(){
        return shared.user;
    }

    public User getUser() {
        return user;
    }
}
