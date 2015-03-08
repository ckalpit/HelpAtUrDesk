package com.digitalwebweaver.elearning.HelpAtUrDesk.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by k on 3/4/2015.
 */
public class PostProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PostDbHelper mOpenHelper;
    public static final String SQL_INSERT_OR_REPLACE = "__sql_insert_or_replace__";
    static final int POSTS = 100;
    static final int POST_WITH_CATEGORY = 101;
    static final int POST_WITH_CATEGORY_AND_TAG = 102;
    static final int POSTS_ANSWER = 105;

    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BlogPostContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, BlogPostContract.PATH_BLOG_POST, POSTS);
//        matcher.addURI(authority, BlogPostContract.PATH_BLOG_POST, POST_WITH_CATEGORY);
//        matcher.addURI(authority, BlogPostContract.PATH_BLOG_POST, POST_WITH_CATEGORY_AND_TAG);
//        matcher.addURI(authority, BlogPostContract.PATH_BLOG_POST, POSTS_ANSWER);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new PostDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case POSTS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BlogPostContract.BlogPostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case POST_WITH_CATEGORY:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BlogPostContract.BlogPostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case POST_WITH_CATEGORY_AND_TAG:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BlogPostContract.BlogPostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case POSTS_ANSWER:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BlogPostContract.BlogPostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case POSTS:
                return BlogPostContract.BlogPostEntry.CONTENT_TYPE;
            case POST_WITH_CATEGORY:
                return BlogPostContract.BlogPostEntry.CONTENT_TYPE;
            case POST_WITH_CATEGORY_AND_TAG:
                return BlogPostContract.BlogPostEntry.CONTENT_TYPE;
            case POSTS_ANSWER:
                return BlogPostContract.BlogPostEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        boolean replace = false;
        long _id;
        if (contentValues.containsKey(SQL_INSERT_OR_REPLACE)) {
            replace = contentValues.getAsBoolean(SQL_INSERT_OR_REPLACE);
            // Clone the values object, so we don't modify the original.
            // This is not strictly necessary, but depends on your needs
            contentValues = new ContentValues(contentValues);

            // Remove the key, so we don't pass that on to db.insert() or db.replace()
            contentValues.remove(SQL_INSERT_OR_REPLACE);
        }

        switch (match) {
            case POSTS:
                if (replace) {
                    _id = db.replace(BlogPostContract.BlogPostEntry.TABLE_NAME, null, contentValues);
                } else {
                    _id = db.insert(BlogPostContract.BlogPostEntry.TABLE_NAME, null, contentValues);
                }
//                _id = db.insert(BlogPostContract.BlogPostEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = BlogPostContract.BlogPostEntry.buildPostsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
//                _id = db.insert(BlogPostContract.BlogPostEntry.TABLE_NAME, null, contentValues);
//                if (_id > 0)
//                    returnUri = BlogPostContract.BlogPostEntry.buildPostsUri(_id);
//                else
                //throw new android.database.SQLException("Failed to insert row into " + uri);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case POSTS:
                rowsUpdated = db.update(BlogPostContract.BlogPostEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long _id;
        boolean replace = false;
        switch (match) {
            case POSTS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        if (value.containsKey(SQL_INSERT_OR_REPLACE)) {
                            replace = value.getAsBoolean(SQL_INSERT_OR_REPLACE);
                            // Clone the values object, so we don't modify the original.
                            // This is not strictly necessary, but depends on your needs
                            value = new ContentValues(value);

                            // Remove the key, so we don't pass that on to db.insert() or db.replace()
                            value.remove(SQL_INSERT_OR_REPLACE);
                        }
                        if (replace) {
                            _id = db.replace(BlogPostContract.BlogPostEntry.TABLE_NAME, null, value);
                        } else {
                            _id = db.insert(BlogPostContract.BlogPostEntry.TABLE_NAME, null, value);
                        }
//                        long _id = db.insert(BlogPostContract.BlogPostEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
