package com.example.rohit.arishit_f.security;

import android.util.Log;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
    @author: Amogh Kalyanshetti
 */
public class SecurityCaller
{
    public static ArrayList<Object> doEncryption(String s)
    {
        String plaincode="";
        String otpKey="";
        System.out.println("s: "+s);

        OTP.plainTextInit();
        plaincode= OTP.convertPlaintextToDigits(s);
        Log.d("plaincode",plaincode);
        otpKey= OTP.keyGeneration(plaincode.length());
        String encryptedByOTP= OTP.encrypt(plaincode,otpKey);

        DES.initEncrypted();
        String otpKeyEncrypted= DES.encrypt(otpKey);

        ArrayList<Object> arrList=new ArrayList<>();
        arrList.add(encryptedByOTP);
        arrList.add(otpKeyEncrypted);
        SecretKey secretKey= DES.getSecretKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        arrList.add(encodedKey);
        return arrList;

    }

    public static String doDecryption(List<Object> arrList)
    {
        String msg=(String) arrList.get(0);

        //Log.d("msgD",msg);
        OTP.plainTextInit();
        String desKey=(String)arrList.get(2);
        byte[] decodedKey = Base64.getDecoder().decode(desKey);


        SecretKey secretKey= new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
        DES.initDecrypted(secretKey);
        String otpKey= DES.decrypt((String) arrList.get(1));
        //Log.d("otpKey",otpKey);
        String decryptedString= OTP.decrypt(msg, otpKey);
        //Log.d("decryptedString",decryptedString);
       String output= OTP.convertDigitsToPlaintext(decryptedString);
       //Log.d("output",output);
        return output;
    }
}
