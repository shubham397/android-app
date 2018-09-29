package com.example.user.trendy.ForYou.TopSelling;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.trendy.Bag.Cart;
import com.example.user.trendy.Bag.Db.AddToCart_Model;
import com.example.user.trendy.Bag.Db.DBHelper;
import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.Category.ProductDetail.ProductView;
import com.example.user.trendy.Category.ProductDetail.SelectItemModel;
import com.example.user.trendy.Interface.CartController;
import com.example.user.trendy.Interface.CommanCartControler;
import com.example.user.trendy.Interface.TopSellingInterface;
import com.example.user.trendy.R;
import com.example.user.trendy.databinding.TopsellingAdapterBinding;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TopSellingAdapter extends RecyclerView.Adapter<TopSellingAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TopSellingModel> itemsList;
    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;
    private List<AddToCart_Model> cartList = new ArrayList<>();
    DBHelper db;
    SelectItemModel model;
    CartController cartController;
    CommanCartControler commanCartControler;





    public TopSellingAdapter(Context mContext, ArrayList<TopSellingModel> itemsList, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
    }

    public TopSellingAdapter(ArrayList<TopSellingModel> itemsList, FragmentManager fragmentManager) {

        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        TopsellingAdapterBinding topSellingAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.topselling_adapter, parent, false);
        return new ViewHolder(topSellingAdapterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setTopselling(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private final TopsellingAdapterBinding binding;


        public ViewHolder(final TopsellingAdapterBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;

            binding.setOnitemclick(new TopSellingInterface() {
                @Override
                public void onClicksellingPostion() {
                    Bundle bundle = new Bundle();
                    bundle.putString("category", "topselling");
                    bundle.putSerializable("category_id", itemsList.get(getAdapterPosition()));
                    Fragment fragment = new ProductView();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container, fragment, "fragment");
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    ft.commit();
                }
            });

            binding.setOnitemclickplus(new Plus() {

                @Override
                public void OnclickPlus() {
                    cartController = new CartController(mContext);
                    commanCartControler = (CommanCartControler)cartController;
                    commanCartControler.AddToCart(itemsList.get(getAdapterPosition()).getProduct_ID().trim());
                }

                @Override
                public void OnclickWhislilst() {
                    cartController = new CartController(mContext);
                    commanCartControler = (CommanCartControler)cartController;
                    commanCartControler.AddToWhislist(itemsList.get(getAdapterPosition()).getProduct_ID().trim());
                }
            });

        }
    }



}
