package pl.jwojcik.gascompanion.models;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.HashMap;


public class User extends Object {

    public String uid;
    public String name;
    public String email;
    public String gender;
    public String birthday;
    public Bitmap image;
    public String loginType;

    public User() {

    }

    public User(String email, String loginType) {
        this.email = email;
        this.loginType = loginType;
    }

    public User(String name, String email, String gender, String birthday, String loginType) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.loginType = loginType;
    }

    public HashMap<String, Object> firebaseDetails() {
        HashMap<String, Object> result = new HashMap<>();
        if (!TextUtils.isEmpty(name))
            result.put("name", name);
        if (!TextUtils.isEmpty(email))
            result.put("email", email);
        if (!TextUtils.isEmpty(gender))
            result.put("gender", gender);
        if (!TextUtils.isEmpty(birthday))
            result.put("birthday", birthday);
        if (!TextUtils.isEmpty(loginType))
            result.put("loginType", loginType);
        result.put("uid", uid);

        return result;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getLoginType() {
        return loginType;
    }
}
