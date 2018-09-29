package com.example.user.trendy.ForYou.TopCollection;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.user.trendy.Category.CategoryProduct;
import com.example.user.trendy.Category.ProductDetail.ProductView;
import com.example.user.trendy.ForYou.TopSelling.TopSellingAdapter;
import com.example.user.trendy.ForYou.TopSelling.TopSellingModel;
import com.example.user.trendy.Interface.FragmentRecyclerViewClick;
import com.example.user.trendy.R;
import com.example.user.trendy.databinding.TopcollectionAdapterBinding;

import java.util.ArrayList;

public class TopCollectionAdapter  extends RecyclerView.Adapter<TopCollectionAdapter.ViewHolder> {

    Context mContext;
    ArrayList<TopCollectionModel> itemsList;
    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;


    public TopCollectionAdapter(Context mContext, ArrayList<TopCollectionModel> itemsList, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
    }

    public TopCollectionAdapter( ArrayList<TopCollectionModel> itemsList, FragmentManager fragmentManager) {

        this.itemsList = itemsList;
        this.fragmentManager=fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        TopcollectionAdapterBinding topcollectionAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.topcollection_adapter, parent, false);
        return new ViewHolder(topcollectionAdapterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setTopcollection(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TopcollectionAdapterBinding binding;


        public ViewHolder(final TopcollectionAdapterBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;


        binding.setItemclick(new FragmentRecyclerViewClick() {
            @Override
            public void onClickPostion() {
                Bundle bundle = new Bundle();
                bundle.putString("category","topcollection");
                bundle.putSerializable("category_id",itemsList.get(getAdapterPosition()));

//                onItemClick.onClick(itemsList.get(getAdapterPosition()).getProduct_ID());
//                    Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
//                            .setLineItemsInput(Input.value(Arrays.asList(
//                                    new Storefront.CheckoutLineItemInput(5, new ID(itemsList.get(getAdapterPosition()).getProduct_ID()))
//                            )));
////
                    Fragment fragment = new ProductView();

                    fragment.setArguments(bundle);
                    FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container,fragment,"fragment");
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    // ft.addToBackStack("fragment");
                    ft.commit();
            }
        });
        }

    }

}

