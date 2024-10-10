package fvtc.edu.grocerylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter {
    private ArrayList<Item> itemData;
    private String activityTitle;
    private View.OnClickListener onItemClickListener;
    public static final String TAG = "ItemAdapter";
    private Context parentContext;
    public static final String FILENAME = "MasterList.txt";
    FileIO fileIO;

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDescription;
        public TextView tvIsOnShoppingList;
        public TextView tvIsInCart;
        public CheckBox chkSelector;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvIsOnShoppingList = itemView.findViewById(R.id.tvIsOnShoppingList);
            tvIsInCart = itemView.findViewById(R.id.tvIsInCart);
            chkSelector = itemView.findViewById(R.id.chkOnShoppingList);
            //code involved with clicking an item in the list

            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }

        public TextView getTvDescription(){ return tvDescription; }
        public TextView getTvIsOnShoppingList(){return tvIsOnShoppingList; }
        public TextView getTvIsInCart(){return tvIsInCart; }

    }
    public ItemAdapter(ArrayList<Item> data, Context context, String activityTitle){
        itemData = data;
        Log.d(TAG, "ItemAdapter: " + data.size());
        this.activityTitle = activityTitle;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        Log.d(TAG, "setOnItemClickListener: ");
        onItemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complex_item_view, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item currentItem = itemData.get(position);
        boolean checked = false;
        Log.d(TAG, "onBindViewHolder: " + itemData.get(position));
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.getTvDescription().setText(itemData.get(position).getDescription());
        itemViewHolder.getTvIsOnShoppingList().setText(itemData.get(position).isOnShoppingList());
        itemViewHolder.getTvIsInCart().setText(itemData.get(position).isInCart());
        if(currentItem.isOnShoppingList() == "1")  checked = true;
        else checked = false;
        ((ItemViewHolder) holder).chkSelector.setChecked(checked);

        itemViewHolder.chkSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //itemViewHolder.chkSelector.setChecked(isChecked);
                Log.d(TAG, "onCheckedChanged: checked: " + itemViewHolder.tvDescription.getText().toString());
                checkBoxInteraction(currentItem, isChecked, activityTitle);

            }
        });
        Log.d(TAG, "onBindViewHolder: bound");
    }
    private void checkBoxSetup(Item item, boolean isChecked, String title){
        if (title.equals("Master List")) {
            // Perform specific action for "Master List"
            if (isChecked) {
                Log.d("ItemAdapter", "Item added to Master List: " + item.getDescription());
                // Add more logic like saving to a file, updating UI, etc.
            } else {
                Log.d("ItemAdapter", "Item removed from Master List: " + item.getDescription());
            }
        } else if (title.equals("Shopping List")) {
            // Perform specific action for "Shopping List"
            if (isChecked) {
                Log.d("ItemAdapter", "Item added to Shopping List: " + item.getDescription());
                // Add more logic for Shopping List like saving to file, updating UI, etc.
            } else {
                Log.d("ItemAdapter", "Item removed from Shopping List: " + item.getDescription());
            }
        } else {
            // Default action if the title doesn't match known cases
            Log.d("ItemAdapter", "Unknown list title: " + title);
        }
    }
    @Override
    public int getItemCount() { return itemData.size(); }
    private void checkBoxInteraction(Item item, boolean isChecked, String title){
        fileIO = new FileIO();
        if (title.equals("Master List")) {
            // Perform specific action for "Master List"
            if (isChecked) {
                Log.d("ItemAdapter", "Item added to Master List: " + item.getDescription());
                // Add more logic like saving to a file, updating UI, etc.
                item.setOnShoppingList("1");

                Log.d(TAG, "checkBoxInteraction: " + item);
            } else {
                Log.d("ItemAdapter", "Item removed from Master List: " + item.getDescription());
                item.setOnShoppingList("0");
            }
            writeItemToFile(item, FILENAME);
        } else if (title.equals("Shopping List")) {
            // Perform specific action for "Shopping List"
            if (isChecked) {
                Log.d("ItemAdapter", "Item added to Shopping List: " + item.getDescription());
                // Add more logic for Shopping List like saving to file, updating UI, etc.
                item.setInCart("1");
            } else {
                Log.d("ItemAdapter", "Item removed from Shopping List: " + item.getDescription());
                item.setInCart("0");
            }
            writeItemToFile(item, FILENAME);
        } else {
            // Default action if the title doesn't match known cases
            Log.d("ItemAdapter", "Unknown list title: " + title);
        }
    }
    private void writeItemToFile(Item item, String filename) {
        try {
            // Open the file in append mode or update the file
            OutputStreamWriter writer = new OutputStreamWriter(parentContext.openFileOutput(filename, Context.MODE_PRIVATE | Context.MODE_APPEND));

            // Convert the item to a flat-file compatible format (like CSV)
            String itemData = item.getId() + "," + item.getDescription() + "," + item.isOnShoppingList() + "," + item.isInCart() + "\r\n";

            // Write the item data to the file
            writer.write(itemData);

            Log.d("FileIO", "Item written to file: " + itemData);
            writer.close();
        } catch (FileNotFoundException e) {
            Log.e("FileIO", "FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            Log.e("FileIO", "IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
