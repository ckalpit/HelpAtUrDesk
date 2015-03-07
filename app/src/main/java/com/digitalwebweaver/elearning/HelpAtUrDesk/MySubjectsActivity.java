package com.digitalwebweaver.elearning.HelpAtUrDesk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MySubjectsActivity extends Activity {

    TextView DistributedSystemTextButton, ParallelProcessingTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subjects);
        ParallelProcessingTextButton = (TextView) findViewById(R.id.pp);
        DistributedSystemTextButton = (TextView) findViewById(R.id.Ds);
        DistributedSystemTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = getTaskId();
                Intent intent = new Intent(getApplicationContext(), QuestionAnswerActivity.class);
                startActivity(intent);
            }
        });

    }

}
