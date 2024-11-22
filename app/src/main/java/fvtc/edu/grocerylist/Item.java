package fvtc.edu.grocerylist;

import android.util.Log;

public class Item {
    public static final String TAG = "Item";
    private String Owner;
    private int Id;
    private String Description;
    private boolean IsOnShoppingList;
    private boolean IsInCart;
    private int ImgId;
    public Item(int id,
                String description,
                boolean isOnShoppingList,
                boolean isInCart,
                String owner,
                int imgId){
        Id = id;
        Description = description;
        IsOnShoppingList = isOnShoppingList;
        IsInCart = isInCart;
        Owner = owner;
        ImgId = imgId;
    }
    public Item(){
        Id = -1;
        Description = "";
        IsOnShoppingList = false;
        IsInCart = false;
        Owner = null;
        ImgId = 0;
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

    public int getImgId() {
        return ImgId;
    }
    public void setImgId(int imgId) { ImgId = imgId; }
}
