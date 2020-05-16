package com.example.rohit.arishit_f.security;

import android.util.Log;

import java.security.SecureRandom;
import java.util.HashMap;

/*
*@author: Amogh Kalyanshetti
*/
public class OTP
{
    private static String plaintext;
    private static String digitString;
    private static String otpString;
    private static String encryptedString;
    private static String decryptedString;
    private static HashMap<String,String> hashMap;//for storing plaincode and text information
    public static String keyGeneration(int size)
    {
        StringBuilder key=new StringBuilder();
        SecureRandom rand=new SecureRandom();
        //https://www.geeksforgeeks.org/random-vs-secure-random-numbers-java/
        for (int i=0;i<size;i++)
            key.append(Integer.toString(rand.nextInt(9)));
        return key.toString();

    }
    public static void plainTextInit()
    {
        hashMap=new HashMap<>();
        hashMap.put("a","1");
        hashMap.put("e","2");
        hashMap.put("i","3");
        hashMap.put("n","4");
        hashMap.put("o","5");
        hashMap.put("t","6");
        hashMap.put("b","70");
        hashMap.put("c","71");
        hashMap.put("d","72");
        hashMap.put("f","73");
        hashMap.put("g","74");
        hashMap.put("h","75");
        hashMap.put("j","76");
        hashMap.put("k","77");
        hashMap.put("l","78");
        hashMap.put("m","79");
        hashMap.put("p","80");
        hashMap.put("q","81");
        hashMap.put("r","82");
        hashMap.put("s","83");
        hashMap.put("u","84");
        hashMap.put("v","85");
        hashMap.put("w","86");
        hashMap.put("x","87");
        hashMap.put("y","88");
        hashMap.put("z","89");
        hashMap.put(".","91");
        hashMap.put(":","92");
        hashMap.put("\'","93");
        hashMap.put("+","95");
        hashMap.put("-","96");
        hashMap.put("=","97");
    }
    public static HashMap<String,String> reverseHashMap(HashMap<String,String> hashMap)
    {
        HashMap<String, String> h2=new HashMap<>();
        //hashMap.forEach((k,v)-> h2.put(v,k));
        for(String key:hashMap.keySet())
        {
            h2.put(hashMap.get(key),key);
        }
        return h2;
    }
    public static String convertDigitsToPlaintext(String decryptedString)
    {
        System.out.println();
        StringBuilder stringBuffer=new StringBuilder();
        HashMap<String,String> h2=reverseHashMap(hashMap);
        for(int i=5;i<decryptedString.length();i++)
        {
            char ch=decryptedString.charAt(i);
            int no=Integer.parseInt(String.valueOf(ch));

            if((decryptedString.length()-i)>=3 && decryptedString.charAt(i)==decryptedString.charAt(i+1) && decryptedString.charAt(i+1)==decryptedString.charAt(i+2))
            {
                stringBuffer.append(decryptedString.charAt(i));
                i=i+2;
            }
            else if(no>=1 && no<=6)
            {
                stringBuffer.append(h2.get(Integer.toString(no)));
            }
            else
            {
                char ch1= decryptedString.charAt(i+1);
                String code= String.valueOf(ch) + ch1;
                if(code.equals("94"))
                {
                    stringBuffer.append("()");
                }
                else if(code.equals("90") || code.equals("99"))
                {
                    stringBuffer.append(" ");
                }
                else
                {
                    stringBuffer.append(h2.get(code));
                }
                i++;
            }
        }
        return stringBuffer.toString();
    }
    public static String convertPlaintextToDigits(String plaintext)
    {
        plaintext=plaintext.toLowerCase();
        Log.d("plaintext",plaintext);
        StringBuilder stringBuffer=new StringBuilder();
        stringBuffer.append("KEYID");
        for(int i=0;i<plaintext.length();i++)
        {
            char ch=plaintext.charAt(i);
            if(ch=='(')
            {
                if(plaintext.charAt(i+1)==')')
                    stringBuffer.append("94");
            }
            else if(ch==' ')
            {
                if(i==0)
                {
                    if(Character.isDigit(plaintext.charAt(i+1)))
                    {
                        stringBuffer.append("90");
                    }
                    else
                    {
                        stringBuffer.append("99");
                    }

                }
                if(Character.isDigit(plaintext.charAt(i+1) )|| Character.isDigit(plaintext.charAt(i-1)))
                {

                    stringBuffer.append("90");

                }
                else
                {
                    stringBuffer.append("99");
                }
            }
            else if(Character.isDigit(ch))
            {
                stringBuffer.append(ch);
                stringBuffer.append(ch);
                stringBuffer.append(ch);
            }
            else
            {
                Log.d("ch",String.valueOf(ch));
                stringBuffer.append(hashMap.get(String.valueOf(ch)));
            }
        }
        stringBuffer.append("91");
        return stringBuffer.toString();
    }

    public static void printInGroupOf5(String str)
    {
        int counter=0;
        for(int i=0;i<str.length();i++)
        {
            counter++;
            System.out.print(str.charAt(i));
            if(counter%5==0)
                System.out.print("  ");
        }
    }
    public static String encrypt(String digitString,String otpString)
    {
        int a,b,res;
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<5;i++)
        {
            sb.append(otpString.charAt(i));
        }
        for(int i=5;i<otpString.length();i++)
        {

            a=Integer.parseInt(String.valueOf(digitString.charAt(i)));
            b=Integer.parseInt(String.valueOf(otpString.charAt(i)));
            res=modularSubstraction(a,b);
            sb.append(res);
        }
        encryptedString=sb.toString();
        return encryptedString;
    }
    public static String decrypt( String encryptedString,String otpString)
    {
        int a,b,res;
        StringBuilder sb=new StringBuilder();
        sb.append("KEYID");
        for(int i=5;i<encryptedString.length();i++)
        {
            a=Integer.parseInt(String.valueOf(encryptedString.charAt(i)));
            b=Integer.parseInt(String.valueOf(otpString.charAt(i)));
            res=modularAddition(a,b);
            sb.append(res);
        }
        decryptedString=sb.toString();
        return decryptedString;
    }
    private static int modularAddition(int a,int b)
    {
        int res=a+b;
        String s=Integer.toString(res);
        if(s.length()>1)
        {
            res=Integer.parseInt(s.substring(1));
        }
        return res;
    }
    private static int modularSubstraction(int a,int b)
    {
        if(a>=b)
        {
            return (a-b);
        }
        else
        {
            String sb = "1" +a;
            a=Integer.parseInt(sb);
            return (a-b);
        }
    }
}
