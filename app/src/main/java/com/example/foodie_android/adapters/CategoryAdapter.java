package com.example.foodie_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie_android.R;
import com.example.foodie_android.models.CategoryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context mContext;
    private ArrayList<CategoryItem> mCategoryItemList;
    private OnCategoryClickListener mListener;

    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }

    public void setOnCategoryListener(OnCategoryClickListener listener) {
        mListener = listener;
    }

    public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryItemList) {
        mContext = context;
        mCategoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem currentItem = mCategoryItemList.get(position);
        String ID = currentItem.getID();
        String name = currentItem.getName();
        String imageUrl = currentItem.getImageUrl();

        holder.categoryName.setText(name);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.cardBg);
    }

    @Override
    public int getItemCount() {
        return mCategoryItemList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public ImageView cardBg;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            cardBg = itemView.findViewById(R.id.category_card_bg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onCategoryClick(position);
                        }
                    }
                }
            });
        }
    }



}
