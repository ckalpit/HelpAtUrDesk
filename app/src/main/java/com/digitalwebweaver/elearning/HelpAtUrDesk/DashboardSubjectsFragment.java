package com.digitalwebweaver.elearning.HelpAtUrDesk;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalwebweaver.elearning.HelpAtUrDesk.service.HelpAtUrDeskService;

/**
 * Created by k on 2/28/2015.
 */
public class DashboardSubjectsFragment extends Fragment {

    TextView DistributedSystemTextButton, ParallelProcessingTextButton,BIDMTextButton;
    public DashboardSubjectsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_main, container, false);

        ParallelProcessingTextButton = (TextView)rootView.findViewById(R.id.pp);
        BIDMTextButton = (TextView)rootView.findViewById(R.id.bidmTextButton);
        DistributedSystemTextButton = (TextView)rootView.findViewById(R.id.Ds);
        DistributedSystemTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuestionAnswerActivity.class);
                intent.putExtra("subjectNameSlug","DS");
                startActivity(intent);
            }
        });
        ParallelProcessingTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuestionAnswerActivity.class);
                intent.putExtra("subjectNameSlug","PP");
                startActivity(intent);
            }
        });
        BIDMTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuestionAnswerActivity.class);
                intent.putExtra("subjectNameSlug","BIDM");
                startActivity(intent);
            }
        });
//        FetchPostsTask questionsTask = new FetchPostsTask(getActivity());
//        questionsTask.execute("4");
        updatePostsDB();
        return rootView;
    }
    private void updatePostsDB(){
        Intent intent = new Intent(getActivity(), HelpAtUrDeskService.class);
        intent.putExtra(HelpAtUrDeskService.NUMBER_OF_POSTS_EXTRA, "4");//4 is number of post you want to fetch per call
        getActivity().startService(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}