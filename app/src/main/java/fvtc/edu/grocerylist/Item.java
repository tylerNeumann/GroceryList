package fvtc.edu.grocerylist;

import android.util.Log;

public class Item {
    public static final String TAG = "Item";
    private int Id;
    private String Description;
    private boolean IsOnShoppingList;
    private boolean IsInCart;
    private int imgId;
    public Item(int id,
                String description,
                boolean isOnShoppingList,
                boolean isInCart,
                int imgId){
        Id = id;
        Description = description;
        IsOnShoppingList = isOnShoppingList;
        IsInCart = isInCart;
        this.imgId = imgId;
    }
    public Item(){
        Id = -1;
        Description = "";
        IsOnShoppingList = false;
        IsInCart = false;
        this.imgId = 0;
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
    public void setInCart(boolean inCart) {
        IsInCart = inCart;
        Log.d(TAG, "setInCart: IsInCart = " + IsInCart);
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) { Id = id; }

    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) { this.imgId = imgId; }
}
