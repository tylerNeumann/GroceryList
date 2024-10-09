package fvtc.edu.grocerylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter {
    private ArrayList<Item> itemData;
    private View.OnClickListener onItemClickListener;
    public static final String TAG = "ItemAdapter";
    private Context parentContext;

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDescription;
        public TextView tvIsOnShoppingList;
        public TextView tvIsInCart;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvIsOnShoppingList = itemView.findViewById(R.id.tvIsOnShoppingList);
            tvIsInCart = itemView.findViewById(R.id.tvIsInCart);
            //code involved with clicking an item in the list

            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }

        public TextView getTvDescription(){ return tvDescription; }
        public TextView getTvIsOnShoppingList(){return tvIsOnShoppingList; }
        public TextView getTvIsInCart(){return tvIsInCart; }

    }
    public ItemAdapter(ArrayList<Item> data, Context context){
        itemData = data;
        Log.d(TAG, "ItemAdapter: " + data.size());
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
        Log.d(TAG, "onBindViewHolder: " + itemData.get(position));
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.getTvDescription().setText(itemData.get(position).getDescription());
        itemViewHolder.getTvIsOnShoppingList().setText(itemData.get(position).isOnShoppingList());
        itemViewHolder.getTvIsInCart().setText(itemData.get(position).isInCart());
        Log.d(TAG, "onBindViewHolder: bound");
    }

    @Override
    public int getItemCount() { return itemData.size(); }
}
