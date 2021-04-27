package com.example.healthcareservice;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<ServiceItem> mServiceItemsData;
    private ArrayList<ServiceItem> mServiceItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    ServiceItemAdapter(Context context, ArrayList<ServiceItem> itemsData) {
        this.mServiceItemsData = itemsData;
        this.mServiceItemsDataAll = itemsData;
        this.mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ServiceItemAdapter.ViewHolder holder, int position) {
        ServiceItem currentItem = mServiceItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return mServiceItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return serviceFilter;
    }

    private Filter serviceFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ServiceItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mServiceItemsDataAll.size();
                results.values = mServiceItemsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(ServiceItem serviceItem : mServiceItemsDataAll) {
                    if(serviceItem.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(serviceItem);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mServiceItemsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameText;
        private TextView mProviderText;
        private TextView mCategoryText;
        private TextView mCommentText;
        private ImageView mItemImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mNameText = itemView.findViewById(R.id.itemName);
            mProviderText = itemView.findViewById(R.id.itemProvider);
            mCategoryText = itemView.findViewById(R.id.itemCategory);
            mCommentText = itemView.findViewById(R.id.itemComment);
            mItemImage = itemView.findViewById(R.id.itemImage);

            itemView.findViewById(R.id.subscribeForService).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Activity", "Add bookmark button clicked!");
                    ((ServiceListActivity)mContext).updateAlertIcon();
                }
            });
        }

        public void bindTo(ServiceItem currentItem) {
            mNameText.setText(currentItem.getName());
            mProviderText.setText(currentItem.getProvidedBy());
            mCategoryText.setText(Arrays.toString(currentItem.getCaterory()));
            mCommentText.setText(currentItem.getComment());

            Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
        }
    }
}
