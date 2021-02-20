package com.example.accmailbot;

import android.provider.BaseColumns;
import android.util.Base64;

public class enSchema {
    public enSchema(String Email,String Subject,String Body){email=Email;subject=Subject;body=Body;}

    public static String email ="";
    public static String subject="";
    public static String body="";

    public byte[] getStruct(){
         String Content_struct="Content-Type: text/html; charset=\"UTF-8\"\n" +
                "MIME-Version: 1.0\n" +
                "Content-Transfer-Encoding: 7bit\n" +
                "to: "+email+"\n" +
                "subject: "+subject+"\n"+
                "\n" +
                body+"";

         byte[]  result=Content_struct.getBytes();
         return result;
    }
}
