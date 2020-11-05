package com.app.marketeers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Splash extends AppCompatActivity {
    public static final int SPLASH_TIME_OUT=3000;
    ProgressDialog progressDialog;
    SharedPreferences sp_token;
    SharedPreferences.Editor ed_token;

String token="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_splash);




               // Toast.makeText(Splash.this, "TOKEN: "+token, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(Splash.this,PostsActivity.class);
                     //   intent.putExtra("send_token",token);
                        startActivity(intent);

                        finish();
                    }
                },SPLASH_TIME_OUT);


    }
}
