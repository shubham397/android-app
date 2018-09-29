package com.example.user.trendy.Account.Orders;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.user.trendy.Account.OrderList.OrderList;
import com.example.user.trendy.databinding.OrderAdapterBinding;
import com.example.user.trendy.Interface.FragmentRecyclerViewClick;
import com.example.user.trendy.R;

import java.util.ArrayList;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context mContext;
    ArrayList<OrderModel> itemsList;

    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;

    public OrderAdapter(Context mContext, ArrayList<OrderModel> itemsList, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        OrderAdapterBinding orderAdapterBinding =  DataBindingUtil.inflate(layoutInflater, R.layout.order_adapter, parent, false);
        return new ViewHolder(orderAdapterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.binding.setOrder(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        private final OrderAdapterBinding binding;



        public ViewHolder(final OrderAdapterBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;


            binding.setItemclick(new FragmentRecyclerViewClick() {
                @Override
                public void onClickPostion() {


                    Log.e("itemlist", String.valueOf(itemsList.get(getAdapterPosition())));
                    Log.e("orderetititle",itemsList.get(1).getOrderd().getLineItems().getEdges().get(1).getNode().getVariant().getProduct().getTitle());

                    Fragment fragment = new OrderList();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderlist",itemsList.get(getAdapterPosition()));
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    transaction.replace(R.id.home_container, fragment, "account");
//                    transaction.addToBackStack(null);
                    transaction.commit();


                }
            });
        }


    }
}

