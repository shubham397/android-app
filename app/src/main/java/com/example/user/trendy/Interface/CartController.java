package com.example.user.trendy.Interface;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.example.user.trendy.Bag.Db.AddToCart_Model;
import com.example.user.trendy.Bag.Db.DBHelper;
import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.Category.ProductDetail.SelectItemModel;
import com.example.user.trendy.Whislist.AddWhislistModel;
import com.example.user.trendy.Whislist.WhislistDB.DBWhislist;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CartController extends ViewModel implements CommanCartControler {

    DBHelper db;
    DBWhislist dbWhislist;
    SelectItemModel model;
    Context mContext;
    private List<AddToCart_Model> cartList = new ArrayList<>();
    private List<AddWhislistModel> whislist = new ArrayList<>();

    public CartController(Context mContext) {
        this.mContext = mContext;
        db = new DBHelper(mContext);
        dbWhislist = new DBWhislist(mContext);

    }

    @Override
    public void AddToCart(String id) {
        cartList.clear();

        cartList = db.getCartList();

        Log.e("plus", "plus");
        String text = "gid://shopify/Product/" + id.trim();

        String converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
        Log.e("coverted", converted.trim());

        getProductVariantID(converted.trim());
    }

    @Override
    public void AddQuantity(String id) {
//        String getquantity = db.getQuantity(id.trim());
//        Log.e("getquantity", getquantity);
        db.update(id.trim(), 1);

    }


    @Override
    public void RemoveQuantity(String id) {
        db.decreaseqty(id);
    }

    @Override
    public int getTotalPrice() {
        cartList.clear();

        cartList = db.getCartList();
        int totalcost1 = 0;
        for (int i = 0; i < cartList.size(); i++) {
            int qty = cartList.get(i).getQty();
            Double cost = cartList.get(i).getProduct_price();
            Log.e("qty", "" + String.valueOf(qty));
            Log.e("cost", "" + String.valueOf(cost));
            Log.e("icost", "" + String.valueOf(totalcost1));
            totalcost1 = totalcost1 + (qty * (cost.intValue()));
            Log.e("cost", "" + String.valueOf(totalcost1));
        }
        return totalcost1;
    }

    @Override
    public void UpdateShipping(String id, String value) {

        db.updateshipping(id.trim(), value);
    }

    @Override
    public void AddToWhislist(String id) {
        whislist.clear();

        whislist = dbWhislist.getCartList();

        Log.e("plus", "plus");
        String text = "gid://shopify/Product/" + id.trim();

        String converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
        Log.e("coverted", converted.trim());

        getProductVariantID1(converted.trim());
    }

    private void getProductVariantID1(String productID) {

        GraphClient graphClient = GraphClient.builder(mContext)
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(mContext.getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID(productID), nodeQuery -> nodeQuery
                        .onProduct(productQuery -> productQuery
                                .title()
                                .description()
                                .descriptionHtml()
                                .tags()
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
                    Log.e("tttyt", String.valueOf(model.getProduct().getTags()));

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
                        if (whislist.size() == 0) {
                            Log.e("empty", "empty");
                            int qty = 1;
                            dbWhislist.insertToDo(productVariant.get(0), model.getProduct().getTitle());
                        } else {

//                            db.checkUser(productVariant.get(0).getId().toString().trim());
                            if (dbWhislist.checkUser(productVariant.get(0).getId().toString().trim())) {

//                                db.update(productVariant.get(0).getId().toString().trim(), 1);
                            } else {
                                dbWhislist.insertToDo(productVariant.get(0), model.getProduct().getTitle());
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

    private void getProductVariantID(String productID) {
        GraphClient graphClient = GraphClient.builder(mContext)
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(mContext.getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID(productID), nodeQuery -> nodeQuery
                        .onProduct(productQuery -> productQuery
                                .title()
                                .description()
                                .descriptionHtml()
                                .tags()
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
                    Log.e("tttyt", String.valueOf(model.getProduct().getTags()));

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
                            db.insertToDo(productVariant.get(0), qty, model.getProduct().getTitle(), String.valueOf(model.getProduct().getTags()), model.getShip());
                        } else {

//                            db.checkUser(productVariant.get(0).getId().toString().trim());
                            if (db.checkUser(productVariant.get(0).getId().toString().trim())) {

                                db.update(productVariant.get(0).getId().toString().trim(), 1);
                            } else {
                                db.insertToDo(productVariant.get(0), 1, model.getProduct().getTitle(), String.valueOf(model.getProduct().getTags()), model.getShip());
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

}

