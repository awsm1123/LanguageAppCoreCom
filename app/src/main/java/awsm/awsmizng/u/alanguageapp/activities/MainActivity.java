package awsm.awsmizng.u.alanguageapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;

import awsm.awsmizng.u.alanguageapp.fragments.ArchiveFragment;
import awsm.awsmizng.u.alanguageapp.fragments.ProfileFragment;
import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.fragments.UploadArticlesFragment;
import awsm.awsmizng.u.alanguageapp.fragments.UploadFragment;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements UploadFragment.OnFragmentInteractionListener, UploadArticlesFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Constants.theme == Constants.LIGHT_THEME){
            setTheme(R.style.ThemeLight);
        } else if(Constants.theme == Constants.DARK_THEME){
            setTheme(R.style.ThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkReadPermission();
        showFrament(UploadFragment.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.botton_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Class fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.upload:
                        fragment = UploadFragment.class;
                        break;
                    case R.id.archive:
                        fragment = ArchiveFragment.class;
                        break;
                    case R.id.profile:
                        fragment = ProfileFragment.class;
                        break;
                }

                showFrament(fragment);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        if ("dark".equals(outValue.string) && Constants.theme == Constants.LIGHT_THEME) {
            recreate();
        }
        if ("light".equals(outValue.string) && Constants.theme == Constants.DARK_THEME) {
            recreate();
        }
    }

    private void showFrament(Class fragmentClass) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrame, fragment)
                .commit();
    }

    private void checkReadPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(Constants.READ_PERMISSION_LOG_TAG, "Permission is granted1");
            } else {

                Log.v(Constants.READ_PERMISSION_LOG_TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(Constants.READ_PERMISSION_LOG_TAG, "Permission is granted1");
        }
    }

    @Override
    public void messageFromParentFragment(Uri uri) {
        Log.i("TAG", "received communication from parent fragment");
    }

    @Override
    public void messageFromChildFragment(Uri uri) {
        Log.i("TAG", "received communication from child fragment");
    }
}
