package com.app.marketeers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsActivity extends AppCompatActivity {
RecyclerView rv_posts;
ImageView iv_contact;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

AVLoadingIndicatorView avi_loader3;
    String allpotsts_url="https://themarketeers.online/api/all_posts.php";
    String sendtoken_url="https://themarketeers.online/wp-content/themes/newscard/fcm_notification/insert_token.php";
List<Rowlist> list=new ArrayList<>();
RowAdapter rowAdapter;
String token;
SharedPreferences sp_loginsave;
SharedPreferences.Editor ed_loginsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        rv_posts=findViewById(R.id.rv_posts);
        avi_loader3=findViewById(R.id.avi_loader3);
        iv_contact=findViewById(R.id.iv_contact);
        sp_loginsave = getSharedPreferences("SAVELOGINDETAILS", MODE_PRIVATE);
        ed_loginsave = sp_loginsave.edit();



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().
                        equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    token = intent.getStringExtra("token");
                    ed_loginsave.putString("SAVETOKEN",token);
                    ed_loginsave.commit();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendTokenToServer(sp_loginsave.getString("SAVETOKEN",""));

                        }
                    },2000);
                    //sendFCMPush();

                    //Displaying the token as toast
                    // Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                    Log.d("msg", "onReceiveToken: " + token);
                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    // Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                //  Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                // Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(PostsActivity.this, GCMRegistrationIntentService.class);
            startService(itent);
        }


        showPosts();

        iv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(PostsActivity.this,ContactActivity.class));
            }
        });

    }



    private void sendTokenToServer(final String savetoken) {
        RequestQueue rq= Volley.newRequestQueue(PostsActivity.this);
        StringRequest srq=new StringRequest(Request.Method.POST, sendtoken_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("msg", "Refreshedtoken2: "+response);
                try {

                        JSONObject jsonObject=new JSONObject(response);
                       String success=jsonObject.getString("success");
                        if(success.equals("1")){
                            //Toast.makeText(PostsActivity.this, "Token Sent",
                              //      Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(PostsActivity.this, "Something went wrong..Please try again later", Toast.LENGTH_SHORT).show();
                        }
                        // Toast.makeText(Splash.this, ""+success, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("msg", "Refreshedtoken3: "+e.toString());

                    Toast.makeText(PostsActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("msg", "TokenResponse2: "+error.toString());
               // Toast.makeText(PostsActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("token", savetoken);
                return new JSONObject(params2).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        rq.add(srq);

    }
    private void sendFCMPush() {

        final String SERVER_KEY = "AAAA1u73PNw:APA91bG1BJZJauqPMUth5Nl8d3wS8s9DRYji07cF6ykw6havDoTzxB3uFU3Lu1I63XwSRWbLFXqkvRdSVHXAB8v0A8jONu2h1MLdEVxsNAXGoYu7loNtwHSflMFPmpqlY06-WlBF4fP6";

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;
        final JSONArray jsonArray=new JSONArray();

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", "msg");
            objData.put("title", "The Marketeers");
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("title", "The Marketeers");
            dataobjData.put("text", "Hiii");

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("msg", "onResponse111111: "+response.toString() );
                        Toast.makeText(PostsActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("msg", "onResponse1111112: "+error.toString() );


                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsObjRequest);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        PostsActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    private void showPosts() {
        RequestQueue requestQueue= Volley.newRequestQueue(PostsActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, allpotsts_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("postlist");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);
                        Rowlist rowlist=new Rowlist(jsonObject1.getString("id"),
                                jsonObject1.getString("title"),jsonObject1.getString("content"),
                                jsonObject1.getString("image"));

                        list.add(rowlist);

                    }

                    rv_posts.setLayoutManager(new LinearLayoutManager(PostsActivity.this,RecyclerView.VERTICAL,false));
rowAdapter=new RowAdapter(PostsActivity.this,list);
rv_posts.setAdapter(rowAdapter);
avi_loader3.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    avi_loader3.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                avi_loader3.setVisibility(View.GONE);
                Toast.makeText(PostsActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(stringRequest);
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
