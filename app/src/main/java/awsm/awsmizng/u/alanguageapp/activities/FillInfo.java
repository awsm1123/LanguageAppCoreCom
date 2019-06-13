package awsm.awsmizng.u.alanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.database.AppExecutors;
import awsm.awsmizng.u.alanguageapp.database.UserDatabase;
import awsm.awsmizng.u.alanguageapp.models.FirebaseUserProfile;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FillInfo extends AppCompatActivity {

    DatabaseReference databaseReference2;
    private UserDatabase userDatabase;

    @BindView(R.id.etDisplayName)
    EditText etDisplayName;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.germany)
    ImageView germany;
    @BindView(R.id.japan)
    ImageView japan;
    @BindView(R.id.india)
    ImageView india;
    @BindView(R.id.spain)
    ImageView spain;
    @BindView(R.id.france)
    ImageView france;
    @BindView(R.id.tvLanguageChosen)
    TextView tvLanguageChosen;
    @BindView(R.id.llForm)
    LinearLayout llForm;
    @BindView(R.id.llProgress)
    LinearLayout llProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        ButterKnife.bind(this);

        userDatabase =  UserDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        switch (intent.getIntExtra("redirect", -1)){
            case 0:
                //restarted activity. read room if fails query firebase

                FirebaseUserProfile RoomUserProfile = userDatabase.userDao().loadUserDetails();
                if(RoomUserProfile != null){
                    Constants.uploaderID = RoomUserProfile.getUserID();
                    Constants.uploaderName = RoomUserProfile.getUserName();
                    Constants.language = RoomUserProfile.getLanguage();
                    Constants.theme = RoomUserProfile.getTheme();
                    Log.v(Constants.ROOM_DATABASE_LOG_TAG, "Read from room");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else{
                    queryFirebase();
                    Log.v(Constants.ROOM_DATABASE_LOG_TAG, "Read from firebase");
                }

                break;
            case 1:
                //new sign in
                queryFirebase();
                break;

                default:
                    Toast.makeText(getApplicationContext(), "Sign In Failure", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
        }

        editTextWatcher();
    }

    private void queryFirebase(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference2 = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADERS);
        Query ref = databaseReference2.orderByChild("userID").equalTo(user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseUserProfile userProfile = snapshot.getValue(FirebaseUserProfile.class);
                        Constants.language = userProfile.getLanguage();
                        Constants.uploaderName = userProfile.getUserName();
                        Constants.uploaderID = userProfile.getUserID();

                        saveUserToRoom();
                        Log.v(Constants.ROOM_DATABASE_LOG_TAG, "saved existing user to room");

                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    ViewGroup transiion = findViewById(R.id.transition_scene);
                    TransitionManager.beginDelayedTransition(transiion);
                    llProgress.setVisibility(View.GONE);
                    llForm.setVisibility(View.VISIBLE);
                    /*TODO add welcome message to form*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.germany, R.id.japan, R.id.india, R.id.spain, R.id.france, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.germany:
                Constants.language = "GERMAN";
                tvLanguageChosen.setText(getString(R.string.langPref) + Constants.language);
                break;
            case R.id.japan:
                Constants.language = "JAPANESE";
                tvLanguageChosen.setText(getString(R.string.langPref) + Constants.language);
                break;
            case R.id.india:
                Constants.language = "SANSKRIT";
                tvLanguageChosen.setText(getString(R.string.langPref) + Constants.language);
                break;
            case R.id.spain:
                Constants.language = "SPANISH";
                tvLanguageChosen.setText(getString(R.string.langPref) + Constants.language);
                break;
            case R.id.france:
                Constants.language = "FRENCH";
                tvLanguageChosen.setText(getString(R.string.langPref) + Constants.language);
                break;
            case R.id.submit:
                Constants.uploaderID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Constants.uploaderName = etDisplayName.getText().toString();
                FirebaseUserProfile firebaseUserProfile = new FirebaseUserProfile(Constants.uploaderName,
                        Constants.language,
                        Constants.uploaderID,
                        "0",
                        Constants.sdf.format(new Date())
                );
                databaseReference2.child(Constants.uploaderID).setValue(firebaseUserProfile);

                saveUserToRoom();
                Log.v(Constants.ROOM_DATABASE_LOG_TAG, "saved new user to room");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
    }

    private void saveUserToRoom(){
        final FirebaseUserProfile roomUserProfile =  new FirebaseUserProfile(Constants.uploaderID,
                Constants.uploaderName,
                Constants.language,
                Constants.LIGHT_THEME);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                userDatabase.userDao().insertUser(roomUserProfile);
            }
        });
    }

    private void editTextWatcher(){
        etDisplayName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!etDisplayName.getText().toString().isEmpty()) {
                    submit.setEnabled(true);
                } else {
                    etDisplayName.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
