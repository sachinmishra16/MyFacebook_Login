package com.blueapple.my_facebook_login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class AfterLogin_Activity extends AppCompatActivity {

    Button btn_logout;

    CircleImageView imageView;
    TextView textView,text_email;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_);

        btn_logout=findViewById(R.id.btn_logout);

        Bundle bundle=getIntent().getExtras();


        String name=bundle.getString("username");
        String image_url=bundle.getString("imageurl");

        btn_logout=findViewById(R.id.btn_logout);

        imageView=findViewById(R.id.profilepic_id);
        textView=findViewById(R.id.text_nameid);
        text_email=findViewById(R.id.text_emailid);

        textView.setText(name);
        Glide.with(AfterLogin_Activity.this).load(image_url).into(imageView);

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();


      /*  Bundle bundle=getIntent().getExtras();

        String email=bundle.getString("email");
        String name=bundle.getString("name");
        String image_url=bundle.getString("image_url");

        text_email.setText(email);
        textView.setText(name);
        Glide.with(AfterLogin_Activity.this).load(image_url).into(imageView);*/

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logOut();

                editor.clear();
                editor.commit();

                startActivity(new Intent(AfterLogin_Activity.this,MainActivity.class));
                finishAffinity();


                Toast.makeText(AfterLogin_Activity.this, "logged out", Toast.LENGTH_SHORT).show();

            }
        });





    }
}
