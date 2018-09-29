package com.example.user.trendy.Bag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.user.trendy.Bag.Db.AddRemoveCartItem;
import com.example.user.trendy.Bag.Db.AddToCart_Model;
import com.example.user.trendy.Bag.Db.DBHelper;
import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.Category.ProductDetail.SelectItemModel;
import com.example.user.trendy.ForYou.TopSelling.TopSellingModel;
import com.example.user.trendy.Util.SharedPreference;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.RetryHandler;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.graphql.support.Input;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Cart extends AppCompatActivity implements AddRemoveCartItem {
    String checkoutId = "", productid, productvarientid;
    GraphClient graphClient;
    DBHelper db;
    SelectItemModel model;
    private static int cart_count = 0;
    private List<AddToCart_Model> cartList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = new DBHelper(this);
        cartList = db.getCartList();
        graphClient = GraphClient.builder(Cart.this)
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();

        Intent i = getIntent();
        if (i != null) {
            productid = i.getStringExtra("productid");
//            productvarientid = i.getStringExtra("productvarientid");
            Log.e("productid", "" + productid);
//            Log.e("productvarientid", "" + productvarientid);

            String text = "gid://shopify/Product/" + productid.trim();

            String converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
            Log.e("coverted", converted.trim());

            getProductVariantID(converted.trim());
//            checkid();
        } else {
            Log.e("intent", "null");
        }
    }

    public void getProductVariantID(String productID) {
        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID(productID), nodeQuery -> nodeQuery
                        .onProduct(productQuery -> productQuery
                                .title()
                                .description()
                                .descriptionHtml()
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
                                                        .compareAtPrice()
                                                        .availableForSale()
                                                        .image(args -> args.src())
                                                        .weight()
                                                        .weightUnit()
                                                        .available()
                                                )
                                        )
                                )
                        )
                )
        );

        graphClient.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {


                if (response != null) {
                    Storefront.Product product = (Storefront.Product) response.data().getNode();
                    Log.e("titit", product.getTitle());
                    model = new SelectItemModel();
                    model.setProduct(product);
                    model.setShip("true");

                    List<Storefront.ProductVariant> productVariant = new ArrayList<>();
                    for (final Storefront.ProductVariantEdge productVariantEdge : product.getVariants().getEdges()) {
                        productVariant.add(productVariantEdge.getNode()
                        );
//
                        Log.d("Product varient Id : ", String.valueOf(productVariantEdge.getNode().getId()));


                    }
                    String available = productVariant.get(0).getAvailableForSale().toString();
                    Log.e("available", available);
                    if (productVariant.get(0).getAvailableForSale()) {
                        if (cartList.size() == 0) {
                            Log.e("empty", "empty");
                            int qty = 1;
                            db.insertToDo(productVariant.get(0), qty, model.getProduct().getTitle(), String.valueOf(model.getProduct().getTags()),model.getShip());
                        } else {
                            for (int i = 0; i < cartList.size(); i++) {
                                if ((cartList.get(i).getProduct_varient_id().trim()).equals(productVariant.get(0).getId().toString().trim())) {

                                    Log.e("update", "empty");
                                    int qty = cartList.get(i).getQty() + 1;
                                    //updatequery
                                    Log.e("id", String.valueOf(i));
                                    Log.e("qty", String.valueOf(qty));
//                                    db.update(Integer.parseInt(cartList.get(i).getCol_id()), String.valueOf(qty));

                                } else {
                                    Log.e("nequal", "empty");
                                    Log.e("" + cartList.get(i).getProduct_varient_id(), "" + productVariant.get(0).getId());
                                    int qty = 1;
                                    Log.e("variant_id",productVariant.get(0).getId().toString());
                                    db.insertToDo(productVariant.get(0), qty, model.getProduct().getTitle(),String.valueOf(model.getProduct().getTags()),model.getShip());

                                }
                            }
                        }

                    }
                }
                return;

            }

            @Override
            public void onFailure(@NonNull GraphError error) {

//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }


        });


    }


    public void checkid() {
        checkoutId = SharedPreference.getData("checkoutid", getApplicationContext());
        Log.e("checkoutid", checkoutId);

        if (checkoutId.trim().length() != 0) {
            Log.e("checkid", "there");
            cart(productid);
        } else {
            Log.e("checkid", "null");
            cart(productvarientid.trim());
//            ID checkout=new ID(checkoutId);
//            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
//                    .node(checkout, nodeQuery -> nodeQuery
//                            .onCheckout(checkoutQuery -> checkoutQuery
//                                    .availableShippingRates(availableShippingRatesQuery -> availableShippingRatesQuery
//                                            .ready()
//                                            .shippingRates(shippingRateQuery -> shippingRateQuery
//                                                    .handle()
//                                                    .price()
//                                                    .title()
//                                            )
//                                    )
//                            )
//                    )
//            );
//            graphClient.queryGraph(query).enqueue(
//                    new GraphCall.Callback<Storefront.QueryRoot>() {
//                        @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
//                            Storefront.Checkout checkout = (Storefront.Checkout) response.data().getNode();
//                            List<Storefront.ShippingRate> shippingRates = checkout.getAvailableShippingRates().getShippingRates();
//                        }
//
//                        @Override public void onFailure(@NonNull final GraphError error) {
//                        }
//                    },
//                    null,
//                    RetryHandler.exponentialBackoff(800, TimeUnit.MILLISECONDS, 1.2f)
//                            .whenResponse(
//                                    response -> ((Storefront.Checkout) response.data()).getAvailableShippingRates().getReady() == false
//                            )
//                            .maxCount(5)
//                            .build()
//            );
        }
    }

    public void cart(String id) {
        Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
                .setLineItemsInput(Input.value(Arrays.asList(
                        new Storefront.CheckoutLineItemInput(3, new ID(id))
                )));

        Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery

                .checkoutCreate(input, createPayloadQuery -> createPayloadQuery
                        .checkout(checkoutQuery -> checkoutQuery
                                .webUrl()
                        )
                        .userErrors(userErrorQuery -> userErrorQuery
                                .field()
                                .message()
                        )
                )
        );

        graphClient.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {

                if (!response.data().getCheckoutCreate().getUserErrors().isEmpty()) {
                    for (Storefront.UserError userError : response.data().getCheckoutCreate().getUserErrors())
                        Log.e("data", userError.getMessage());

                    // handle user friendly errors
                } else {
                    checkoutId = String.valueOf(response.data().getCheckoutCreate().getCheckout().getId());
                    String checkoutWebUrl = response.data().getCheckoutCreate().getCheckout().getWebUrl();
                    Log.d("checkoutId", checkoutId);
                    Log.d("checkoutWebUrl", checkoutWebUrl);
                    //    checkId();
                    updateemail();
                }
            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                // handle errors
            }
        });

    }

    public void updateemail() {
        ID checkout = new ID(checkoutId);
//        SharedPreference.saveData("email","it.jeevi@gmail.com",getApplicationContext());
        String email = SharedPreference.getData("email", getApplicationContext());
        Log.e("email", email);
        Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
                .checkoutEmailUpdate(checkout, email.trim(), emailUpdatePayloadQuery -> emailUpdatePayloadQuery
                        .checkout(checkoutQuery -> checkoutQuery
                                .email()
                                .lineItems(args -> args.edges(checkoutQuer -> checkoutQuer
                                        .node(nodee -> nodee
                                                .variant(var -> var
                                                        .title()
                                                ))))
                                .webUrl()
                        )
                        .userErrors(userErrorQuery -> userErrorQuery
                                .field()
                                .message()
                        )
                )
        );

        graphClient.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {
//                String email = "" + response.data().getCustomerCreate().getCustomer().getEmail().toString();
//                Log.e("email", "" + email);
                String email1 = response.data().getCheckoutEmailUpdate().getCheckout().getEmail();
                Log.e("email", "" + email1);
                String title = response.data().getCheckoutCreate().getCheckout().getLineItems().getEdges().get(0).getNode().getVariant().getTitle().toString();
                Log.e("title", "" + title);
            }

            @Override
            public void onFailure(@NonNull GraphError error) {

            }
        });
    }

    @Override
    public void AddCartItem() {
        cart_count++;
    }

    @Override
    public void RemoveCartItem() {
        cart_count--;


    }
}

