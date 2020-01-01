package com.example.abhishekaryan.shoppinglist.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.abhishekaryan.shoppinglist.Infrastructure.ShoppingListApplication;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Bus;

public class BaseActivity extends AppCompatActivity {

    protected ShoppingListApplication application;
    protected Bus bus;
    protected FirebaseAuth mauth;
    protected FirebaseAuth.AuthStateListener authStateListener;
    protected String userEmail,userName;
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application=(ShoppingListApplication)getApplication();
        bus=application.getBus();
        bus.register(this);
        sharedPreferences=getSharedPreferences(Utils.SHARED_PREFENCES,MODE_PRIVATE);
        userEmail=sharedPreferences.getString(Utils.EMAIL,"");
        userName=sharedPreferences.getString(Utils.USERNAME,"");
        mauth=FirebaseAuth.getInstance();

        if(!(this instanceof LoginActivity) || (this instanceof RegisterActivity) ||
                (this instanceof SplashScreenActivity)) {
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if(user==null ){


//                        SharedPreferences.Editor editor=sharedPreferences.edit();
//                        editor.putString(Utils.USERNAME,null);
//                        editor.putString(Utils.EMAIL,null);
//                        editor.commit();
//
//                        Intent intent=new Intent(getApplicationContext()
//                                ,LoginActivity.class);
//                        startActivity(intent);
//                        finish();




                    }

                    else {

//                        mauth.signOut();
//                        Intent intent=new Intent(getApplicationContext()
//                                ,RegisterActivity.class);
//                        startActivity(intent);
//                        finish();

                    }

                }
            };
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(!(this instanceof LoginActivity) || (this instanceof RegisterActivity) ||
                (this instanceof SplashScreenActivity)) {

            mauth.addAuthStateListener(authStateListener);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        if(!(this instanceof LoginActivity) || (this instanceof RegisterActivity) ||
                (this instanceof SplashScreenActivity)) {

            mauth.removeAuthStateListener(authStateListener);

        }

    }
    @Override
    public void onBackPressed() {

        finish();
    }

//    private void printKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.i("KeyHash:",
//                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.e("jk", "Exception(NameNotFoundException) : " + e);
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("mkm", "Exception(NoSuchAlgorithmException) : " + e);
//        }
//    }

}
