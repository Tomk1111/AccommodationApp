package ie.ul.accommodationapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ie.ul.accommodationapp.Messages;
import ie.ul.accommodationapp.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public MessageAdapter (List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_messages_layout, parent, false);

        //initialise Firebase connections
        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        //retrive and display messages for user
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);
        String fromUserID = messages.getFrom();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //image check on RTDB
                if (! (dataSnapshot.child("image").getValue().toString().equals(""))){
                    String imageReceiver = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(imageReceiver).into(holder.receiverProfileImage);
                }
                else{
                    //do we need picasso here to load the placeholder image -- dont think so - already regereenced in the xml
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Starting to display messages
        holder.receiverMessageText.setVisibility(View.INVISIBLE);
        holder.senderMessageText.setVisibility(View.INVISIBLE);
        holder.receiverProfileImage.setVisibility(View.INVISIBLE);
        //this is the sender - print sender messages
        if (fromUserID.equals(messageSenderId)){
            holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
            holder.senderMessageText.setText(messages.getMessage());
            holder.senderMessageText.setVisibility(View.VISIBLE);
        } else {
            holder.receiverProfileImage.setVisibility(View.VISIBLE);
            holder.receiverMessageText.setVisibility(View.VISIBLE);

            holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
            holder.receiverMessageText.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        //how much messages are available to show
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;

        //using this can initialise the view from the chat layout
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
        }
    }


}
