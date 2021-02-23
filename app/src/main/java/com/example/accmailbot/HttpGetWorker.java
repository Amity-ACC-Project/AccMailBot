package com.example.accmailbot;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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

public class HttpGetWorker extends Worker {
    public HttpGetWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String response="";

        HttpURLConnection UrlConnection=null;
        InputStream inputStream=null;
        URL url = createURL(UserDetails.TokenUrl);


        try {
            String jsonString="{\"token_uri\": \""+UserDetails.token_uri+"\", \"client_id\": \""+UserDetails.client_id+"\",\"client_secret\":\""+UserDetails.client_secret+"\",\"refresh_token\":\""+UserDetails.refresh_token+"\"}";
            UrlConnection=(HttpURLConnection) url.openConnection();
            UrlConnection.setRequestMethod("POST");
            UrlConnection.setReadTimeout(10000);
            UrlConnection.setConnectTimeout(15000);
            try(OutputStream os = UrlConnection.getOutputStream()) {
                byte[] input = jsonString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            UrlConnection.connect();
            if(UrlConnection.getResponseCode()==200){
                inputStream=UrlConnection.getInputStream();
                response=readFromStream(inputStream);
                inputStream.close();
            }
            response=getToken(response);
            if(response!=null){
                Log.i("getTokenResults: ",  UserDetails.access_token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(UrlConnection!=null){
                UrlConnection.disconnect();
            }

        }
        if(response!=null) {
            return Result.success();
        }else
            return  Result.failure();
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
    private String getToken(String Response){
        try {
            JSONObject data= new JSONObject(Response);
            boolean check=data.getBoolean("success");
            if(check){
                String token=data.getString("access_token");
                UserDetails.access_token=token;
                return token;}
            else {
                return null;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
