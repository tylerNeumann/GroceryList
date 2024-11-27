package fvtc.edu.grocerylist;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter {
    private ArrayList<Item> itemData;
    private View.OnClickListener onItemClickListener;
    private CompoundButton.OnCheckedChangeListener onItemCheckedChangeListener;
    public static final String TAG = "ItemAdapter";
    private Context parentContext;
    int itemPosition;

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
        itemViewHolder.getTvDescription().setText(itemData.get(position).getItem());

        Bitmap teamPhoto = currentItem.getPhoto();
        if(teamPhoto != null){
            itemViewHolder.getImageButtonPhoto().setImageBitmap(teamPhoto);
        }
        else {
            itemViewHolder.getImageButtonPhoto().setImageResource(R.drawable.photoicon);
        }

        itemViewHolder.getChkSelector().setOnCheckedChangeListener(null);

        if (MainActivity.title.equals("Master List for " + MainActivity.ownerName)) {
            itemViewHolder.getChkSelector().setChecked(currentItem.getIsOnShoppingList());
        } else if (MainActivity.title.equals("Shopping List for " + MainActivity.ownerName)) {
            itemViewHolder.getChkSelector().setChecked(currentItem.getIsInCart());
            Log.i(TAG, "onBindViewHolder: getIsInCart hit");
        } else {
            itemViewHolder.getChkSelector().setChecked(false);
        }

        itemViewHolder.getChkSelector().setTag(holder);

        itemViewHolder.chkSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.i(TAG, "onCheckedChanged: start");
            //itemViewHolder.chkSelector.setChecked(isChecked);

            if (MainActivity.title.equals("Master List for " + MainActivity.ownerName)) {
                currentItem.setOnShoppingList(isChecked);
                Log.d(TAG, "Item " + (isChecked ? "added to" : "removed from") + " Master List: " + currentItem.getItem());
            } else if (MainActivity.title.equals("Shopping List for " + MainActivity.ownerName)) {
                currentItem.setInCart(isChecked);
                Log.i(TAG, "Item " + (isChecked ? "added to" : "removed from") + " Shopping List: " + currentItem.getItem());
            } else {
                Log.d(TAG, "Unknown list title: " + MainActivity.title);
            }

            RestClient.execPutRequest(currentItem, parentContext.getString(R.string.APIURL) + currentItem.getId(), parentContext, new VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<Item> result) {

                    Log.d(TAG, "onSuccess: Put" + currentItem.getId());
                }
            });
            Log.i(TAG, "onCheckedChanged: " + currentItem);
            notifyItemChanged(position);

            onItemCheckedChangeListener.onCheckedChanged(buttonView, checkedState());
        });
        Log.d(TAG, "onBindViewHolder: bound");
    }
    @Override
    public int getItemCount() { return itemData.size(); }
    private boolean checkedState(){
        boolean checked = false;
        if(MainActivity.title.equals("Master List for " + MainActivity.ownerName)){
            if(itemData.get(itemPosition).getIsOnShoppingList()) checked = true;
            else checked = false;
            Log.d(TAG, "checkedState masterList: " + checked);
        }
        else if(MainActivity.title.equals("Shopping List for " + MainActivity.ownerName)){
            if(itemData.get(itemPosition).getIsInCart()) checked = true;
            else checked = false;
            Log.d(TAG, "checkedState shoppingList: " + checked);
        }
        else checked = false;
        Log.d(TAG, "checkedState: " + checked);
        return checked;
    }
}
