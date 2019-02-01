package pl.jwojcik.gascompanion.services.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import pl.jwojcik.gascompanion.services.ImageResultListener;

public class PhotoService extends FirebaseService {

    private static PhotoService shared = new PhotoService();

    private StorageReference storageRef;

    private PhotoService() {
        this.storageRef = getStorageRef();
    }

    public static PhotoService getInstance(){
        return shared;
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
}
