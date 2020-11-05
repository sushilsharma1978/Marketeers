package com.app.marketeers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {
TextView tv_phone,tv_email;
ImageView iv_back1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        tv_phone=findViewById(R.id.tv_phone);
        tv_email=findViewById(R.id.tv_email);
        iv_back1=findViewById(R.id.iv_back1);

        tv_phone.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_email.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        iv_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+917814613916";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","themarketeers1999@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }
}
