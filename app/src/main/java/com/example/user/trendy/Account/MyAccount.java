package com.example.user.trendy.Account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.trendy.Account.Orders.OrderAdapter;
import com.example.user.trendy.Account.Orders.OrderModel;
import com.example.user.trendy.Bag.Db.AddToCart_Adapter;
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
import com.shopify.graphql.support.Input;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MyAccount extends Fragment {
    LinearLayout change_email, edit_profile;
    String accessToken;
    private GraphClient graphClient;
    TextView name, email, mobile_number;
    String nametext, emailtext, mobiletext, firstname, lastname;
    RecyclerView order_recyclerview;
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    OrderAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.myaccount, container, false);

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        mobile_number = view.findViewById(R.id.mobile_number);
        edit_profile = view.findViewById(R.id.edit_profile);
        order_recyclerview = view.findViewById(R.id.order_recyclerview);

        change_email = view.findViewById(R.id.change_email);
        accessToken = SharedPreference.getData("accesstoken", getActivity());
        Log.e("accestoken", "" + accessToken);
        graphClient = GraphClient.builder(getActivity())
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(getActivity().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();

        if (accessToken != null) {
            getEmailId();
        }


        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.home_container, new Changeemail(), "account");
//                    transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MyAccountEdit();
                Bundle bundle = new Bundle();
                bundle.putString("firstname", firstname);
                bundle.putString("lastname", lastname);
                bundle.putString("mobile", mobiletext);
                bundle.putString("email", emailtext);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.home_container, fragment, "account");
//                    transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        if (accessToken != null) {
            getOrders();
        }
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        order_recyclerview.setLayoutManager(layoutManager1);
        order_recyclerview.setItemAnimator(new DefaultItemAnimator());


        adapter = new OrderAdapter(getActivity(), orderModelArrayList, getFragmentManager());
        order_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;

    }

    public void getEmailId() {
        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(accessToken, customer -> customer
                        .firstName()
                        .lastName()
                        .email()
                        .phone()
                        .displayName()
                        .id()
                )
        );

        QueryGraphCall call = graphClient.queryGraph(query);

        call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                Log.e("data", "user..." + response.data().getCustomer().getFirstName());
                Log.e("data", "user..." + response.data().getCustomer().getLastName());
                Log.e("data", "user..." + response.data().getCustomer().getEmail());
                Log.e("data", "user..." + response.data().getCustomer().getPhone());
                Log.e("data", "user..." + response.data().getCustomer().getDisplayName());
                Log.e("data", "user..." + response.data().getCustomer().getId());

                firstname = response.data().getCustomer().getFirstName();
                lastname = response.data().getCustomer().getLastName();
                nametext = response.data().getCustomer().getFirstName() + "" + response.data().getCustomer().getLastName();
                emailtext = "" + response.data().getCustomer().getEmail();
                mobiletext = "" + response.data().getCustomer().getPhone();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (nametext != null) {
                            name.setText(firstname + " " + lastname);
                            email.setText(emailtext.trim());
                            if (mobiletext.trim().equals("null")) {
                                mobiletext = "";
                                mobile_number.setText(mobiletext);
                            } else {
                                mobile_number.setText(mobiletext.trim());
                            }
                        }

                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Log.e("TAG", "Failed to execute query", error);
            }
        });

    }

    private void getOrders() {
        orderModelArrayList.clear();
        Storefront.QueryRootQuery query = Storefront.query(root -> root
                        .customer(accessToken, customer -> customer
                                        .orders(arg -> arg.first(10), connection -> connection
                                                        .edges(edge -> edge
                                                                        .node(node -> node

                                                                                        .totalPrice()
                                                                                        .processedAt()
                                                                                        .orderNumber()
                                                                                        .totalPrice()
                                                                                        .email()
                                                                                        .processedAt()
                                                                                        .totalShippingPrice()
                                                                                        .subtotalPrice()
                                                                                        .shippingAddress(address -> address
                                                                                                .address1()
                                                                                                .address2()
                                                                                                .city()
                                                                                                .country()
                                                                                                .firstName()
                                                                                                .lastName())
                                                                                        .lineItems(args -> args.first(10), lineItemsArguments -> lineItemsArguments
                                                                                                        .edges(orderLineItemEdgeQuery -> orderLineItemEdgeQuery
                                                                                                                        .node(orderLineItemQuery -> orderLineItemQuery
                                                                                                                                        .quantity()
                                                                                                                                        .customAttributes(attributeQuery -> attributeQuery.key().value())
                                                                                                                                        .variant(productVariantQuery -> productVariantQuery
                                                                                                                                                        .title()
                                                                                                                                                        .price()
                                                                                                                                                        .sku()
                                                                                                                                                        .weight()
                                                                                                                                                        .weightUnit()
                                                                                                                                                        .image(image -> image.src())
                                                                                                                                                        .product(produt1 -> produt1
                                                                                                                                                                .title()
                                                                                                                                                        )
//                                                                                        .tags()
//                                                                                        .images(image->image
//                                                                                        .edges(imageedge->imageedge
//                                                                                        .node(imagenode->imagenode
//                                                                                        .src()
//                                                                                        .id())))

                                                                                                                                        )
                                                                                                                        )
                                                                                                        )
                                                                                        )
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
                Log.e("data", "user..." + response.data().getCustomer().getOrders().getEdges().get(0).getNode().getLineItems().getEdges().get(0).getNode().getVariant().getProduct().getTitle());
                Log.e("came", "inside");
                Log.e("data", "user..." + response.data().getCustomer().getOrders().toString());


                for (Storefront.OrderEdge order : response.data().getCustomer().getOrders().getEdges()) {
                    OrderModel orderModel = new OrderModel();
                    orderModel.setOrderd(order.getNode());
                    orderModel.setLineitemsize(order.getNode().getLineItems().getEdges().size());
                    orderModelArrayList.add(orderModel);
                }
                Log.e("orderModelArrayList", String.valueOf(orderModelArrayList.size()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();

                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Log.e("TAG", "Failed to execute query", error);
            }
        });


    }

}
