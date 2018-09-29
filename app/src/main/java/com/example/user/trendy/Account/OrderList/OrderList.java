package com.example.user.trendy.Account.OrderList;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.trendy.Account.Orders.OrderModel;
import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.R;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class OrderList extends Fragment {
    RecyclerView orderlist_recycler;
    OrderlistAdapter adapter;
    ArrayList<OrderListModel> orderModelArrayList = new ArrayList<>();
    ArrayList<String> orderModelArrayList1 = new ArrayList<>();
    private GraphClient graphClient;
    int ordernumber;
    TextView shipping, subtotal, total;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.youororderlist, container, false);
        OrderModel orderModel = (OrderModel) getArguments().getSerializable("orderlist");
//        orderModelArrayList.add(orderModel);
        ordernumber = orderModel.getOrderd().getOrderNumber();
        String order = String.valueOf(ordernumber);
//        orderfetch(order.trim());

        orderlist_recycler = view.findViewById(R.id.orderlist_recycler);
        shipping = view.findViewById(R.id.shipping);
        subtotal = view.findViewById(R.id.subtotal);
        total = view.findViewById(R.id.total);


        shipping.setText(String.valueOf(orderModel.getOrderd().getTotalShippingPrice()));
        total.setText(String.valueOf(orderModel.getOrderd().getTotalPrice()));
        subtotal.setText(String.valueOf(orderModel.getOrderd().getSubtotalPrice()));
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        orderlist_recycler.setLayoutManager(layoutManager1);
        orderlist_recycler.setItemAnimator(new DefaultItemAnimator());
        adapter = new OrderlistAdapter(getActivity(), orderModelArrayList, getFragmentManager());
        orderlist_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        for (int i = 0; i < orderModel.getOrderd().getLineItems().getEdges().size(); i++) {

            OrderListModel orderListModel = new OrderListModel();
            orderListModel.setId(String.valueOf(orderModel.getOrderd().getOrderNumber()));
            orderListModel.setTitle(orderModel.getOrderd().getLineItems().getEdges().get(i).getNode().getVariant().getProduct().getTitle());
            orderListModel.setImage(orderModel.getOrderd().getLineItems().getEdges().get(i).getNode().getVariant().getImage().getSrc());
            orderListModel.setShippingtax(String.valueOf(orderModel.getOrderd().getShippingAddress()));
            orderListModel.setSubtotal(String.valueOf(orderModel.getOrderd().getSubtotalPrice()));
            orderListModel.setTotalcost(String.valueOf(orderModel.getOrderd().getTotalPrice()));
            orderListModel.setQuantity(String.valueOf(orderModel.getOrderd().getLineItems().getEdges().get(i).getNode().getQuantity()));
            orderListModel.setProductcost(String.valueOf(orderModel.getOrderd().getLineItems().getEdges().get(i).getNode().getVariant().getPrice()));

            orderModelArrayList.add(orderListModel);
        }

        Log.e("orderModelArrayList", String.valueOf(orderModelArrayList.size()));
        adapter.notifyDataSetChanged();


        return view;
    }

}
