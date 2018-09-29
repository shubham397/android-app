package com.example.user.trendy.Category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.Constants;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Categories extends Fragment {
    public String collectionurl = "https://cdn.shopify.com/s/files/1/2364/1061/t/4/assets/tf.json?3518560706218956420";

    private TextView mTextMessage;
    Toolbar toolbar;
    RecyclerView recyclerView;
    GraphClient graphClient;
    ArrayList<CategoryModel> categoryList = new ArrayList<>();
    ArrayList<SubCategoryModel> subCategoryModelArrayList;
    CategoreDetailAdapter categoreDetailAdapter;
    private RequestQueue mRequestQueue;
    String imageurl = "";
    LinearLayout subcategory;
    String converted;
    private String image1="";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.categories, container, false);

//        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
//        toolbar.setTitle("Categories");
//        toolbar.getMenu().clear();

//        mTextMessage = view.findViewById(R.id.message);
//        mTextMessage.setText(R.string.title_categories);
        subcategory = view.findViewById(R.id.sublayout);
        subcategory.setVisibility(View.GONE);


        recyclerView = view.findViewById(R.id.categories_recyclerview);

        graphClient = GraphClient.builder(getActivity())
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(getActivity().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();


        //    LinearLayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        categoreDetailAdapter = new CategoreDetailAdapter(getActivity(), categoryList, getFragmentManager());
        recyclerView.setAdapter(categoreDetailAdapter);
        // productlist();
        collectionList();
        return view;
    }

    public void productlist() {
        categoryList.clear();
        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .shop(shopQuery -> shopQuery
                        .collections(arg -> arg.first(100), collectionConnectionQuery -> collectionConnectionQuery
                                .edges(collectionEdgeQuery -> collectionEdgeQuery
                                        .node(collectionQuery -> collectionQuery
                                                .title()
                                                .handle()
                                                .image(args -> args.src())
                                                .products(arg -> arg.first(10), productConnectionQuery -> productConnectionQuery
                                                        .edges(productEdgeQuery -> productEdgeQuery
                                                                .node(productQuery -> productQuery
                                                                        .title()
                                                                        .productType()
                                                                        .description()
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        graphClient.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {

                List<Storefront.Collection> collections = new ArrayList<>();
                for (Storefront.CollectionEdge collectionEdge : response.data().getShop().getCollections().getEdges()) {
                    collections.add(collectionEdge.getNode());
                    CategoryModel categoreDetail = new CategoryModel();
                    categoreDetail.setCollection(collectionEdge.getNode());

                    if (collectionEdge.getNode().getImage() != null) {
                        Log.d("Collection Image :", collectionEdge.getNode().getImage().getSrc());
                        //    categoreDetail.setImageurl(collectionEdge.getNode().getImage().getSrc());
                    }
                    categoryList.add(categoreDetail);

                    Log.d("Collection value :", collectionEdge.getNode().getTitle());
                    Log.d("Collection ID :", String.valueOf(collectionEdge.getNode().getId()));
                    // Log.d("Collection ID :", collectionEdge.getNode().getImage().getSrc());


                    List<Storefront.Product> products = new ArrayList<>();
                    for (Storefront.ProductEdge productEdge : collectionEdge.getNode().getProducts().getEdges()) {
                        products.add(productEdge.getNode());

//                        Log.d("product : ", String.valueOf(productEdge.getNode().getTitle()));
//                        Log.d("product ID : ", String.valueOf(productEdge.getNode().getId()));
//                        Log.d("product Description : ", String.valueOf(productEdge.getNode().getDescription()));


                        //   getProductVariantID(String.valueOf(productEdge.getNode().getId()));
                    }
                }
//
//                for(int i=0;i<collections.size();i++)
//                {
////                    if(String.valueOf(collections.get(i).getImage().getSrc())!=null)
//                }

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

//

                        categoreDetailAdapter.notifyDataSetChanged();

                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                Log.e("product fail", "Failed to execute query", error);

            }

        });


    }

    private void collectionList() {

        mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.navigation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response", response);

                            JSONObject obj = new JSONObject(response);
                            Log.e("response1", response);
                            categoryList.clear();
                            JSONObject menu = obj.getJSONObject("menu");
                            //  String status = obj.getString("menu");

                            String title = menu.getString("title");
                            Log.e("title", title);
                            // JSONObject allhistoryobj = obj.getJSONObject("insurance");
                            JSONArray jsonarray = menu.getJSONArray("items");
                            Log.e("jsonarray", String.valueOf(jsonarray));

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject collectionobject = jsonarray.getJSONObject(i);
                                CategoryModel categoreDetail = new CategoryModel();

                                String id = "" + collectionobject.getString("subject_id");
                                String collectiontitle = collectionobject.getString("title");
                                String nav = collectionobject.getString("type");
//                                String image = collectionobject.getString("image");


                                Log.e("id", id);
                                Log.e("collectiontitle", collectiontitle);

                                if (id.trim().length() != 0) {
                                    String text = "gid://shopify/Collection/" + id.trim();

                                    converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
                                    Log.e("coverted", converted.trim());
                                }

                                if (nav.trim().equals("http") || nav.trim().equals("collection")) {
                                    categoreDetail.setId(id.trim());
                                    categoreDetail.setCollectiontitle(collectiontitle);
                                    categoreDetail.setImageurl("");

                                    JSONArray jsonarray1 = collectionobject.getJSONArray("items");
                                    Log.e("jsonarray1", String.valueOf(jsonarray1));
                                    subCategoryModelArrayList = new ArrayList<>();
                                    if (jsonarray1.length() != 0) {
                                        for (int j = 0; j < jsonarray1.length(); j++) {
                                            JSONObject subcollectionobject = jsonarray1.getJSONObject(j);
                                            SubCategoryModel subCategoryModel = new SubCategoryModel();

                                            String subid = "" + subcollectionobject.getString("subject_id");
                                            String subcollectiontitle = subcollectionobject.getString("title");
                                            String type=subcollectionobject.getString("type");
                                            if(type.trim().equals("collection")) {
                                                 image1 = subcollectionobject.getString("image");
                                            }

                                            if (!subid.trim().equals("null")) {


                                                if (subid.trim().length() != 0) {
                                                    String text = "gid://shopify/Collection/" + subid.trim();

                                                    converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
                                                    Log.e("coverted", converted.trim());
                                                }
                                                subCategoryModel.setId(subid.trim());
                                                subCategoryModel.setTitle(subcollectiontitle);
                                                subCategoryModel.setImage(image1);
                                                subCategoryModelArrayList.add(subCategoryModel);
                                                categoreDetail.setSubCategoryModelArrayList(subCategoryModelArrayList);

//                                                String text="gid://shopify/Product/"+subid.trim();
//
//                                                String converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
//                                                Log.e("converted", "" + converted);
//                                                    getProductByCollection(converted);
//                                                    Log.e("imageurl1", "" + imageurl);
//                                                    categoreDetail.setImageurl(imageurl);
                                            }

                                            Log.e("subid", subid);
                                            Log.e("subcollectiontitle", subcollectiontitle);
                                        }
//                                    } else {
//                                        categoryList.add(categoreDetail);
                                    }
                                    categoryList.add(categoreDetail);
                                }


                            }


                            categoreDetailAdapter = new CategoreDetailAdapter(getActivity(), categoryList, getFragmentManager());

                            recyclerView.setAdapter(categoreDetailAdapter);
                            categoreDetailAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

        };
        stringRequest.setTag("categories_page");
        // VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);

    }

    public void getProductByCollection(String categoryID) {
        imageurl = "";
        Log.e("inside", "came");
        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID(categoryID), nodeQuery -> nodeQuery
                        .onCollection(collectionQuery -> collectionQuery
                                .title()
                                .image(args -> args.src())
                                .products(arg -> arg.first(100), productConnectionQuery -> productConnectionQuery
                                        .edges(productEdgeQuery -> productEdgeQuery
                                                .node(productQuery -> productQuery
                                                        .title()
                                                        .productType()
                                                        .description()
                                                        .descriptionHtml()
                                                        .images(arg -> arg.first(10), imageConnectionQuery -> imageConnectionQuery
                                                                .edges(imageEdgeQuery -> imageEdgeQuery
                                                                        .node(imageQuery -> imageQuery
                                                                                .src()
                                                                        )
                                                                )
                                                        )
                                                        .tags()
                                                        .variants(arg -> arg.first(10), variantConnectionQuery -> variantConnectionQuery
                                                                .edges(variantEdgeQuery -> variantEdgeQuery
                                                                        .node(productVariantQuery -> productVariantQuery
                                                                                .price()
                                                                                .title()
                                                                                .image(args -> args.src())
                                                                                .weight()
                                                                                .weightUnit()
                                                                                .compareAtPrice()
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


                List<Storefront.Product> products = new ArrayList<>();
                Storefront.Collection product = (Storefront.Collection) response.data().getNode();

//                if (product.responseData != null) {
//                    if (product.getImage() != null) {
//                        imageurl = product.getImage().getSrc();
//                        Log.e("imagee", "" + imageurl);
//                    }

//                for(Storefront.ProductEdge productEdge : product.getProducts().getEdges()) {
//                    ProductModel productDetail = new ProductModel();
//                    productDetail.setProduct_Name(productEdge.getNode().getTitle());
//                    productDetail.setProduct_description(productEdge.getNode().getDescription());
//
//                    Log.d("prodcut title : ", productEdge.getNode().getTitle());
//                }


                Log.e("subinside", "came");

                for (Storefront.ProductEdge productEdge : product.getProducts().getEdges()) {
                    Log.e("product_title : ", productEdge.getNode().getTitle());
                    Log.e("product_ID : ", String.valueOf(productEdge.getNode().getId()));


                    ArrayList<Storefront.Image> productImages = new ArrayList<>();
                    for (final Storefront.ImageEdge imageEdge : productEdge.getNode().getImages().getEdges()) {
                        productImages.add(imageEdge.getNode());
                        Log.d("Product Image: ", productImages.get(0).getSrc());
                        imageurl = productImages.get(0).getSrc();
                    }


                    List<Storefront.ProductVariant> productVariants = new ArrayList<>();


                    for (final Storefront.ProductVariantEdge productVariantEdge : productEdge.getNode().getVariants().getEdges()) {
                        productVariants.add(productVariantEdge.getNode());


                        if (productVariantEdge.getNode().getImage() != null)
                            // productDetail.setImageUrl(productVariantEdge.getNode().getImage().getSrc());

                            Log.d("Product varient Id : ", String.valueOf(productVariantEdge.getNode().getId()));
                        Log.d("Product title : ", String.valueOf(productVariantEdge.getNode().getTitle()));
                        Log.d("Product price : ", String.valueOf(productVariantEdge.getNode().getPrice()));


                    }
                }
            }


            @Override
            public void onFailure(@NonNull GraphError error) {

            }
        });
    }


}
