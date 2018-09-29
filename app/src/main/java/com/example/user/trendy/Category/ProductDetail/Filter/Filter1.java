package com.example.user.trendy.Category.ProductDetail.Filter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.trendy.Category.CategoreDetailAdapter;
import com.example.user.trendy.Category.CategoryModel;
import com.example.user.trendy.Category.ProductDetail.Filter.Adapter.FilterRecyclerAdapter;
import com.example.user.trendy.Category.ProductDetail.Filter.Adapter.FilterValRecyclerAdapter;
import com.example.user.trendy.Category.SubCategoryModel;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Filter1 extends Fragment {
    private List<String> rootFilters;
    private ArrayList<MainFilterModel> filterModels = new ArrayList<>();
    private TextView btnClear;
    private Button btnFilter;
    private RecyclerView filterValListView;
    private FilterRecyclerAdapter adapter;
    private FilterValRecyclerAdapter filterValAdapter;
    private RecyclerView filterListView;
    private ArrayList<FilterDefaultMultipleListModel> tagMultipleListModels = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> vendorMultipleListModels = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> typeMultipleListModels = new ArrayList<>();
    ArrayList<String> vendorarray = new ArrayList<>();
    ArrayList<String> producttag = new ArrayList<>();
    ArrayList<String> producttype = new ArrayList<>();
    private ArrayList<String> vendorSelected = new ArrayList<String>();
    private ArrayList<String> typeSelected = new ArrayList<String>();
    private ArrayList<String> tagSelected = new ArrayList<String>();
    String collectionid;
    private RequestQueue mRequestQueue;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.filter1, container, false);

//        ProductModel productModel=(ProductModel)getArguments().getSerializable("productlist");


        collectionid = getArguments().getString("collectionid");
        Log.e("collection", collectionid);
        getTaglist();
        vendorarray = getArguments().getStringArrayList("vendorarray");
//        producttag=getArguments().getStringArrayList("producttag");
        producttype = getArguments().getStringArrayList("producttype");
        Log.e("vendorarray", String.valueOf(vendorarray.size()));
//        producttag.add(0, "veg");
//        producttag.add(1, "Non veg");

        rootFilters = Arrays.asList(this.getResources().getStringArray(R.array.filter_category));
        for (int i = 0; i < rootFilters.size(); i++) {
            /* Create new MainFilterModel object and set array value to @model
             * Description:
             * -- Class: MainFilterModel.java
             * -- Package:main.shop.javaxerp.com.shoppingapp.model
             * */
            MainFilterModel model = new MainFilterModel();
            /*Title for list item*/
            model.setTitle(rootFilters.get(i));
            Log.e("title1", rootFilters.get(i));
            /*Subtitle for list item*/
            model.setSub("All");
            /*Example:
             * --------------------------------------------
             * Brand => title
             * All => subtitle
             * --------------------------------------------
             * Color => title
             * All => subtitle
             * --------------------------------------------
             * */

            /*add MainFilterModel object @model to ArrayList*/
            filterModels.add(model);
        }


        filterListView = (RecyclerView) view.findViewById(R.id.filter_dialog_listview);
        adapter = new FilterRecyclerAdapter(getActivity(), R.layout.filter_list_item_layout, filterModels);
        filterListView.setAdapter(adapter);
        filterListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        filterListView.setHasFixedSize(true);

        filterValListView = (RecyclerView) view.findViewById(R.id.filter_value_listview);
        filterValAdapter = new FilterValRecyclerAdapter(getActivity(), R.layout.filter_list_val_item_layout, vendorMultipleListModels, MainFilterModel.VENDOR);
        filterValListView.setAdapter(filterValAdapter);
        filterValListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        filterValListView.setHasFixedSize(true);


        btnFilter = (Button) view.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (FilterDefaultMultipleListModel model : vendorMultipleListModels) {
                    if (model.isChecked()) {
                        filterModels.get(MainFilterModel.INDEX_VENDOR).getSubtitles().add(model.getName());
                    }
                }

                for (FilterDefaultMultipleListModel model : typeMultipleListModels) {
                    if (model.isChecked()) {
                        filterModels.get(MainFilterModel.INDEX_TYPE).getSubtitles().add(model.getName());
                    }
                }

                for (FilterDefaultMultipleListModel model : tagMultipleListModels) {
                    if (model.isChecked()) {
                        filterModels.get(MainFilterModel.INDEX_TAG).getSubtitles().add(model.getName());
                    }

                }
                /*Get value from checked of size checkbox*/
                vendorSelected = filterModels.get(MainFilterModel.INDEX_VENDOR).getSubtitles();
                filterModels.get(MainFilterModel.INDEX_VENDOR).setSubtitles(new ArrayList<String>());

                /*Get value from checked of color checkbox*/
                typeSelected = filterModels.get(MainFilterModel.INDEX_TYPE).getSubtitles();
                filterModels.get(MainFilterModel.INDEX_TYPE).setSubtitles(new ArrayList<String>());

                /*Get value from checked of price checkbox*/
                tagSelected = filterModels.get(MainFilterModel.INDEX_TAG).getSubtitles();
                filterModels.get(MainFilterModel.INDEX_TAG).setSubtitles(new ArrayList<String>());

                if (vendorSelected.isEmpty() && typeSelected.isEmpty() && tagSelected.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select size,color,brand", Toast.LENGTH_SHORT).show();
                }

                if (!vendorSelected.isEmpty() || !typeSelected.isEmpty() || !tagSelected.isEmpty()) {
                    Toast.makeText(getActivity(), "Selected Vendor is " + vendorSelected.toString() + "\n" + "Selected Type is " + typeSelected.toString() + "\n" + "Selected Tag is " + tagSelected.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnClear = view.findViewById(R.id.btn_clear);
        /*TODO: Clear user selected */
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (FilterDefaultMultipleListModel selectedModel : vendorMultipleListModels) {
                    selectedModel.setChecked(false);

                }

                for (FilterDefaultMultipleListModel selectedModel : typeMultipleListModels) {
                    selectedModel.setChecked(false);

                }

                for (FilterDefaultMultipleListModel selectedModel : tagMultipleListModels) {
                    selectedModel.setChecked(false);

                }
                adapter.notifyDataSetChanged();
                filterValAdapter.notifyDataSetChanged();
            }
        });


        adapter.setOnItemClickListener(new FilterRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                filterItemListClicked(position, v);
                adapter.setItemSelected(position);
            }
        });
        filterItemListClicked(0, null);
        adapter.setItemSelected(0);

//        sizes = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.filter_size)));
        for (String vendor : vendorarray) {

            /* Create new FilterDefaultMultipleListModel object for brand and set array value to brand model {@model}
             * Description:
             * -- Class: FilterDefaultMultipleListModel.java
             * -- Package:main.shop.javaxerp.com.shoppingapp.model
             * NOTE: #checked value @FilterDefaultMultipleListModel is false;
             * */
            FilterDefaultMultipleListModel model = new FilterDefaultMultipleListModel();
            model.setName(vendor);

            /*add brand model @model to ArrayList*/
            vendorMultipleListModels.add(model);
        }
//        styles = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.filter_brand)));
        for (String type : producttype) {

            /* Create new FilterDefaultMultipleListModel object for brand and set array value to brand model {@model}
             * Description:
             * -- Class: FilterDefaultMultipleListModel.java
             * -- Package:main.shop.javaxerp.com.shoppingapp.model
             * NOTE: #checked value @FilterDefaultMultipleListModel is false;
             * */
            FilterDefaultMultipleListModel model = new FilterDefaultMultipleListModel();
            model.setName(type);

            /*add brand model @model to ArrayList*/
            typeMultipleListModels.add(model);
        }
//        colors = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.filter_color)));
        Log.e("producttag", String.valueOf(producttag.size()));
        for (String tag : producttag) {

            /* Create new FilterDefaultMultipleListModel object for brand and set array value to brand model {@model}
             * Description:
             * -- Class: FilterDefaultMultipleListModel.java
             * -- Package:main.shop.javaxerp.com.shoppingapp.model
             * NOTE: #checked value @FilterDefaultMultipleListModel is false;
             * */
            FilterDefaultMultipleListModel model = new FilterDefaultMultipleListModel();
            model.setName(tag);

            /*add brand model @model to ArrayList*/
            tagMultipleListModels.add(model);
        }
        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void filterItemListClicked(int position, View v) {
        if (position == 0) {
            filterValAdapter = new FilterValRecyclerAdapter(getActivity(), R.layout.filter_list_val_item_layout, vendorMultipleListModels, MainFilterModel.VENDOR);
        } else if (position == 1) {
            filterValAdapter = new FilterValRecyclerAdapter(getActivity(), R.layout.filter_list_val_item_layout, typeMultipleListModels, MainFilterModel.TYPE);
        } else {
            filterValAdapter = new FilterValRecyclerAdapter(getActivity(), R.layout.filter_list_val_item_layout, tagMultipleListModels, MainFilterModel.TAG);
        }

        filterValListView.setAdapter(filterValAdapter);

        filterValAdapter.setOnItemClickListener(new FilterValRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                filterValitemListClicked(position);
            }
        });
        filterValAdapter.notifyDataSetChanged();
    }

    private void filterValitemListClicked(int position) {
        filterValAdapter.setItemSelected(position);
    }

    public void getTaglist() {
        producttag.clear();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.filter_tag,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e("taga", response.toString());

                        try {
                            Log.e("tag1", response.toString());
                            JSONObject obj = new JSONObject(response);
                            Iterator keys = obj.keys();
                            Log.e("Keys", "" + String.valueOf(keys));

                            while (keys.hasNext()) {
                                String dynamicKey = (String) keys.next();
                                Log.d("Dynamic Key", dynamicKey);


                                JSONArray array = obj.getJSONArray(dynamicKey);
                                for (int i = 0; i < array.length(); i++) {
                                    producttag.add(array.getString(i));
                                    Log.d("Array Value", array.getString(i));
                                }


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
                                FilterDefaultMultipleListModel model = new FilterDefaultMultipleListModel();
                                model.setName(tag);

                                /*add brand model @model to ArrayList*/
                                tagMultipleListModels.add(model);
                            }
//        }


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
                params.put("collection_id", "33238122615");

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
