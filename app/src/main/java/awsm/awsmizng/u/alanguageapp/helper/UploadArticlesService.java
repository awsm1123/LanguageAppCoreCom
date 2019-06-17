package awsm.awsmizng.u.alanguageapp.helper;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.UUID;

import awsm.awsmizng.u.alanguageapp.models.FirebaseUserProfile;
import awsm.awsmizng.u.alanguageapp.models.Upload;
import awsm.awsmizng.u.alanguageapp.statics.Constants;

public class UploadArticlesService extends IntentService {

    StorageReference storageReference;
    DatabaseReference databaseReference, databaseReference2;
    static String uploads, LOG_INTENT="UPLOADING";

    public  UploadArticlesService(){
        super("UploadArticlesService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        Log.v(LOG_INTENT, "started");
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOAD_ARTICLES);
        databaseReference2 = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADERS);

        Log.v(LOG_INTENT, "uploading");

        Uri data = intent.getData();
        final String fileName = intent.getStringExtra("fileName");
        final String theme = intent.getStringExtra("theme");
        final ResultReceiver rec = intent.getParcelableExtra("receiver");

        StorageReference sRef = storageReference.child(Constants.language).child(Constants.STORAGE_PATH_UPLOADS + fileName + UUID.randomUUID() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Upload upload = new Upload(
                                        fileName,
                                        uri.toString(),
                                        Constants.uploaderName,
                                        Constants.uploaderID,
                                        Constants.sdf.format(new Date())
                                );
                                databaseReference.child(Constants.language)
                                        .child(theme)
                                        .child(databaseReference.push().getKey())
                                        .setValue(upload);

                                Query ref = databaseReference2.orderByChild("userID").equalTo(Constants.uploaderID);
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                FirebaseUserProfile userProfile = snapshot.getValue(FirebaseUserProfile.class);
                                                uploads = userProfile.getPoints();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                int updateUpload = 0;
                                if (!TextUtils.isEmpty(uploads) && TextUtils.isDigitsOnly(uploads)) {
                                    updateUpload = Integer.parseInt(uploads) + 1;
                                }

                                databaseReference2.child(Constants.uploaderID).child("points").setValue(updateUpload+"");
                                databaseReference2.child(Constants.uploaderID).child("lastActive").setValue(Constants.sdf.format(new Date()));
                            }
                        });

                        Bundle bundle = new Bundle();
                        bundle.putString("resultValue", fileName + " uploaded successfully");
                        bundle.putString("uploadProgress", "success");
                        // Here we call send passing a resultCode and the bundle of extras
                        rec.send(Activity.RESULT_OK, bundle);

                       /*  TransitionManager.beginDelayedTransition(transitionsContainer);
                        readyUIforInput();
                        tvUploadStatus.setText("File Uploaded Successfully"); */
                    }
                })
               .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Bundle bundle = new Bundle();
                        bundle.putString("resultValue", "Failure To Upload File");
                        bundle.putString("uploadProgress", "failure");
                        rec.send(Activity.RESULT_OK, bundle);
                      //  tvUploadStatus.setText("Failure To Upload File");
                       // Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
               .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Bundle bundle = new Bundle();
                        bundle.putString("resultValue", (int) progress + "% Uploaded...");
                        bundle.putString("uploadProgress", "uploading");
                        bundle.putInt("progress", (int) progress);
                        rec.send(Activity.RESULT_OK, bundle);
                       // tvUploadStatus.setText((int) progress + "% Uploading...");
                    }
                })
        ;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_INTENT, "destryong ntent");
    }
}
