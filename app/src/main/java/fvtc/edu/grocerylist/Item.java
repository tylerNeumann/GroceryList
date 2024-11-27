package fvtc.edu.grocerylist;

import android.graphics.Bitmap;
import android.util.Log;

public class Item {
    public static final String TAG = "Item";
    private int Id;
    private String Description;
    private boolean IsOnShoppingList;
    private boolean IsInCart;
    private String Owner;
    private Double latitude;
    private Double longitude;
    private Bitmap Photo;
    public Item(int id,
                String description,
                boolean isOnShoppingList,
                boolean isInCart,
                String owner,
                Double latitude,
                Double longitude,
                Bitmap Photo){
        Id = id;
        Description = description;
        IsOnShoppingList = isOnShoppingList;
        IsInCart = isInCart;
        Owner = owner;
    }
    public Item(){
        Id = -1;
        Description = "";
        IsOnShoppingList = false;
        IsInCart = false;
        Owner = null;
    }
    public String toString(){ return Id + "|" + Description + "|" + IsOnShoppingList + "|" + IsInCart;}

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public boolean isOnShoppingList() {
        return IsOnShoppingList;
    }
    public void setOnShoppingList(boolean onShoppingList) {
        IsOnShoppingList = onShoppingList;
    }

    public boolean isInCart() {
        return IsInCart;
    }
    public void setInCart(boolean inCart) { IsInCart = inCart; }

    public int getId() {
        return Id;
    }
    public void setId(int id) { Id = id; }

    public String getOwner() { return Owner; }
    public void setOwner(String owner) { Owner = owner; }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Bitmap getPhoto() { return Photo; }
    public void setPhoto(Bitmap photo) { Photo = photo; }
}
