package com.example.abhishekaryan.shoppinglist.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Dialog.AddDialogFragment;
import com.example.abhishekaryan.shoppinglist.Dialog.DeleteDialogFragment;
import com.example.abhishekaryan.shoppinglist.Entities.ShoppingList;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.R;
import com.example.abhishekaryan.shoppinglist.Views.ShoppingListViews.ShoppingListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.example.abhishekaryan.shoppinglist.Dialog.DeleteDialogFragment.EXTRA_BOOLEAN;
import static com.example.abhishekaryan.shoppinglist.Dialog.DeleteDialogFragment.EXTRA_SHOPPING_LIST_ID;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private String toolbarName;
    private ProgressDialog progressDialog;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth=FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();

        if(user==null) {


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Utils.USERNAME, null);
            editor.putString(Utils.EMAIL, null);
            editor.commit();
            Toast.makeText(MainActivity.this,"User is null",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext()
                    , LoginActivity.class);
            startActivity(intent);
            finish();
        }

        floatingActionButton=(FloatingActionButton)findViewById(R.id.activity_main_floating_btn);
        floatingActionButton.setOnClickListener(this);
        recyclerView=(RecyclerView)findViewById(R.id.activity_main_recylerView);

       if(userName.contains(" ")){

           toolbarName=userName.substring(0,userName.indexOf(" ")) + "'s Shooping List";
       }
       else {

           toolbarName=userName + "'s Shooping List";
       }
       getSupportActionBar().setTitle(toolbarName);
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        String data=sharedPreferences.getString(Utils.LIST_ORDER_PREFERENCES,Utils.ORDER_BY_KEY);
        Log.i("Abhishek Aryan","the value is" +data);





        DatabaseReference reference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_REFERNCE +  Utils.encodeEmail(userEmail));

        Query query;
        if(data.equals(Utils.ORDER_BY_KEY)){
            query=reference.orderByKey();
        }
        else {
            query=reference.orderByChild(data);
        }


        FirebaseRecyclerOptions<ShoppingList> options =
                new FirebaseRecyclerOptions.Builder<ShoppingList>()
                        .setQuery(query, ShoppingList.class)
                        .build();

        adapter= new FirebaseRecyclerAdapter<ShoppingList, ShoppingListViewHolder>(options) {
            @NonNull
            @Override
            public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.list_shopping_list,parent,false);

                return new ShoppingListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ShoppingListViewHolder holder,
                                            int position, @NonNull final ShoppingList model) {


                holder.listPopulate(model);
                holder.listShoppingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ShoppingList list=model;

                        Intent intent=new Intent(MainActivity.this,ListDetailsActivity.class);
                        intent.putExtra(ListDetailsActivity.LIST_DETAILS,list);
                        startActivity(intent);

                    }
                });

                holder.listShoppingView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if(userEmail.equals(model.getOwnerEmail())){


                            callDelDialog(model);
                            return true;

                        }
                        else {

                            Toast.makeText(MainActivity.this,"Only Owner Can delete the list",
                                    Toast.LENGTH_SHORT).show();

                            return true;
                        }


                    }
                });
            }
        };

        adapter.startListening();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




    }

    private void callDelDialog(ShoppingList model) {
        FragmentManager fragmentManager=(MainActivity.this).getFragmentManager();
        DeleteDialogFragment dialogFragment=DeleteDialogFragment.newInstance(model,true);
        dialogFragment.show(fragmentManager,DeleteDialogFragment.class.getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.stopListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.activity_main_menu_title:

//                progressDialog=new ProgressDialog(this);
//                progressDialog.setMessage("Attempting to Login in your Account");
//                progressDialog.setTitle("Loading....");
//                progressDialog.setCancelable(false);
                SharedPreferences sharedPreferences1=getSharedPreferences(Utils.SHARED_PREFENCES,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences1.edit();
                editor.putString(Utils.USERNAME,null);
                editor.putString(Utils.EMAIL,null);
                editor.commit();
                mauth.signOut();
                Intent intent=new Intent(getApplicationContext()
                        ,LoginActivity.class);
                startActivity(intent);
                finish();
                //progressDialog.dismiss();

            case R.id.activity_main_sort:
                startActivity(new Intent(this,SettingsActivity.class));
        }

        return true;
    }
    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onClick(View v) {


        AddDialogFragment dialogFragment=AddDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(),AddDialogFragment.class.getSimpleName());

    }
}
