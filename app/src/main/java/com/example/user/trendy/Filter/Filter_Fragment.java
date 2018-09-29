package com.example.user.trendy.Filter;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.trendy.Category.Categories;
import com.example.user.trendy.Category.CategoryProduct;
import com.example.user.trendy.Category.ProductDetail.Filter.FilterDefaultMultipleListModel;
import com.example.user.trendy.Category.ProductDetail.Filter.MainFilterModel;
import com.example.user.trendy.Filter.Filter_Type.FilterAdapter;
import com.example.user.trendy.Filter.Filter_Type.FilterModel;
import com.example.user.trendy.Filter.Price.PriceAdapter;
import com.example.user.trendy.Filter.Price.PriceModel;
import com.example.user.trendy.Filter.SortBy.SortByAdapter;
import com.example.user.trendy.Filter.SortBy.SortByModel;
import com.example.user.trendy.Product.ProductList;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import static com.example.user.trendy.Category.CategoryProduct.i;
import static com.example.user.trendy.R.color.appcolor;

public class Filter_Fragment extends Fragment {
    RecyclerView sortby_recycler, filter_recycler, price_recycler;
    FilterAdapter filterAdapter;
    PriceAdapter priceAdapter;
    SortByAdapter sortByAdapter;
    ArrayList<FilterModel> filterModelArrayList = new ArrayList<>();
    ArrayList<PriceModel> priceModelArrayList = new ArrayList<>();
    ArrayList<SortByModel> sortByModelArrayList = new ArrayList<>();
    private RequestQueue mRequestQueue;
    ArrayList<String> producttag = new ArrayList<>();
    private String collectionid;
    ArrayList<String> selectedarray = new ArrayList<>();
    Button btn_filter;
    private ArrayList<String> selectedFilterList;
    private ArrayList<String> selectedsortList;
    private ArrayList<String> selectedpriceList;
    int firstsplit, secondsplit, thirdsplit, fourthsplit;
    ArrayList<String> pricelist = new ArrayList<>();
    ArrayList<String> sortlist = new ArrayList<>();
    Toolbar toolbar;
    TextView type;
    String min_price, max_price;
    String dynamicKey = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Filter");
        toolbar.setTitleTextColor(getResources().getColor(R.color.appcolor));

        collectionid = getArguments().getString("collectionid");
        Log.e("collection", collectionid);
        getTaglist();


        sortby_recycler = view.findViewById(R.id.sortby_recycler);
        filter_recycler = view.findViewById(R.id.filter_recycler);
        price_recycler = view.findViewById(R.id.price_recycler);
        btn_filter = view.findViewById(R.id.btn_filter1);
        type = view.findViewById(R.id.type);

        filterAdapter = new FilterAdapter(getActivity(), filterModelArrayList, getFragmentManager());
        filter_recycler.setAdapter(filterAdapter);
        filter_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        priceAdapter = new PriceAdapter(getActivity(), priceModelArrayList, getFragmentManager());
        price_recycler.setAdapter(priceAdapter);
        price_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        sortByAdapter = new SortByAdapter(getActivity(), sortByModelArrayList, getFragmentManager());
        sortby_recycler.setAdapter(sortByAdapter);
        sortby_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        sortlist.add("Price : Low to High");
        sortlist.add("Price : High to Low");

        for (String tag : sortlist) {
            SortByModel sortByModel = new SortByModel(tag, false);
            sortByModelArrayList.add(sortByModel);
        }
        sortByAdapter.notifyDataSetChanged();

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("inside", "came");

                selectedFilterList = new ArrayList<>();

                selectedFilterList = ((FilterAdapter) filterAdapter).getSelectedContactList();
                selectedsortList = ((SortByAdapter) sortByAdapter).getSelectedSortList();
                selectedpriceList = ((PriceAdapter) priceAdapter).getSelectedPriceList();

                Log.e("adaa", "" + selectedFilterList.toString() + selectedpriceList.toString() + selectedsortList.toString());
                if (selectedpriceList.size() != 0) {
                    String price = selectedpriceList.get(0).trim();
                    StringTokenizer tokens = new StringTokenizer(price, "-");
                    min_price = tokens.nextToken().trim();// this will contain "Fruit"
                    max_price = tokens.nextToken().trim();
                    Log.e("pricec", "" + min_price + max_price);
                }
                Fragment ProductList = new CategoryProduct();
                Bundle bundle = new Bundle();
                bundle.putString("collection","filter");
                bundle.putString("min_price", min_price);
                bundle.putString("max_price", max_price);
                bundle.putString("collectionid", collectionid);
                bundle.putString("dynamicKey", dynamicKey);
                bundle.putStringArrayList("selectedFilterList", selectedFilterList);
                ProductList.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.home_container, ProductList, "Categories");
//                    transaction.addToBackStack(null);
                transaction.commit();

//                postFilter();
            }
        });


        filterAdapter.notifyDataSetChanged();


        return view;
    }


    public void getTaglist() {
        filterModelArrayList.clear();
        pricelist.clear();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.filter_tag1 + collectionid.trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e("taga", response.toString());

                        try {
                            Log.e("tag1", response.toString());
                            JSONObject obj = new JSONObject(response);
                            JSONObject obj1 = obj.getJSONObject("filters");

                            Iterator keys = obj1.keys();
                            Log.e("Keys", "" + String.valueOf(keys));

                            while (keys.hasNext()) {
                                dynamicKey = (String) keys.next();
                                Log.d("Dynamic Key", "" + dynamicKey);

                                JSONArray array = obj1.getJSONArray(dynamicKey);
                                for (int i = 0; i < array.length(); i++) {
                                    producttag.add(array.getString(i));
                                    Log.d("Array Value", array.getString(i));
                                }


                            }

                            String minprice = obj.getString("min_price");
                            Log.d(" minprice", minprice);
                            String max_price = obj.getString("max_price");
                            Log.d(" max_price", max_price);

                            int splitvalue;
                            splitvalue = Integer.parseInt(max_price) / 4;
                            firstsplit = Math.round(Integer.parseInt(minprice) + splitvalue);
                            secondsplit = Math.round(firstsplit + splitvalue);
                            thirdsplit = Math.round(secondsplit + splitvalue);
                            fourthsplit = Math.round(Integer.parseInt(max_price));

                            String first = minprice + " - " + String.valueOf(firstsplit);
                            String second = String.valueOf(firstsplit + 1 + " - " + secondsplit);
                            String third = String.valueOf(secondsplit + 1 + " - " + thirdsplit);
                            String fourth = String.valueOf(thirdsplit + 1 + " - " + max_price);

                            pricelist.add(first);
                            pricelist.add(second);
                            pricelist.add(third);
                            pricelist.add(fourth);
                            for (String tag : pricelist) {
                                PriceModel priceModel = new PriceModel(tag, false);
                                priceModelArrayList.add(priceModel);

                            }

//
                            Log.e("producttag", String.valueOf(producttag.size()));
                            for (String tag : producttag) {

                                /* Create new FilterDefaultMultipleListModel object for brand and set array value to brand model {@model}
                                 * Description:
                                 * -- Class: FilterDefaultMultipleListModel.java
                                 * -- Package:main.shop.javaxerp.com.shoppingapp.model
                                 * NOTE: #checked value @FilterDefaultMultipleListModel is false;
                                 * */
                                FilterModel model = new FilterModel(tag, false);
//                                model.setName(tag);

                                /*add brand model @model to ArrayList*/
                                filterModelArrayList.add(model);

                            }
                            filterAdapter.notifyDataSetChanged();
                            priceAdapter.notifyDataSetChanged();

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    type.setText(dynamicKey);
                                }
                            });


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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                String insurance_id = SharedPreference.getData("insurance_id", getActivity());
//                Log.e("insurance_id", String.valueOf(insurance_id));
                params.put("collection_id", collectionid.trim());

                return params;
            }
        };
        stringRequest.setTag("insurance_view");
        //  VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        mRequestQueue.add(stringRequest);


    }

}
