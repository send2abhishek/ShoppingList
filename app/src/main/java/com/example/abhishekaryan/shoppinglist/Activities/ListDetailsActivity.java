package com.example.abhishekaryan.shoppinglist.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhishekaryan.shoppinglist.Dialog.AddDialogItemIntoList;
import com.example.abhishekaryan.shoppinglist.Dialog.ChangeListNameDialogFragment;
import com.example.abhishekaryan.shoppinglist.Dialog.DeleteDialogFragment;
import com.example.abhishekaryan.shoppinglist.Dialog.RemoveDialogFragment;
import com.example.abhishekaryan.shoppinglist.Entities.Items;
import com.example.abhishekaryan.shoppinglist.Entities.ShoppingList;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.R;
import com.example.abhishekaryan.shoppinglist.Services.ItemListService;
import com.example.abhishekaryan.shoppinglist.Services.ShoppingListService;
import com.example.abhishekaryan.shoppinglist.Views.ShoppingItemViews.ListItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

public class ListDetailsActivity extends BaseActivity implements View.OnClickListener {


    public static final String LIST_DETAILS="LIST_DETAILS";
    private String listName;
    private ShoppingList shoppingList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    private FloatingActionButton addItem;
    private RecyclerView recyclerViewListItem;
    private FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        addItem=(FloatingActionButton)findViewById(R.id.list_details_floating_btn);
        addItem.setOnClickListener(this);

        recyclerViewListItem=(RecyclerView)findViewById(R.id.list_details_add_recylerView);

        shoppingList =getIntent().getParcelableExtra(LIST_DETAILS);
        databaseReference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_REFERNCE +
                        Utils.encodeEmail(shoppingList.getOwnerEmail()) + "/" + shoppingList.getId());

        bus.post(new ShoppingListService.changeTitleActivityRequest(databaseReference));
        listName=shoppingList.getListName();
        getSupportActionBar().setTitle(listName);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference reference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_ITEM_REFERNCE +
                        "/" + shoppingList.getId());

        FirebaseRecyclerOptions<Items> options =
                new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(reference, Items.class)
                        .build();
        adapter=new FirebaseRecyclerAdapter<Items, ListItemViewHolder>(options) {

            @NonNull
            @Override
            public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(ListDetailsActivity.this).inflate(R.layout.list_shopping_item, parent, false);

                return new ListItemViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ListItemViewHolder holder, int position, @NonNull final Items model) {

                holder.Populate(model, ListDetailsActivity.this, userEmail);
                holder.listItemItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callRemoveDialog(model, shoppingList);
                    }
                });


                holder.newListItemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bus.post(new ItemListService.ChangeBoughtItemStatusRequest(model,userEmail,shoppingList.getId()));
                    }
                });
            }
        };



        adapter.startListening();
        recyclerViewListItem.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListItem.setAdapter(adapter);
    }

    private void callRemoveDialog(Items model, ShoppingList shoppingList) {

        FragmentManager manager=(ListDetailsActivity.this).getFragmentManager();
        RemoveDialogFragment fragment=RemoveDialogFragment.newInstance(model,shoppingList);
        fragment.show(manager,RemoveDialogFragment.class.getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.list_details_activity_menu,menu);

       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id){

            case R.id.list_details_menu_edit:
                FragmentManager manager=getFragmentManager();
                ChangeListNameDialogFragment fragment=ChangeListNameDialogFragment.newInstance(shoppingList);
                fragment.show(manager,ChangeListNameDialogFragment.class.getSimpleName());
                return true;

            case R.id.list_details_menu_del:
                DialogFragment dialogFragment=DeleteDialogFragment.newInstance(shoppingList,false);
                dialogFragment.show(getFragmentManager(),DeleteDialogFragment.class.getSimpleName());
                return true;

            case R.id.list_details_menu_share:

                startActivity(new Intent(this,shareListActivity.class));
                return true;

        }

        return true;
    }


    @Subscribe
    public void OnListTitleUpdate(ShoppingListService.changeTitleActivityResponse response){

        eventListener=response.valueEventListener;
        shoppingList=response.shoppingList;
        getSupportActionBar().setTitle(shoppingList.getListName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }

    @Override
    public void onClick(View v) {


        FragmentManager manager=getFragmentManager();

        AddDialogItemIntoList addDialogItemIntoList=AddDialogItemIntoList.newInstance(shoppingList);
        addDialogItemIntoList.show(manager,AddDialogItemIntoList.class.getSimpleName());

    }
}
