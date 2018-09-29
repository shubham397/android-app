package com.example.user.trendy.Filter.Filter_Type;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.user.trendy.Interface.FragmentRecyclerViewClick;
import com.example.user.trendy.R;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    Context mContext;
    ArrayList<FilterModel> itemsList;
    private ArrayList<String> selectedList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private LayoutInflater layoutInflater;

    public FilterAdapter(Context mContext, ArrayList<FilterModel> itemsList, FragmentManager fragmentManager) {
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
                R.layout.filterbyadapter, null);

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
                CheckBox cb = (CheckBox) v;
                FilterModel filterModel = (FilterModel) cb.getTag();

                filterModel.setChecked(cb.isChecked());
                itemsList.get(pos).setChecked(cb.isChecked());

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
        public CheckBox chkSelected;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            Name = (TextView) itemLayoutView.findViewById(R.id.txt_item_list_title);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.cbSelected);
        }

    }

    public ArrayList<String> getSelectedContactList() {
        return selectedList;
    }


}



