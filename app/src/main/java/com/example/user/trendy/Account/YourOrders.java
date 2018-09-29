package com.example.user.trendy.Account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.SharedPreference;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class YourOrders extends Fragment {

    private String accessToken;
    private GraphClient graphClient;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.youororder, container, false);

        accessToken = SharedPreference.getData("accesstoken", getActivity());
        Log.e("accestoken", "" + accessToken);
        graphClient = GraphClient.builder(getActivity())
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(getActivity().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();

        if (accessToken != null) {
            getOrders();
        }


        return view;
    }

    private void getOrders() {

        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(accessToken, customer -> customer
                        .orders(arg -> arg.first(10), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .orderNumber()
                                                .totalPrice()
                                        )
                                )
                        )
                )
        );
        QueryGraphCall call = graphClient.queryGraph(query);

        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                Log.e("data", "user..." + response.data().getCustomer().getOrders().getEdges().get(0).getNode().getOrderNumber());


            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Log.e("TAG", "Failed to execute query", error);
            }
        });


    }

}
