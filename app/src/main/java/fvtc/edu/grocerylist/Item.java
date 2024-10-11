package fvtc.edu.grocerylist;

import android.util.Log;

import java.util.Objects;

public class Item {
    public static final String TAG = "ItemClass";
    private int Id;
    private String Description;
    private String IsOnShoppingList;
    private String IsInShoppingCart;
    private boolean IsOnList = false;
    private boolean IsInCart = false;
    public Item(int id, String description, String isOnShoppingList, String isInCart){
        Id = id;
        Description = description;
        IsOnShoppingList = isOnShoppingList;
        IsInShoppingCart = isInCart;
    }
    public Item(){
        Id = -1;
        Description = "";
        IsOnShoppingList = "0";
        IsInShoppingCart = "0";
    }
    public String toString(){ return Id + "|" + Description + "|" + IsOnShoppingList + "|" + IsInShoppingCart;}

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public String isOnShoppingList() {
        return IsOnShoppingList;
    }
    public void setOnShoppingList(String onShoppingList) {
        IsOnShoppingList = onShoppingList;
    }

    public String isInShoppingCart() {
        return IsInShoppingCart;
    }
    public void setInShoppingCart(String inCart) {
        IsInShoppingCart = inCart;
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) { Id = id; }

    public boolean getIsOnList(){
        Log.d(TAG, "getIsOnList: hit");
        if(Objects.equals(isOnShoppingList(), "1")) {
            Log.d(TAG, "getIsOnList: = " + isOnShoppingList());
            IsOnList = true;
            Log.d(TAG, "getIsOnList: IsOnList = " + IsOnList);
            return IsOnList;
        }
        else {
            Log.d(TAG, "getIsOnList: failed");
            IsOnList = false;
            Log.d(TAG, "getIsOnList: IsOnList = " + IsOnList);
            return IsOnList;
        }
    }
    public boolean getIsInCart(){
        Log.d(TAG, "getIsInCart: hit");
        if(Objects.equals(isInShoppingCart(), "1")) {
            Log.d(TAG, "getIsInCart: = " + isOnShoppingList());
            IsInCart = true;
            Log.d(TAG, "getIsInCart: IsInCart = " + IsInCart);
            return IsInCart;
        }
        else {
            Log.d(TAG, "getIsInCart: failed");
            IsInCart = false;
            Log.d(TAG, "getIsInCart: IsInCart = " + IsInCart);
            return IsInCart;
        }
    }
}
