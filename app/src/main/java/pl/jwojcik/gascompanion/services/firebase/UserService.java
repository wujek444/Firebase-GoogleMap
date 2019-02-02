package pl.jwojcik.gascompanion.services.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import pl.jwojcik.gascompanion.Constants;
import pl.jwojcik.gascompanion.models.CurrentUserService;
import pl.jwojcik.gascompanion.models.User;
import pl.jwojcik.gascompanion.services.ObjectResultListener;

public class UserService extends FirebaseService {

    public static UserService shared = new UserService();

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private StorageReference storageRef;

    private UserService() {
        this.firebaseAuth = getFirebaseAuth();
        this.usersRef = getUsersRef();
        this.storageRef = getStorageRef();
    }

    public static UserService getInstance() {
        return shared;
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
                newId = getFirebaseAuth().getCurrentUser().getUid();
                user.setUid(newId);
            } else {
                newId = getUsersRef().push().getKey();
                user.setUid(newId);
            }
        } else {
            newId = user.getUid();
        }
        usersRef.child(newId).child(KEY_DETAILS).setValue(user.firebaseDetails());
    }

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }


    public void getUser(final String uid, final ObjectResultListener listener) {

        usersRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.child(KEY_DETAILS).getValue(User.class);
                listener.onResult(true, null, user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResult(false, databaseError.getMessage(), null);
            }
        });
    }
}
