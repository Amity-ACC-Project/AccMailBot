package com.example.accmailbot;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.accmailbot.UserDetails;

public class AsyncGetToken extends AsyncTask<URL,Void,String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s==null){
            return ;
        }else {
            UserDetails.access_token=s;
            Log.i("AsyncResults: ",  UserDetails.access_token);
        }
    }



    @Override
    protected String doInBackground(URL... urls) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(UrlConnection!=null){
                UrlConnection.disconnect();
            }

        }

        return response;
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
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF_8"));

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
