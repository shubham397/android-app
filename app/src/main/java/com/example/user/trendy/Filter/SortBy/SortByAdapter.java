package com.example.user.trendy.Filter.SortBy;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;


import com.example.user.trendy.Interface.FragmentRecyclerViewClick;
import com.example.user.trendy.R;

import java.util.ArrayList;

public class SortByAdapter extends RecyclerView.Adapter<SortByAdapter.ViewHolder> {

    Context mContext;
    ArrayList<SortByModel> itemsList;
    private ArrayList<String> selectedList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;

    public SortByAdapter(Context mContext, ArrayList<SortByModel> itemsList, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.itemsList = itemsList;
        this.fragmentManager = fragmentManager;
    }


    // Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.sortbyadapter, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final int pos = position;

        viewHolder.Name.setText(itemsList.get(position).getTitle());

        viewHolder.chkSelected.setChecked(itemsList.get(position).isChecked());

        viewHolder.chkSelected.setTag(itemsList.get(position));


        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                RadioButton cb = (RadioButton) v;
                SortByModel filterModel = (SortByModel) cb.getTag();

                for (int i = 0; i < itemsList.size(); i++) {
                    itemsList.get(i).setChecked(false);
//                    viewHolder.chkSelected.setVisibility(View.GONE);
                    selectedList.remove(itemsList.get(i).getTitle());
                }
                notifyDataSetChanged();

                filterModel.setChecked(cb.isChecked());
                itemsList.get(pos).setChecked(cb.isChecked());
//                viewHolder.chkSelected.setVisibility(View.VISIBLE);

                if (cb.isChecked()) {
                    selectedList.add(itemsList.get(pos).getTitle());
                    Log.e("added_type", String.valueOf(selectedList));
                } else {
                    selectedList.remove(itemsList.get(pos).getTitle());
                    Log.e("removed_tytpe", String.valueOf(selectedList));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Name;
        public RadioButton chkSelected;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            Name = (TextView) itemLayoutView.findViewById(R.id.txt_item_list_title);
            chkSelected = (RadioButton) itemLayoutView.findViewById(R.id.cbSelected);
        }

    }

    public ArrayList<String> getSelectedSortList() {
        return selectedList;
    }


}


