package fvtc.edu.grocerylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    int itemPosition;
    //FileIO fileIO;

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDescription;
        public TextView tvIsOnShoppingList;
        public TextView tvIsInCart;
        public CheckBox chkSelector;
        public ImageView imgPhoto;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvIsOnShoppingList = itemView.findViewById(R.id.tvIsOnShoppingList);
            tvIsInCart = itemView.findViewById(R.id.tvIsInCart);
            chkSelector = itemView.findViewById(R.id.chkOnShoppingList);
            imgPhoto = itemView.findViewById(R.id.imgView);
            //code involved with clicking an item in the list

            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
            chkSelector.setOnCheckedChangeListener(onItemCheckedChangeListener);
        }

        public TextView getTvDescription(){ return tvDescription; }
        public CheckBox getChkSelector(){ return chkSelector; }
        public ImageView getImageButtonPhoto() { return imgPhoto; }
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
        itemPosition = position;
        Item currentItem = itemData.get(position);
        Log.i(TAG, "onBindViewHolder: " + itemData.get(position));
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.getTvDescription().setText(itemData.get(position).getDescription());
        itemViewHolder.getImageButtonPhoto().setImageResource(itemData.get(position).getImgId());

        itemViewHolder.getChkSelector().setOnCheckedChangeListener(null);

        if (MainActivity.title.equals("Master List")) {
            itemViewHolder.getChkSelector().setChecked(currentItem.isOnShoppingList());
        } else if (MainActivity.title.equals("Shopping List")) {
            itemViewHolder.getChkSelector().setChecked(currentItem.isInCart());
            Log.i(TAG, "onBindViewHolder: isInCart hit");
        } else {
            itemViewHolder.getChkSelector().setChecked(false);
        }

        itemViewHolder.getChkSelector().setTag(holder);

        GroceryListDataSource ds = new GroceryListDataSource(parentContext);
        itemViewHolder.chkSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.i(TAG, "onCheckedChanged: start");
            //itemViewHolder.chkSelector.setChecked(isChecked);

            if (MainActivity.title.equals("Master List")) {
                currentItem.setOnShoppingList(isChecked);
                Log.d(TAG, "Item " + (isChecked ? "added to" : "removed from") + " Master List: " + currentItem.getDescription());
            } else if (MainActivity.title.equals("Shopping List")) {
                currentItem.setInCart(isChecked);
                Log.i(TAG, "Item " + (isChecked ? "added to" : "removed from") + " Shopping List: " + currentItem.getDescription());
            } else {
                Log.d(TAG, "Unknown list title: " + MainActivity.title);
            }

            ds.update(currentItem);
            notifyItemChanged(position);

            onItemCheckedChangeListener.onCheckedChanged(buttonView, checkedState());
        });
        Log.d(TAG, "onBindViewHolder: bound");
    }
    @Override
    public int getItemCount() { return itemData.size(); }
    private boolean checkedState(){
        boolean checked = false;
        if(MainActivity.title.equals("Master List")){
            if(itemData.get(itemPosition).isOnShoppingList()) checked = true;
            else checked = false;
            Log.d(TAG, "checkedState masterList: " + checked);
        }
        else if(MainActivity.title.equals("Shopping List")){
            if(itemData.get(itemPosition).isInCart()) checked = true;
            else checked = false;
            Log.d(TAG, "checkedState shoppingList: " + checked);
        }
        else checked = false;
        Log.d(TAG, "checkedState: " + checked);
        return checked;
    }
}
