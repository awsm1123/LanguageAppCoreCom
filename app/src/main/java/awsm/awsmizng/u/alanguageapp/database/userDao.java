package awsm.awsmizng.u.alanguageapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import awsm.awsmizng.u.alanguageapp.models.FirebaseUserProfile;

@Dao
public interface userDao {
    @Query("SELECT * FROM userDetails ORDER BY userID")
    FirebaseUserProfile loadUserDetails();

    @Delete
    void deleteUser(FirebaseUserProfile  userProfile);

    @Insert
    void insertUser(FirebaseUserProfile userProfile);

    @Query("UPDATE userDetails SET theme=:theme WHERE userID = :userID")
    void updateUser(String userID, int theme);
}
