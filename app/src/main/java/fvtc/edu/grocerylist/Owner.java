package fvtc.edu.grocerylist;
public class Owner {
    String Name;
    int ItemCount;

    public Owner(String name, int itemCount) {
        Name = name;
        ItemCount = itemCount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }
}
