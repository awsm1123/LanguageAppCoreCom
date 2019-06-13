package awsm.awsmizng.u.alanguageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import awsm.awsmizng.u.alanguageapp.database.AppExecutors;
import awsm.awsmizng.u.alanguageapp.database.UserDatabase;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Settings extends AppCompatActivity {

    private UserDatabase userDatabase;

    @BindView(R.id.btLogOut)
    TextView btLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        userDatabase =  UserDatabase.getInstance(getApplicationContext());
    }

    @OnClick(R.id.btLogOut)
    public void onViewClicked() {
        logout();
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                userDatabase.userDao().deleteUser(userDatabase.userDao().loadUserDetails());
            }
        });
        Intent intent = new Intent(getApplicationContext(), Crossway.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
