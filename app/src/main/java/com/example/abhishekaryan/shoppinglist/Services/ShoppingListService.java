package com.example.abhishekaryan.shoppinglist.Services;

import com.example.abhishekaryan.shoppinglist.Entities.ShoppingList;
import com.example.abhishekaryan.shoppinglist.Infrastructure.ServiceResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ShoppingListService {

    private ShoppingListService() {
    }

    public static class AddShoppingListRequest {

        public String ShoppingListName;
        public String ownerName;
        public String ownerEmail;

        public AddShoppingListRequest(String shoppingListName, String ownerName, String ownerEmail) {
            ShoppingListName = shoppingListName;
            this.ownerName = ownerName;
            this.ownerEmail = ownerEmail;
        }
    }
    public static class AddShoppingListResponse extends ServiceResponse{

    }

    public static class DeleteShoppingListRequest {

        public String ShoppingListId;
        public String ownerEmail;

        public DeleteShoppingListRequest(String shoppingListId, String ownerEmail) {
            ShoppingListId = shoppingListId;
            this.ownerEmail = ownerEmail;
        }
    }
    public static class DeleteShoppingListResponse extends ServiceResponse{

    }

    public static class ChangeShoppingListRequest {

        public String newShoppingListName;
        public String ShoppingListId;
        public String ownerEmail;

        public ChangeShoppingListRequest(String newShoppingListName, String shoppingListId, String ownerEmail) {
            this.newShoppingListName = newShoppingListName;
            ShoppingListId = shoppingListId;
            this.ownerEmail = ownerEmail;
        }
    }
    public static class ChangeShoppingListResponse extends ServiceResponse{

    }
    public static class changeTitleActivityRequest{

        public DatabaseReference reference;

        public changeTitleActivityRequest(DatabaseReference reference) {
            this.reference = reference;
        }
    }

    public static class changeTitleActivityResponse{

        public ShoppingList shoppingList;
        public ValueEventListener valueEventListener;
    }
}
