package pl.jwojcik.gascompanion.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class GasStation extends Object implements Parcelable {

    private String id;
    private String name;
    private String subTitle;
    private String description;
    private MyLocation location;
    private String address;
    private String place_id;
    private double rating;
    private List<String> photos;
    private List<Double> pb95Price;
    private List<Double> pb98Price;
    private List<Double> dieselPrice;
    public List<Double> lpgPrice;

    public GasStation() {

    }

    public GasStation(String name, String subTitle, String description, String address, MyLocation location) {
        this.name = name;
        this.subTitle = subTitle;
        this.description = description;
        this.address = address;
        this.location = location;
    }

    public GasStation(JSONObject jsonObject) {
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
            for (int i = 0; i < jsonArray.length(); i ++) {
                subTitle += jsonArray.get(i).toString() + " & ";
            }
            if (subTitle.length() > 0)
                subTitle = subTitle.substring(0, subTitle.length() - 3);

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
        if (photos != null && !photos.isEmpty())
            result.put("photos", photos);

        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<Double> getPb95Price() {
        return pb95Price;
    }

    public void setPb95Price(List<Double> pb95Price) {
        this.pb95Price = pb95Price;
    }

    public List<Double> getPb98Price() {
        return pb98Price;
    }

    public void setPb98Price(List<Double> pb98Price) {
        this.pb98Price = pb98Price;
    }

    public List<Double> getDieselPrice() {
        return dieselPrice;
    }

    public void setDieselPrice(List<Double> dieselPrice) {
        this.dieselPrice = dieselPrice;
    }
}
