package pl.jwojcik.gascompanion.models;

import android.graphics.Bitmap;

/**
 * Created by king on 18/08/2017.
 */

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
        shared.user.image = bitmap;
    }

    public static User getLoggedUser(){
        return shared.user;
    }

    public User getUser() {
        return user;
    }
}
