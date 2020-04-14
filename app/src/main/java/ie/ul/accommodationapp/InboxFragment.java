package ie.ul.accommodationapp;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ie.ul.accommodationapp.Adapters.ConversationAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    private Toolbar mToolbar;

    private RecyclerView recyclerview;
    private ConversationAdapter conversationAdapter;
    private ArrayList<Conversation> convoList;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inititialise the arraylist and get data to fill it
        //this will be data coming from firebase 
        convoList = new ArrayList<Conversation>();
        convoList.add(new Conversation("user1", "2019-04-14 16:19:22"));
        convoList.add(new Conversation("user2", "2019-04-14 15:19:22"));
        convoList.add(new Conversation("user3", "2019-04-14 14:19:22"));

        for (Conversation o : convoList) {
            System.out.println("user:" + o.getUser() + " " + o.getTime()); //test print
        } //prints this

        //inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        conversationAdapter = new ConversationAdapter(convoList);

        //adding the adapter to the recycling view
        recyclerview.setAdapter(conversationAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager( getActivity() ));  //getActivity - gets the fragments activity

        //needed for the toolbar
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

}
