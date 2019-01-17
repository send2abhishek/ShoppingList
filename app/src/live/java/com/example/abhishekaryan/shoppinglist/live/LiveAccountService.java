package com.example.abhishekaryan.shoppinglist.live;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Activities.LoginActivity;
import com.example.abhishekaryan.shoppinglist.Activities.MainActivity;
import com.example.abhishekaryan.shoppinglist.Entities.user;
import com.example.abhishekaryan.shoppinglist.Infrastructure.ShoppingListApplication;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.Services.AccountServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

public class LiveAccountService extends BaseLiveService {
    public LiveAccountService(ShoppingListApplication application) {
        super(application);
    }


    @Subscribe
    public void RegisterUser(final AccountServices.RegisterUserRequest request){

        AccountServices.RegisterUserResponse response=new AccountServices.RegisterUserResponse();
        if(request.UserName.isEmpty()){

            response.setPropertyErrors("username","Please enter the username");
        }

        if(request.Email.isEmpty()){

            response.setPropertyErrors("email","Please enter the email");
        }

        if(response.didSucceed()){

            request.progressDialog.show();

            SecureRandom secureRandom=new SecureRandom();
            final String randomPassword=new BigInteger(32,secureRandom).toString();

            auth.createUserWithEmailAndPassword(request.Email,randomPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){

                                Toast.makeText(application.getApplicationContext(),task.getException()
                                        .getMessage(),Toast.LENGTH_SHORT).show();

                            }

                            else {

                                auth.sendPasswordResetEmail(request.Email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(!task.isSuccessful()) {
                                                    Toast.makeText(application.getApplicationContext(),
                                                            task.getException()
                                                            .getMessage(), Toast.LENGTH_SHORT).show();
                                                    request.progressDialog.dismiss();
                                                }

                                                else {

                                                    DatabaseReference reference=FirebaseDatabase.getInstance()
                                                            .getReferenceFromUrl(Utils.FIREBASE_USER_REFERNCE +

                                                             Utils.encodeEmail(request.Email)
                                                            );


                                                    HashMap<String,Object> timeJoined=new HashMap<>();
                                                    timeJoined.put("dateJoined",ServerValue.TIMESTAMP);
                                                    reference.child("email").setValue(request.Email);
                                                    reference.child("name").setValue(request.UserName);
                                                    reference.child("hasLoggedInWithPassword").setValue(false);
                                                    reference.child("timeJoined").setValue(timeJoined);

                                                    Toast.makeText(application.getApplicationContext(),
                                                            "Please check your Email"
                                                            , Toast.LENGTH_SHORT).show();
                                                    request.progressDialog.dismiss();

                                                    Intent intent=new Intent(application.getApplicationContext()
                                                            ,LoginActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                            Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    application.startActivity(intent);

                                                }
                                            }



                                        });
                            }

                        }
                    });

            Toast.makeText(application.getApplicationContext(),"User will register shortly",
                    Toast.LENGTH_SHORT).show();
        }

        bus.post(response);
    }

    @Subscribe
    public void LoginUser(final AccountServices.LoginUserRequest request){

        AccountServices.LoginUserResponse response=new AccountServices.LoginUserResponse();
        if(request.userEmail.isEmpty()){

            response.setPropertyErrors("email","Please enter email");
        }
        if(request.userPassword.isEmpty()){

            response.setPropertyErrors("password","Please enter password");
        }

        if(response.didSucceed()){
            request.progressDialog.show();
            auth.signInWithEmailAndPassword(request.userEmail,request.userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(application.getApplicationContext(),
                                        task.getException()
                                                .getMessage(), Toast.LENGTH_SHORT).show();
                                request.progressDialog.dismiss();

                            }

                            else {
                                final DatabaseReference reference=FirebaseDatabase.getInstance()
                                        .getReferenceFromUrl(Utils.FIREBASE_USER_REFERNCE +

                                                Utils.encodeEmail(request.userEmail));

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        user data=dataSnapshot.getValue(user.class);

                                        if(data!=null) {
                                            reference.child("hasLoggedInWithPassword").setValue(true);

                                            SharedPreferences sharedPreferences = request.sharedPreferences;
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            editor.putString(Utils.USERNAME,data.getName());
                                            editor.putString(Utils.EMAIL,data.getEmail());
                                            editor.commit();

                                            request.progressDialog.dismiss();
                                            Intent intent=new Intent(application.getApplicationContext()
                                                    ,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                            application.getApplicationContext().startActivity(intent);
                                        }

                                        else {
                                            request.progressDialog.dismiss();
                                            Toast.makeText(application.getApplicationContext(),
                                                    "Something bad happen", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        request.progressDialog.dismiss();
                                        Toast.makeText(application.getApplicationContext(),
                                                databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });



                            }

                        }
                    });



        }
        bus.post(response);
    }

    @Subscribe
    public void FaceBookLogin(final AccountServices.LogUserInFacebookRequest request){

        request.progressDialog.show();
        AuthCredential authCredential=FacebookAuthProvider.getCredential(request.accessToken.getToken());
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){

                            Toast.makeText(application.getApplicationContext(),
                                    task.getException()
                                            .getMessage(), Toast.LENGTH_SHORT).show();
                            request.progressDialog.dismiss();
                        }

                        else {

                            final DatabaseReference reference=FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl(Utils.FIREBASE_USER_REFERNCE +

                                            Utils.encodeEmail(request.userEmail));

                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue()==null){
                                        HashMap<String,Object> timeJoined=new HashMap<>();
                                        timeJoined.put("dateJoined",ServerValue.TIMESTAMP);
                                        reference.child("email").setValue(request.userEmail);
                                        reference.child("name").setValue(request.userName);
                                        reference.child("hasLoggedInWithPassword").setValue(true);
                                        reference.child("timeJoined").setValue(timeJoined);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    Toast.makeText(application.getApplicationContext(),
                                            databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    request.progressDialog.dismiss();

                                }
                            });
                            SharedPreferences sharedPreferences = request.sharedPreferences;
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Utils.USERNAME,request.userName);
                            editor.putString(Utils.EMAIL,request.userEmail);
                            editor.commit();

                            request.progressDialog.dismiss();
                            Intent intent=new Intent(application.getApplicationContext()
                                    ,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            application.startActivity(intent);


                        }

                    }
                });
    }
}
