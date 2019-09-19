package com.blueapple.my_facebook_login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    LoginButton loginButton;
    ImageView imageView;
    TextView textView,text_email;
    String email;
    LoginManager fbLoginManager;
    Button button;


    private CallbackManager callbackManager;
    SharedPreferences sharedPreferences,preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        loginButton = findViewById(R.id.login_button);
        imageView = findViewById(R.id.profilepic_id);
        textView = findViewById(R.id.text_nameid);
        text_email = findViewById(R.id.text_emailid);
        button = findViewById(R.id.btn_loginid);


        // loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String name = preferences.getString("name", null);
        if (name != null) {


            final ProgressDialog pd=new ProgressDialog(this);
            pd.setMessage("wait");
            pd.show();

            //   Toast.makeText(this, "signed in", Toast.LENGTH_SHORT).show();

            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    final Profile profile = Profile.getCurrentProfile();


                    Intent intent = new Intent(MainActivity.this, AfterLogin_Activity.class);
                    intent.putExtra("username", profile.getFirstName() + " " + profile.getLastName());
                    intent.putExtra("imageurl", profile.getProfilePictureUri(150, 150).toString());

                    Toast.makeText(MainActivity.this, "" + profile.getFirstName(), Toast.LENGTH_SHORT).show();

                    editor.putString("name", profile.getFirstName());
                    editor.commit();
                    pd.dismiss();
                    startActivity(intent);
                }
            },1000);


        }

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");


                        //  Toast.makeText(MainActivity.this, "success"+loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

                        Log.d("token : ", loginResult.getAccessToken().getToken());
                        Profile profile=Profile.getCurrentProfile();

                        Intent intent=new Intent(MainActivity.this,AfterLogin_Activity.class);
                        intent.putExtra("username",profile.getFirstName());
                        intent.putExtra("imageurl",profile.getProfilePictureUri(150,150).toString());


                        // Toast.makeText(MainActivity.this, ""+profile.getFirstName(), Toast.LENGTH_SHORT).show();

                        editor.putString("name",profile.getFirstName());
                        editor.commit();
                        startActivity(intent);
                        finishAffinity();
                    }

                    @Override
                    public void onCancel()
                    {
                        Toast.makeText(MainActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email"));


            }
        });


        //to generate the keyHash this method id used.

        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(
                    "com.blueapple.my_facebook_login",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d( "KeyHash:" , Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));

//                Toast.makeText(getApplicationContext(), Base64.encodeToString(md.digest(),
//                        Base64.DEFAULT), Toast.LENGTH_LONG).show();

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
}
