package com.digitalwebweaver.elearning.HelpAtUrDesk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by k on 3/2/2015.
 */
public class QuestionAnswerActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new QuestionsFragment())
                    .commit();
        }
    }

}
