package com.example.accmailbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.WorkInfo;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

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
        AsyncPostData Task1=new AsyncPostData();
        //Task.execute();Task1.execute();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data data=new Data.Builder()
                        .putString("RawData",UserDetails.raw_data)
                        .build();
                Constraints constraints=new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();
                WorkRequest Request=new OneTimeWorkRequest.Builder(HttpGetWorker.class)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .addTag("getToken")
                        .build();
                Data data1=new Data.Builder()
                        .putString("token",UserDetails.access_token)
                        .build();
                Constraints constraints1=new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();
                WorkRequest PostRequest=new OneTimeWorkRequest.Builder(HttpPostWorker.class)
                        .setInputData(data1)
                        .setConstraints(constraints1)
                        .addTag("PostData")
                        .build();


                WorkManager.getInstance(MainActivity.this).enqueue(PostRequest);


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