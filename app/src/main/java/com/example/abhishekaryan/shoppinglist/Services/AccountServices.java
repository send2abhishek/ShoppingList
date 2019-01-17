package com.example.abhishekaryan.shoppinglist.Services;

import android.app.ProgressDialog;
import android.content.SharedPreferences;

import com.example.abhishekaryan.shoppinglist.Infrastructure.ServiceResponse;
import com.facebook.AccessToken;

public class AccountServices {


    private AccountServices() {
    }


    public static class RegisterUserRequest{

        public String UserName;
        public String Email;
        public ProgressDialog progressDialog;

        public RegisterUserRequest(String userName, String email, ProgressDialog progressDialog) {
            UserName = userName;
            Email = email;
            this.progressDialog = progressDialog;
        }
    }


    public static class RegisterUserResponse extends ServiceResponse {


    }

    public static class LoginUserRequest{

        public String userEmail;
        public String userPassword;
        public ProgressDialog progressDialog;
        public SharedPreferences sharedPreferences;

        public LoginUserRequest(String userEmail, String userPassword,
                                ProgressDialog progressDialog, SharedPreferences sharedPreferences) {
            this.userEmail = userEmail;
            this.userPassword = userPassword;
            this.progressDialog = progressDialog;
            this.sharedPreferences = sharedPreferences;
        }
    }

    public static class LoginUserResponse extends ServiceResponse{

    }

    public static class LogUserInFacebookRequest{

        public AccessToken accessToken;
        public ProgressDialog progressDialog;
        public String userName;
        public String userEmail;
        public SharedPreferences sharedPreferences;

        public LogUserInFacebookRequest(AccessToken accessToken, ProgressDialog progressDialog,
                                        String userName, String userEmail, SharedPreferences sharedPreferences) {
            this.accessToken = accessToken;
            this.progressDialog = progressDialog;
            this.userName = userName;
            this.userEmail = userEmail;
            this.sharedPreferences = sharedPreferences;
        }
    }
}
