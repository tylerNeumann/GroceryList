package fvtc.edu.grocerylist;

import java.util.ArrayList;

public class Owner {
    private String Name;
    private ArrayList<Item> Items;
    public Owner(){
        Name = "";
        Items = new ArrayList<>();
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Item> getItems() {
        return Items;
    }
    public void setItems(ArrayList<Item> items) {
        Items = items;
    }

    public int getItemsCount() { return Items.size(); }
}
