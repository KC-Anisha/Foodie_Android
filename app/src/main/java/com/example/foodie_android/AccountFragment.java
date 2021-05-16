package com.example.foodie_android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.foodie_android.adapters.CategoryMealAdapter;
import com.example.foodie_android.models.CategoryMealItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment implements CategoryMealAdapter.OnCategoryMealClickListener {

    private RecyclerView mRecyclerView;
    private CategoryMealAdapter mCategoryAdapter;
    private ArrayList<CategoryMealItem> mCategoryItemList;
    private RequestQueue mRequestQueue;

    Button logoutBtn;
    ImageView profileImage;
    TextView greeting;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public AccountFragment() {
        // Required empty public constructor
    }


    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Logout
        logoutBtn = view.findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        // Greeting
        greeting = view.findViewById(R.id.account_greeting);
        String fullName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String[] split = fullName.split(" ");
        String firstName = split[0];
        greeting.setText("Hi " + firstName + "!");

        // Profile Picture
        profileImage = view.findViewById(R.id.account_img);
        Glide.with(this)
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.profile_placeholder).circleCrop()
                .error(R.drawable.profile_placeholder).circleCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop()
                .into(profileImage);

        // Recyclerview stuff
        mRecyclerView = view.findViewById(R.id.savedMeals_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCategoryItemList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getActivity());

        // Populate Saved Meals RecyclerView
        DocumentReference userDbInfo = db.collection("users").document(userUID);
        userDbInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> savedMeals = (List<String>) document.get("savedMeals");
                        for (String mealID: savedMeals) {
                            parseJSON(mealID);
                        }
//                        mCategoryAdapter = new CategoryMealAdapter(getActivity(), mCategoryItemList);
//                        mRecyclerView.setAdapter(mCategoryAdapter);
//                        mCategoryAdapter.setOnCategoryMealListener(AccountFragment.this);
                    } else {
                        Log.d("ACCOUNT", "No such document");
                    }
                } else {
                    Log.d("ACCOUNT", "get failed with ", task.getException());
                }
            }
        });


        return view;
    }

    private void parseJSON(String mealID) {
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + mealID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("meals");
                    JSONObject category = jsonArray.getJSONObject(0);
                    String id = category.getString("idMeal");
                    String name = category.getString("strMeal");
                    String imageURL = category.getString("strMealThumb");
                    mCategoryItemList.add(new CategoryMealItem(id, name, imageURL));
                    mCategoryAdapter = new CategoryMealAdapter(getActivity(), mCategoryItemList);
                    mRecyclerView.setAdapter(mCategoryAdapter);
                    mCategoryAdapter.setOnCategoryMealListener(AccountFragment.this);
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