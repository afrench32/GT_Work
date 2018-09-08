package edu.gatech.seclass.sdpcryptogram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.sdpcryptogram.vo.Cryptogram;
import edu.gatech.seclass.sdpcryptogram.vo.User;
import edu.gatech.seclass.sdpcryptogram.vo.Attempt;

/**
 * Created by a0s01bo on 05/07/18.
 *
 * CryptogramDBManager
 *
 * Contains helper methods for the app to work with an SQLite database
 */

public class CryptogramDBManager {

    private CryptogramDBHelper cryptogramDBHelper;

    /**
     * Constructor
     * @param context, context to use
     */
    public CryptogramDBManager(Context context) {
        cryptogramDBHelper = new CryptogramDBHelper(context);
    }

    /**
     * Insert a new user into the database
     * @param user, user object to insert
     * @return success, T if creation was successful, F otherwise
     */
    public boolean insertUser(User user) {

        long commitID = 0;

        SQLiteDatabase database = cryptogramDBHelper.getWritableDatabase();

        boolean success = false;

        ContentValues contentValue = new ContentValues();
        contentValue.put(cryptogramDBHelper.FIRST_NAME, user.getFirstName());
        contentValue.put(cryptogramDBHelper.LAST_NAME, user.getLastName());
        contentValue.put(cryptogramDBHelper.USER_NAME,user.getUserName());
        contentValue.put(cryptogramDBHelper.EMAIL_ID,user.getEmailId());

        try {
            commitID = database.insert(cryptogramDBHelper.TABLE_NAME, null, contentValue);
        }
        catch (SQLiteConstraintException notUnique) {
            // username is not unique
        }

        database.close();

        if (commitID > 0) {
            success = true;
        }

        return success;
    }

    /**
     * Insert a new cryptogram into the database
     * @param cryptogram, cryptogram object to insert
     * @return success, true if creation was successful, false otherwise
     */
    public boolean insertCryptogram(Cryptogram cryptogram) {

        SQLiteDatabase database = cryptogramDBHelper.getWritableDatabase();

        boolean success = false;
        long commitID = 0;

        ContentValues contentValue = new ContentValues();
        contentValue.put(cryptogramDBHelper.PUZZLE_NAME, cryptogram.getPuzzleName());
        contentValue.put(cryptogramDBHelper.ENCODED, cryptogram.getEncoded());
        contentValue.put(cryptogramDBHelper.DECODED, cryptogram.getDecoded());
        contentValue.put(cryptogramDBHelper.CREATED_BY, cryptogram.getCreatedBy());
        contentValue.put(cryptogramDBHelper.ATTEMPTS_ALLOWED,cryptogram.getAttemptsAllowed());
        contentValue.put(cryptogramDBHelper.DATE_CREATED, cryptogram.getDateCreated());

        try {
            commitID = database.insert(cryptogramDBHelper.TABLE_CRYPTO, null, contentValue);
        }
        catch (SQLiteConstraintException notUnique) {
            // puzzle name not unique
        }

        database.close();

        if (commitID > 0) {
            success = true;
        }

        return success;
    }

    /**
     * Inserts an attempt into the attempts database
     * @param attempt, an attempt object representing a new attempt at a puzzle
     * @return success, boolean representing a successful commit
     */
    public boolean insertAttempt(Attempt attempt) {

        SQLiteDatabase database = cryptogramDBHelper.getWritableDatabase();

        boolean success = false;
        long commitID = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(cryptogramDBHelper.USER_NAME, attempt.getUsername());
        contentValues.put(cryptogramDBHelper.PUZZLE_NAME, attempt.getPuzzleName());
        contentValues.put(cryptogramDBHelper.ATTEMPTS_MADE, attempt.getAttemptsMade());
        contentValues.put(cryptogramDBHelper.ATTEMPTS_ALLOWED, attempt.getAttemptsAllowed());
        contentValues.put(cryptogramDBHelper.SOLVED, attempt.getSolved());
        contentValues.put(cryptogramDBHelper.DATE_COMPLETED, attempt.getDateCompleted());

        try {
            commitID = database.insert(cryptogramDBHelper.TABLE_ATTEMPTS, null, contentValues);
        }
        catch (SQLiteConstraintException isNull) {
            // one or more null values
        }

        database.close();

        if (commitID > 0) {
            success = true;
        }

        return success;
    }

    /**
     * returns a user object representing a specific user from the database
     * @param username, username to get
     * @return user object with user data if object found, null if not
     */
    public User getUser(String username) {

        User user;
        Cursor cursor = null;
        SQLiteDatabase database = cryptogramDBHelper.getReadableDatabase();
        try {
            System.err.println("searching for: <" + username + ">");
            String args[] = {username};
            //
            cursor = database.query(cryptogramDBHelper.TABLE_NAME, null,
                    "userName = ?", args,null, null, null, null);
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                String Id = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ID));
                String firstName = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.LAST_NAME));
                String userName = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.USER_NAME));
                String emailId = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.EMAIL_ID));
                user = new User(firstName, lastName, userName, emailId);
            }
            else if (cursor.getCount() == 0) {
                System.err.println("no such user: " + username);
                user = null;
            }
            else {
                System.err.println(cursor.getCount() + " matching lines in databse: too many!");
                user = null;
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
            user = null;
        }

        database.close();
        cursor.close();

        return user;
    }

    /**
     * Get a specified cryptogram from the database
     * @param puzzleName, puzzleName to get
     * @return Cryptogram representing the puzzle, null if no such puzzle exists
     */
    public Cryptogram getCrypto(String puzzleName) {

        String args[] = {puzzleName};

        SQLiteDatabase database = cryptogramDBHelper.getReadableDatabase();
        //Cursor cursor =  database.rawQuery( "select * from cryptogram where puzzleName = ?", args);
        Cursor cursor = database.query(cryptogramDBHelper.TABLE_CRYPTO, null,
                "puzzleName = ?", args, null, null, null, null);

        Cryptogram cryptogram = null;
        boolean nonempty = cursor.moveToFirst();

        if (nonempty) {
            String encoded = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ENCODED));
            String decoded = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.DECODED));
            String createdBy = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.CREATED_BY));
            String attemptsAllowed = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ATTEMPTS_ALLOWED));
            String dateCreated = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.DATE_CREATED));
            cryptogram = new Cryptogram(encoded, decoded, createdBy, puzzleName, attemptsAllowed, dateCreated);
        }

        database.close();
        cursor.close();

        return cryptogram;
    }

    /**
     * Get all Cryptograms from the database. This seems to work.
     * @return List of Cryptograms, Empty List if no Cryptograms exists.
     */
    public List<Cryptogram> getAllCryptograms() {

        String args[] = {};

        SQLiteDatabase database = cryptogramDBHelper.getReadableDatabase();
        //Cursor cursor =  database.rawQuery( "select * from cryptogram where puzzleName = ?", args);
        Cursor cursor = database.query(cryptogramDBHelper.TABLE_CRYPTO, null,
                null, args, null, null, null, null);
        List<Cryptogram> listOfCryptograms=new ArrayList<>();
        try {
            while (cursor.moveToNext()) {

                String encoded = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ENCODED));
                String decoded = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.DECODED));
                String createdBy = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.CREATED_BY));
                String attemptsAllowed = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ATTEMPTS_ALLOWED));
                String puzzleName=cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.PUZZLE_NAME));
                String dateCreated = cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.DATE_CREATED));

                Cryptogram cryptogram = new Cryptogram(encoded, decoded, createdBy, puzzleName, attemptsAllowed, dateCreated);
                System.err.println(cryptogram.getPuzzleName() + " by " + cryptogram.getCreatedBy() + ": " + cryptogram.getDecoded() + "->" + cryptogram.getEncoded() + ", " + cryptogram.getAttemptsAllowed());
                listOfCryptograms.add(cryptogram);

            }
        }
        finally {
            cursor.close();
            database.close();
        }
        return listOfCryptograms;
    }

    /**
     * Get specified attempt from the databse
     * @param userName, username of attempter
     * @param puzzleName, puzzleName attempter is attempting
     * @return attempt, Attempt representing the attempt, null if no such puzzle exists
     */
    public Attempt getAttempt(String userName, String puzzleName) {

        String args[] = {userName, puzzleName};

        SQLiteDatabase database = cryptogramDBHelper.getReadableDatabase();
        //Cursor cursor = database.rawQuery("select * from attempts where userName = ? " + "AND puzzleName = ?", args);
        Cursor cursor = database.query(cryptogramDBHelper.TABLE_ATTEMPTS, null, "(userName = ? AND puzzleName = ?)", args, null, null, null, null);

        Attempt attempt = null;
        boolean nonempty = cursor.moveToFirst();

        if(nonempty) {

            int attemptsMade = Integer.parseInt(
                    cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ATTEMPTS_MADE)));
            int attemptsAllowed = Integer.parseInt(
                    cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ATTEMPTS_ALLOWED)));
            boolean solved = Boolean.parseBoolean(cursor.getString(
                    cursor.getColumnIndex(cryptogramDBHelper.SOLVED)));
            String dateCompleted = cursor.getString(
                    cursor.getColumnIndex(cryptogramDBHelper.DATE_COMPLETED));

            attempt = new Attempt(userName, puzzleName, attemptsMade, attemptsAllowed, solved, dateCompleted);
        }

        cursor.close();
        database.close();

        return attempt;
    }

    /**
     * Get all attempts from the database
     * @return attemptList, list of Attempt objects representing the attempt, empty if none
     */
    public List<Attempt> getAllAttempts() {

        String args[] = {};

        SQLiteDatabase database = cryptogramDBHelper.getReadableDatabase();
        Cursor cursor = database.query(cryptogramDBHelper.TABLE_ATTEMPTS, null, null, args, null, null, null, null);

        List<Attempt> attemptList=new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                int attemptsMade = Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ATTEMPTS_MADE)));
                int attemptsAllowed = Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ATTEMPTS_ALLOWED)));
                boolean solved = Boolean.parseBoolean(cursor.getString(
                        cursor.getColumnIndex(cryptogramDBHelper.SOLVED)));
                String puzzleName=cursor.getString(
                        cursor.getColumnIndex(cryptogramDBHelper.PUZZLE_NAME));
                String dateCompleted = cursor.getString(
                        cursor.getColumnIndex(cryptogramDBHelper.DATE_COMPLETED));
                String userName = cursor.getString(
                        cursor.getColumnIndex(cryptogramDBHelper.USER_NAME));
                Attempt attempt = new Attempt(userName, puzzleName, attemptsMade, attemptsAllowed, solved, dateCompleted);
                System.err.println(attempt.getUsername() + " attempted " + attempt.getPuzzleName() + ", has tried it " + attempt.getAttemptsMade() + " times out of " + attempt.getAttemptsAllowed() + ". Solved: " + attempt.getSolved());
                attemptList.add(attempt);
            }
        }
        finally {
            cursor.close();
            database.close();
        }
        return attemptList;
    }

    /**
     * Updates attempt object in attempts table with current information
     * @param attempt, attempt object with current information
     * @return success, boolean representing successful update
     */
    public boolean updateAttempt(Attempt attempt) {

        SQLiteDatabase database = cryptogramDBHelper.getWritableDatabase();
        boolean success = false;
        long commitID = 0;

        String args[] = {attempt.getUsername(), attempt.getPuzzleName()};

        ContentValues contentValues = new ContentValues();
        contentValues.put(cryptogramDBHelper.USER_NAME, attempt.getUsername());
        contentValues.put(cryptogramDBHelper.PUZZLE_NAME, attempt.getPuzzleName());
        contentValues.put(cryptogramDBHelper.ATTEMPTS_MADE, attempt.getAttemptsMade());
        contentValues.put(cryptogramDBHelper.ATTEMPTS_ALLOWED, attempt.getAttemptsAllowed());
        contentValues.put(cryptogramDBHelper.SOLVED, attempt.getSolved());
        contentValues.put(cryptogramDBHelper.DATE_COMPLETED, attempt.getDateCompleted());
        commitID = database.update(cryptogramDBHelper.TABLE_ATTEMPTS, contentValues,
                "(userName = ? AND puzzleName = ?)", args);
        database.close();


        if (commitID > 0) {
            success = true;
        }

        return success;

    }

    /**
     * Close connection to database
     */
    public void close() {
        cryptogramDBHelper.close();
    }

    /**
     * Test query for the user database
     * @return string with result: number of rows, username for each row
     */
    public String testQueryUser() {
        Cursor cursor;
        SQLiteDatabase database = cryptogramDBHelper.getReadableDatabase();
        int count;
        String names = "";

        cursor = database.query(cryptogramDBHelper.TABLE_NAME, null, null,
                null, null, null, null, null);
        count = cursor.getCount();
        while (cursor.moveToNext()) {
            names += ", " + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.USER_NAME));
        }
        cursor.close();

        return ("There are: " + count + " users:\n\t" + names);
    }

    /**
     * Test query for the cryptogram database
     * @return string with result: number of rows, puzzleName, solution, encoded for each row
     */
    public String testQueryCryptogram() {
        Cursor cursor;
        SQLiteDatabase database = cryptogramDBHelper.getReadableDatabase();
        int count;
        String puzzles = "";

        cursor = database.query(cryptogramDBHelper.TABLE_CRYPTO, null, null,
                null, null, null, null, null);
        count = cursor.getCount();
        while (cursor.moveToNext()) {
            puzzles += "\n" + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.PUZZLE_NAME))
                    + ": " + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.DECODED))
                    + "->" + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ENCODED));
        }
        cursor.close();

        return ("There are: " + count + " cryptograms:" + puzzles);
    }

    /**
     * Test query for the attempt database
     * @return string with result: number of rows, username, puzzleName, attemptsAllowed, attemptsTaken, solved, date completed for each row
     */
    public String testQueryAttempt() {
        Cursor cursor;
        SQLiteDatabase database = cryptogramDBHelper.getReadableDatabase();
        int count;
        String attempts = "";

        cursor = database.query(cryptogramDBHelper.TABLE_ATTEMPTS, null, null,
                null, null, null, null, null);
        count = cursor.getCount();
        while (cursor.moveToNext()) {
            attempts += "\n" + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.USER_NAME))
                    + ", " + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.PUZZLE_NAME))
                    + ":" + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ATTEMPTS_ALLOWED))
                    + ", " + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.ATTEMPTS_MADE))
                    + ", " + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.SOLVED))
                    + ", " + cursor.getString(cursor.getColumnIndex(cryptogramDBHelper.DATE_COMPLETED));
        }
        cursor.close();

        return ("There are: " + count + " attempts:" + attempts);
    }

}
