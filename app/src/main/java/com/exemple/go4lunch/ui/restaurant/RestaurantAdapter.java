package com.exemple.go4lunch.ui.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.go4lunch.R;
import com.exemple.go4lunch.model.restaurant.RestaurantStateItem;

public class RestaurantAdapter extends ListAdapter<RestaurantStateItem, RestaurantAdapter.ViewHolder> {

    public RestaurantAdapter(){
        super(new RestaurantItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_restaurant,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView restaurantName;
        private final TextView restaurantAddress;
        private final TextView restaurantOpenHour;
        private final TextView restaurantFoodType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurant_item_name);
            restaurantAddress = itemView.findViewById(R.id.restaurant_item_address);
            restaurantOpenHour = itemView.findViewById(R.id.restaurant_item_time);
            restaurantFoodType = itemView.findViewById(R.id.restaurant_item_food);

        }

        public void bind(RestaurantStateItem item) {
        }
    }

    private static class RestaurantItemCallback extends DiffUtil.ItemCallback<RestaurantStateItem>{
        @Override
        public boolean areItemsTheSame(@NonNull RestaurantStateItem oldItem, @NonNull RestaurantStateItem newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getGeometry().equals(newItem.getGeometry());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RestaurantStateItem oldItem, @NonNull RestaurantStateItem newItem){
            return oldItem.equals(newItem);
        }
    }
}
