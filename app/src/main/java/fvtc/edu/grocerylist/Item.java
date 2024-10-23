package fvtc.edu.grocerylist;

public class Item {
    private int Id;
    private String Description;
    private boolean IsOnShoppingList;
    private boolean IsInCart;
    public Item(int id, String description, boolean isOnShoppingList, boolean isInCart){
        Id = id;
        Description = description;
        IsOnShoppingList = isOnShoppingList;
        IsInCart = isInCart;
    }
    public Item(){
        Id = -1;
        Description = "";
        IsOnShoppingList = false;
        IsInCart = false;
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
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) { Id = id; }

    /*public boolean getIsOnShoppingList(){
        if(isOnShoppingList() == "1") return true;
        else return false;
    }
    public boolean getIsInCart(){
        if(isInCart() == "1") return true;
        else return false;
    }*/
}
