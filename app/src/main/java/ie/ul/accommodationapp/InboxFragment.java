package ie.ul.accommodationapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ie.ul.accommodationapp.Adapters.ConversationAdapter;

import static com.firebase.ui.auth.AuthUI.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    private Toolbar mToolbar;


    private RecyclerView conversationList;
    private ConversationAdapter conversationAdapter;
    private ArrayList<Conversation> convoList;
    private DatabaseReference contactRef;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private View PrivateChatsView;
    private String currentUserId;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        contactRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        //inflate the layout for this fragment
        PrivateChatsView = inflater.inflate(R.layout.fragment_inbox, container, false);

        conversationList = (RecyclerView) PrivateChatsView.findViewById(R.id.inbox_recyclerview);
        conversationList.setLayoutManager(new LinearLayoutManager( getContext() ));

        //needed for the toolbar
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitle("Inbox");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return PrivateChatsView;
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Conversation> options =
                new FirebaseRecyclerOptions.Builder<Conversation>()
                .setQuery(contactRef, Conversation.class)
                .build();

        FirebaseRecyclerAdapter<Conversation, ConversationViewHolder> adapter =
                new FirebaseRecyclerAdapter<Conversation, ConversationViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ConversationViewHolder conversationViewHolder, int i, @NonNull Conversation conversation) {
                        final String userIDs = getRef(i).getKey();

                        usersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    //check for profile image not all users have house images
                                    if (dataSnapshot.hasChild("image")) {
                                        final String returnedImage = dataSnapshot.child("image").getValue().toString();
                                        if (! (returnedImage.equals(""))){
                                            //add image to screen here
                                            Picasso.get().load(returnedImage).into(conversationViewHolder.profileImage);
                                        }


                                    }
                                    final String returnedName = dataSnapshot.child("name").getValue().toString();
                                    conversationViewHolder.userName.setText(returnedName);

                                    //if desire to add the 'last seen' feature that could be added here

                                    conversationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //this will get the current item from the recycler view that is clicked
                                            //now fetch information relating to this item
                                            //investigate potential of adding custom onClick color to the item that is clicked
                                            Bundle bundle = new Bundle();
                                            bundle.putString("uid",userIDs); //string key - pair
                                            bundle.putString("userName",returnedName); //string key - pair
                                            Navigation.findNavController(v).navigate(R.id.action_inboxFragment_to_chatFragment,bundle);
                                        }
                                    });

                                }//if the data doesnt exist dont adding anything to the page - add future later if no messages add a textview telling you no messages exist
                            } 

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout, parent, false);
                        return new ConversationViewHolder(view);
                    }
                };
        conversationList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImage;
        TextView userName;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.users_profile_image);
            userName = itemView.findViewById(R.id.user_profile_name);

        }
    }
}
