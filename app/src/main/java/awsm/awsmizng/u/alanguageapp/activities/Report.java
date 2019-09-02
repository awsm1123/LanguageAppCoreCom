package awsm.awsmizng.u.alanguageapp.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.models.ReportUpload;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Report extends AppCompatActivity {

    @BindView(R.id.etReportText)
    TextInputEditText etReportText;
    @BindView(R.id.btSubmit)
    Button btSubmit;

    String text = null;
    DatabaseReference databaseReference;
    @BindView(R.id.tvHeading)
    TextView tvHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Constants.theme == Constants.LIGHT_THEME) {
            setTheme(R.style.ThemeLight);
        } else if (Constants.theme == Constants.DARK_THEME) {
            setTheme(R.style.ThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        if (getIntent().getIntExtra("redirect", 0) == 0) {
            tvHeading.setText("Provide Feedback");
            databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_FEEDBACK);
        } else {
            tvHeading.setText("Report Bug");
            databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_BUGS);
        }

        etReportText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!etReportText.getText().toString().isEmpty()) {
                    btSubmit.setEnabled(true);
                } else {
                    btSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.btSubmit)
    public void onViewClicked() {
        text = etReportText.getText().toString().trim();
        ReportUpload report = new ReportUpload(
                Constants.uploaderName,
                Constants.uploaderID,
                Constants.sdf.format(new Date()),
                text
        );

        databaseReference.child(databaseReference.push().getKey()).setValue(report);

        Toast.makeText(getApplicationContext(), "Thanks!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
