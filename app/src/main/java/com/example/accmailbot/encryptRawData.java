package com.example.accmailbot;

import android.util.Base64;
import com.example.accmailbot.UserDetails;

public class encryptRawData {

    public static String email ="";
    public static String subject="";
    public static String body="";


    public static void encryptData(String Email, String Subject, String Body){
        email=Email;
        subject=Subject;
        body=Body;
         String Content_struct="Content-Type: text/html; charset=\"UTF-8\"\n" +
                "MIME-Version: 1.0\n" +
                "Content-Transfer-Encoding: 7bit\n" +
                "to: "+email+"\n" +
                "subject: "+subject+"\n"+
                "\n" +
                body+"";

        byte[] msg=Content_struct.getBytes();
        String en=Base64.encodeToString(msg,Base64.CRLF);
        UserDetails.raw_data=en;
    }
}
