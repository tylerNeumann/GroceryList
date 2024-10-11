package fvtc.edu.grocerylist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

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

    private CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            RecyclerView.ViewHolder viewHolder;
            viewHolder = (RecyclerView.ViewHolder) buttonView.getTag();
            int position = viewHolder.getAdapterPosition();
            if(getTitle().equals("Master List")){
                if(isChecked) items.get(position).setOnShoppingList("1");
                else items.get(position).setOnShoppingList("0");
            }
            else{
                if(isChecked) items.get(position).setInCart("1");
                else items.get(position).setInCart("0");

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Master List");
        title = "Master List";
        parentContext = this;
        createItems();
        rebind();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void createItems() {
        items = new ArrayList<Item>();
        items.add(new Item(1, "Protein Shake", "0", "0"));
        items.add(new Item(2, "Pop Tarts", "1", "0"));
        items.add(new Item(3, "Mtn Dew", "1", "0"));
        items.add(new Item(4, "Pretzels", "0", "0"));
        items.add(new Item(5, "Shampoo", "0", "0"));
        items.add(new Item(6, "Cheese", "0", "0"));
        Log.d(TAG, "createItems: items" + items.size());

        FileIO.writeFile(FILENAME,this,createDataArray(items));
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
        }
        else {
            Log.d(TAG, "onOptionsItemSelected: delete");
            deleteChecked();
            FileIO.writeFile(FILENAME,this,createDataArray(items));
            rebind();
        }
        return super.onOptionsItemSelected(item);
    }

    public void readFile() {
        try {
            FileIO fileIO = new FileIO();
            //Log.d(TAG, "readFile: start");
            ArrayList<String> strData = fileIO.readFile(FILENAME, this);
            if (getTitle() == "Master List") items = new ArrayList<Item>();
            if (getTitle() == "Shopping List") shoppingList = new ArrayList<Item>();
            //Log.d(TAG, "readFile: " + getTitle());
            Log.d(TAG, "readFile: data size " + strData.size());
            for (String s : strData) {
                //Log.d(TAG, "readFile: split string");
                String[] data = s.split("\\|");

                if (getTitle() == "Master List") {
                    items.add(new Item(Integer.parseInt(data[0]), data[1], data[2], data[3]));
                    Log.d(TAG, "ReadTextFile: " + items.get(items.size() - 1).getDescription());
                }

                if (getTitle() == "Shopping List") {
                    //Log.d(TAG, "readFile: start shopping list");
                    //Log.d(TAG, "ReadFile: filling shopping list" + data[1] + ": " + data[2]);
                    if(Objects.equals(data[2], "1")){
                        //Log.d(TAG, "ReadFile: add item to shopping list" + data[2]);
                        shoppingList.add(new Item(Integer.parseInt(data[0]), data[1], data[2], data[3]));
                        Log.d(TAG, "ReadTextFile: " + shoppingList.get(shoppingList.size() - 1).getDescription());
                    }
                }
            }
        } catch (Exception e){
            Log.d(TAG, "ShowMasterList: error" + e.getMessage());
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
                                item.setInCart("0");
                                if(getTitle() == "Master List"){
                                    item.setOnShoppingList("0");
                                }
                                else{
                                    item.setOnShoppingList("1");
                                    shoppingList.add(item);
                                    rvItems.setAdapter(itemAdapter);
                                }
                                Log.d(TAG, "onClick: add item: " + item);
                                items.add(item);
                                FileIO.writeFile(FILENAME,(AppCompatActivity) parentContext,createDataArray(items));
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
        if(getTitle() == "Master List"){
            items.removeIf(item -> item.isOnShoppingList().equals("1"));
        }
        if(getTitle() == "Shopping List"){
            items.removeIf(item -> item.isInCart().equals("1"));
        }

    }
    public void rebind(){
        rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(layoutManager);
        readFile();
        if(getTitle() == "Master List"){
            Log.d(TAG, "rebind: hit master list");
            itemAdapter = new ItemAdapter(items, this);
            itemAdapter.setOnItemCheckedChangeListener(onCheckedChangedListener);
        }
        if(getTitle() == "Shopping List"){
            Log.d(TAG, "rebind: hit shopping list");
            itemAdapter = new ItemAdapter(shoppingList, this);
            itemAdapter.setOnItemCheckedChangeListener(onCheckedChangedListener);
        }
        rvItems.setAdapter(itemAdapter);
    }
}