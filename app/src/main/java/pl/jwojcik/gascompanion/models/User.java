package pl.jwojcik.gascompanion.models;

import android.text.TextUtils;

import java.util.HashMap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User extends Object {

    private String uid;
    private String name;
    private String email;
    private String gender;
    private String birthday;
    private String loginType;

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
}
