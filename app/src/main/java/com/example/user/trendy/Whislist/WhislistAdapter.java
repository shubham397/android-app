package com.example.user.trendy.Whislist;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.trendy.Interface.CartController;
import com.example.user.trendy.Interface.CommanCartControler;
import com.example.user.trendy.R;
import com.example.user.trendy.Whislist.WhislistDB.DBWhislist;
import com.example.user.trendy.databinding.WhislistAdapterBinding;

import java.util.List;

public class WhislistAdapter extends   RecyclerView.Adapter<WhislistAdapter.ViewHolder> {

    List<AddWhislistModel> items;
    Context mContext;
    private LayoutInflater layoutInflater;
    CartController cartController;
    CommanCartControler commanCartControler;
    FragmentManager fragmentManager;
    TextView textView;
    String state;

    public WhislistAdapter(List<AddWhislistModel> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    public WhislistAdapter(List<AddWhislistModel> items, Context mContext, FragmentManager fragmentManager) {
        this.items = items;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }


        WhislistAdapterBinding whislistAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.whislist_adapter, parent, false);
        return new ViewHolder(whislistAdapterBinding);
//        return null;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.binding.setWhislistitem(items.get(position));
        Log.d("Product varient id ", items.get(position).getProduct_varient_id());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    @Override
//    public void shippingvisibility(String state, ArrayList<String> productlist) {
//        state=state;
//        productlist=productlist;
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView remove, shipping_visibility;
        LinearLayout decrease, increase;
        DBWhislist db = new DBWhislist(mContext);

        private final WhislistAdapterBinding binding;

        public ViewHolder(final WhislistAdapterBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;
            remove = itemView.findViewById(R.id.remove);


            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("iddd", items.get(getAdapterPosition()).getProduct_varient_id());
//                    remove1.removeItem(items.get(getAdapterPosition()).getProduct_varient_id());
                    DBWhislist db = new DBWhislist(mContext);
                    if (db.deleteRow(items.get(getAdapterPosition()).getProduct_varient_id().trim())) {
                        items.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });

        }
    }




}

