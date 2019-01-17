package com.example.abhishekaryan.shoppinglist.live;

import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Entities.Items;
import com.example.abhishekaryan.shoppinglist.Infrastructure.ShoppingListApplication;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.Services.ItemListService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class LiveAddItemService extends BaseLiveService {
    public LiveAddItemService(ShoppingListApplication application) {
        super(application);
    }


    @Subscribe
    public void OnItemListAddRequest(ItemListService.AddItemRequest request){


        ItemListService.AddItemResponse response=new ItemListService.AddItemResponse();
        if(request.ListName.trim().isEmpty()){

            response.setPropertyErrors("error","Please fill the list item name");
        }

        if(response.didSucceed()){

            DatabaseReference reference=FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_ITEM_REFERNCE +
                               request.ItemId).push();

            DatabaseReference reference1=FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_REFERNCE +
                            Utils.encodeEmail(request.OwnerEmail) + "/" + request.ItemId);

            HashMap<String,Object> timeLastChanged=new HashMap<>();
            timeLastChanged.put("date",ServerValue.TIMESTAMP);

            Map newListData=new HashMap();
            newListData.put("dateListLastChanged",timeLastChanged);
            reference1.updateChildren(newListData); 

            Items items=new Items(reference.getKey(),request.ListName,request.OwnerEmail,"",false);

            reference.setValue(items);
            Toast.makeText(application.getApplicationContext(),
                    "List added ",Toast.LENGTH_SHORT).show();


        }


        bus.post(response);

    }

    @Subscribe
    public void OnItemListRemoveRequest(ItemListService.RemoveItemRequest request){

        ItemListService.RemoveItemResponse response=new ItemListService.RemoveItemResponse();


        DatabaseReference ListItemreference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_ITEM_REFERNCE +
                          request.ShoopingListId + "/" + request.ItemId);

        DatabaseReference reference1=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_REFERNCE +
                        Utils.encodeEmail(request.OwnerEmail) + "/" + request.ShoopingListId);

        ListItemreference.removeValue();

        HashMap<String,Object> timeLastChanged=new HashMap<>();
        timeLastChanged.put("date",ServerValue.TIMESTAMP);



        Map newListData=new HashMap();
        newListData.put("dateListLastChanged",timeLastChanged);
        reference1.updateChildren(newListData);
        Toast.makeText(application.getApplicationContext(),
                "List deleted ",Toast.LENGTH_SHORT).show();

    }


    @Subscribe
    public void ChangeItemBoughtStatus(ItemListService.ChangeBoughtItemStatusRequest request){


        DatabaseReference ListItemreference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_SHOPPING_LIST_ITEM_REFERNCE +
                        request.ShoopingListId + "/" + request.items.getId());


        if(!request.items.isBought()){

            Map newListData=new HashMap();
            newListData.put("boughtBy",request.UserEmail);
            newListData.put("bought",true);
            ListItemreference.updateChildren(newListData);


        }

        else if(request.items.getBoughtBy().equals(request.UserEmail)){
            Map newListData=new HashMap();
            newListData.put("boughtBy","");
            newListData.put("bought",false);
            ListItemreference.updateChildren(newListData);

        }


    }
}
