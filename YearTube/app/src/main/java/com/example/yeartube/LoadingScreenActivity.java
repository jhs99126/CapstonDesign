package com.example.yeartube;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class LoadingScreenActivity extends AppCompatActivity {

    private TextView textView;
    private ProgressBar progressBar;
    String text;
    String str;
    static RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        progressBar = findViewById(R.id.progress_bar);

        // MainActivity에서 전달된 텍스트 가져오기
        text = getIntent().getStringExtra("text");

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }


        makeRequest();

    }
    public void makeRequest() {
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("url",text);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = String.valueOf(jsonBodyObj.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://267d-117-16-244-19.ngrok-free.app/check/", jsonBodyObj,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        //여기서 response 다루기
                        try {
                            String result = response.getString("result");
                            if (result.equals("y")) {
                                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                                String title = response.getString("title");
                                int num_problem_sentences = response.getInt("num_problem_sentences");
                                int age = response.getInt("age");

                                ArrayList<String> sentences = new ArrayList<String>();
                                ArrayList<String> gpts = new ArrayList<String>();
                                JSONArray jsentences = response.getJSONArray("problem_sentences");
                                //JSONArray jgpts = response.getJSONArray("gpt");
                                for (int i = 0; i < num_problem_sentences; i++) {
                                    Object element = jsentences.get(i);
                                    JSONObject arr = (JSONObject) element;
                                    sentences.add(arr.getString("sentence") + " : \n <" + arr.getString("reason") + ">\n");
                                    //gpts.add(arr.getString("gpt"));
                                }

                                intent.putStringArrayListExtra("problem_sentences", sentences);
                                //intent.putStringArrayListExtra("gpts", gpts);
                                intent.putExtra("title", title);
                                intent.putExtra("age", age);
                                intent.putExtra("num_problem_sentences", num_problem_sentences);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("error response",error.getMessage());

                    }
                }
        );

        request.setShouldCache(false);
        requestQueue.add(request);

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                60000 ,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    @Override
    public void onBackPressed() {
        // 아무런 동작도 하지 않음
    }
}