package com.digitalwebweaver.elearning.HelpAtUrDesk;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by k on 3/6/2015.
 */
public class QuestionsAdapter extends CursorAdapter {
    public QuestionsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_questions, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView QuestionView, QuestionType, dateModified;
        QuestionView = (TextView) view.findViewById(R.id.questionView);
        QuestionType = (TextView) view.findViewById(R.id.questionTag);
        dateModified = (TextView) view.findViewById(R.id.dateModified);
        QuestionView.setText(cursor.getString(QuestionsFragment.COL_QUESTION));
        QuestionType.setText(cursor.getString(QuestionsFragment.COL_QUESTION_TAG));
        dateModified.setText(cursor.getString(QuestionsFragment.COL_MODIFIED_DATE));
    }
}
