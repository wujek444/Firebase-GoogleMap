package pl.jwojcik.gascompanion.services.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import lombok.Getter;

@Getter
public class FirebaseService {

    public static FirebaseService shared = new FirebaseService();
    public static final String KEY_USERS = "users";
    public static final String KEY_GAS_STATIONS = "gasStations";
    public static final String KEY_FOODS = "foods";
    public static final String KEY_DETAILS = "details";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_PRICES = "prices";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private DatabaseReference gasStationsRef;
    private DatabaseReference pricesRef;
    private DatabaseReference foodsRef;
    private StorageReference storageRef;

    public FirebaseService() {
        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference(KEY_USERS);
        gasStationsRef = FirebaseDatabase.getInstance().getReference(KEY_GAS_STATIONS);
        pricesRef = FirebaseDatabase.getInstance().getReference(KEY_PRICES);
        foodsRef = FirebaseDatabase.getInstance().getReference(KEY_FOODS);
        storageRef = FirebaseStorage.getInstance().getReference();
    }

}
