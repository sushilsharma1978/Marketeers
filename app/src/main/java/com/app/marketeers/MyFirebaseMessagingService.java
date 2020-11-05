package com.app.marketeers;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Intent intent;
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Toast.makeText(this, "remote"+remoteMessage.getData().toString(), Toast.LENGTH_SHORT).show();

            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {

                JSONObject json = new JSONObject(remoteMessage.getData().toString());

                sendPushNotification(json);
            } catch (Exception e) {
                Toast.makeText(this, "error1"+e.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Data Payload1: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 intent = new Intent(getApplicationContext(), PostsActivity.class);
                startForegroundService(intent);
            } else {
                intent = new Intent(getApplicationContext(), PostsActivity.class);
                startService(intent);
            }
          //

            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

}