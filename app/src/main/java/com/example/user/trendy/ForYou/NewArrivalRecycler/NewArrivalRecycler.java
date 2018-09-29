package com.example.user.trendy.ForYou.NewArrivalRecycler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.ForYou.NewArrival.NewArrivalModel;
import com.example.user.trendy.ForYou.ResultCallBackInterface;
import com.example.user.trendy.ForYou.TopCollection.TopCollectionModel;
import com.example.user.trendy.ForYou.TopSelling.TopSellingModel;
import com.example.user.trendy.R;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NewArrivalRecycler extends Fragment implements ResultCallBackInterface {
    RecyclerView newarrival_recycler;
    GraphClient graphClient;
    private ArrayList<Object> objects = new ArrayList<>();
    ResultCallBackInterface resultCallBackInterface;
    String newproduct = "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzMyODY3OTQyNjg=";
    static ArrayList<NewArrivalModel> newArrivalModelArrayList1 = new ArrayList<>();
    NewMainAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.newarrival1, container, false);

        newarrival_recycler = view.findViewById(R.id.newarrival_recycler);
        resultCallBackInterface = (ResultCallBackInterface) this;

        graphClient = GraphClient.builder(getActivity())
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(getActivity().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();
    //    getNewArrivals();

//        adapter = new NewMainAdapter(getActivity(), getObject());
//        newarrival_recycler.setAdapter(adapter);
//        newarrival_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        return view;
    }

    private ArrayList<Object> getObject() {

//        adapter.notifyDataSetChanged();

        return objects;
    }

    public static ArrayList<NewArrivalModel> getNewArrival1() {
        //    adapter.notifyDataSetChanged();
        return newArrivalModelArrayList1;
    }


    public void getNewArrivals() {
        newArrivalModelArrayList1.clear();

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID(newproduct), nodeQuery -> nodeQuery
                        .onCollection(collectionQuery -> collectionQuery
                                .title()
                                .products(arg -> arg.first(10), productConnectionQuery -> productConnectionQuery
                                        .edges(productEdgeQuery -> productEdgeQuery
                                                .node(productQuery -> productQuery
                                                        .title()
                                                        .productType()
                                                        .description()
                                                        .images(arg -> arg.first(10), imageConnectionQuery -> imageConnectionQuery
                                                                .edges(imageEdgeQuery -> imageEdgeQuery
                                                                        .node(imageQuery -> imageQuery
                                                                                .src()
                                                                        )
                                                                )
                                                        )
                                                        .variants(arg -> arg.first(10), variantConnectionQuery -> variantConnectionQuery
                                                                .edges(variantEdgeQuery -> variantEdgeQuery
                                                                        .node(productVariantQuery -> productVariantQuery
                                                                                .price()
                                                                                .title()
                                                                                .image(args -> args.src())
                                                                                .weight()
                                                                                .weightUnit()
                                                                                .available()
                                                                        )
                                                                )
                                                        )
                                                )
                                        )


                                ))));

        graphClient.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                Storefront.Collection product = (Storefront.Collection) response.data().getNode();

//                resultCallBackInterface.newArrivals(product);
            }

            @Override
            public void onFailure(@NonNull GraphError error) {

            }
        });
    }
//
//    @Override
//    public void bestCollection(Storefront.Collection collection) {
//
//    }
//
//    @Override
//    public void topSelling(String id, String title, String price, String image, String collectionname) {
//
//    }
//
//
//
//
//    @Override
//    public void newArrivals(Storefront.Collection collection) {
//        getActivity().runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                Toast.makeText(getActivity(), collection.getTitle(), Toast.LENGTH_SHORT).show();
////                      Toast.makeText(getActivity(), collection.getProducts().getEdges().get(1).getNode().getTitle(), Toast.LENGTH_SHORT).show();
//                for (int i = 0; i < collection.getProducts().getEdges().size(); i++) {
//                    NewArrivalModel newArrivalModel = new NewArrivalModel(collection.getProducts().getEdges().get(i).getNode(), collection.getTitle());
//                    Log.e("product", collection.getProducts().getEdges().get(i).getNode().getTitle());
//                    newArrivalModelArrayList1.add(newArrivalModel);
//
//
//                }
//                getObject().add(newArrivalModelArrayList1.get(0));
//                adapter.notifyDataSetChanged();
//
//
//            }
//        });
//
//    }


    @Override
    public void bestCollection(ArrayList<TopCollectionModel> arrayList) {

    }

    @Override
    public void topSelling(ArrayList<TopSellingModel> arrayList) {

    }

    @Override
    public void newArrivals(ArrayList<NewArrivalModel> arrayList) {

    }
}
