package com.digitalwebweaver.elearning.HelpAtUrDesk.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.digitalwebweaver.elearning.HelpAtUrDesk.DashboardActivity;
import com.digitalwebweaver.elearning.HelpAtUrDesk.R;
import com.digitalwebweaver.elearning.HelpAtUrDesk.data.BlogPostContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by k on 3/8/2015.
 */
public class HelpAtUrDeskSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = HelpAtUrDeskSyncAdapter.class.getSimpleName();
    private static int pageCount = 0;
    public static final String SQL_INSERT_OR_REPLACE = "__sql_insert_or_replace__";

    // Interval at which to sync with the blog posts, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours *3 = 9 hours
    public static final int SYNC_INTERVAL = 60 * 180 * 3;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final int HAUD_NOTIFICATION_ID = 8949;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public HelpAtUrDeskSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");

        String numberOfPosts = "4";

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String questionAnswerJsonStr = null;
        int defaultPage = 1;

        try {
            // Construct the URL for the helpaturdesk query
            do {
                final String QUESTIONS_BASE_URL =
                        "http://helpaturdesk.imkalpit.com/?json=1";
                final String QUERY_PARAM = "count";
                final String QUERY_PAGE = "page";
                Uri builtUri;
                if (pageCount == 0) {
                    builtUri = Uri.parse(QUESTIONS_BASE_URL).buildUpon()
                            .appendQueryParameter(QUERY_PARAM, numberOfPosts)
                            .appendQueryParameter(QUERY_PAGE, String.valueOf(defaultPage))
                            .build();
                } else {
                    builtUri = Uri.parse(QUESTIONS_BASE_URL).buildUpon()
                            .appendQueryParameter(QUERY_PARAM, numberOfPosts)
                            .appendQueryParameter(QUERY_PAGE, String.valueOf(defaultPage))
                            .build();
                }
                defaultPage++;
                URL url = new URL(builtUri.toString());

                // Create the request to blog posts, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return;
                }
                questionAnswerJsonStr = buffer.toString();
                getQuestionsListFromJson(questionAnswerJsonStr);
            } while (pageCount + 1 != defaultPage);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the data, there's no point in attemping
            // to parse it.
            return;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return;

    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private void getQuestionsListFromJson(String questionAnswerInfoJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String QUESTION_ANSWER_OBJECT = "posts";
        final String QUESTION_TITLE = "title";
        final String POST_LINK = "url";
        final String ANSWER_TITLE = "content";
        final String MODIFIED_DATE = "modified";
        final String POST_CATEGORY = "category";
        final String POST_TAG = "tag";
        final String POST_ATTACHMENTS = "attachments";

        try {
            JSONObject questionAnswerInfoJson = new JSONObject(questionAnswerInfoJsonStr);
            JSONArray postsArray = questionAnswerInfoJson.getJSONArray(QUESTION_ANSWER_OBJECT);
            pageCount = questionAnswerInfoJson.getInt("pages");
            // Insert the new post information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(postsArray.length());
            for (int i = 0; i < postsArray.length(); i++) {
                // Get the JSON object representing the day
                String url, answer, question, modifiedDate, category, tag, attachments;
                JSONObject currentPostObject = postsArray.getJSONObject(i);

                question = currentPostObject.get(QUESTION_TITLE).toString();
                answer = currentPostObject.get(ANSWER_TITLE).toString();
                url = currentPostObject.get(POST_LINK).toString();
                modifiedDate = currentPostObject.get(MODIFIED_DATE).toString();
                category = currentPostObject.get(POST_CATEGORY).toString();
                tag = currentPostObject.get(POST_TAG).toString();
                attachments = currentPostObject.get(POST_ATTACHMENTS).toString();

                ContentValues PostValues = new ContentValues();
                PostValues.put(SQL_INSERT_OR_REPLACE, true);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_TITLE, question);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_CONTENT, answer);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_CATEGORY, category);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_TAG, tag);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_MODIFIED, modifiedDate);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_URL, url);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_ATTACHMENTS, attachments);

                cVVector.add(PostValues);
                notifyUserQnAUpdates();
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = getContext().getContentResolver().bulkInsert(BlogPostContract.BlogPostEntry.CONTENT_URI, cvArray);
            }

            Log.w(LOG_TAG, "FetchingPosts Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void notifyUserQnAUpdates() {
        Context context = getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        long lastSync = prefs.getLong(lastNotificationKey, 0);

        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
            // Last sync was more than 1 day ago, let's send a notification.

            Resources resources = context.getResources();
            String title = context.getString(R.string.app_name);

            // Define the text
            String contentText = "Question And Answeres are up-to date.";

            // NotificationCompatBuilder is a very convenient way to build backward-compatible
            // notifications.  Just throw in some data.
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(contentText);

            // Make something interesting happen when the user clicks on the notification.
            // In this case, opening the app is sufficient.
            Intent resultIntent = new Intent(context, DashboardActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            // HAUD_NOTIFICATION_ID allows you to update the notification later on.
            mNotificationManager.notify(HAUD_NOTIFICATION_ID, mBuilder.build());

            //refreshing last sync
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(lastNotificationKey, System.currentTimeMillis());
            editor.commit();
        }
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        HelpAtUrDeskSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);

        }
        return newAccount;
    }
}
