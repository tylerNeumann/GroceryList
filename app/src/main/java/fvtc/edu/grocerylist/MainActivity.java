package fvtc.edu.grocerylist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    public ItemAdapter itemAdapter;
    RecyclerView rvItems;
    ArrayList<Item> items;
    ArrayList<Item> shoppingList;
    private Context parentContext;
    public static String title;
    public static String ownerName = null;
    Item item;
    private CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "onCheckedChanged: ");
            RecyclerView.ViewHolder viewHolder;
            viewHolder = (RecyclerView.ViewHolder) buttonView.getTag();
            int position = viewHolder.getAdapterPosition();
            item = items.get(position);
            if(getTitle().equals("Master List for " + ownerName)){
                if(isChecked) item.setOnShoppingList(true);
                else item.setOnShoppingList(false);
            }
            else if(getTitle().equals("Shopping List for " + ownerName)){
                if(isChecked) item.setInCart(true);
                else item.setInCart(false);
            }
            RestClient.execPutRequest(item, getString(R.string.APIURL) + item.getId(), parentContext, new VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<Item> result) {

                    Log.d(TAG, "onSuccess: Put" + item.getId());
                }
            });
            Log.i(TAG, "onCheckedChanged: " + item);
        }
    };
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            item = new Item();
            if(getTitle().equals("Master List for " + ownerName)){
                item = items.get(position);
                Log.i(TAG, "onClick: " + item.getItem());
            }
            if(getTitle().equals("Shopping List for " + ownerName)){
                item = shoppingList.get(position);
                Log.i(TAG, "onClick: " + item.getItem());
            }
            Intent intent = new Intent(MainActivity.this, ItemEditer.class);
            intent.putExtra("itemId", item.getId());
            intent.putExtra("itemDescription", item.getItem());
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
        readFromAPI();
        rebind();
    }
    private void initialSetup() {
        Log.i(TAG, "initialSetup: start");
        items = new ArrayList<Item>();
        shoppingList = new ArrayList<>();
        SharedPreferences preferences = getApplication().getSharedPreferences("myprefs", MODE_PRIVATE);
        ownerName = preferences.getString("owner", "");
        setTitle("Master List for " + ownerName);
        title = "Master List for " + ownerName;
        parentContext = this;
        if(ownerName == ""){
            startActivity(new Intent(MainActivity.this, SetOwner.class));
        }
        readFromAPI();
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
            setTitle("Master List for " + ownerName);
            title = "Master List for " + ownerName;
            readFromAPI();
            rebind();
        }
        else if (id == R.id.action_ShowShoppingList) {
            //Log.d(TAG, "onOptionsItemSelected: shopping list " + shoppingList.size());
            setTitle("Shopping List for " + ownerName);
            title = "Shopping List for " + ownerName;
            readFromAPI();
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
                                item.setItem(etAddItem.getText().toString());
                                item.setInCart(false);
                                if(getTitle().equals("Master List for " + ownerName) ){
                                    item.setOnShoppingList(false);
                                }
                                else{
                                    item.setOnShoppingList(true);
                                    shoppingList.add(item);
                                    rvItems.setAdapter(itemAdapter);
                                }
                                Log.d(TAG, "onClick: add item: " + item);
                                RestClient.execPostRequest(item, getString(R.string.APIURL), parentContext,
                                        new VolleyCallback() {
                                            @Override
                                            public void onSuccess(ArrayList<Item> result) {
                                                item.setId(result.get(0).getId());
                                                Log.d(TAG, "onSuccess: Post" + item.getId());
                                            }
                                        });
                                readFromAPI();
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
        if(getTitle().equals("Master List for " + ownerName) ){
            for(Item currentItem : items){
                if(currentItem.getIsOnShoppingList()){
                    RestClient.execDeleteRequest(currentItem, getString(R.string.APIURL) + currentItem.getId(), this,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(ArrayList<Item> result) {
                                    Log.i(TAG, "onSuccess: delete " + currentItem.getItem());
                                }
                            });
                }
            }
        }
        if(getTitle().equals("Shopping List for " + ownerName) ){

            for(int count = 0; count < shoppingList.size(); count++){
                if(shoppingList.get(count).getIsInCart()) {
                    item = shoppingList.get(count);
                    item.setOnShoppingList(false);
                    item.setInCart(false);
                    RestClient.execPutRequest(item, getString(R.string.APIURL) + item.getId(), this,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(ArrayList<Item> result) {
                                    Log.i(TAG, "onSuccess: delete " + item.getItem());
                                }
                            });
                    Log.d(TAG, "deleteChecked: reset item: " + item.getItem());
                }
                else Log.d(TAG, "deleteChecked: failed if");
            }
        }
        readFromAPI();
        rebind();
        Log.d(TAG, "deleteChecked: size: " + items.size());
    }
    public void rebind(){
        rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);

        Log.i(TAG, "rebind: " + getTitle());
        if(getTitle().equals("Master List for " + ownerName)){
            Log.d(TAG, "rebind: hit master list");
            itemAdapter = new ItemAdapter(items, this);
            Log.i(TAG, "rebind: " + items.toString());
        }
        if(getTitle().equals("Shopping List for " + ownerName)){
            Log.d(TAG, "rebind: hit shopping list");
            fillShoppingList();
            itemAdapter = new ItemAdapter(shoppingList, this);
        }
        itemAdapter.setOnItemCheckedChangeListener(onCheckedChangedListener);
        itemAdapter.setOnItemClickListener(onClickListener);
        rvItems.setAdapter(itemAdapter);
        Log.i(TAG, "rebind: " + items.toString() + " " + items.size());
    }
    public void clearAll(){
        if(getTitle().equals("Master List for " + ownerName) ){
            Log.i(TAG, "clearAll: master list");
            for(int count = 0; count < items.size(); count++) {
                item = items.get(count);
                if (item.getIsOnShoppingList()) {
                    item.setOnShoppingList(false);
                    RestClient.execPutRequest(item, getString(R.string.APIURL) + item.getId(), parentContext, new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Item> result) {

                            Log.d(TAG, "onSuccess: Put" + item.getId());
                        }
                    });
                    Log.d(TAG, "clearAll: reset item: " + item);
                } else {
                    Log.d(TAG, "clearAll: failed if on " + item);
                }
            }
        }
        if(getTitle().equals("Shopping List for " + ownerName) ){
            for(int count = 0; count < shoppingList.size(); count++){
                item = shoppingList.get(count);
                if(shoppingList.get(count).getIsInCart()) {

                    Log.i(TAG, "clearAll: item: " + item);
                    item.setInCart(false);
                    RestClient.execPutRequest(item, getString(R.string.APIURL) + item.getId(), parentContext, new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Item> result) {

                            Log.d(TAG, "onSuccess: Put" + item.getId());
                        }
                    });
                }
                else {
                    Log.d(TAG, "clearAll: failed if on " + item);
                }
            }
        }
        readFromAPI();
        rebind();
    }
    private void readFromAPI(){
        try {
            Log.d(TAG, "readFromAPI: start");
            RestClient.execGetRequest(getString(R.string.APIURL) + ownerName, this, new VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<Item> result) {
                    Log.d(TAG, "onSuccess: got here");
                    items = result;
                    rebind();
                    Log.i(TAG, "onSuccess: " + items.toString());
                }
            });
        }catch (Exception e){
            Log.e(TAG, "readFromAPI: Error" + e.getMessage() );
        }
    }
    private void fillShoppingList(){
        readFromAPI();
        for(Item item: items) {
            if(item.getIsOnShoppingList()) {
                shoppingList.add(item);
            }
        }
    }
    private void fillItemsArray(){
        items.add(new Item(1, "Protein Shake", false, false, ownerName, 0.0, 0.0, null));
        items.add(new Item(2, "Pop Tarts", false, false, ownerName, 0.0, 0.0, null));
        items.add(new Item(3, "Mtn Dew", false, false, ownerName, 0.0, 0.0, null));
        items.add(new Item(4, "Pretzels", false, false, ownerName, 0.0, 0.0, null));
        items.add(new Item(5, "Shampoo", false, false, ownerName, 0.0, 0.0, null));
        items.add(new Item(6, "Cheese", false, false, ownerName, 0.0, 0.0, null));
    }
    private void fillAPI(){
        fillItemsArray();
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            Log.i(TAG, "fillAPI: item = " + item);
            RestClient.execPostRequest(item, getString(R.string.APIURL), this, new VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<Item> result) {
                    item.setId(result.get(0).getId());
                    Log.d(TAG, "onSuccess: Post" + item.getId());
                }
            });
        }
        readFromAPI();
        rebind();
    }
}