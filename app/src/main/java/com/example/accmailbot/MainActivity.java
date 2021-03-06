package com.example.accmailbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
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

        encryptData("harshsinghrajawat86@gmail.com,sahil777vishwakarma@gmail.com","Test","<h1>This Mail is from AccMailBot</h1><br><b>Hi</b> From Java");
        

        textView.setText(UserDetails.refresh_token);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Data data=new Data.Builder()
                        .putString("Context",MainActivity.this)
                        .build();*/
                Constraints constraints=new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();
                WorkRequest GetRequest=new OneTimeWorkRequest.Builder(HttpRequestWorker.class)
                        .setConstraints(constraints)
                        .addTag("GetData")
                        .build();


                WorkManager.getInstance(MainActivity.this).enqueue(GetRequest);


                /*WorkManager.getInstance(MainActivity.this).enqueue(Request);
                WorkManager.getInstance(MainActivity.this).getWorkInfoByIdLiveData(Request.getId())
                        .observe(MainActivity.this, new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                                if("SUCCEEDED".equals(String.valueOf(workInfo.getState()))){
                                    Log.i("MainActivity: ",""+workInfo.getState());
                                    WorkManager.getInstance(MainActivity.this).enqueue(PostRequest);

                                }else {
                                    Log.i("Token Work Result: ",""+workInfo.getState());
                                }

                            }
                        });*/
            }
        });



    }



}