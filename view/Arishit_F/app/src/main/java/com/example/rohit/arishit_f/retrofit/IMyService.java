package com.example.rohit.arishit_f.retrofit;
import com.example.rohit.arishit_f.constants.Constants;
import com.example.rohit.arishit_f.login.*;
import com.example.rohit.arishit_f.task.ContactRetro;

import org.json.JSONArray;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import io.reactivex.Observable;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IMyService {

    static final String IP = Constants.SERVER_URI;

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password") String password,
                                 @Field("token") String token);

    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password,
                                    @Field("username") String username,
                                    @Field("mobileno") String mobileno,
                                    @Field("designation") String designation,
                                    @Field("uid") String uid,
                                    @Field("IMEI") String IMEI,
                                    @Field("TS") String TS,
                                    @Field("org_token") String org_token
    );


    @GET("users/me")
    Call<MessageResult> getMyDetails(
            @Header("Authorization") String authHeader
    );

    @POST("getFAQs")
    Call<MessageResult> getFAQ();


    @GET("users/list")
    Call<MessageResult> getUserList(@Query("user_id") String user_id);

    @Multipart
    @POST("/analyze")
    Call<MessageResult> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);

    @FormUrlEncoded
    @POST("otp/request")
    Call<MessageResult> requestOtp(
            @Field("userid") String userid,
            @Field("email") String email,
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("otp/requestEmailOTP")
    Call<MessageResult> requestEmailOtp(
            @Field("userid") String userid,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("register/store")
    Call<MessageResult> requestStore(
            @Field("result") String result
    );

    @FormUrlEncoded
    @POST("otp/verify")
    Call<MessageResult> verifyOtp(
            @Field("userid") String userid,
            @Field("otpMobile") String otp_Mobile,
            @Field("otpEmail") String otp_Email
    );

    @FormUrlEncoded
    @POST("otp/verifyEmailOTP")
    Call<MessageResult> verifyEmailOtp(
            @Field("userid") String userid,
            @Field("otpEmail") String otp_Email
    );

    @FormUrlEncoded
    @POST("resetPassword")
    Call<MessageResult> resetPassword(
            @Field("userid") String userid,
            @Field("newPassword") String newPassword
    );

    @FormUrlEncoded
    @POST("createMeeting")
    Call<MessageResult> createMeeting(
            @Field("agenda") String agenda,
            @Field("members") String members,
            @Field("time") String date,
            @Field("date") String time,
            @Field("presentation_id") String presentation_id
    );

    @GET("getMember")
    Call<List<ContactRetro>> getstatus(@Query("presentation_id") String presentation_id);

    @GET("getTask")
    Call<MessageResult> getTask();

    @FormUrlEncoded
    @POST("CreateTask")
    Call<MessageResult> createTask(
            @Field("for_mem") String for_mem,
            @Field("title") String title,
            @Field("desc") String desc
    );

    @FormUrlEncoded
    @POST("getToken")
    Call<MessageResult> getToken(
            @Field("user") String user
    );

    //Face Recognition
    @Multipart
    @POST("/api/photo")
    Call<ResponseBody> uploadTrainImage(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3
    );

    @Multipart
    @POST("/api/photo")
    Call<ResponseBody> uploadValImage(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file1
    );

    @GET("/getChat")
    Call<MessageResult> getChats(
            @Query("sender_id") String sender_id,
            @Query("receiver_id") String receiver_id
    );

    @GET("/getGroupChat")
    Call<MessageResult> getGroupChats(
            @Query("group_id") String group_id
    );

    @FormUrlEncoded
    @POST("setReceived")
    Call<MessageResult> setReceived(
            @Field("msg_id") String msg_id
    );

    @FormUrlEncoded
    @POST("setPoll")
    Call<MessageResult> setpoll(
            @Field("msg_id") String msg_id
    );

    @FormUrlEncoded
    @POST("setAgree")
    Call<MessageResult> setagree(
            @Field("msg_id") String msg_id,
            @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("setDisagree")
    Call<MessageResult> setdisagree(
            @Field("msg_id") String msg_id,
            @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("setNeutral")
    Call<MessageResult> setneutral(
            @Field("msg_id") String msg_id,
            @Field("user_id") String user_id

    );

    @GET("/checkReceived")
    Call<MessageResult> checkReceived(
            @Query("msg_id") String msg_id
    );

    @POST("updateVaultPassword")
    @FormUrlEncoded
    Call<MessageResult> updateVaultPassword(
            @Header("Authorization") String authHeader,
            @Field("vault_password") String vault_password
    );

    /*
        @Multipart
        @POST("/upload")
        Call<ResponseBody> uploadVideo(@Part("name") RequestBody requestBody,@Part MultipartBody.Part file);*/
    @FormUrlEncoded
    @POST("createGroup")
    Call<MessageResult> createGroup(
            @Field("group_id") String group_id,
            @Field("group_name") String group_name,
            @Field("url") String url,
            @Field("objective") String objective,
            @Field("members") JSONArray members,
            @Field("admin") String admin,
            @Field("creation_time") String creation_time
    );


    @FormUrlEncoded
    @POST("removeMembers")
    Call<MessageResult> removeGroupMember(
            @Field("group_id") String group_id,
            @Field("member_ids") JSONArray members
    );

    @FormUrlEncoded
    @POST("addMembers")
    Call<MessageResult> addGroupMember(
            @Field("group_id") String group_id,
            @Field("member_add") JSONArray members
    );

    @FormUrlEncoded
    @POST("getGroupInfo")
    Call<MessageResult> getGroupInfo(
            @Field("group_id") String group_id
    );

    @FormUrlEncoded
    @POST("modifyGroupUrl")
    Call<MessageResult> modifyGroup(
            @Field("group_id") String group_id,
            @Field("group_name") String group_name,
            @Field("url") String url
    );

    @FormUrlEncoded
    @POST("modifyGroupInfo")
    Call<MessageResult> modifyGroupInfo(
            @Field("group_id") String group_id,
            @Field("name") String name,
            @Field("objective") String objective
    );

    //Upload image
    @Multipart
    @POST("uploadImage")
    Call<MessageResult> uploadImage(
            @Part("description") RequestBody requestBody,
            @Part MultipartBody.Part file

    );

    @FormUrlEncoded
    @POST("users/orglist")
    Call<MessageResult> getOrgList(
            @Field("org_token") String org_token
    );

    @FormUrlEncoded
    @POST("changePassword")
    Call<MessageResult> changePassword(
            @Field("userId") String userId,
            @Field("password") String password,
            @Field("newPassword") String newPassword
    );

    @FormUrlEncoded
    @POST("checkPreviousPassword")
    Call<MessageResult> checkPreviousPassword(
            @Field("userId") String userId,
            @Field("password") String password,
            @Field("newPassword") String newPassword
    );

    @FormUrlEncoded
    @POST("getUserInfo")
    Call<MessageResult> getInfo(
            @Field("user_id") String username1
    );

    @FormUrlEncoded
    @POST("modifyUserInfo")
    Call<MessageResult> modifyInfo(
            @Field("username1") String username1,
            @Field("name") String name,
            @Field("id") String id,
            @Field("designation") String designation,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("url") String url
    );

    @POST("saveNotificationData")
    @FormUrlEncoded
    Observable<String> saveData(@Field("notify_message_receipts") Boolean notify_message_receipts,
                                @Field("notify_new_message") Boolean notify_new_message,
                                @Field("notify_scheduled_meeting") Boolean notify_scheduled_meeting,
                                @Field("notify_task_allocation") Boolean notify_task_allocation,
                                @Field("notify_app_update") Boolean notify_app_update,
                                @Field("user_id") String user_id
    );

    @POST("saveSettingsData")
    @FormUrlEncoded
    Observable<String> saveData(@Field("profile_picture_visibility") int profile_picture_visibility,
                                @Field("org_details_visibility") int org_details_visibility,
                                @Field("email_visibility") int email_visibility,
                                @Field("contact_details_visibility") int contact_details_visibility,
                                @Field("designation_visibility") int designation_visibility,
                                @Field("user_id") String user_id
    );


    @POST("saveFeedbackData")
    @FormUrlEncoded
    Observable<String> saveData(@Field("like_application") String like_application,
                                @Field("like_features") String like_features,
                                @Field("suggestions") String suggestions,
                                @Field("feedbackImage") String feedbackImage,
                                @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST("deleteMessage")
    Call<MessageResult> deleteMessage(
            @Field("reference_id") String reference_id
    );

    @FormUrlEncoded
    @POST("setImportant")
    Call<MessageResult> setImportant(
            @Field("reference_id") String reference_id
    );

    @FormUrlEncoded
    @POST("hideMessage")
    Call<MessageResult> hideMessage(
            @Field("reference_id") String reference_id
    );

    @FormUrlEncoded
    @POST("getMessageInfo")
    Call<MessageResult> getMessageInfo(
            @Field("reference_id") String reference_id
    );

    @FormUrlEncoded
    @POST("setSecurityLevel")
    Call<MessageResult> set_security_level(
            @Field("reference_id") String reference_id,
            @Field("security_level") String security_level
    );

    @FormUrlEncoded
    @POST("block_user")
    Call<MessageResult> blockUser(
            @Field("logged_in_user") String logged_in_user,
            @Field("user_to_be_blocked") String user_to_be_blocked
    );

    @FormUrlEncoded
    @POST("unblock_user")
    Call<MessageResult> unblockUser(
            @Field("logged_in_user") String logged_in_user,
            @Field("user_to_be_unblocked") String user_to_be_unblocked
    );

    @FormUrlEncoded
    @POST("check_block_user")
    Call<MessageResult> check_block_user(
            @Field("logged_in_user") String logged_in_user,
            @Field("user_to_be_checked") String user_to_be_checked
    );

    @FormUrlEncoded
    @POST("setSSLChat")
    Call<MessageResult> setSSLChat(
            @Field("logged_in_user") String logged_in_user,
            @Field("user_to_be_checked") String user_to_be_checked,
            @Field("confidentialLevel") String confidentialLevel

    );

    @FormUrlEncoded
    @POST("check_isConfidential")
    Call<MessageResult> check_isConfidential(
            @Field("logged_in_user") String logged_in_user,
            @Field("user_to_be_checked") String user_to_be_checked

    );

    @FormUrlEncoded
    @POST("check_ssl_vault")
    Call<MessageResult> check_ssl_vault(
            @Field("logged_in_user") String logged_in_user,
            @Field("user_to_be_checked") String user_to_be_checked

    );

    @GET("users/list")
    Call<MessageResult> getUserList();

    @FormUrlEncoded
    @POST("getSSLmsg")
    Call<MessageResult> getSSLmsg(
            @Field("reference_id") String reference_id

    );

    @POST("getHoneyUsers")
    Call<MessageResult> getHoneyUsers();

    @FormUrlEncoded
    @POST("getHoneyMsg")
    Call<MessageResult> getHoneyMsg(
            @Field("user_name") String msg
    );
    @FormUrlEncoded
    @POST("loginsessionend")
    Call<MessageResult> loginSessionEnd(
            @Field("email") String Username
    );

    @FormUrlEncoded
    @POST("checklogin")
    Call<MessageResult> checkLogin(
            @Field("email") String Username
    );

    @GET("users/me/logout")
    Call<MessageResult> logout(
            @Header("Authorization") String authHeader
    );
}

