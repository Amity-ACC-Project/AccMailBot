package com.example.accmailbot;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequestWorker extends Worker {
    public HttpRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    static String response=null;
    static  boolean GetToken=true;
    static int i=1;


    @NonNull
    @Override
    public Result doWork() {
        String TAG="HTTP Worker";
        String TokenPost="{\"token_uri\": \""+UserDetails.token_uri+"\", \"client_id\": \""+UserDetails.client_id+"\",\"client_secret\":\""+UserDetails.client_secret+"\",\"refresh_token\":\""+UserDetails.refresh_token+"\"}";

        URL GetUrl=createURL(UserDetails.TokenUrl);


        OkHttpClient client=new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody TokenBody=RequestBody.create(JSON,TokenPost);


        Request getRequest=new Request.Builder()
                .url(GetUrl)
                .post(TokenBody)
                .build();

        Call GetCall = client.newCall(getRequest);
            GetCall.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e(TAG, "Couldn't get Token: " + e);

                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                    Log.i(TAG, "This is getToken Worker\n");
                    Log.i(TAG, ""+response.body().toString());
                    try {
                        JSONObject data = new JSONObject(response.body().string());
                        boolean check = data.getBoolean("success");
                        if (check) {
                            String token = data.getString("access_token");
                            UserDetails.access_token = token;
                            Log.i(TAG, UserDetails.access_token);
                        } else {
                            Log.i(TAG, response.body().string());
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (UserDetails.access_token != null) {
/*
                        Constraints constraints=new Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build();
                        WorkRequest PostRequest=new OneTimeWorkRequest.Builder(HttpRequestWorker.class)
                                .setConstraints(constraints)
                                .addTag("PostData")
                                .build();

                        WorkManager.getInstance(getApplicationContext()).enqueue(PostRequest);*/
                        //String TAG="PostRequest";
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
                                Log.e(TAG, "Unable to Send: " + e);

                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response r) throws IOException {


                                HttpRequestWorker.response = r.body().string();


                                Log.i(TAG, "" + HttpRequestWorker.response);


                            }
                        });

                        try {
                            Thread.sleep(2000);
                            HttpRequestWorker.i++;
                            if (HttpRequestWorker.response.contains("UNAUTHENTICATED")) {


                            } else {
                                Log.i(TAG, "Confirmed");
                                HttpRequestWorker.GetToken = false;
                            }


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });


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


    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("utf-8"));

            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while(line!=null){
                output.append(line);
                line=reader.readLine();
            }
        }

        return output.toString();
    }
}
