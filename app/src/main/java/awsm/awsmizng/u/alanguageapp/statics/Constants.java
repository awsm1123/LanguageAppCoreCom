package awsm.awsmizng.u.alanguageapp.statics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class Constants {
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOAD_ARTICLES = "uploads/articles";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
    public static final String DATABASE_PATH_BUGS = "bugs";
    public static final String DATABASE_PATH_FEEDBACK = "feedback";
    public static final String DATABASE_PATH_UPLOADERS = "uploaders";
    public static final DatabaseReference DATABASE_BASE_REFERENCE = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOAD_ARTICLES);

    public static final String READ_PERMISSION_LOG_TAG = "Read Permission";
    public static final String ROOM_DATABASE_LOG_TAG = "MY ROOM DATABASE";

    public static final int PICK_PDF_CODE = 2342;
    public static final int RC_SIGN_IN = 6969;
    public static final int LIGHT_THEME = 0;
    public static final int DARK_THEME = 1;

    public static String uploaderName = null;
    public static String uploaderID = null;
    public static String language = null;
    public static int theme = LIGHT_THEME;

    public static int DISPLAY_WIDTH = 0;
    public static int DISPLAY_HEIGHT = 0;

    public static SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

}
