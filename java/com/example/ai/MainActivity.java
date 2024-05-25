package com.example.ai;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.generateimageusingchatapiexample.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<ImageModel> imageList;
    Adapter adapter;
    ImageModel imageModel1;

    String gg;
    String url;
    //String url = "https://api.openai.com/v1/images/generations";
    String accesstoken = "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";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList = new ArrayList<>();
        // set up recyclerview
        adapter = new Adapter(imageList, MainActivity.this);
        binding.recyclerView.setAdapter(adapter);
        GridLayoutManager ggm = new GridLayoutManager(MainActivity.this, 2);
        binding.recyclerView.setLayoutManager(ggm);

        binding.btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = binding.editText.getText().toString().trim();
                gg = question;
                url = "https://lexica.art/api/v1/search?q="+gg;
//               url = url+question;
                if(!question.equals("")){
                    Toast.makeText(MainActivity.this, "Message is send", Toast.LENGTH_SHORT).show();
                    callApi(gg);
                }else {
                    Toast.makeText(MainActivity.this, "Please enter image title", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void callApi(String questiono) {

        // Request a string response from the provided URL.
//        imageModel.add(new MessageModelClass("Typing....", MessageModelClass.SENT_BY_GPT));
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONArray jsonArray ;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("images");
                    Toast.makeText(MainActivity.this, " inside the api call ", Toast.LENGTH_SHORT).show();
                    Log.e("responseData", response.toString());
                    Log.e("ImageUrl", jsonArray.getJSONObject(0).getString("srcSmall"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        imageModel1 = new ImageModel(jsonArray.getJSONObject(i).getString("srcSmall"));
                        imageList.add(imageModel1);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


//                for (int i = 0; i < jsonArray.length(); i++) {
//                    try {
//                        imageModel1 = new ImageModel(jsonArray.getJSONObject(i).getString("srcSmall"));
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    imageList.add(imageModel1);
//                }

                binding.progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("onErrorResponse", error.toString());
            }
        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String , String> map = new HashMap<>();
//
//                map.put("q",questiono);
//                return map;
//            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        request.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 10000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 10000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
        requestQueue.add(request);
    }
}
