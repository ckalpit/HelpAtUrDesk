package com.digitalwebweaver.elearning.HelpAtUrDesk;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.digitalwebweaver.elearning.HelpAtUrDesk.data.BlogPostContract.BlogPostEntry;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle intent = getIntent().getExtras();
            Bundle args = new Bundle();
            args.putString("SELECTED_QUESTION", intent.getString("SELECTED_QUESTION"));

            AnswerFragment fragment = new AnswerFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.answer_detail_container, fragment)
                    .commit();

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AnswerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        public String questionId;

        String ANSWER_SHARE_HASHTAG = "#HelpAtUrDesk";

        private ShareActionProvider mShareActionProvider;
        private static final int ANSWER_LOADER = 0;
        private String mAnswerView;
        private String postUrl;


        private static final String[] ANSWER_VIEW_COLUMNS = {
                BlogPostEntry._ID,
                BlogPostEntry.COLUMN_POST_TITLE,
                BlogPostEntry.COLUMN_POST_CONTENT,
                BlogPostEntry.COLUMN_POST_URL
        };
        private static final int COL_POST_ID = 0;
        private static final int COL_POST_TILE = 1;
        private static final int COL_POST_CONTENT = 2;
        private static final int COL_POST_URL = 3;

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            getLoaderManager().initLoader(ANSWER_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        public AnswerFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                questionId = arguments.getString("SELECTED_QUESTION");
            }
            View rootView = inflater.inflate(R.layout.fragment_detail_web_view, container, false);

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);

            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set/change the share intent.
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            // Attach an intent to this ShareActionProvider.  You can update this at any time,
            // like when the user selects a new piece of data they might like to share.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d("Detail activity", "Share Action Provider is null?");
            }
        }

        private Intent createShareForecastIntent() {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Find answer to all GTU related question,one of them is here given below \n" +
                    postUrl + "\n" + ANSWER_SHARE_HASHTAG);
            return shareIntent;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            Intent intent = getActivity().getIntent();
            if ((intent == null || intent.getData() == null) && questionId == null) {
                return null;
            } else {
                Uri postUri = BlogPostEntry.CONTENT_URI;
                return new CursorLoader(getActivity(),
                        postUri,
                        ANSWER_VIEW_COLUMNS,
                        BlogPostEntry._ID + " = ?",
                        new String[]{questionId},
                        null);
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Log.v("AnswersFragment", "In onLoadFinished");
            if (!data.moveToFirst()) {
                return;
            }
            String question = data.getString(COL_POST_TILE);
            String answer = data.getString(COL_POST_CONTENT);
            postUrl = data.getString(COL_POST_URL);
            mAnswerView = "<strong>Question:</strong> " + question + "<br><br><strong>Answer:</strong><br>" + answer;
            try {
                WebView myWebView = (WebView) getView().findViewById(R.id.answerWebView);
                myWebView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return true;
                    }
                });

                myWebView.setClickable(false);

                myWebView.loadDataWithBaseURL(null, mAnswerView, "text/html", "utf-8", null);
                if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareForecastIntent());
                }
            }catch (NullPointerException e){
                e.printStackTrace();
                Log.d("Detail Activity","Web View null pointer exception");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
