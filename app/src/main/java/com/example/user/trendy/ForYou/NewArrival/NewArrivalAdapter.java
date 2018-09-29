package com.example.user.trendy.ForYou.NewArrival;

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

import com.example.user.trendy.Category.ProductDetail.ProductView;
import com.example.user.trendy.ForYou.TopCollection.TopCollectionAdapter;
import com.example.user.trendy.ForYou.TopCollection.TopCollectionModel;
import com.example.user.trendy.ForYou.TopSelling.Plus;
import com.example.user.trendy.Interface.CartController;
import com.example.user.trendy.Interface.CommanCartControler;
import com.example.user.trendy.Interface.FragmentRecyclerViewClick;
import com.example.user.trendy.Interface.TopSellingInterface;
import com.example.user.trendy.R;
import com.example.user.trendy.databinding.NewarrivalAdapterBinding;
import com.example.user.trendy.databinding.TopsellingAdapterBinding;

import java.util.ArrayList;

public class NewArrivalAdapter extends RecyclerView.Adapter<NewArrivalAdapter.ViewHolder> {

    Context mContext;
    ArrayList<NewArrivalModel> itemsList;
    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;
    CartController cartController;
    CommanCartControler commanCartControler;


    public NewArrivalAdapter(Context mContext, ArrayList<NewArrivalModel> itemsList, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
    }

    public NewArrivalAdapter(ArrayList<NewArrivalModel> itemsList, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.itemsList = itemsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        NewarrivalAdapterBinding newarrivalAdapterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.newarrival_adapter, parent, false);
        return new ViewHolder(newarrivalAdapterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setNewarrival(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.e("itemsize1", String.valueOf(itemsList.size()));
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final NewarrivalAdapterBinding binding;


        public ViewHolder(final NewarrivalAdapterBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;

            binding.setItemclick(new FragmentRecyclerViewClick() {
                @Override
                public void onClickPostion() {
                    Bundle bundle = new Bundle();
                    bundle.putString("category", "newarrival");
                    bundle.putSerializable("category_id", itemsList.get(getAdapterPosition()));

//                onItemClick.onClick(itemsList.get(getAdapterPosition()).getProduct_ID());
//                    Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
//                            .setLineItemsInput(Input.value(Arrays.asList(
//                                    new Storefront.CheckoutLineItemInput(5, new ID(itemsList.get(getAdapterPosition()).getProduct_ID()))
//                            )));
////
                    Fragment fragment = new ProductView();

                    fragment.setArguments(bundle);
                    FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container, fragment, "fragment");
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    // ft.addToBackStack("fragment");
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

                }
            });


        }

    }


}

