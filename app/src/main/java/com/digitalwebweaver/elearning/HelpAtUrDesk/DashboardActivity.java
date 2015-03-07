package com.digitalwebweaver.elearning.HelpAtUrDesk;

import android.app.Activity;
import android.os.Bundle;


public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DashboardSubjectsFragment())
                    .commit();
        }
    }
}
