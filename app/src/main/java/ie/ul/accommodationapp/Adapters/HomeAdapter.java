package ie.ul.accommodationapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.ul.accommodationapp.Listing;
import ie.ul.accommodationapp.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> implements Filterable {

    private List<Listing> mData;
    private List<Listing> mDataFiltered;
    private Context mContext;

    public HomeAdapter(Context mContext, List<Listing> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.setTitleText(mDataFiltered.get(position).getPrice() + "");
        holder.setDescriptionTextView(mDataFiltered.get(position).getDescription());
        holder.setPriorityTextView(mDataFiltered.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listing listing = mDataFiltered.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("listingModel", listing);
                try {
                    Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_houseDetailsFragment2, bundle);
                } catch (Exception e) {
                    Navigation.findNavController(v).navigate(R.id.action_likedAdsFragment_to_houseDetailsFragment3, bundle);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if (key.isEmpty()) {
                    mDataFiltered = mData;
                } else {
                    List<Listing> listFiltered = new ArrayList<>();
                    for (Listing listingRow : mData) {
                        if (listingRow.getAddress().toLowerCase().contains(key.toLowerCase())
                                || listingRow.getDescription().contains(key.toLowerCase())) {
                            listFiltered.add(listingRow);
                        }
                    }
                    mDataFiltered = listFiltered;
                }






                FilterResults filterResults = new FilterResults();
                filterResults.values=mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (List<Listing>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView, descriptionTextView, priorityTextView;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            descriptionTextView = itemView.findViewById(R.id.text_view_description);
            priorityTextView = itemView.findViewById(R.id.text_view_priority);
        }

        public void setTitleText(String text) {
            titleTextView.setText(text);
        }

        public void setDescriptionTextView(String text) {
            descriptionTextView.setText(text);
        }

        public void setPriorityTextView(String text) {
            priorityTextView.setText(text);
        }
    }


}
