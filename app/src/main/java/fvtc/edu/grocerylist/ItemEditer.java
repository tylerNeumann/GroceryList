package fvtc.edu.grocerylist;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ItemEditer extends AppCompatActivity {

    public static final String TAG = "ItemEditer";
    Item item;
    int itemId;
    String itemDescription;
    ArrayList<Item> items;
    public static final int PERMISSION_REQUEST_PHONE = 102;
    public static final int PERMISSION_REQUEST_CAMERA = 103;
    public static final int CAMERA_REQUEST = 1888;
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
        initImgBtn();
        SetForEditing(false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_item_editer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void initItems(int itemId){
        try {
            RestClient.execGetOneRequest(getString(R.string.APIURL) + itemId,
                    this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Item> result) {
                            item = result.get(0);
                            rebindItem();
                        }
                    });
            Log.i(TAG, "initItems: " + item.toString());
        }catch (Exception e){
            Log.d(TAG, "initItems: " + e.getMessage());
        }
        rebindItem();
    }

    private void rebindItem() {
        EditText etDescription = findViewById(R.id.etDescription);
        if(item != null) {
            etDescription.setText(item.getItem());
        }

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
                RestClient.execPutRequest(item, getString(R.string.APIURL) + item.getId(), ItemEditer.this, new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<Item> result) {

                        Log.d(TAG, "onSuccess: Put" + item.getId());
                    }
                });
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
                if(item != null)item.setItem(s.toString());
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
    private void initImgBtn() {
        ImageButton imageTeam = findViewById(R.id.imgItem);
        imageTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 23){
                    // Check for the manifest permission
                    if(ContextCompat.checkSelfPermission(ItemEditer.this, android.Manifest.permission.CAMERA) != PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(ItemEditer.this, android.Manifest.permission.CAMERA)){
                            Snackbar.make(findViewById(R.id.activity_item_editer), "Teams requires this permission to take a photo.",
                                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "onClick: snackBar");
                                    ActivityCompat.requestPermissions(ItemEditer.this,
                                            new String[] {android.Manifest.permission.CAMERA},PERMISSION_REQUEST_PHONE);
                                }
                            }).show();
                        }
                        else {
                            Log.d(TAG, "onClick: 1st else");
                            ActivityCompat.requestPermissions(ItemEditer.this,
                                    new String[] {android.Manifest.permission.CAMERA},PERMISSION_REQUEST_PHONE);
                            takePhoto();
                        }
                    }
                    else{
                        Log.d(TAG, "onClick: 2nd else");
                        takePhoto();
                    }
                }
                else{
                    // Only rely on the previous permissions
                    Log.d(TAG, "onClick: 3rd else");
                    takePhoto();
                }
            }
        });
    }
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CAMERA_REQUEST){
            if(resultCode == RESULT_OK){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo,144,144,true);
                ImageButton imageButton = findViewById(R.id.imgItem);
                imageButton.setImageBitmap(scaledPhoto);
                item.setPhoto(scaledPhoto);
            }
        }
    }
}