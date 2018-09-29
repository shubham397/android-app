package com.example.user.trendy.ForYou.NewArrivalRecycler;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.user.trendy.ForYou.NewArrival.NewArrivalAdapter;
import com.example.user.trendy.ForYou.NewArrival.NewArrivalModel;
import com.example.user.trendy.R;

import java.util.ArrayList;



public class NewMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> items;
    private final int HORIZONTAL = 0;
    private final int VERTICAL = 2;
    private final int HORIZONTAL1 = 1;
    ArrayList<NewArrivalModel> newArrival;
    FragmentManager fragmentManager;


    public NewMainAdapter(Context context, ArrayList<NewArrivalModel> newArrival, FragmentManager fragmentManager) {
        this.context = context;
        this.newArrival = newArrival;
        this.fragmentManager=fragmentManager;
    }

//    public NewMainAdapter(ArrayList<NewArrivalModel> newArrival) {
//        this.newArrival=newArrival;
//    }


    //this method returns the number according to the Vertical/Horizontal object


    //this method returns the holder that we've inflated according to the viewtype.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case HORIZONTAL:
                view = inflater.inflate(R.layout.blank, parent, false);
                holder = new HorizontalViewHolder(view);
                break;

            case HORIZONTAL1:
                view = inflater.inflate(R.layout.newarrival1, parent, false);
                holder = new NewArrivalViewHolder(view);

                break;

            case VERTICAL:
                view = inflater.inflate(R.layout.blank, parent, false);
                holder = new VerticalViewHolder(view);
                break;



            default:
                view = inflater.inflate(R.layout.blank, parent, false);
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
//
//        TopCollectionAdapter adapter1 = new TopCollectionAdapter(getBestCollection());
//        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        holder.recyclerView.setAdapter(adapter1);
    //    holder.bestselling.setText("j");
//        Log.d("collectiontitle", "" + getBestCollection().get(0).getCollectionTitle());

    }

    //
    private void topSelling(HorizontalViewHolder holder) {
        Log.d("Adapter1", "come");
//        TopSellingAdapter adapter = new TopSellingAdapter(getTopSellingCollection());
//        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        holder.recyclerView.setAdapter(adapter);
      //  holder.topselling.setText("io");
//        Log.d("collectiontitle", "" + getTopSellingCollection().get(0).getCollectionTitle());
    }

    private void newArrival(NewArrivalViewHolder holder) {

        NewArrivalAdapter adapter = new NewArrivalAdapter(context,newArrival, fragmentManager);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter);
//        holder.newarrival.setText(String.valueOf(newArrival.get(0).getCollectionTitle()));
//       Log.d("collectiontitley", "" +newArrival.get(0).getCollectionTitle());
    }

    @Override
    public int getItemCount() {
        Log.e("item Sizelist",""+ String.valueOf(newArrival.size()));
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HORIZONTAL;
        else if (position == 2)
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

        HorizontalViewHolder(View itemView) {
            super(itemView);
         //   topselling = itemView.findViewById(R.id.category_title);
//            recyclerView = itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView bestselling;

        VerticalViewHolder(View itemView) {
            super(itemView);
         //  bestselling = itemView.findViewById(R.id.category_title);
           // recyclerView = itemView.findViewById(R.id.bestselling_recyclerView);
        }
    }

    public class NewArrivalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView newarrival;

        NewArrivalViewHolder(View itemView) {
            super(itemView);
         //   newarrival = itemView.findViewById(R.id.new_arrival_text);
            recyclerView = itemView.findViewById(R.id.newarrival_recycler);
        }
    }


}

