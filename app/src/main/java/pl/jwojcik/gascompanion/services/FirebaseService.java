package pl.jwojcik.gascompanion.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pl.jwojcik.gascompanion.Constants;
import pl.jwojcik.gascompanion.models.CurrentUserService;
import pl.jwojcik.gascompanion.models.Food;
import pl.jwojcik.gascompanion.models.GasStation;
import pl.jwojcik.gascompanion.models.Price;
import pl.jwojcik.gascompanion.models.User;


public class FirebaseService {

    public static FirebaseService shared = new FirebaseService();
    private static final String KEY_USERS = "users";
    private static final String KEY_GAS_STATIONS = "gasStations";
    private static final String KEY_FOODS = "foods";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_PRICES = "prices";

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

    public void login(String email, String password, final ObjectResultListener listener) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            listener.onResult(false, task.getException().getLocalizedMessage(), null);
                        } else {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                getUser(firebaseAuth.getCurrentUser().getUid(), listener);
                            } else {
                                listener.onResult(false, "Please check your inbox for a verification email and follow the provided link to continue.", null);
                            }
                        }

                    }
                });
    }

    public void signup(final String email, String password, final ObjectResultListener listener) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            listener.onResult(false, task.getException().getLocalizedMessage(), null);
                        } else {
                            User user = new User(email, Constants.TYPE_EMAIL);
                            createUser(user, true);
                            listener.onResult(true, null, null);
                        }
                    }
                });
    }

    public void sendVerificationEmail(final ObjectResultListener listener) {

        firebaseAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.signOut();
                            listener.onResult(true, null, null);
                        } else {
                            listener.onResult(false, task.getException().getLocalizedMessage(), null);
                        }
                    }
                });
    }

    public void signinWithFacebook(AuthCredential credential, final User user, final ObjectResultListener listener) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            listener.onResult(false, task.getException().getLocalizedMessage(), null);
                        } else {
                            if (user != null) {
                                createUser(user, true);
                                CurrentUserService.login(user);
                            }
                            listener.onResult(true, null, null);
                        }
                    }
                });
    }

    public void createUser(User user, boolean isNewId) {

        String newId;
        if (isNewId) {
            if (isLoggedIn()) {
                newId = firebaseAuth.getCurrentUser().getUid();
                user.setUid(newId);
            } else {
                newId = usersRef.push().getKey();
                user.setUid(newId);
            }
        } else {
            newId = user.getUid();
        }
        usersRef.child(newId).child(KEY_DETAILS).setValue(user.firebaseDetails());
        if (user.getImage() != null) {
            uploadUserPhoto(user.getImage(), newId);
        }
    }

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public void uploadUserPhoto(Bitmap bitmap, String uid) {
        StorageReference imageRef = storageRef.child(KEY_USERS).child(uid).child(KEY_PHOTO);
        uploadPhoto(bitmap, imageRef);
    }

    public void uploadPhoto(Bitmap bitmap, StorageReference imageRef) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    public void getUser(final String uid, final ObjectResultListener listener) {

        usersRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.child(KEY_DETAILS).getValue(User.class);
                String path = KEY_USERS + "/"+ uid + "/" + KEY_PHOTO;

                downloadPhoto(path, new ImageResultListener() {
                    @Override
                    public void onResult(boolean isSuccess, String error, Bitmap bitmap) {
                        if (isSuccess) {
                            if (bitmap != null)
                                user.setImage(bitmap);
                        }
                        listener.onResult(true, null, user);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResult(false, databaseError.getMessage(), null);
            }
        });
    }

    public void downloadPhoto(String path, final ImageResultListener listener) {

        StorageReference imageRef = storageRef.child(path);
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                listener.onResult(true, null, bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onResult(false, e.getLocalizedMessage(), null);
            }
        });
    }

    public void createGasStation(GasStation gasStation, ObjectResultListener listener) {

        String newId = gasStation.getPlace_id();
        gasStationsRef.child(newId).setValue(gasStation.firebaseDetails());
        listener.onResult(true, null, null);
    }

    public void createPrice(Price price, ObjectResultListener listener){
        pricesRef.child(generateRandomId()).setValue(price);
        listener.onResult(true, null, null);
    }

    public void createFood(Food food, boolean withNewId, ObjectResultListener listener) {

        String newId;
        if (withNewId) {
            newId = foodsRef.push().getKey();
            food.setId(newId);
        } else {
            newId = food.getId();
        }
        foodsRef.child(newId).setValue(food.firebaseDetails());
        if (food.getImage() != null) {
            StorageReference imageRef = storageRef.child("foods").child(newId).child("photo");
            uploadPhoto(food.getImage(), imageRef);
        }
    }

    public void getGasStations(final ResultListener listener) {

        gasStationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<GasStation> list = new ArrayList<GasStation>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    GasStation gasStation = snapshot.getValue(GasStation.class);
                    list.add(gasStation);
                }
                listener.onResult(true, null, list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResult(false, databaseError.getMessage(), null);
            }
        });
    }

    public void getFoods(final ResultListener listener) {

        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Food> list = new ArrayList<Food>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Food food = snapshot.getValue(Food.class);
                    list.add(food);
                }
                listener.onResult(true, null, list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResult(false, databaseError.getMessage(), null);
            }
        });
    }

    public String generateRandomId(){
        return RandomStringUtils.randomAlphanumeric(20).toUpperCase();
    }

}
