package awsm.awsmizng.u.alanguageapp.database;


import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import awsm.awsmizng.u.alanguageapp.models.FirebaseUserProfile;

@Database(entities = {FirebaseUserProfile.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    private static final String LOG_TAG = UserDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "LanguageUserDatabase";
    private static UserDatabase sInstance;

    public static UserDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        UserDatabase.class, UserDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract userDao userDao();
}
