package com.example.miteshbhai.pp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;


public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

      //  private static String[] answers;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle intent = getActivity().getIntent().getExtras();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
           // answers = Constantvalues.getAnsArray();
           // if(intent!= null && intent.hasExtra(Intent.EXTRA_TEXT)){
               //int ans = intent.getI;
               // String position = "item clicked is at:\t"+intent.getStringExtra(Intent.EXTRA_TEXT);
               // ((TextView)rootView.findViewById(R.id.DetailTextView)).setText(position);

           // }
            if(intent != null){
                int position = intent.getInt("MyItem");
                String answer = Constantvalues.getAnsArray(position);
                ((TextView)rootView.findViewById(R.id.DetailTextView)).setText(answer);
            }


            return rootView;
        }
    }
}
