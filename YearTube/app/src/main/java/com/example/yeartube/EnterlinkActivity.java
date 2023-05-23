package com.example.yeartube;

import static java.sql.DriverManager.println;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EnterlinkActivity extends AppCompatActivity {


    Button btn_send_link;
    EditText edit_youtube_link;
    String text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterlink_activity);

        edit_youtube_link = (EditText) findViewById(R.id.edit_youtube_link);
        //edit_youtube_link.setText("https://www.youtube.com/shorts/FBq8KJmn4g0");
        btn_send_link = (Button) findViewById(R.id.btn_send_link);
        text = edit_youtube_link.getText().toString();

        btn_send_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoadingScreenActivity.class);
                intent.putExtra("text", text);
                startActivity(intent);
            }
        });


    }


}

