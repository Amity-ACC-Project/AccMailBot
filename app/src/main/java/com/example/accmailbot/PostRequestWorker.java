package com.example.accmailbot;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostRequestWorker extends Worker {
    public PostRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String TAG="PostRequest";
        Log.i(TAG,"PostRequestWorker Running");
        OkHttpClient client = new OkHttpClient();
        URL PostUrl = createURL(UserDetails.PostUrl);
        String SendPost = "{\"raw\":\"" + UserDetails.raw_data + "\"}";


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, SendPost);


        Request PostRequest = new Request.Builder()
                .url(PostUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + UserDetails.access_token)
                .addHeader("Host", "gmail.googleapis.com")
                .addHeader("Content-type", "application/json")
                .build();
        Call PostCall;

        //while (HttpRequestWorker.i <= UserDetails.intervals) {
        PostCall = client.newCall(PostRequest);


        PostCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("HttpService", "Unable to Send: " + e);

                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response r) throws IOException {


                HttpRequestWorker.response = r.body().string();


                Log.i("Final Response: ", "" + HttpRequestWorker.response);


            }
        });

        try {
            Thread.sleep(2000);
            HttpRequestWorker.i++;
            if (HttpRequestWorker.response.contains("UNAUTHENTICATED")) {


            } else {
                Log.i("Status", "Confirmed");
                HttpRequestWorker.GetToken = false;
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //}
        return Result.success();
    }
    private URL createURL(String StringUrl){
        URL url=null;
        try{
            url=new URL(StringUrl);

        } catch (MalformedURLException exception) {
            Log.e("Error with creating URL", String.valueOf(exception));
            return null;
        }
        return url;
    }
}
