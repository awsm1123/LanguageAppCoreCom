package awsm.awsmizng.u.alanguageapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.statics.Constants;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Constants.theme == Constants.LIGHT_THEME) {
            setTheme(R.style.ThemeLight);
        } else if (Constants.theme == Constants.DARK_THEME) {
            setTheme(R.style.ThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
