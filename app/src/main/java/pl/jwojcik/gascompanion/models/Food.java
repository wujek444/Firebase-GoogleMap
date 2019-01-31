package pl.jwojcik.gascompanion.models;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.HashMap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Food extends Object {

    private String id;
    private String name;
    private double price;
    private Bitmap image;
    private String type;
    private String currencyType;

    public Food(String id, String name, String type, double price, String currencyType) {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setType(type);
        this.setCurrencyType(currencyType);
    }

    public HashMap<String, Object> firebaseDetails() {
        HashMap<String, Object> result = new HashMap<>();
        if (!TextUtils.isEmpty(name))
            result.put("name", name);
        if (!TextUtils.isEmpty(type))
            result.put("type", type);
        if (!TextUtils.isEmpty(currencyType))
            result.put("currencyType", currencyType);
        result.put("price", price);
        result.put("id", id);

        return result;
    }

}
