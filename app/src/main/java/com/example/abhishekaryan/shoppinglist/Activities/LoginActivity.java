package com.example.abhishekaryan.shoppinglist.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.R;
import com.example.abhishekaryan.shoppinglist.Services.AccountServices;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button RegisterBtn;
    private LinearLayout linearLayout;
    private EditText email;
    private EditText password;
    private Button LoginBtn;
    private ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private LoginButton FbloginButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sharedPreferences=getSharedPreferences(Utils.SHARED_PREFENCES,MODE_PRIVATE);
        RegisterBtn=(Button) findViewById(R.id.activity_login_register_btn);
        linearLayout=(LinearLayout)findViewById(R.id.activity_login_linearLayout);
        linearLayout.setBackgroundResource(R.drawable.background_screen_two);
        email=(EditText)findViewById(R.id.activity_login_user_email);
        password=(EditText)findViewById(R.id.activity_login_user_password);
        LoginBtn=(Button)findViewById(R.id.activity_login_login_btn);
        LoginBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Attempting to Login in your Account");
        progressDialog.setTitle("Loading....");
        progressDialog.setCancelable(false);
        FbloginButton = (LoginButton) findViewById(R.id.login_button);
        FbloginButton.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(id==R.id.activity_login_register_btn){

            Intent intent=new Intent(this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        if(id==R.id.activity_login_login_btn){



            bus.post(new AccountServices.LoginUserRequest(email.getText().toString()
                    ,password.getText().toString(),progressDialog,sharedPreferences));
        }

        if(id==R.id.login_button){

            faceBookSignIn();
        }


    }

    private void faceBookSignIn() {

        FbloginButton.setReadPermissions(Arrays.asList(
                "email","public_profile"));

        callbackManager = CallbackManager.Factory.create();
        //FbloginButton.setReadPermissions("email","public_profile");
        FbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {


                GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {


                                try {
                                    String email=object.getString("email");
                                    String name=object.getString("name");
                                    bus.post(new AccountServices.LogUserInFacebookRequest(loginResult.getAccessToken()
                                    ,progressDialog,name,email,sharedPreferences));
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );

                Bundle paramters=new Bundle();
                paramters.putString("fields","id,name,gender,birthday");
                graphRequest.setParameters(paramters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"Uncaught Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {


                Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Subscribe
    public void onLoginRequestResponse(AccountServices.LoginUserResponse response){

        if(!response.didSucceed()){

            password.setError(response.getPropertyErrors("password"));
            email.setError(response.getPropertyErrors("email"));
        }
    }


}
