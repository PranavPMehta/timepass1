package com.example.rohit.arishit_f.constants;

//please define all constants here
public class Constants
{
    private Constants()
    {
        super();
    }
    public static final String IP_ADDRESS="http://192.168.0.205";//set your ip address here and it will be used everywhere

    private static final int PORT_NO=3000;
    private static final int PORT_NO_CHAT=3001;
    private static final int PORT_NO_Group_CHAT=3002;

    public static final String SERVER_URI=IP_ADDRESS+":"+PORT_NO;
    public static final String CHAT_SERVER_URI=IP_ADDRESS+":"+PORT_NO_CHAT;
    public static final String Group_CHAT_SERVER_URI=IP_ADDRESS+":"+PORT_NO_Group_CHAT;

}