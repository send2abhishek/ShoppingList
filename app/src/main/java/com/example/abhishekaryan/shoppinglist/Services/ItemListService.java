package com.example.abhishekaryan.shoppinglist.Services;

import com.example.abhishekaryan.shoppinglist.Entities.Items;
import com.example.abhishekaryan.shoppinglist.Infrastructure.ServiceResponse;

public class ItemListService {

    private ItemListService() {
    }


    public static class AddItemRequest{

        public String ItemId;
        public String ListName;
        public String OwnerEmail;

        public AddItemRequest(String itemId, String listName, String ownerEmail) {
            ItemId = itemId;
            ListName = listName;
            OwnerEmail = ownerEmail;
        }
    }

    public static class AddItemResponse extends ServiceResponse{

    }

    public static class RemoveItemRequest{
        public String ItemId;
        public String ShoopingListId;
        public String OwnerEmail;

        public RemoveItemRequest(String itemId, String shoopingListId, String ownerEmail) {
            ItemId = itemId;
            ShoopingListId = shoopingListId;
            OwnerEmail = ownerEmail;
        }
    }

    public static class RemoveItemResponse extends ServiceResponse{

    }

    public static class ChangeBoughtItemStatusRequest{

        public Items items;
        public String UserEmail;
        public String ShoopingListId;

        public ChangeBoughtItemStatusRequest(Items items, String userEmail,
                                             String shoopingListId) {
            this.items = items;
            UserEmail = userEmail;
            ShoopingListId = shoopingListId;
        }
    }
}
