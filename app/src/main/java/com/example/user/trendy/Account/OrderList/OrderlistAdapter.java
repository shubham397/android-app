package com.example.user.trendy.Account.OrderList;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.user.trendy.Account.Orders.OrderModel;
import com.example.user.trendy.Interface.FragmentRecyclerViewClick;
import com.example.user.trendy.R;
import com.example.user.trendy.databinding.OrderAdapterlistBinding;

import java.util.ArrayList;

public class OrderlistAdapter extends RecyclerView.Adapter<OrderlistAdapter.ViewHolder> {
    Context mContext;
    ArrayList<OrderListModel> itemsList;

    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;

    public OrderlistAdapter(Context mContext, ArrayList<OrderListModel> itemsList, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }


        OrderAdapterlistBinding orderAdapterListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.order_adapterlist, parent, false);
        return new ViewHolder(orderAdapterListBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.binding.setOrderlist(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.e("itemsizeorder", String.valueOf(itemsList.size()));
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final OrderAdapterlistBinding binding;


        public ViewHolder(final OrderAdapterlistBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;


            binding.setItemclick(new FragmentRecyclerViewClick() {
                @Override
                public void onClickPostion() {


                }
            });
        }


    }
}


