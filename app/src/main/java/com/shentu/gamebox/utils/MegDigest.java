package com.shentu.gamebox.utils;

import android.text.TextUtils;

import java.security.MessageDigest;

public class MegDigest{

    public static String Md5(String string){
        if (TextUtils.isEmpty(string)){
            return "";
        }

        MessageDigest md5 = null;

        try{
            md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(string .getBytes());
            String result = "";
            for(byte mbyte:digest){
                String s = Integer.toHexString(mbyte & 0xff);
                if (s.length() == 1){
                    s = "0" + s;
                }
                result += s ;
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
