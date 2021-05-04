package com.example.foodie_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodie_android.adapters.CategoryAdapter;
import com.example.foodie_android.models.CategoryItem;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView mRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    private ArrayList<CategoryItem> mCategoryItemList;
    private RequestQueue mRequestQueue;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Recyclerview stuff
        mRecyclerView = view.findViewById(R.id.explore_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mCategoryItemList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();
        // Inflate the layout for this fragment
        return view;
    }

    private void parseJSON() {
        String url = "https://www.themealdb.com/api/json/v1/1/categories.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject category = jsonArray.getJSONObject(i);
                        String id = category.getString("idCategory");
                        String name = category.getString("strCategory");
                        String imageURL = category.getString("strCategoryThumb");
                        mCategoryItemList.add(new CategoryItem(id, name, imageURL));
                    }
                    mCategoryAdapter = new CategoryAdapter(getActivity(), mCategoryItemList);
                    mRecyclerView.setAdapter(mCategoryAdapter);
                    mCategoryAdapter.setOnCategoryListener(HomeFragment.this);
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
    public void onCategoryClick(int position) {
        CategoryItem categoryItem = mCategoryItemList.get(position);
        Fragment fragment = CategoryFragment.newInstance(categoryItem.getName());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}