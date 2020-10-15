package com.risahabhapps.zomatoapiinternshala.Adapters;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.risahabhapps.zomatoapiinternshala.POJO.RestautantPOJO.Restaurant;
import com.risahabhapps.zomatoapiinternshala.R;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.FinalorderViewHolder>{
    Animation slideleftminus;
    Context context;
    List<Restaurant> data;
    public static String oid;
    public RestaurantAdapter(Context context, List<Restaurant> data){
        this.context=context;
        this.data=data;
    }

    @Override
    public FinalorderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.restaurant_item_layout,parent,false);
        return new FinalorderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FinalorderViewHolder holder, int position) {
        final Restaurant restaurantList= data.get(position);

        slideleftminus = AnimationUtils.loadAnimation(context,
                R.anim.slideleftminus);

        holder.background.startAnimation(slideleftminus);
        holder.name.setText(restaurantList.getRestaurant().getName());
        holder.cousine.setText(restaurantList.getRestaurant().getCuisines());
        holder.price.setText(restaurantList.getRestaurant().getCurrency()+restaurantList.getRestaurant().getAverageCostForTwo()+" for two");
        Glide.with(context)
                .load(restaurantList.getRestaurant().getFeaturedImage())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        try{return data.size();}
        catch (Exception e){
        }
        return 0;
    }

    public static class FinalorderViewHolder extends RecyclerView.ViewHolder {
        TextView name,cousine,price;
        ImageView image;
        LinearLayout background;
        public FinalorderViewHolder(View itemView) {
            super(itemView);
            background=itemView.findViewById(R.id.background);
            name=itemView.findViewById(R.id.name);
            cousine=itemView.findViewById(R.id.cousine);
            price=itemView.findViewById(R.id.price);
            image=itemView.findViewById(R.id.image);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setClipToOutline(true);
            }
        }
    }
}