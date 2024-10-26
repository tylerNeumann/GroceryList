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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter {
    private ArrayList<Item> itemData;
    private View.OnClickListener onItemClickListener;
    private CompoundButton.OnCheckedChangeListener onItemCheckedChangeListener;
    public static final String TAG = "ItemAdapter";
    private Context parentContext;
    //FileIO fileIO;

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
            chkSelector.setOnCheckedChangeListener(onItemCheckedChangeListener);
        }

        public TextView getTvDescription(){ return tvDescription; }
        public TextView getTvIsOnShoppingList(){ return tvIsOnShoppingList; }
        public TextView getTvIsInCart(){ return tvIsInCart; }
        public CheckBox getChkSelector(){ return chkSelector; }

    }
    public ItemAdapter(ArrayList<Item> data, Context context){
        itemData = data;
        Log.d(TAG, "ItemAdapter: " + data.size());
        parentContext = context;
    }

    public void setOnItemCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener){
        Log.d(TAG, "setOnItemCheckedChangeListener: ");
        onItemCheckedChangeListener = listener;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        Log.d(TAG, "setOnItemClickListener: ");
        onItemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: hit");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complex_item_view, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item currentItem = itemData.get(position);
        Log.d(TAG, "onBindViewHolder: " + itemData.get(position));
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.getTvDescription().setText(itemData.get(position).getDescription());
        //itemViewHolder.getTvIsOnShoppingList().setText(itemData.get(position).isOnShoppingList());
        //itemViewHolder.getTvIsInCart().setText(itemData.get(position).isInCart());

        itemViewHolder.getChkSelector().setOnCheckedChangeListener(null);
        itemViewHolder.getChkSelector().setChecked(itemData.get(position).isOnShoppingList());
        itemViewHolder.getChkSelector().setTag(holder);
        itemViewHolder.chkSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "onCheckedChanged: start");
            //itemViewHolder.chkSelector.setChecked(isChecked);
            GroceryListDataSource ds = new GroceryListDataSource(parentContext);
            if (MainActivity.title.equals("Master List")) {
                // Perform specific action for "Master List"
                if (isChecked) {
                    Log.d("ItemAdapter", "Item added to Master List: " + itemData.get(position).getDescription());
                    // Add more logic like saving to a file, updating UI, etc.

                    Log.d(TAG, "checkBoxInteraction: " + itemData.get(position));
                } else {
                    Log.d("ItemAdapter", "Item removed from Master List: " + itemData.get(position).getDescription());

                }
                ds.update(itemData.get(position));
                notifyItemChanged(itemData.indexOf(itemData.get(position)));
                onItemCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
            }
            else if (MainActivity.title.equals("Shopping List")) {
                // Perform specific action for "Shopping List"
                if (isChecked) {
                    Log.d("ItemAdapter", "Item added to Shopping List: " + itemData.get(position).getDescription());
                    // Add more logic for Shopping List like saving to file, updating UI, etc.

                } else {
                    Log.d("ItemAdapter", "Item removed from Shopping List: " + itemData.get(position).getDescription());

                }
                Log.d(TAG, "checkBoxInteraction: item: " + itemData.get(position).toString());
                ds.update(itemData.get(position));
                notifyItemChanged(itemData.indexOf(itemData.get(position)));

            }
            else {
                // Default action if the title doesn't match known cases
                Log.d("ItemAdapter", "Unknown list title: " + MainActivity.title);
            }
        });
        Log.d(TAG, "onBindViewHolder: bound");
    }
    @Override
    public int getItemCount() { return itemData.size(); }
    /*private void deleteItem(int position) {
        Log.d(TAG, "deleteItem: " + position);
        Item Item = itemData.get(position);
        itemData.remove(position);

        //FileIO.writeFile(ItemsListActivity.FILENAME,
        //                (AppCompatActivity) parentContext.getApplicationContext(),
        //                ItemsListActivity.createDataArray(ItemData));
        //                ItemsListActivity.createDataArray(ItemData));
        //                notifyDataSetChanged();

        Log.d(TAG, "deleteItem: parentContext: " + parentContext);
        GroceryListDataSource ds = new GroceryListDataSource(parentContext);
        Log.d(TAG, "deleteItem: " + Item.toString());
        boolean didDelete = ds.delete(Item) > 0;
        Log.d(TAG, "deleteItem: " + didDelete);
        notifyDataSetChanged();

    }*/
}
