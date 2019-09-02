package awsm.awsmizng.u.alanguageapp.activities;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.database.AppExecutors;
import awsm.awsmizng.u.alanguageapp.database.UserDatabase;
import awsm.awsmizng.u.alanguageapp.models.FirebaseUserProfile;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Settings extends AppCompatActivity {

    @BindView(R.id.swTheme)
    Switch swTheme;
    @BindView(R.id.btAbout)
    TextView btAbout;
    @BindView(R.id.btFeedback)
    TextView btFeedback;
    private UserDatabase userDatabase;

    @BindView(R.id.btLogOut)
    TextView btLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Constants.theme == Constants.LIGHT_THEME) {
            setTheme(R.style.ThemeLight);
        } else if (Constants.theme == Constants.DARK_THEME) {
            setTheme(R.style.ThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        if (Constants.theme == Constants.DARK_THEME) {
            swTheme.setChecked(true);
        }
        userDatabase = UserDatabase.getInstance(getApplicationContext());


        swTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    changeTheme(Constants.DARK_THEME);
                } else{
                    changeTheme(Constants.LIGHT_THEME);
                }
            }
        });
    }


    private void logout() {
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

    private void changeTheme(final int theme){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                userDatabase.userDao().updateUser(Constants.uploaderID, theme);
            }
        });

        Constants.theme = theme;
        recreate();
    }

    @OnClick({R.id.swTheme, R.id.btAbout, R.id.btFeedback, R.id.btLogOut, R.id.btBug})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.swTheme:
                break;
            case R.id.btAbout:
                startActivity(new Intent(getApplication(), About.class));
                break;
            case R.id.btFeedback:
                startActivity(new Intent(getApplication(), Report.class).putExtra("redirect", 0));
                break;
            case R.id.btLogOut:
                logout();
                break;
            case R.id.btBug:
                startActivity(new Intent(getApplication(), Report.class).putExtra("redirect", 1));
                break;
        }
    }

  /*  @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        super.onBackPressed();
    } */
}
