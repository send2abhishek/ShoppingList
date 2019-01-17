package com.example.abhishekaryan.shoppinglist.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.abhishekaryan.shoppinglist.R;
import com.example.abhishekaryan.shoppinglist.Services.AccountServices;
import com.squareup.otto.Subscribe;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button homeBtn;
    private EditText username;
    private EditText email;
    private Button registerBtn;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linearLayout=(LinearLayout)findViewById(R.id.activity_register_linearLayout);
        linearLayout.setBackgroundResource(R.drawable.background_screen_two);
        homeBtn=(Button)findViewById(R.id.activity_register_home_btn);
        homeBtn.setOnClickListener(this);
        username=(EditText)findViewById(R.id.activity_register_username);
        email=(EditText)findViewById(R.id.activity_register_user_email);
        registerBtn=(Button)findViewById(R.id.activity_register_register_btn);
        registerBtn.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Attempting to Register Account");
        progressDialog.setTitle("Loading....");
        progressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View v) {


        int id=v.getId();

        if(id==R.id.activity_register_home_btn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        else if(id==R.id.activity_register_register_btn){

            setupRegisterAccount();
        }

    }

    private void setupRegisterAccount() {




        bus.post(new AccountServices.RegisterUserRequest(username.getText().toString(),
                email.getText().toString(),
                progressDialog));
    }



    @Subscribe
    public void onRegisterRequestResponse(AccountServices.RegisterUserResponse response){

        if(!response.didSucceed()){

            username.setError(response.getPropertyErrors("username"));
            email.setError(response.getPropertyErrors("email"));
        }
    }
}
