package fvtc.edu.grocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ItemEditer extends AppCompatActivity {

    public static final String TAG = "ItemEditer";
    Item item;
    int itemId;
    String itemDescription;
    ArrayList<Item> items;
    private String APIEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_editer);
        Bundle extras = getIntent().getExtras();
        itemId = extras.getInt("itemId");
        itemDescription = extras.getString("itemDescription");
        Log.i(TAG, "onCreate: id = " + itemId);
        this.setTitle(itemDescription);
        initItems(itemId);

        initToggleButton();
        initSaveButton();
        initTextChanged();
        SetForEditing(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void initItems(int itemId){
        try {
            Log.i(TAG, "initItems: " + item.toString());
        }catch (Exception e){
            Log.d(TAG, "initItems: " + e.getMessage());
        }
        rebindItem();
    }

    private void rebindItem() {
        EditText etDescription = findViewById(R.id.etDescription);
        etDescription.setText(item.getDescription());
    }

    private void initToggleButton() {
        ToggleButton toggleButton = findViewById(R.id.toggleButtonEdit);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetForEditing(toggleButton.isChecked());
            }
        });
    }
    private void SetForEditing(boolean checked) {

        EditText etDescription = findViewById(R.id.etDescription);
        etDescription.setEnabled(checked);

        if(checked) etDescription.requestFocus();//set focus to editName
        else {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }
    private void initSaveButton()  {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIEnd = MainActivity.owner + "/" + item.getId();

                startActivity(new Intent(ItemEditer.this, MainActivity.class));
            }
        });
    }
    private void initTextChanged() {
        EditText etDescription = findViewById(R.id.etDescription);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setDescription(s.toString());
            }
        });
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
            MainActivity.title = "Master List";
            CheckBox chkOnShoppingList = findViewById(R.id.chkOnShoppingList);
        }
        else if (id == R.id.action_ShowShoppingList) {
            //Log.d(TAG, "onOptionsItemSelected: shopping list " + shoppingList.size());
            setTitle("Shopping List");
            MainActivity.title = "Shopping List";
        }
        startActivity(new Intent(ItemEditer.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }
}