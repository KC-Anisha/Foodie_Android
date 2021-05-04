package com.example.foodie_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie_android.R;
import com.example.foodie_android.models.CategoryItem;
import com.example.foodie_android.models.CategoryMealItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryMealAdapter extends RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder> {
    private Context mContext;
    private ArrayList<CategoryMealItem> mCategoryItemList;
    private OnCategoryMealClickListener mListener;

    public interface OnCategoryMealClickListener {
        void onCategoryMealClick(int position);
    }

    public void setOnCategoryMealListener(OnCategoryMealClickListener listener) {
        mListener = listener;
    }

    public CategoryMealAdapter(Context context, ArrayList<CategoryMealItem> categoryMealItemList) {
        mContext = context;
        mCategoryItemList = categoryMealItemList;
    }

    @NonNull
    @Override
    public CategoryMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.categorymeal_item, parent, false);
        return new CategoryMealViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryMealViewHolder holder, int position) {
        CategoryMealItem currentItem = mCategoryItemList.get(position);
        String ID = currentItem.getID();
        String name = currentItem.getName();
        String imageUrl = currentItem.getImageUrl();

        holder.categoryName.setText(name);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mealImage);
    }

    @Override
    public int getItemCount() {
        return mCategoryItemList.size();
    }

    public class CategoryMealViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public ImageView mealImage;
        public String ID;

        public CategoryMealViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryMealName);
            mealImage = itemView.findViewById(R.id.categoryMealPic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onCategoryMealClick(position);
                        }
                    }
                }
            });
        }
    }
}
