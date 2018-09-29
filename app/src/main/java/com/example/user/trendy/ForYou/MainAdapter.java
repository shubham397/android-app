package com.example.user.trendy.ForYou;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trendy.Category.CategoryProduct;
import com.example.user.trendy.ForYou.NewArrival.NewArrivalAdapter;
import com.example.user.trendy.ForYou.NewArrival.NewArrivalModel;
import com.example.user.trendy.ForYou.NewArrivalRecycler.NewMainAdapter;
import com.example.user.trendy.ForYou.TopCollection.TopCollectionAdapter;
import com.example.user.trendy.ForYou.TopCollection.TopCollectionModel;
import com.example.user.trendy.ForYou.TopSelling.TopSellingAdapter;
import com.example.user.trendy.ForYou.TopSelling.TopSellingModel;
import com.example.user.trendy.R;

import java.util.ArrayList;

import static com.example.user.trendy.ForYou.ForYou.getBestCollection;
import static com.example.user.trendy.ForYou.ForYou.getNewArrival;
import static com.example.user.trendy.ForYou.ForYou.getTopSellingCollection;


public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> items;
    private final int HORIZONTAL = 0;
    private final int VERTICAL = 1;
    private final int HORIZONTAL1 = 2;
    private FragmentManager fragmentManager;


    public MainAdapter(Context context, ArrayList<Object> items, FragmentManager fragmentManager) {
        this.context = context;
        this.items = items;
        this.fragmentManager = fragmentManager;
    }

    //this method returns the number according to the Vertical/Horizontal object


    //this method returns the holder that we've inflated according to the viewtype.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case HORIZONTAL:
                view = inflater.inflate(R.layout.topselling_recycler, parent, false);
                holder = new HorizontalViewHolder(view);
                break;

            case VERTICAL:
                view = inflater.inflate(R.layout.bestcollection_recycler, parent, false);
                holder = new VerticalViewHolder(view);
                break;

            case HORIZONTAL1:
                view = inflater.inflate(R.layout.newarrival_recycler, parent, false);
                holder = new NewArrivalViewHolder(view);

                break;

            default:
                view = inflater.inflate(R.layout.bestcollection_recycler, parent, false);
                holder = new VerticalViewHolder(view);

                break;
        }


        return holder;
    }

    //here we bind view with data according to the position that we have defined.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder.getItemViewType() == HORIZONTAL)
            topSelling((HorizontalViewHolder) holder);

        else if (holder.getItemViewType() == VERTICAL)
            bestCollection((VerticalViewHolder) holder);

        else if (holder.getItemViewType() == HORIZONTAL1)
            newArrival((NewArrivalViewHolder) holder);


    }

    private void bestCollection(VerticalViewHolder holder) {


        Log.d("Adapter", "come");

        TopCollectionAdapter adapter1 = new TopCollectionAdapter(getBestCollection(), fragmentManager);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter1);

        if (getBestCollection().size() != 0) {
            holder.bestselling.setText(getBestCollection().get(0).getCollectionTitle());
            Log.d("collectiontitle", "" + getBestCollection().get(0).getCollectionTitle());
        }
        holder.seall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryProduct();
                Bundle bundle = new Bundle();
                bundle.putString("collection", "bestcollection");
                bundle.putSerializable("category_id", getBestCollection().get(0));
                Log.e("iddddddd", getBestCollection().get(0).getCollectionTitle());
                fragment.setArguments(bundle);
                FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container, fragment, "fragment");
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                // ft.addToBackStack("fragment");
                ft.commit();
            }
        });

    }

    //
    private void topSelling(HorizontalViewHolder holder) {
        Log.d("Adapter1", "come");
        TopSellingAdapter adapter = new TopSellingAdapter(context, getTopSellingCollection(), fragmentManager);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
        if (getTopSellingCollection().size() != 0) {
            holder.topselling.setText(String.valueOf(getTopSellingCollection().get(0).getCollectionTitle()));
            Log.d("collectiontitle", "" + getTopSellingCollection().get(0).getCollectionTitle());
        }
        holder.seall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryProduct();
                Bundle bundle = new Bundle();
                bundle.putString("collection", "topselling");
                bundle.putSerializable("category_id", getTopSellingCollection().get(0));
                Log.e("iddddddd", getTopSellingCollection().get(0).getCollectionTitle());
                fragment.setArguments(bundle);
                FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container, fragment, "fragment");
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                // ft.addToBackStack("fragment");
                ft.commit();
            }
        });
    }

    /**
     * @param holder
     * @implNote
     */
    private void newArrival(NewArrivalViewHolder holder) {

//        NewArrivalAdapter adapter = new NewArrivalAdapter(getNewArrival());
//        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        holder.recyclerView.setAdapter(adapter);

        Log.d("collectiontitlenew", "" + getNewArrival().get(0).getCollectionTitle());
//
        NewMainAdapter adapter = new NewMainAdapter(context, getNewArrival(), fragmentManager);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
        if (getNewArrival() != null) {
            holder.newarrival.setText(String.valueOf(getNewArrival().get(0).getCollectionTitle()));

        }

        holder.seeall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CategoryProduct();
                Bundle bundle = new Bundle();
                bundle.putString("collection", "newarrival");
                bundle.putSerializable("category_id", getNewArrival().get(0));
                Log.e("iddddddd", getNewArrival().get(0).getCollectionTitle());
                fragment.setArguments(bundle);
                FragmentTransaction ft = fragmentManager.beginTransaction().replace(R.id.home_container, fragment, "fragment");
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                // ft.addToBackStack("fragment");
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e("item Sizemain", String.valueOf(items.size()));
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HORIZONTAL;
        else if (position == 1)
            return VERTICAL;
        else
            return HORIZONTAL1;
    }

//    public class AllView extends RecyclerView.ViewHolder{
//        public AllView( View itemView) {
//            super(itemView);
//            if (getItemViewType() == HORIZONTAL) {
//                HorizontalViewHolder(itemView)
//            }
//
//        }
//
//
//    }


    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView topselling;
        LinearLayout seall;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            topselling = itemView.findViewById(R.id.category_title);
            recyclerView = itemView.findViewById(R.id.inner_recyclerView);
            seall = itemView.findViewById(R.id.see_all);
        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView bestselling;
        LinearLayout seall;

        VerticalViewHolder(View itemView) {
            super(itemView);
             bestselling = itemView.findViewById(R.id.category_title);
            recyclerView = itemView.findViewById(R.id.bestselling_recyclerView);
            seall = itemView.findViewById(R.id.see_all);
        }
    }

    public class NewArrivalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView newarrival;
        LinearLayout seeall1;

        NewArrivalViewHolder(View itemView) {
            super(itemView);
            newarrival = itemView.findViewById(R.id.new_arrival_text);
            recyclerView = itemView.findViewById(R.id.newarrival_recyclerView);
            seeall1=itemView.findViewById(R.id.see_all);
        }
    }
}
