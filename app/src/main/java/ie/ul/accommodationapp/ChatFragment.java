package ie.ul.accommodationapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.ul.accommodationapp.Adapters.MessageAdapter;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * */
public class ChatFragment extends Fragment {

    private Toolbar mToolbar;
    private View MessageView;
    private FirebaseAuth mAuth;
    private String messageReceiverID;
    private String messageReceiverName;
    private String messageSenderID;
    private DatabaseReference rootRef;
    private ImageButton SendMessageButton;
    private EditText MessageInputText;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager LinearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get bundle values in onCreate not onCreateView
        Bundle chatBundle = this.getArguments();
        //Data validation on the bundle to check whether data was passed or not.
        if (chatBundle != null) {
            messageReceiverID = chatBundle.getString("uid");
            messageReceiverName = chatBundle.getString("userName");
            //need to get one of the users house images too if one exists
            // need to find out if an account has already been created for this user - if from inbox they definitely have
        }
        System.out.println("userid " + messageReceiverID);
        System.out.println("username " + messageReceiverName);
        Toast.makeText(getActivity(), messageReceiverID, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "name: " + messageReceiverName, Toast.LENGTH_SHORT).show();

        //get the user of this app's ID
        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();


        //initialise the Firebase RTDB connection
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MessageView = inflater.inflate(R.layout.fragment_chat, container, false);

        SendMessageButton = (ImageButton) MessageView.findViewById(R.id.send_message_btn);
        MessageInputText = (EditText) MessageView.findViewById(R.id.messageBox);

        //setting up the recycler view for displaying messages
        messageAdapter = new MessageAdapter(messagesList);
        recyclerView = (RecyclerView) MessageView.findViewById(R.id.messages_list_of_users);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext() ));
        recyclerView.setAdapter(messageAdapter);

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });
        /*//needed for the toolbar
        mToolbar = getActivity().findViewById(R.id.main_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }      //Different toolbar on top, have the name and image up there instead to show who you're chatting to
        });*/

        return MessageView;
    }


    @Override
    public void onStart() {
        super.onStart();

        rootRef.child("Messages").child(messageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messagesList.add(messages);
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void SendMessage(){
        String messageText = MessageInputText.getText().toString();
        if (TextUtils.isEmpty(messageText)){
            Toast.makeText(getActivity(), "First, write a message...", Toast.LENGTH_SHORT).show();
        } else {
            String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

            //random key for each message
            //this creates a key
            DatabaseReference userMessageKeyRef = rootRef.child("Messages").child(messageSenderID).child(messageReceiverID).push();
            String messagePushID = userMessageKeyRef.getKey();

            //storing the details of the message that has been send
            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("from", messageSenderID);

            //adding data to both the sender and receivers nosql item
            Map messageBodyDetails = new HashMap();
            //duplication for both the sender and receiver - nosql data duplication req.
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(), "Message sent successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                    //set textbox back empty after a message has been sent
                    MessageInputText.setText("");
                }
            });
        }


    }
}
