package com.example.user.trendy.Product;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.example.user.trendy.Interface.ProductClickInterface;
import com.example.user.trendy.R;
import com.example.user.trendy.databinding.Product1Binding;
import com.example.user.trendy.databinding.ProductAdapter1Binding;


import java.util.ArrayList;

import static com.example.user.trendy.Product.ProductList.isViewWithCatalog;

public class ProductListAdapter  extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    Context mContext;
    ArrayList<ProductListModel> itemsList;
    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;
    OnItemClick onItemClick;
    ProductClickInterface productClickInterface;


    public ProductListAdapter(Context mContext, ArrayList<ProductListModel> itemsList, FragmentManager fragmentManager, ProductClickInterface productClickInterface) {
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
            ProductAdapter1Binding productAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.product_adapter1, parent, false);

//            view = productAdapterBinding;
            viewHolder = new ViewHolder<>(productAdapterBinding);
        } else {
            Product1Binding productBinding = DataBindingUtil.inflate(layoutInflater, R.layout.product1, parent, false);

//          view = productBinding;

            viewHolder = new ViewHolder<>(productBinding);

        }

        return (ViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.getBinding().setVariable(BR.product, itemsList.get(position));
        viewHolder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=viewHolder.getAdapterPosition();
                Log.e("position", String.valueOf(position));
//                productClickInterface.clickProduct(itemsList.get(position),view,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }



    class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
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
