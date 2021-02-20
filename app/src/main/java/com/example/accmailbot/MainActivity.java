package com.example.accmailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.util.Base64;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         TextView textView=(TextView) findViewById(R.id.txt);
        enSchema schema=new enSchema("harshsinghrajawat86@gmail.com","Test","<h1>Hi</h1> From Java");

        byte[] msg=schema.getStruct();
        String en=Base64.encodeToString(msg,Base64.CRLF);
        textView.setText(en);
        Log.i("MainActivity: ",  en);
        AsyncCode Task=new AsyncCode();
        Task.execute();


       /* Properties properties=new Properties();
        Session session=Session.getDefaultInstance(properties,null);

        Toast.makeText(this,"Session Created",Toast.LENGTH_SHORT).show();
        try {
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress("webproject4551@gmail.com"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("harshsinghrajawat86@gmail.com"));
            message.setText("This is Test");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Toast.makeText(this,"Msg Sent Successfully",Toast.LENGTH_SHORT).show();
        textView.setText("Msg Sent Successfully");*/
    }


}