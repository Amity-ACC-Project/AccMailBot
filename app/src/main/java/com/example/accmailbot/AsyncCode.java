package com.example.accmailbot;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.accmailbot.MainActivity.*;

public class AsyncCode extends AsyncTask<URL,Void,String> {

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
            Log.i("AsyncResults: ",  s);
        }
    }



    @Override
    protected String doInBackground(URL... urls) {
        String response="";

        HttpURLConnection UrlConnection=null;
        InputStream inputStream=null;
        URL url = createURL("https://developers.google.com/oauthplayground/refreshAccessToken");

        try {
            JSONObject postData=getObjects();
            String jsonString="{\"token_uri\": \"https://oauth2.googleapis.com/token\", \"client_id\": \"350076262266-7qeg2kh5c320fk9k2dt1gb4cp1s2g8c2.apps.googleusercontent.com\",\"client_secret\":\"yzH0WYH68JXWIjuNvBLcX4Oy\",\"refresh_token\":\"1//04KtKnUcd16M4CgYIARAAGAQSNwF-L9IrUoIF6HU7BthQIW8Ny6zqp0y0gWhF5KxC8IIpIxSsfay7lxH4CDdNOAFsCee6rBuudcw\"}";
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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }finally {
            if(UrlConnection!=null){
                UrlConnection.disconnect();
            }

        }

        return response;
    }
    private JSONObject getObjects() throws JSONException {
        JSONObject json=new JSONObject();
        json.put("token_uri","https://oauth2.googleapis.com/token");
        json.put("client_id","350076262266-7qeg2kh5c320fk9k2dt1gb4cp1s2g8c2.apps.googleusercontent.com");
        json.put("client_secret","yzH0WYH68JXWIjuNvBLcX4Oy");
        json.put("refresh_token","1//04KtKnUcd16M4CgYIARAAGAQSNwF-L9IrUoIF6HU7BthQIW8Ny6zqp0y0gWhF5KxC8IIpIxSsfay7lxH4CDdNOAFsCee6rBuudcw");
        return  json;
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
}
