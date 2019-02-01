package pl.jwojcik.gascompanion.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.jwojcik.gascompanion.enumerated.GasType;

@Data
@NoArgsConstructor
public class Price implements Parcelable {

    private String userEmail;
    private Double value;
    private Date insertDt;
    private String gasType;

    public Price(Double value, GasType gasType) {
        this.userEmail = CurrentUserService.getLoggedUser().getEmail();
        this.value = value;
        this.insertDt = new Date();
        this.gasType = gasType.getName();
    }

    public Price(JSONObject jsonObject){
        try {
            this.userEmail = jsonObject.getString("userEmail");
            this.value = jsonObject.getDouble("value");
            String dateStr = jsonObject.getString("insertDt");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date insertDt = sdf.parse(dateStr);
            this.insertDt = insertDt;
            this.gasType = jsonObject.getString("gasType");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected Price(Parcel in) {
        this.userEmail = in.readString();
        this.value = in.readDouble();
        this.insertDt = (Date) in.readSerializable();
        this.gasType = in.readString();
    }

    public static final Creator<Price> CREATOR = new Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(userEmail);
        dest.writeDouble(value);
        dest.writeSerializable(insertDt);
        dest.writeSerializable(gasType);
    }
}
