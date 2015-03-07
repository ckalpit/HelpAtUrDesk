package com.digitalwebweaver.elearning.HelpAtUrDesk.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by k on 3/4/2015.
 */
public class BlogPostContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.digitalwebweaver.elearning.HelpAtUrDesk";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BLOG_POST = "posts";

    /* Inner class that defines the table contents of the location table */
    public static final class BlogPostEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BLOG_POST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BLOG_POST;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BLOG_POST;

        // Table name
        public static final String TABLE_NAME = "posts";

        public static final String COLUMN_POST_URL = "post_url";
        public static final String COLUMN_POST_CONTENT = "post_content";
        public static final String COLUMN_POST_TITLE = "post_title";
        public static final String COLUMN_POST_MODIFIED = "post_modified";
        public static final String COLUMN_POST_TAG = "post_tag";
        public static final String COLUMN_POST_CATEGORY = "post_category";
        public static final String COLUMN_POST_ATTACHMENTS = "post_attachments";

        public static Uri buildPostsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }
}
