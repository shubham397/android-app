package com.example.user.trendy.Product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.trendy.Category.CategoryModel;
import com.example.user.trendy.Category.ProductAdapter;
import com.example.user.trendy.Category.ProductModel;
import com.example.user.trendy.Filter.Filter_Fragment;
import com.example.user.trendy.Filter.Filter_Type.FilterModel;
import com.example.user.trendy.Interface.ProductClickInterface;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.Constants;
import com.shopify.buy3.GraphClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ProductList extends Fragment implements ProductClickInterface,View.OnClickListener {
    GraphClient graphClient;
    RecyclerView recyclerView;
    ArrayList<ProductListModel> productDetalList = new ArrayList<>();
    ArrayList vendorarray = new ArrayList();
    ArrayList producttype = new ArrayList();
    ArrayList producttag = new ArrayList();
    ProductListAdapter productAdapter;
    String productid = "";
    String checkoutId;
    TextView category_title;
    TextView view1, subcategory, filter;
    TextView sublistname, all;
    public static int i = 0;
    public static boolean isViewWithCatalog = true;
    CategoryModel detail = new CategoryModel();
    String min_price="",max_price="",collectionid,dynamicKey;
    ArrayList<String> selectedFilterList=new ArrayList<>();
    String product_id,imagesrc;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.category_product, container, false);

        category_title = view.findViewById(R.id.category_title);
        filter = view.findViewById(R.id.filter);
        view1 = view.findViewById(R.id.view);
        filter.setOnClickListener(this);
        view1.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.product_recyclerview);
        min_price= getArguments().getString("min_price");
        max_price= getArguments().getString("max_price");
        collectionid= getArguments().getString("collectionid");
        selectedFilterList=getArguments().getStringArrayList("selectedFilterList");
        dynamicKey=getArguments().getString("dynamicKey");
        postFilter();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        productAdapter = new ProductListAdapter(getActivity(), productDetalList, getFragmentManager(), this);
        recyclerView.setAdapter(productAdapter);
    return view;
    }

    public void postFilter() {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = "http://...";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("collection_id", collectionid);

            JSONObject price= new JSONObject();
            price.put("min_price", min_price);
            price.put("max_price", max_price);
            jsonBody.put("price",price);

            JSONArray food= new JSONArray();
            JSONObject food1= new JSONObject();

            for (int i = 0; i < selectedFilterList.size(); i++) {
                String type = selectedFilterList.get(i).trim();
                Log.e("type",type);
                food1.put("name", dynamicKey);
                food1.put("value", "Filter"+" "+dynamicKey+" "+type);
            }
            food.put(food1);
            jsonBody.put("food",food);



            Log.d("check JSON",jsonBody.toString());



            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.filter_post, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject obj = new JSONObject(response);

                        Iterator keys = obj.keys();
                        Log.e("Keys", "" + String.valueOf(keys));

                        while (keys.hasNext()) {
                            dynamicKey = (String) keys.next();
                            Log.d("Dynamic Key", "" + dynamicKey);

                            JSONArray array = null;
                            try {
                                array = obj.getJSONArray(dynamicKey);

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    String title=object1.getString("title");
                                    String min_price=object1.getString("min_price");
                                    Log.e("image1",title+min_price);

                                    JSONArray array1=object1.getJSONArray("images");
                                    for (int j = 0; j < array1.length(); j++) {
                                        JSONObject object = array1.getJSONObject(j);
                                         product_id=object.getString("product_id");
                                         imagesrc=object.getString("src");
                                        ProductListModel productListModel=new ProductListModel(product_id,imagesrc,min_price,title);
                                        Log.e("image",product_id+imagesrc);
                                        productDetalList.add(productListModel);
                                    }




                                }
                                productAdapter.notifyDataSetChanged();
                            } catch (JSONException e1) {
                                e1.printStackTrace();

                            }


                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
//                    try {
////                        return requestBody == null ? null : requestBody.getBytes("utf-8");
//                        return requestBody == null;
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
//                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    //TODO if you want to use the status code for any other purpose like to handle 401, 403, 404
                    String statusCode = String.valueOf(response.statusCode);
                    //Handling logic
                    return super.parseNetworkResponse(response);
                }
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter:


////                String ps2 = "dGVjaFBhC3M=";
//                byte[] tmp2 = Base64.decode(id,Base64.DEFAULT);
//
//                String val2 = new String(tmp2);
//                String[] str = val2.split("/");
//
////                String decodeid=Base64.decode(id,)
//                Log.d("str value", str[4]);

                Fragment fragment = new Filter_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("collectionid", collectionid);
//                bundle.putStringArrayList("vendorarray", vendorarray);
//                bundle.putStringArrayList("producttag", producttag);
//                bundle.putStringArrayList("producttype", producttype);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.home_container, fragment, "fragment");
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                ft.addToBackStack(null);
                ft.commit();

                break;

            case R.id.view:
                isViewWithCatalog = !isViewWithCatalog;

//                supportInvalidateOptionsMenu();
                //loading = false;
                recyclerView.setLayoutManager(isViewWithCatalog ? new LinearLayoutManager(getActivity()) : new GridLayoutManager(getActivity(), 2));
                recyclerView.setAdapter(productAdapter);

//                LinearLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
////                LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//                recyclerView.setLayoutManager(layoutManager1);
//                recyclerView.setItemAnimator(new DefaultItemAnimator());

//                productAdapter = new ProductAdapter(getActivity(), productDetalList, getFragmentManager(), this);
//                recyclerView.setAdapter(productAdapter);
//                getProductByCollection(converted.trim());
                break;
        }
    }

    @Override
    public void clickProduct(String productid) {

    }

    @Override
    public void OnclickPlus(String productid) {

    }

    @Override
    public void OnclickWhislilst(String productid) {

    }
}
