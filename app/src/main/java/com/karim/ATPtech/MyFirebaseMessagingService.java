package com.karim.ATPtech;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.karim.ATPtech.Model.Request.LoginRequest;
import com.karim.ATPtech.Model.Request.UpdateTokenRequest;
import com.karim.ATPtech.Model.Response.LoginResponse;
import com.karim.ATPtech.Model.Response.UpdateTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String login = "";
    String token = "";

    public MyFirebaseMessagingService() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void onFirstInstall(Activity act){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(act, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                setToken(instanceIdResult.getToken());
                storeToken(instanceIdResult.getToken());
            }
        });
    }
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            //create notification
            createNotification(remoteMessage.getNotification().getBody());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        storeToken(s);
        setToken(s);
        SharedPreferences preferences = getSharedPreferences("ATPTech", MODE_PRIVATE);;
        if(preferences.contains("login")) {
            login = preferences.getString("login", "");
            if (!login.isEmpty())
                updateToken(s, login);
        }
    }


    private void storeToken(String token) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ATPTech", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    void updateToken(final String token, final String login) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        UpdateTokenRequest lr = new UpdateTokenRequest(token, login);

        Call<UpdateTokenResponse> call = null;
        call = service.updatetoken(lr);
        call.enqueue(new Callback<UpdateTokenResponse>() {
            @Override
            public void onResponse(Call<UpdateTokenResponse> call, Response<UpdateTokenResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1)
                        storeToken(token);
                }
            }

            @Override
            public void onFailure(Call<UpdateTokenResponse> call, Throwable t) { }
        });

    }
    private void createNotification( String messageBody) {
        Intent intent = new Intent( getApplicationContext() , MainActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isFromNotif", true);
        PendingIntent resultIntent = PendingIntent.getActivity( getApplicationContext() , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("АТП заявки")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
