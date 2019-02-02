package pl.jwojcik.gascompanion.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GasStation implements Parcelable {

    private String id;
    private String name;
    private String subTitle;
    private String description;
    private MyLocation location;
    private String address;
    private String place_id;
    private double rating;
    private List<String> photos;
    private HashMap<String, Price> prices;

    public GasStation(JSONObject jsonObject) {
        //na potrzeby wyświetlania na mapie
        try {
            name = jsonObject.getString("name");
            double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            location = new MyLocation(lat, lng);
            place_id = jsonObject.getString("place_id");
            rating = jsonObject.getDouble("rating");
            address = jsonObject.getString("vicinity");
            JSONArray jsonArray = jsonObject.getJSONArray("types");
            subTitle = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                subTitle += jsonArray.get(i).toString() + " & ";
            }
            if (subTitle.length() > 0)
                subTitle = subTitle.substring(0, subTitle.length() - 3);
            JSONArray pricesJSONArray = jsonObject.getJSONArray("prices");
            List<Price> pricesList = new ArrayList<>();
            for (int i = 0; i < pricesJSONArray.length(); i++) {
                pricesList.add(new Price((JSONObject) pricesJSONArray.get(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected GasStation(Parcel in) {
        id = in.readString();
        name = in.readString();
        subTitle = in.readString();
        description = in.readString();
        location = in.readParcelable(MyLocation.class.getClassLoader());
        address = in.readString();
        place_id = in.readString();
        rating = in.readDouble();
        photos = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(subTitle);
        dest.writeString(description);
        dest.writeParcelable(location, flags);
        dest.writeString(address);
        dest.writeString(place_id);
        dest.writeDouble(rating);
        dest.writeStringList(photos);
        dest.writeMap(prices);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GasStation> CREATOR = new Creator<GasStation>() {
        @Override
        public GasStation createFromParcel(Parcel in) {
            return new GasStation(in);
        }

        @Override
        public GasStation[] newArray(int size) {
            return new GasStation[size];
        }
    };

    public HashMap<String, Object> firebaseDetails() {
        HashMap<String, Object> result = new HashMap<>();
        if (!TextUtils.isEmpty(name))
            result.put("name", name);
        if (!TextUtils.isEmpty(subTitle))
            result.put("subTitle", subTitle);
        if (!TextUtils.isEmpty(description))
            result.put("description", description);
        if (location != null)
            result.put("location", location);
        if (!TextUtils.isEmpty(address))
            result.put("address", address);
        result.put("id", id);
        result.put("place_id", place_id);
        result.put("rating", rating);
        if(prices != null && !prices.isEmpty())
            result.put("prices", prices);

        return result;
    }
}
