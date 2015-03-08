package com.digitalwebweaver.elearning.HelpAtUrDesk.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

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
public class HelpAtUrDeskService extends IntentService {
    private final String LOG_TAG = HelpAtUrDeskService.class.getSimpleName();
    public static final String NUMBER_OF_POSTS_EXTRA = "post_to_fetch";
    private static int pageCount = 0;
    public static final String SQL_INSERT_OR_REPLACE = "__sql_insert_or_replace__";

    public HelpAtUrDeskService() {
        super("HelpAtUrDeskService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String numberOfPosts = intent.getStringExtra(NUMBER_OF_POSTS_EXTRA);

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

                // Create the request to OpenWeatherMap, and open the connection
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
            // If the code didn't successfully get the weather data, there's no point in attemping
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
     * <p/>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private void getQuestionsListFromJson(String questionAnswerInfoJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String QUESTION_ANSWER_OBJECT = "posts";
//        final String QUESTION_ANSWER_OBJECT_COUNT= "count";
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
//            int count = questionAnswerInfoJson.getInt(QUESTION_ANSWER_OBJECT_COUNT);
            pageCount = questionAnswerInfoJson.getInt("pages");
            // Insert the new weather information into the database
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
                PostValues.put( SQL_INSERT_OR_REPLACE, true );
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_TITLE, question);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_CONTENT, answer);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_CATEGORY, category);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_TAG, tag);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_MODIFIED, modifiedDate);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_URL, url);
                PostValues.put(BlogPostContract.BlogPostEntry.COLUMN_POST_ATTACHMENTS, attachments);

                cVVector.add(PostValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = this.getContentResolver().bulkInsert(BlogPostContract.BlogPostEntry.CONTENT_URI, cvArray);
            }

            Log.w(LOG_TAG, "FetchingPosts Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
