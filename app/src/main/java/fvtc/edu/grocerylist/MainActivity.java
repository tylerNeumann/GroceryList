package fvtc.edu.grocerylist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String FILENAME = "MasterList.txt";

    public ItemAdapter itemAdapter;
    RecyclerView rvItems;
    ArrayList<Item> items;
    ArrayList<Item> shoppingList;
    private Context parentContext;
    public static String title;
    GroceryListDataSource ds;
    private CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "onCheckedChanged: ");
            /*itemAdapter = new ItemAdapter(items, parentContext);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(parentContext);
            rvItems.setLayoutManager(layoutManager);*/
            RecyclerView.ViewHolder viewHolder;
            viewHolder = (RecyclerView.ViewHolder) buttonView.getTag();
            int position = viewHolder.getAdapterPosition();
            if(getTitle().equals("Master List")){
                if(isChecked){
                    items.get(position).setOnShoppingList(true);
                    ds.update(items.get(position));
                }
                else {
                    items.get(position).setOnShoppingList(false);
                    ds.update(items.get(position));
                }
            }
            else if(getTitle().equals("Shopping List")){
                if(isChecked){
                    items.get(position).setInCart(true);
                    ds.update(items.get(position));
                }
                else {
                    items.get(position).setInCart(false);
                    ds.update(items.get(position));
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Master List");
        title = "Master List";
        parentContext = this;
        createItems();
        //rebind();
        deleteAll();

        Log.d(TAG, "onCreate: started program");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void initDatabase(){
        ds = new GroceryListDataSource(this);
        ds.open(false);
        ds.refreshData();
        //Log.d(TAG, "initDatabase: start");
        String sortBy = getSharedPreferences("grocerylistpreferences",
                Context.MODE_PRIVATE)
                .getString("sortby", "description");
        String sortOrder = getSharedPreferences("grocerylistpreferences",
                Context.MODE_PRIVATE)
                .getString("sortorder", "ASC");
        items = ds.get(sortBy, sortOrder);
        //Log.d(TAG, "initDatabase: Groceries: " + items.size());
    }
    private void createItems() {
        items = new ArrayList<Item>();
        Log.d(TAG, "createItems: items: " + items.size());
        initDatabase();

        if(items.isEmpty()) {
            fillItemsArray();
            int results = 0;
            for(Item item : items){
                // Log.d(TAG, "fillDB: start for loop");
                results += ds.insert(item);
                //Log.d(TAG, "fillDB: " + item);
            }
        }
        else items = ds.get("Description", "ASC");
    }
    public static String[] createDataArray(ArrayList<Item> items){
            String[] data = new String[items.size()];
            for (int count = 0; count < items.size(); count++){
                data[count] = items.get(count).toString();
            }
            return data;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        Log.d(TAG, "onCreateOptionsMenu: ");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_ShowMasterList)
        {
            Log.d(TAG, "onOptionsItemSelected: master list");
            setTitle("Master List");
            title = "Master List";
            rebind();
            CheckBox chkOnShoppingList = findViewById(R.id.chkOnShoppingList);
            //chkOnShoppingList.setOnCheckedChangeListener();
        }
        else if (id == R.id.action_ShowShoppingList) {
            //Log.d(TAG, "onOptionsItemSelected: shopping list " + shoppingList.size());
            setTitle("Shopping List");
            title = "Shopping List";
            rebind();
            Log.d(TAG, "ShowShoppingList: " + shoppingList.size());
        }
        else if (id == R.id.action_AddItem) {
            Log.d(TAG, "onOptionsItemSelected: add");
            addItem();
        }
        else if(id == R.id.action_ClearAll){
            Log.d(TAG, "onOptionsItemSelected: clear");
            clearAll();
        }
        else if(id == R.id.action_DeleteChecked){
            Log.d(TAG, "onOptionsItemSelected: delete");
            deleteChecked();
        }
        else {
            deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }
    public void fillItemsArray(){
        items.add(new Item(1, "Protein Shake", false, false));
        items.add(new Item(2, "Pop Tarts", false, false));
        items.add(new Item(3, "Mtn Dew", false, false));
        items.add(new Item(4, "Pretzels", false, false));
        items.add(new Item(5, "Shampoo", false, false));
        items.add(new Item(6, "Cheese", false, false));
    }
    public void fillDB(){
        for(Item item : items){
            ds.insert(item);
        }
    }

    private void addItem() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View addItemView = layoutInflater.inflate(R.layout.additem,null);

        //show dialogue to user modularly.
        new AlertDialog.Builder(this)
                .setTitle(R.string.add_item)
                .setView(addItemView)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: OK");
                                //get the new item
                                EditText etAddItem = addItemView.findViewById(R.id.etAddItem);
                                Item item = new Item();
                                item.setId(items.size() + 1);
                                item.setDescription(etAddItem.getText().toString());
                                item.setInCart(false);
                                if(getTitle() == "Master List"){
                                    item.setOnShoppingList(false);
                                }
                                else{
                                        item.setOnShoppingList(true);
                                    shoppingList.add(item);
                                    rvItems.setAdapter(itemAdapter);
                                }
                                Log.d(TAG, "onClick: add item: " + item);
                                ds.insert(item);
                                rebind();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: Cancel");
                            }
                        }).show();

    }
    public void deleteChecked(){
        boolean loopDone = false;
        if(getTitle() == "Master List"){

            for(Item item : items){
                if(item.isOnShoppingList()){
                    ds.delete(item.getId());
                }
            }
            items = ds.get("Description", "ASC");
            Log.d(TAG, "deleteChecked: size: " + items.size());
        }
        if(getTitle() == "Shopping List"){
            //Log.d(TAG, "deleteChecked: item removed");
            //set deleted items isInShoppingList == "0"
            int i = 0;
            Item item = new Item();

            for(int count = 0; count < shoppingList.size(); count++){
                //Log.d(TAG, "deleteChecked: entered loop");
                if(shoppingList.get(count).isInCart()) {
                    //Log.d(TAG, "deleteChecked: passed if");
                    item = shoppingList.get(count);
                    //Log.d(TAG, "deleteChecked: item: " + item);
                    i = item.getId();
                    //Log.d(TAG, "deleteChecked: i = " + i);
                    i -= 1;
                    //Log.d(TAG, "deleteChecked: item id: " + i);
                    items.get(i).setInCart(false);
                    items.get(i).setOnShoppingList(false);
                    //ds.update(item);
                    Log.d(TAG, "deleteChecked: reset item: " + items.get(i));
                }
                else Log.d(TAG, "deleteChecked: failed if");
            }
        }
        rebind();
    }


    public void rebind(){
        rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);

        if(getTitle() == "Master List"){
            Log.d(TAG, "rebind: hit master list");
            items = ds.get("Description", "ASC");
            itemAdapter = new ItemAdapter(items, this);
            itemAdapter.setOnItemCheckedChangeListener(onCheckedChangedListener);
        }
        if(getTitle() == "Shopping List"){
            Log.d(TAG, "rebind: hit shopping list");
            shoppingList = ds.getShoppingList("Description", "ASC");
            itemAdapter = new ItemAdapter(shoppingList, this);
            itemAdapter.setOnItemCheckedChangeListener(onCheckedChangedListener);
        }
        rvItems.setAdapter(itemAdapter);
    }
    public void clearAll(){
        if(getTitle() == "Master List"){
            int i = 0;
            Item item = new Item();

            for(int count = 0; count < items.size(); count++) {
                //Log.d(TAG, "deleteChecked: entered loop");
                if (items.get(count).isOnShoppingList()) {
                   // Log.d(TAG, "deleteChecked: passed if");
                    item = items.get(count);
                   // Log.d(TAG, "deleteChecked: item: " + item);
                    i = item.getId();
                    //Log.d(TAG, "deleteChecked: i = " + i);
                    i -= 1;
                    //Log.d(TAG, "deleteChecked: item id: " + i);
                    items.get(i).setOnShoppingList(false);
                    ds.update(items.get(i));

                    Log.d(TAG, "deleteChecked: reset item: " + items.get(i));


                } else Log.d(TAG, "deleteChecked: failed if");
            }
        }
        if(getTitle() == "Shopping List"){
            //Log.d(TAG, "deleteChecked: item removed");
            //set deleted items isInShoppingList == "0"
            int i = 0;
            Item item = new Item();

            for(int count = 0; count < shoppingList.size(); count++){
                //Log.d(TAG, "deleteChecked: entered loop");
                if(shoppingList.get(count).isInCart()) {
                    Log.d(TAG, "deleteChecked: passed if");
                    item = shoppingList.get(count);
                    Log.d(TAG, "deleteChecked: item: " + item);
                    i = item.getId();
                    Log.d(TAG, "deleteChecked: i = " + i);
                    i -= 1;
                    Log.d(TAG, "deleteChecked: item id: " + i);
                    items.get(i).setInCart(false);
                    //items.get(i).setOnShoppingList("0");
                    Log.d(TAG, "deleteChecked: reset item: " + items.get(i));


                }
                else Log.d(TAG, "deleteChecked: failed if");
            }

        }
        rebind();
    }
    public void deleteAll(){
        ds.deleteAll();
        fillItemsArray();
        fillDB();
        items.removeAll(items);
        rebind();
    }
}