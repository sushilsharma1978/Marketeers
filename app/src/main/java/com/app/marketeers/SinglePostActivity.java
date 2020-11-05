package com.app.marketeers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SinglePostActivity extends AppCompatActivity {

    ImageView s_iv_image;
    TextView s_tv_title,s_tv_content;
String postid,postcontent,posttitle,postimage;
AVLoadingIndicatorView avi_loader4;
ImageView iv_back;
String singlepost_url="https://themarketeers.online/api/singlepost.php?id=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
       // back=findViewById(R.id.back_IV);
        s_tv_title=findViewById(R.id.s_tv_title);
        s_tv_content=findViewById(R.id.s_tv_content);
        s_iv_image=findViewById(R.id.s_iv_image);
        avi_loader4=findViewById(R.id.avi_loader4);
        iv_back=findViewById(R.id.iv_back);


        postid=getIntent().getStringExtra("postid");
        posttitle=getIntent().getStringExtra("posttitle");
        postimage=getIntent().getStringExtra("postimage");
        postcontent=getIntent().getStringExtra("postcontent");

        String result = postcontent.replaceAll("\n ", "\n");

        s_tv_title.setText(posttitle);
        s_tv_content.setText(result);
        Picasso.get().load(postimage).into(s_iv_image);
        avi_loader4.setVisibility(View.GONE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

      //  showSinglePost();

    }

    private void showSinglePost() {
        RequestQueue requestQueue= Volley.newRequestQueue(SinglePostActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                singlepost_url+postid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("postlist");
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(x);


                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    avi_loader4.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PostsActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(stringRequest);
    }



}
