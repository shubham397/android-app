package com.example.user.trendy.Category;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.android.databinding.library.baseAdapters.BR;
import com.example.user.trendy.Category.ProductDetail.ProductView;
import com.example.user.trendy.Interface.FragmentRecyclerViewClick;
import com.example.user.trendy.Interface.ProductClickInterface;
import com.example.user.trendy.R;
import com.example.user.trendy.databinding.ProductAdapterBinding;
import com.example.user.trendy.databinding.ProductBinding;

import java.util.ArrayList;

import static com.example.user.trendy.Category.CategoryProduct.isViewWithCatalog;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context mContext;
    ArrayList<ProductModel> itemsList;
    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;
    OnItemClick onItemClick;
    ProductClickInterface productClickInterface;


    public ProductAdapter(Context mContext, ArrayList<ProductModel> itemsList, FragmentManager fragmentManager, ProductClickInterface productClickInterface) {
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
        this.productClickInterface = productClickInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(mContext);
        View view;
        RecyclerView.ViewHolder viewHolder;


        if (isViewWithCatalog) {
            ProductAdapterBinding productAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.product_adapter, parent, false);

//            view = productAdapterBinding;
            viewHolder = new ViewHolder<>(productAdapterBinding);
        } else {
          ProductBinding productBinding = DataBindingUtil.inflate(layoutInflater, R.layout.product, parent, false);

//          view = productBinding;

            viewHolder = new ViewHolder<>(productBinding);

        }

        return (ViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.getBinding().setVariable(BR.product, itemsList.get(position));
        viewHolder.getBinding().setVariable(BR.itemclick,productClickInterface);
//        viewHolder.getBinding().setVariable(BR.onitemclickplus,plus;
        //        viewHolder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                int position=viewHolder.getAdapterPosition();
//                productClickInterface.clickProduct(itemsList.get(position).getProduct_ID());
//                Log.e("position",itemsList.get(position).getProduct_ID());
////                productClickInterface.clickProduct(itemsList.get(position).getProduct_ID());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }



    class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder  {
        private final T binding;

        public ViewHolder(T binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public T getBinding() {
            return binding;
        }


    }
//    class ViewHolder extends RecyclerView.ViewHolder {
//
//        private final ProductAdapterBinding binding;
//
//
//        public ViewHolder(final ProductAdapterBinding itembinding) {
//            super(itembinding.getRoot());
//            this.binding = itembinding;
//
//
//            binding.setItemclick(new FragmentRecyclerViewClick() {
//                @Override
//                public void onClickPostion() {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("category_id", itemsList.get(getAdapterPosition()));
//
////                    onItemClick.onClick(itemsList.get(getAdapterPosition()).getProduct().getId().toString());
////                    Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
////                            .setLineItemsInput(Input.value(Arrays.asList(
////                                    new Storefront.CheckoutLineItemInput(5, new ID(itemsList.get(getAdapterPosition()).getProduct_ID()))
////                            )));
////
//                    Fragment fragment = new ProductView();
//
//                    fragment.setArguments(bundle);
//                    FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container, fragment, "fragment");
//                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
//                    // ft.addToBackStack("fragment");
//                    ft.commit();
//
//////
//                }
//            });
//        }


//    }

    public interface OnItemClick {
        void onClick(String value);
    }
}
