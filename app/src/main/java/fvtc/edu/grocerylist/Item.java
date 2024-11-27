package fvtc.edu.grocerylist;

import android.graphics.Bitmap;
import android.util.Log;

public class Item {
    public static final String TAG = "Item";
    private int id;
    private String description;
    private boolean isOnShoppingList;
    private boolean isInCart;
    private String owner;
    private Double latitude;
    private Double longitude;
    private Bitmap photo;
    public Item(int id,
                String description,
                boolean isOnShoppingList,
                boolean isInCart,
                String owner,
                Double latitude,
                Double longitude,
                Bitmap photo){
        this.id = id;
        this.description = description;
        this.isOnShoppingList = isOnShoppingList;
        this.isInCart = isInCart;
        this.owner = owner;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
    }
    public Item(){
        this.id = -1;
        this.description = "";
        this.isOnShoppingList = false;
        this.isInCart = false;
        this.owner = null;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.photo = null;
    }
    public String toString(){ return id + "|" + description + "|" + isOnShoppingList + "|" + isInCart;}

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOnShoppingList() {
        return isOnShoppingList;
    }
    public void setOnShoppingList(boolean onShoppingList) {
        this.isOnShoppingList = onShoppingList;
    }

    public boolean isInCart() {
        return isInCart;
    }
    public void setInCart(boolean inCart) { this.isInCart = inCart; }

    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

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

    public Bitmap getPhoto() { return photo; }
    public void setPhoto(Bitmap photo) { this.photo = photo; }
}
