package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OfferAdapter adapter;
    private List<Offer> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        recyclerView = findViewById(R.id.offers_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the offer list
        offerList = new ArrayList<>();

        adapter = new OfferAdapter(this, offerList);
        recyclerView.setAdapter(adapter);

        loadOffers();
    }

    private void loadOffers() {
        String url = Constants.BASE_URL + "getAllOffers.php";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API Response", response);
                offerList.clear();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String title = object.getString("title");
                        String description = object.getString("description");
                        String validUntil = object.getString("valid_until");
                        Offer offer = new Offer(title, description, validUntil);
                        offerList.add(offer);
                    }
                    adapter.notifyDataSetChanged(); // Notify adapter about data changes
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(OffersActivity.this, "Error parsing offers: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OffersActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(OffersActivity.this).add(stringRequest);
    }

    public void onClickFabBackMyCustomers(View v) { // Navigate back to the MainActivity when the floating action button is clicked
        Intent intent = new Intent(OffersActivity.this, UserHomeActivity.class);
        startActivity(intent);
    }
}