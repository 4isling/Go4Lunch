package com.exemple.go4lunch.ui.workmate;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.exemple.go4lunch.R;
import com.exemple.go4lunch.model.workmate.Workmate;
import com.exemple.go4lunch.model.workmate.WorkmateStateItem;
import com.exemple.go4lunch.ui.restaurant.RestaurantAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class WorkmateAdapter extends ListAdapter<WorkmateStateItem, WorkmateAdapter.ViewHolder> {

    List<WorkmateStateItem> workmatesList;
    public WorkmateAdapter(){
        super(new ListWorkmateItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_workmate,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<WorkmateStateItem> workmatesList){
        this.workmatesList = workmatesList;
    }

    private static class ListWorkmateItemCallback extends DiffUtil.ItemCallback<WorkmateStateItem> {
        @Override
        public boolean areItemsTheSame(@NonNull WorkmateStateItem oldItem, @NonNull WorkmateStateItem newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getAvatarUrl().equals(newItem.getAvatarUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WorkmateStateItem oldItem, @NonNull WorkmateStateItem newItem) {
            return oldItem.equals(newItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView avatarImageView;
        private final TextView nameTextView;
        private final TextView typeOfFoodTextView;
        private final TextView restaurantNameTextView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            avatarImageView =(ImageView) itemView.findViewById(R.id.workmate_item_avatar);
            nameTextView = (TextView) itemView.findViewById(R.id.workmate_item_name);
            typeOfFoodTextView =(TextView) itemView.findViewById(R.id.workmate_item_type_food);
            restaurantNameTextView = (TextView) itemView.findViewById(R.id.workmate_item_restaurent);
        }

        public void bind(WorkmateStateItem item){
            nameTextView.setText(item.getName());
            typeOfFoodTextView.setText(item.getTypeOfFood());
            restaurantNameTextView.setText(item.getRestaurantName());
            Glide.with(avatarImageView)
                    .load(item.getAvatarUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView);
        }
    }
}
