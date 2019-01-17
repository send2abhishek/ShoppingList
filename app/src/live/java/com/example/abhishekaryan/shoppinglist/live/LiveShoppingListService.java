package com.example.abhishekaryan.shoppinglist.live;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Entities.ShoppingList;
import com.example.abhishekaryan.shoppinglist.Infrastructure.ShoppingListApplication;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.Services.ShoppingListService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class LiveShoppingListService extends BaseLiveService {
    public LiveShoppingListService(ShoppingListApplication application) {
        super(application);
    }


    @Subscribe
    public void AddShoppingList(ShoppingListService.AddShoppingListRequest request){

        ShoppingListService.AddShoppingListResponse response=new ShoppingListService.AddShoppingListResponse();
        if(request.ShoppingListName.isEmpty()){

            response.setPropertyErrors("listName","Shopping List must have a name");
        }

        if(response.didSucceed()){



            DatabaseReference reference=FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_REFERNCE +  Utils.encodeEmail(request.ownerEmail)).push();

            HashMap<String,Object> timeStampedCreated=new HashMap<>();
            timeStampedCreated.put("timestamp",ServerValue.TIMESTAMP);
            ShoppingList shoppingList=new ShoppingList(reference.getKey(),request.ShoppingListName,
                    request.ownerName,Utils.decodeEmail(request.ownerEmail),timeStampedCreated);

            reference.child("id").setValue(shoppingList.getId());
            reference.child("listName").setValue(shoppingList.getListName());
            reference.child("ownerName").setValue(shoppingList.getOwnerName());
            reference.child("ownerEmail").setValue(shoppingList.getOwnerEmail());
            reference.child("dateListCreated").setValue(shoppingList.getDateListCreated());
            reference.child("dateListLastChanged").setValue(shoppingList.getDateListLastChanged());
            Toast.makeText(application.getApplicationContext(),
                    "List added ",Toast.LENGTH_SHORT).show();

        }

        bus.post(response);
    }


    @Subscribe
    public void DeleteShoppingList(ShoppingListService.DeleteShoppingListRequest request){
        DatabaseReference reference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_REFERNCE +
                        Utils.encodeEmail(request.ownerEmail) + "/" + request.ShoppingListId);
        reference.removeValue();
        DatabaseReference reference1=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_ITEM_REFERNCE + request.ShoppingListId);
        reference1.removeValue();


    }

    @Subscribe
    public void ChangeShoppingListName(ShoppingListService.ChangeShoppingListRequest request){

        ShoppingListService.ChangeShoppingListResponse response=new ShoppingListService.ChangeShoppingListResponse();

        if(request.newShoppingListName.isEmpty()){

            response.setPropertyErrors("newName","Please enter new Shooping List name");
        }

        if(response.didSucceed()){

            DatabaseReference reference=FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_REFERNCE +
                            Utils.encodeEmail(request.ownerEmail) + "/" + request.ShoppingListId);

            HashMap<String,Object> timeLastChanged=new HashMap<>();
            timeLastChanged.put("date",ServerValue.TIMESTAMP);

            Map newListData=new HashMap();
            newListData.put("listName",request.newShoppingListName);
            newListData.put("dateListLastChanged",timeLastChanged);
            reference.updateChildren(newListData);
        }

        bus.post(response);
    }


    @Subscribe
    public void ChangeTitleRequest(ShoppingListService.changeTitleActivityRequest request){

        final ShoppingListService.changeTitleActivityResponse response=new ShoppingListService.changeTitleActivityResponse();

        response.valueEventListener=request.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                response.shoppingList=dataSnapshot.getValue(ShoppingList.class);
                if(response.shoppingList!=null){

                    bus.post(response);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(application.getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }



}
