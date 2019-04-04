package com.example.musicforlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent=getIntent();
        String message=intent.getStringExtra(MainActivity.TEST_MESSAGE);
        TextView textView=findViewById(R.id.txtTest);
        textView.setText(message);

    }
}
