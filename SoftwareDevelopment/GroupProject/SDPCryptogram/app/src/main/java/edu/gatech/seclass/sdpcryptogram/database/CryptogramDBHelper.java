package edu.gatech.seclass.sdpcryptogram.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.gatech.seclass.sdpcryptogram.vo.User;

/**
 * Created by a0s01bo on 05/07/18.
 */

public class CryptogramDBHelper extends SQLiteOpenHelper {

    private static CryptogramDBHelper sInstance;

    private static final String DATABASE_NAME = "cryptogram.db";
    private static final int DATABASE_VERSION = 5;

    public static final String ID="id";

    public static final String TABLE_NAME = "User";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String USER_NAME = "userName";
    public static final String EMAIL_ID = "emailId";

    public static final String TABLE_CRYPTO = "cryptogram";
    public static final String ENCODED = "encoded";
    public static final String DECODED = "decoded";
    public static final String CREATED_BY = "createdBy";
    public static final String PUZZLE_NAME = "puzzleName";
    public static final String ATTEMPTS_ALLOWED = "attemptsAllowed";
    public static final String DATE_CREATED = "dateCreated";

    public static final String TABLE_ATTEMPTS = "attempts";
    public static final String ATTEMPTS_MADE = "attemptsMade";
    public static final String SOLVED = "solved";
    public static final String DATE_COMPLETED = "dateCompleted";

    // create table representing users
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FIRST_NAME + " TEXT NOT NULL, " +
            LAST_NAME + " TEXT NOT NULL, " +
            USER_NAME + " TEXT NOT NULL UNIQUE, " +
            EMAIL_ID + " TEXT NOT NULL" +
            ")";

    // create table representing cryptograms
    private static final String CREATE_TABLE_CRYPTO = "CREATE TABLE " + TABLE_CRYPTO + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PUZZLE_NAME + " TEXT NOT NULL UNIQUE, " +
            ENCODED + " TEXT, " +
            DECODED+" TEXT, " +
            CREATED_BY + " TEXT NOT NULL, " +
            ATTEMPTS_ALLOWED + " INTEGER NOT NULL, " +
            DATE_CREATED + " TEXT NOT NULL" +
            ")";

    // create table representing all attempted cryptograms by all users
    private static final String CREATE_TABLE_ATTEMPTS = "CREATE TABLE " + TABLE_ATTEMPTS + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_NAME + " TEXT NOT NULL, " +
            PUZZLE_NAME + " TEXT NOT NULL, " +
            ATTEMPTS_MADE + " INTEGER NOT NULL, " +
            ATTEMPTS_ALLOWED + " INTEGER NOT NULL, " +
            SOLVED + " BOOLEAN NOT NULL, " +
            DATE_COMPLETED + " TEXT, " +
            "UNIQUE (" + USER_NAME + ", " + PUZZLE_NAME + ")" +
            ")";

    public CryptogramDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_CRYPTO);
        db.execSQL(CREATE_TABLE_ATTEMPTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CRYPTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTEMPTS);
        onCreate(db);
    }

}
