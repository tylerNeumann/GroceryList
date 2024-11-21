package fvtc.edu.grocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fvtc.edu.grocerylist.MainActivity;

public class SetOwner extends AppCompatActivity {
String owner;
public static final String TAG = "Owner";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_owner);

        initSaveButton();
        initTextChanged();
    }

    private void initSaveButton() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getApplication().getSharedPreferences("myprefs", MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("owner", owner);
                editor.commit();
                Log.d(TAG, "initSaveButton: " + owner);
                startActivity(new Intent(SetOwner.this, MainActivity.class));
            }
        });

    }
    private void initTextChanged() {
        EditText etOwner = findViewById(R.id.etOwner);
        etOwner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                owner = s.toString();
                Log.d(TAG, "afterTextChanged: " + owner);
            }
        });
    }
}