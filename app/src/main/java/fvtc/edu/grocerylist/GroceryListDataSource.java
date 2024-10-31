package fvtc.edu.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class GroceryListDataSource {
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    ArrayList<Item> GroceryList = new ArrayList<Item>();
    public static final String TAG = "GroceryListDataSource";

    public GroceryListDataSource(Context context) {
        Log.d(TAG, "GroceryListDataSource: ");
        dbHelper = new DatabaseHelper(context,
                DatabaseHelper.DATABASE_NAME,
                null,
                DatabaseHelper.DATABASE_VERSION);
        open();
    }
    public void open() throws SQLException {
        open(false);
    }

    public void open(boolean refresh)  throws SQLException{
        boolean freshApp = true;
        db = dbHelper.getWritableDatabase();
        Log.d(TAG, "open: " + db.isOpen());
        if(!freshApp) refreshData();
        //if(refresh) refreshData();

    }

    public void close()
    {
        dbHelper.close();
    }
    public void refreshData()
    {
        if(!GroceryList.isEmpty()){
            deleteAll();
            Log.d(TAG, "refreshData: Start");
            GroceryList.add(new Item(1, "Protein Shake", false, false));
            GroceryList.add(new Item(2, "Pop Tarts", false, false));
            GroceryList.add(new Item(3, "Mtn Dew", false, false));
            GroceryList.add(new Item(4, "Pretzels", false, false));
            GroceryList.add(new Item(5, "Shampoo", false, false));
            GroceryList.add(new Item(6, "Cheese", false, false));

            // Delete and reinsert all the GroceryList
            int results = 0;
            for(Item item : GroceryList){
                results += insert(item);
            }
            Log.d(TAG, "refreshData: End: " + results + " rows...");
        }
    }

    public Item get(int id)
    {
        Item item = null;

        try{
            String query = "Select * from tblGroceryList where id = " + id;
            Cursor cursor = db.rawQuery(query, null);

            //Cursor cursor = db.query("tblGroceryList",null, null, null, null, null, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setDescription(cursor.getString(1));
                boolean onShoppingList = cursor.getInt(2) == 1;
                item.setOnShoppingList(onShoppingList);
                boolean inCart = cursor.getInt(3) == 1;
                item.setInCart(inCart);


                //item.setLatitude(cursor.getDouble(7));
                //item.setLongitude(cursor.getDouble(8));

                Log.d(TAG, "get: " + item.toString());

                cursor.moveToNext();

            }
        }
        catch(Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return item;
    }

    public ArrayList<Item> get(String sortBy, String sortOrder)
    {
        Log.d(TAG, "get: Start");

        try {
            String sql = "SELECT * from tblGroceryList ORDER BY " + sortBy + " " + sortOrder;
            Cursor cursor = db.rawQuery(sql, null);
            Item item;
            cursor.moveToFirst();
            ArrayList<String> descriptions = new ArrayList<String>();
            while(!cursor.isAfterLast())
            {
                Log.d(TAG, "get: entered while loop");
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setDescription(cursor.getString(1));
                boolean onShoppingList = cursor.getInt(2) == 1;
                item.setOnShoppingList(onShoppingList);
                boolean inCart = cursor.getInt(3) == 1;
                item.setInCart(inCart);


                GroceryList.add(item);
                Log.d(TAG, "get: " + item.toString());
                cursor.moveToNext();
            }
            Log.d(TAG, "get: end");
        }
        catch(Exception e)
        {
            Log.d(TAG, "get: error");
            Log.e(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }

        return GroceryList;
    }

    public int deleteAll()
    {
        Log.d(TAG, "deleteAll: ");
        try{
            return db.delete("tblGroceryList", null, null);
        }
        catch(Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Item item)
    {
        Log.d(TAG, "delete: Start item");
        try{
            int id = item.getId();
            if(id < 1)
                return 0;
            Log.d(TAG, "delete: " + id);
            return delete(id);
        }
        catch(Exception e)
        {
            Log.d(TAG, "Delete: failed: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(int id)
    {
        try{
            Log.d(TAG, "delete: Start : " + id);
            Log.d(TAG, "delete: database " + db.isOpen());
            int rowsaffected = db.delete("tblGroceryList", "id = " + id, null);
            Log.d(TAG, "delete: rowsaffected: " + rowsaffected);
            return rowsaffected;
        }
        catch(Exception e)
        {
            Log.d(TAG, "Delete: error" + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int getNewId()
    {
        int newId =-1;
        try{
            // Get the highest id in the table and add 1
            String sql = "SELECT max(id) from tblGroceryList";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            newId = cursor.getInt(0) + 1;
            cursor.close();
        }
        catch(Exception e)
        {

        }
        return newId;
    }

    public int update(Item item)
    {
        Log.d(TAG, "update: Start " + item.toString());
        int rowsaffected = 0;

        if(item.getId() < 1)
            return insert(item);

        try{
            ContentValues values = new ContentValues();
            values.put("description", item.getDescription());
            values.put("isOnShoppingList", item.isOnShoppingList());
            values.put("isInCart", item.isInCart());
            //values.put("latitude", item.getLatitude());
            //values.put("longitude", item.getLongitude());

            String where = "id = " + item.getId();
            rowsaffected = (int)db.update("tblGroceryList", values, where, null);
            Log.i(TAG, "update: end");
        }
        catch(Exception e)
        {
            Log.i(TAG, "update: hit error");
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }
        Log.d(TAG, "update: rowsaffected: " + rowsaffected);
        return rowsaffected;
    }
    public int insert(Item item)
    {
        Log.d(TAG, "insert: Start");
        int rowsaffected = 0;
        String rowAffected;
        try{
            if(db != null) {
                ContentValues values = new ContentValues();
                values.put("description", item.getDescription());
                values.put("isOnShoppingList", item.isOnShoppingList());
                values.put("isInCart", item.isInCart());

                rowsaffected = (int)db.insert("tblGroceryList", null, values);
            }
            else Log.d(TAG, "insert: db is null");
            
        }
        catch(Exception e)
        {
            Log.d(TAG, "insert: " + e.getMessage());
            e.printStackTrace();
        }
        return rowsaffected;
    }
    public ArrayList<Item> getShoppingList(String sortBy, String sortOrder)
    {
        ArrayList<Item> shoppingList = new ArrayList<>();
        Log.d(TAG, "get: get shopping list Start");

        try {
            String sql = "SELECT * from tblGroceryList WHERE IsOnShoppingList = 1 ORDER BY " + sortBy + " " + sortOrder;
            Cursor cursor = db.rawQuery(sql, null);
            Item item;
            cursor.moveToFirst();
            Log.d(TAG, "getShoppingList: cursor " + cursor.moveToFirst());
            while(!cursor.isAfterLast())
            {
                Log.d(TAG, "getShoppingList: entered while loop");
                item = new Item();
                item.setId(cursor.getInt(0));
                item.setDescription(cursor.getString(1));
                item.setOnShoppingList(true);
                boolean inCart = (cursor.getInt(3) == 1);
                item.setInCart(inCart);
                Log.d(TAG, "get: " + item.toString());

                shoppingList.add(item);

                cursor.moveToNext();
            }

        }
        catch(Exception e)
        {
            Log.d(TAG, "get: " + e.getMessage());
            e.printStackTrace();
        }

        Log.d(TAG, "getShoppingList: " + shoppingList.size());
        return shoppingList;
    }
}
