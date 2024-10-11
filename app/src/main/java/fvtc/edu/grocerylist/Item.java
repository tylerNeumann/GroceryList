package fvtc.edu.grocerylist;

public class Item {
    private int Id;
    private String Description;
    private String IsOnShoppingList;
    private String IsInCart;
    public Item(int id, String description, String isOnShoppingList, String isInCart){
        Id = id;
        Description = description;
        IsOnShoppingList = isOnShoppingList;
        IsInCart = isInCart;
    }
    public Item(){
        Id = -1;
        Description = "";
        IsOnShoppingList = "0";
        IsInCart = "0";
    }
    public String toString(){ return Id + "|" + Description + "|" + IsOnShoppingList + "|" + IsInCart;}

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

    public String isInCart() {
        return IsInCart;
    }
    public void setInCart(String inCart) {
        IsInCart = inCart;
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) { Id = id; }

    public boolean getIsOnShoppingList(){
        if(isOnShoppingList() == "1") return true;
        else return false;
    }
    public boolean getIsInCart(){
        if(isInCart() == "1") return true;
        else return false;
    }
}
