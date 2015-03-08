package com.digitalwebweaver.elearning.HelpAtUrDesk;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.digitalwebweaver.elearning.HelpAtUrDesk.data.BlogPostContract.BlogPostEntry;


public class QuestionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int QUESTIONS_LOADER = 0;
    public String questionId;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";
    private static final String[] QUESTION_VIEW_COLUMNS = {
            BlogPostEntry.TABLE_NAME + "." + BlogPostEntry._ID,
            BlogPostEntry.COLUMN_POST_TITLE,
            BlogPostEntry.COLUMN_POST_MODIFIED,
            BlogPostEntry.COLUMN_POST_TAG
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    static final int COL_QUESTION_ID = 0;
    static final int COL_QUESTION = 1;
    static final int COL_MODIFIED_DATE = 2;
    static final int COL_QUESTION_TAG = 3;

    ListView mListView;
    private QuestionsAdapter mQuestionAdapter;
    private String subjectSlug;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    public QuestionsFragment() {
        setHasOptionsMenu(true);
    }

    public static String getANSWERS(int position) {
//        return ANSWERS[questionId];
        return "1";
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(QUESTIONS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        List<String> Questions = new ArrayList<String>(Arrays.asList(question));
        Bundle intent = getActivity().getIntent().getExtras();
        if (intent.containsKey("subjectNameSlug")) {
            subjectSlug = intent.getString("subjectNameSlug");
            getActivity().setTitle("Questions List (" + subjectSlug + ")");
        }
        mQuestionAdapter = new QuestionsAdapter(getActivity(), null, 0);
        mListView = (ListView) rootView.findViewById(R.id.ListView_Questions);
        mListView.setAdapter(mQuestionAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                questionId = cursor.getString(COL_QUESTION_ID);
                ((Callback) getActivity()).onItemSelected(questionId);
                mPosition = i;

            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //  updateQuestions();
    }

//    private void updateQuestions() {
//        FetchPostsTask questionsTask = new FetchPostsTask(getActivity());
//        questionsTask.execute("4");
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.questionsfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
//            FetchQuestionsTask weatherTask = new FetchQuestionsTask();
//            weatherTask.execute("ds");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri postUri = BlogPostEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                postUri,
                QUESTION_VIEW_COLUMNS,
                BlogPostEntry.COLUMN_POST_CATEGORY + " = ?",
                new String[]{subjectSlug},
                BlogPostEntry.COLUMN_POST_MODIFIED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("QuestionFragment", "In onLoadFinished");
        if (!data.moveToFirst()) {
            return;
        }
        mQuestionAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuestionAdapter.swapCursor(null);
    }
}


