package ie.ul.accommodationapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ie.ul.accommodationapp.Conversation;
import ie.ul.accommodationapp.R;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private ArrayList<Conversation> conversationList;

    //constructor    --  you can change the way the data is added
    public ConversationAdapter(ArrayList<Conversation> convos){
        this.conversationList = convos;
    }

    @NonNull
    @Override
    public ConversationAdapter.ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get the context from the viewgroup paramter
        Context context = parent.getContext();
        //check our layout inflater from the context
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate this layout using the language layout
        //LanguageLayout - tells us how the viewholder is going to display each item
        View convoView = inflater.inflate(R.layout.conversation_layout,parent, false);

        //creates a new view holder and passes in the view from above
        ConversationViewHolder viewHolder = new ConversationViewHolder(convoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.ConversationViewHolder holder, int position) {
        //this is where you retrive data from the arraylist of conversations
        /*String name = conversationList.get(position).getUser();
        String date = conversationList.get(position).getTime();

        //then you add it to the textviews that you refer to below
        holder.txtName.setText(name);
        holder.txtDate.setText(date); */
    }

    @Override
    public int getItemCount() {
        return conversationList.size(); // return whatever you are passing that is storing the data
    }

    //this is a view holder and it is needed for extending RecyclerView Adapter (as adapter needs a VH)
    public class ConversationViewHolder extends RecyclerView.ViewHolder{

        ///adding textviews here
        TextView txtName;
        TextView txtDate;

        public ConversationViewHolder(@NonNull View itemView){
            super(itemView);

            //refer to the xml id and add it to the textview you created
            txtName = itemView.findViewById(R.id.name);
            txtDate = itemView.findViewById(R.id.date);
        }
    }//end of viewholder
}
