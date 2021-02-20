package com.example.accmailbot;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static com.example.accmailbot.encryptRawData.encryptData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         TextView textView=(TextView) findViewById(R.id.txt);
        encryptData("harshsinghrajawat86@gmail.com","Test","<h1>Hi</h1> From Java");


        textView.setText(UserDetails.refresh_token);
        AsyncGetToken Task=new AsyncGetToken();
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