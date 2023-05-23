package com.example.yeartube;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    TextView textView_title;
    TextView textView_sentence;
    TextView textView_age;
    ImageView img_age;
    TextView textView_gpt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        img_age = findViewById(R.id.img_age);
        textView_title = findViewById(R.id.textView_title);
        textView_sentence = findViewById(R.id.textView_sentence);
        textView_age = findViewById(R.id.textView_age);

        String sentence = "[문제가 될 수 있는 문장] \n";
        String title = "[제목] : ";

        Intent intent = getIntent();

        title = title + intent.getStringExtra("title");
        textView_title.setText(title);

        int num_problem_sentences = intent.getIntExtra("num_problem_sentences", 0);

        int age = intent.getIntExtra("age", 0);
        ArrayList<String> sentences = (ArrayList<String>) intent.getStringArrayListExtra("problem_sentences");
        //ArrayList<String> gpts = (ArrayList<String>) intent.getStringArrayListExtra("gpts");
        if (num_problem_sentences == 0) {
            sentence = sentence + " 문제가 될만한 문장이 없음.";
        }
        for (int i = 0; i < num_problem_sentences; i++) {

            sentence = sentence + " " + sentences.get(i) + "\n";
            //gpt = gpt + " " + gpts.get(i) + "\n";

        }

        textView_sentence.setText(sentence);
        //textView_gpt.setText(gpt);

        switch (age) {
            case 0:
                img_age.setImageResource(R.drawable.ageall);
                textView_age.setText("전체 이용가");
                break;
            case 7:
                img_age.setImageResource(R.drawable.age7);
                textView_age.setText("7세 이상 이용 권장");
                break;
            case 12:
                img_age.setImageResource(R.drawable.age12);
                textView_age.setText("12세 이상 이용 권장");
                break;
            default:
                break;
        }


    }
}
