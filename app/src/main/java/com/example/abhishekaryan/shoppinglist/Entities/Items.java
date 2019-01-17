package com.example.abhishekaryan.shoppinglist.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {

    private String id;
    private String itemName;
    private String ownerEmail;
    private String boughtBy;
    private boolean bought;

    public Items() {
    }

    public Items(String id, String itemName, String ownerEmail, String boughtBy, boolean bought) {
        this.id = id;
        this.itemName = itemName;
        this.ownerEmail = ownerEmail;
        this.boughtBy = boughtBy;
        this.bought = bought;
    }

    protected Items(Parcel in) {
        id = in.readString();
        itemName = in.readString();
        ownerEmail = in.readString();
        boughtBy = in.readString();
        bought = in.readByte() != 0;
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getBoughtBy() {
        return boughtBy;
    }

    public boolean isBought() {
        return bought;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(itemName);
        dest.writeString(ownerEmail);
        dest.writeString(boughtBy);
        dest.writeByte((byte) (bought ? 1 : 0));
    }
}
