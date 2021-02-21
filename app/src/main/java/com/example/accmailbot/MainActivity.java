package com.example.accmailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.accmailbot.encryptRawData.encryptData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         TextView textView=(TextView) findViewById(R.id.txt);
         Button button=(Button)findViewById(R.id.button);

        encryptData("harshsinghrajawat86@gmail.com","Test","<h1>Hi</h1> From Java");


        textView.setText(UserDetails.refresh_token);
        AsyncGetToken Task=new AsyncGetToken();
        Task.execute();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncPostData task=new AsyncPostData();
                task.execute();
            }
        });

    }


}