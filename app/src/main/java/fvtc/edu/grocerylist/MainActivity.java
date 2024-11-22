package fvtc.edu.grocerylist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static String owner = null;
    private String APIEnd;
    Item item;
    private CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "onCheckedChanged: ");
            RecyclerView.ViewHolder viewHolder;
            viewHolder = (RecyclerView.ViewHolder) buttonView.getTag();
            int position = viewHolder.getAdapterPosition();
            if(getTitle().equals("Master List for " + owner)){
                if(isChecked){
                    items.get(position).setOnShoppingList(true);
                    ds.update(items.get(position));
                    Log.i(TAG, "onCheckedChanged: " + items.get(position));
                }
                else {
                    items.get(position).setOnShoppingList(false);
                    ds.update(items.get(position));
                    Log.i(TAG, "onCheckedChanged: " + items.get(position));
                }
            }
            else if(getTitle().equals("Shopping List for " + owner)){
                if(isChecked){
                    Log.i(TAG, "onCheckedChanged: shopping list checked");
                    items.get(position).setInCart(true);
                    ds.update(items.get(position));
                    Log.i(TAG, "onCheckedChanged: " + items.get(position));
                }
                else {
                    Log.i(TAG, "onCheckedChanged: shopping list unchecked");
                    items.get(position).setInCart(false);
                    ds.update(items.get(position));
                    Log.i(TAG, "onCheckedChanged: " + items.get(position));
                }
            }
        }
    };
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            item = new Item();
            if(getTitle().equals("Master List for " + owner)){
                item = items.get(position);
                Log.i(TAG, "onClick: " + item.getDescription());
            }
            if(getTitle().equals("Shopping List for " + owner)){
                item = shoppingList.get(position);
                Log.i(TAG, "onClick: " + item.getDescription());
            }
            Intent intent = new Intent(MainActivity.this, ItemEditer.class);
            intent.putExtra("itemId", item.getId());
            intent.putExtra("itemDescription", item.getDescription());
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initialSetup();
        Log.d(TAG, "onCreate: started program");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        rebind();
    }
    private void initialSetup() {
        Log.i(TAG, "initialSetup: start");
        items = new ArrayList<Item>();
        shoppingList = new ArrayList<>();
        SharedPreferences preferences = getApplication().getSharedPreferences("myprefs", MODE_PRIVATE);
        owner = preferences.getString("owner", "");
        setTitle("Master List for " + owner);
        title = "Master List for " + owner;
        parentContext = this;
        if(owner == null){
            startActivity(new Intent(MainActivity.this, SetOwner.class));
        }
        ds = new GroceryListDataSource(this);
        ds.open(false);
        rebind();
        Log.i(TAG, "initialSetup: end");
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
            setTitle("Master List for " + owner);
            title = "Master List for " + owner;
            rebind();
            CheckBox chkOnShoppingList = findViewById(R.id.chkOnShoppingList);
            //chkOnShoppingList.setOnCheckedChangeListener();
        }
        else if (id == R.id.action_ShowShoppingList) {
            //Log.d(TAG, "onOptionsItemSelected: shopping list " + shoppingList.size());
            setTitle("Shopping List for " + owner);
            title = "Shopping List for " + owner;
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
        else {
            Log.d(TAG, "onOptionsItemSelected: delete");
            deleteChecked();
        }
        return super.onOptionsItemSelected(item);
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
                                item.setId(items.size() + 1);
                                item.setDescription(etAddItem.getText().toString());
                                item.setInCart(false);
                                item.setImgId(R.drawable.photoicon);
                                if(getTitle().equals("Master List for " + owner) ){
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
        if(getTitle().equals("Master List for " + owner) ){
            for(Item currentItem : items){
                APIEnd = owner + "/" + currentItem.getId();
                if(currentItem.isOnShoppingList()){
                    RestClient.execDeleteRequest(currentItem, getString(R.string.APIURL) + APIEnd, this,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(ArrayList<Item> result) {
                                    Log.i(TAG, "onSuccess: delete " + currentItem.getDescription());
                                }
                            });
                }
            }
        }
        if(getTitle().equals("Shopping List for " + owner) ){

            for(int count = 0; count < shoppingList.size(); count++){
                if(shoppingList.get(count).isInCart()) {
                    item = shoppingList.get(count);
                    APIEnd = owner + "/" + item.getId();
                    item.setOnShoppingList(false);
                    item.setInCart(false);
                    RestClient.execPutRequest(item, getString(R.string.APIURL) + owner + "/" + item.getId(), this,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(ArrayList<Item> result) {
                                    Log.i(TAG, "onSuccess: delete " + item.getDescription());
                                }
                            });
                    Log.d(TAG, "deleteChecked: reset item: " + item.getDescription());
                }
                else Log.d(TAG, "deleteChecked: failed if");
            }
        }
        rebind();
        Log.d(TAG, "deleteChecked: size: " + items.size());
    }


    public void rebind(){
        rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);

        Log.i(TAG, "rebind: " + getTitle());
        if(getTitle().equals("Master List for " + owner)){
            Log.d(TAG, "rebind: hit master list");
            if(!items.isEmpty()) items.removeAll(items);
            if(items.isEmpty()) items = ds.get("Description", "ASC");
            itemAdapter = new ItemAdapter(items, this);
            itemAdapter.setOnItemCheckedChangeListener(onCheckedChangedListener);
            itemAdapter.setOnItemClickListener(onClickListener);
        }
        if(getTitle().equals("Shopping List for " + owner)){
            Log.d(TAG, "rebind: hit shopping list");
            if(!shoppingList.isEmpty()) shoppingList.removeAll(shoppingList);
            if(shoppingList.isEmpty()) shoppingList = ds.getShoppingList("Description", "ASC");
            itemAdapter = new ItemAdapter(shoppingList, this);
            itemAdapter.setOnItemCheckedChangeListener(onCheckedChangedListener);
            itemAdapter.setOnItemClickListener(onClickListener);
        }
        rvItems.setAdapter(itemAdapter);
        Log.i(TAG, "rebind: " + items.toString() + " " + items.size());
    }
    public void clearAll(){
        if(getTitle().equals("Master List for " + owner) ){
            Log.i(TAG, "clearAll: master list");
            for(int count = 0; count < items.size(); count++) {
                item = items.get(count);
                if (item.isOnShoppingList()) {
                    Log.i(TAG, "clearAll: " + count);
                    item.setOnShoppingList(false);
                    ds.update(item);
                    Log.d(TAG, "clearAll: reset item: " + items.get(count));
                } else {
                    Log.d(TAG, "clearAll: failed if on " + items.get(count));
                }
            }
        }
        if(getTitle().equals("Shopping List for " + owner) ){
            for(int count = 0; count < shoppingList.size(); count++){
                item = shoppingList.get(count);
                if(shoppingList.get(count).isInCart()) {

                    Log.i(TAG, "clearAll: item: " + item);
                    item.setInCart(false);
                    Log.i(TAG, "clearAll: reset item: " + items.get(item.getId()));
                    ds.update(item);
                }
                else {
                    Log.d(TAG, "clearAll: failed if on " + item);
                }
            }
        }
        rebind();
    }
}