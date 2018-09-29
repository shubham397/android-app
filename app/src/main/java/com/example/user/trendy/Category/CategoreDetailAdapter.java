package com.example.user.trendy.Category;

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

import com.example.user.trendy.Interface.FragmentRecyclerViewClick;
import com.example.user.trendy.R;
import com.example.user.trendy.databinding.CategoreyAdapterBinding;

import java.util.ArrayList;


public class CategoreDetailAdapter extends RecyclerView.Adapter<CategoreDetailAdapter.ViewHolder> {

    Context mContext;
    ArrayList<CategoryModel> itemsList;
    ArrayList<SubCategoryModel> subCategoryModelArrayList;

    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;

    public CategoreDetailAdapter(Context mContext, ArrayList<CategoryModel> itemsList, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
    }
    @Override
    public CategoreDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        CategoreyAdapterBinding categoreyAdapterBinding =  DataBindingUtil.inflate(layoutInflater, R.layout.categorey_adapter, parent, false);
        return new ViewHolder(categoreyAdapterBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.binding.setCategory(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        private final CategoreyAdapterBinding binding;



        public ViewHolder(final CategoreyAdapterBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;


            binding.setItemclick(new FragmentRecyclerViewClick() {
                @Override
                public void onClickPostion() {

                    if(itemsList.get(getAdapterPosition()).getSubCategoryModelArrayList()==null) {

                        Fragment fragment = new CategoryProduct();
                        Bundle bundle = new Bundle();
                        bundle.putString("collection","api");
                        bundle.putSerializable("category_id", itemsList.get(getAdapterPosition()));
                        Log.e("iddddddd", itemsList.get(getAdapterPosition()).getId());
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container, fragment, "fragment");
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                        // ft.addToBackStack("fragment");
                        ft.commit();
                    }else{

                        Fragment fragment = new SubCategory();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("category_id", itemsList.get(getAdapterPosition()));
                        Log.e("iddddddd1", String.valueOf(itemsList.get(getAdapterPosition()).getSubCategoryModelArrayList()));
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container, fragment, "fragment");
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.commit();

                    }
//
//                    Intent intent = new Intent(mContext, Main2Activity.class);
//                    intent.putExtra("productDetail",itemsList.get(getAdapterPosition()).getCollection().getId());
//                    mContext.startActivity(intent);

                }
            });
        }


    }
}
