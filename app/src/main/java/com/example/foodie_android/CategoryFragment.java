package com.example.foodie_android;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodie_android.adapters.CategoryAdapter;
import com.example.foodie_android.adapters.CategoryMealAdapter;
import com.example.foodie_android.models.CategoryItem;
import com.example.foodie_android.models.CategoryMealItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CategoryFragment extends Fragment implements CategoryMealAdapter.OnCategoryMealClickListener {
    private RecyclerView mRecyclerView;
    private CategoryMealAdapter mCategoryAdapter;
    private ArrayList<CategoryMealItem> mCategoryItemList;
    private RequestQueue mRequestQueue;

    private static final String ARG_PARAM1 = "category name";
    private String categoryTitle;

    public static CategoryFragment newInstance(String title) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryTitle = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        TextView title = view.findViewById(R.id.category_title);
        title.setText(categoryTitle);

        // Recyclerview stuff
        mRecyclerView = view.findViewById(R.id.categorymeals_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryItemList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();

        // Inflate the layout for this fragment
        return view;
    }

    private void parseJSON() {
        String url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=" + categoryTitle;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("meals");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject category = jsonArray.getJSONObject(i);
                        String id = category.getString("idMeal");
                        String name = category.getString("strMeal");
                        String imageURL = category.getString("strMealThumb");
                        mCategoryItemList.add(new CategoryMealItem(id, name, imageURL));
                    }
                    mCategoryAdapter = new CategoryMealAdapter(getActivity(), mCategoryItemList);
                    mRecyclerView.setAdapter(mCategoryAdapter);
                    mCategoryAdapter.setOnCategoryMealListener(CategoryFragment.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onCategoryMealClick(int position) {
        CategoryMealItem categoryMealItem = mCategoryItemList.get(position);
        Fragment fragment = MealDetailFragment.newInstance(categoryMealItem.getID());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}