package com.example.foodie_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodie_android.adapters.CategoryMealAdapter;
import com.example.foodie_android.models.CategoryMealItem;
import com.example.foodie_android.models.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MealDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "id";
    private String id;
    private RequestQueue mRequestQueue;
    Meal meal;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView title;
    TextView category;
    TextView area;
    TextView tags;
    WebView video;
    TextView ing;
    TextView instructions;
    TextView source;
    Button saveMealBtn;

    public MealDetailFragment() {
        // Required empty public constructor
    }

    public static MealDetailFragment newInstance(String id) {
        MealDetailFragment fragment = new MealDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_detail, container, false);

        // Get views
        title = view.findViewById(R.id.meal_title);
        category = view.findViewById(R.id.category);
        area = view.findViewById(R.id.area);
        tags = view.findViewById(R.id.tags);
        video = view.findViewById(R.id.meal_video);
        ing = view.findViewById(R.id.ingridients_tv);
        instructions = view.findViewById(R.id.instructions_tv);
        source = view.findViewById(R.id.source_tv);

        // Parse JSON and set views
        mRequestQueue = Volley.newRequestQueue(getActivity());
        parseJSON();

        // Save meal
        saveMealBtn = view.findViewById(R.id.save_meal_btn);
        saveMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MEALID", "MEAL TO BE SAVED: " + meal.getID());
                DocumentReference userDBInfo = db.collection("users").document(userUID);
                userDBInfo.update("savedMeals", FieldValue.arrayUnion(meal.getID()));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void parseJSON() {
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("meals");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject mealObj = jsonArray.getJSONObject(i);
                        String id = mealObj.getString("idMeal");
                        String name = mealObj.getString("strMeal");
                        String category = mealObj.getString("strCategory");
                        String area = mealObj.getString("strArea");
                        String instructions = mealObj.getString("strInstructions");
                        String tags = mealObj.getString("strTags");
                        String url = mealObj.getString("strYoutube");
                        HashMap<String, String> ingridients = new HashMap<>();
                        for (int j = 1; j < 20; j++) {
                            String temp = mealObj.getString("strIngredient"+j);
                            if (!temp.isEmpty()) {
                                String temp2 = mealObj.getString("strMeasure"+j);
                                ingridients.put(temp, temp2);
                            }
                        }
                        String source = mealObj.getString("strSource");
                        meal = new Meal(id, name, category, area, instructions, tags, url, ingridients, source);
                        setView(meal);
                    }
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

    private void setView(Meal meal) {
        title.setText(meal.getName());
        category.setText(meal.getCategory());
        area.setText(meal.getArea());
        if (meal.getTags() != null && !meal.getTags().isEmpty() && !meal.getTags().equalsIgnoreCase("null")) {
            tags.setText(meal.getTags());
        }
        instructions.setText(meal.getInstructions());
        if (meal.getSource() != null || !meal.getSource().isEmpty()) {
            source.setClickable(true);
            source.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='" + meal.getSource() + "'> Source </a>";
            source.setText(Html.fromHtml(text));
        }
        String ingredients= "";
        for (Map.Entry<String,String> item : meal.getIngredients().entrySet()) {
            ingredients = ingredients.concat(item.getKey() + " (" + item.getValue() + ") \n");
        }
        ing.setText(ingredients);
        String videoID = meal.getVideoURL().substring(meal.getVideoURL().lastIndexOf("=") + 1);

        video.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings = video.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        video.loadUrl("https://www.youtube.com/embed/" + videoID);
    }
}