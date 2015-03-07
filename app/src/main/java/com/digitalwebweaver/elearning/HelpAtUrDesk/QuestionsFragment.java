package com.digitalwebweaver.elearning.HelpAtUrDesk;


import android.content.Intent;
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


    private QuestionsAdapter mQuestionAdapter;
    private String subjectSlug;

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
        final ListView listView = (ListView) rootView.findViewById(R.id.ListView_Questions);
        listView.setAdapter(mQuestionAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("SELECTED_QUESTION", cursor.getString(COL_QUESTION_ID));
                startActivity(intent);
            }
        });
        this.setHasOptionsMenu(true);
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        //  updateQuestions();
    }

    private void updateQuestions() {
        FetchPostsTask questionsTask = new FetchPostsTask(getActivity());
        questionsTask.execute("4");
    }

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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuestionAdapter.swapCursor(null);
    }
}


