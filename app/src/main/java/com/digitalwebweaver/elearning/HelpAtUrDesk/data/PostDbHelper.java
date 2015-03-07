package com.digitalwebweaver.elearning.HelpAtUrDesk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by k on 3/4/2015.
 */
public class PostDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    //Help At Ur Desk ->haud
    static final String DATABASE_NAME = "haud_posts.db";

    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_POSTS_TABLE = "CREATE TABLE " + BlogPostContract.BlogPostEntry.TABLE_NAME + " (" +
                BlogPostContract.BlogPostEntry._ID + " INTEGER PRIMARY KEY," +
                BlogPostContract.BlogPostEntry.COLUMN_POST_URL + " TEXT UNIQUE NOT NULL, " +
                BlogPostContract.BlogPostEntry.COLUMN_POST_TITLE + " TEXT NOT NULL, " +
                BlogPostContract.BlogPostEntry.COLUMN_POST_CONTENT + " LONGTEXT NOT NULL, " +
                BlogPostContract.BlogPostEntry.COLUMN_POST_CATEGORY + " TEXT NOT NULL, " +
                BlogPostContract.BlogPostEntry.COLUMN_POST_TAG + " TEXT NOT NULL, " +
                BlogPostContract.BlogPostEntry.COLUMN_POST_ATTACHMENTS + " TEXT, " +
                BlogPostContract.BlogPostEntry.COLUMN_POST_MODIFIED + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BlogPostContract.BlogPostEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
