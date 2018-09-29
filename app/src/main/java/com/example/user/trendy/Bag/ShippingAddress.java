package com.example.user.trendy.Bag;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.trendy.Bag.Db.AddToCart_Model;
import com.example.user.trendy.Bag.Db.DBHelper;
import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.Category.CategoreDetailAdapter;
import com.example.user.trendy.Category.CategoryModel;
import com.example.user.trendy.Category.SubCategoryModel;
import com.example.user.trendy.Interface.CartController;
import com.example.user.trendy.Interface.CommanCartControler;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.Constants;
import com.example.user.trendy.Util.SharedPreference;
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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShippingAddress extends Fragment implements TextWatcher {
    String emailstring, firstnamestring, lastnamestring, pincode, area, state, city, country, check_ship_bill = "", s_pincode, s_area, s_state, s_city, s_country, include_state = "", exclude_state = "", product_id = " ";
    String b_area, b_city, b_state, b_country, b_pincode;
    EditText email, first_name, last_name, shipping_door_street_input, shipping_pin_input, shipping_city_input, shipping_state_input, shipping_country_input;
    EditText billing_door_street_input, billing_city, billing_state, billing_country, billing_pin;
    CheckBox same;
    private RequestQueue mRequestQueue;
    ArrayList<String> citylist = new ArrayList<>();
    LinearLayout payment_section;
    LinearLayout layout_same, layout_placing;
    private GraphClient graphClient;
    private List<AddToCart_Model> cartList = new ArrayList<>();
    TextView placing, placing1;
    ArrayList<String> getInclude = new ArrayList<>();
    ArrayList<String> productlist = new ArrayList<>();
    CartController cartController;
    CommanCartControler commanCartControler;
    int a = 0;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.shippingaddress, container, false);
        cartController = new CartController(getActivity());
        commanCartControler = (CommanCartControler) cartController;

        graphClient = GraphClient.builder(getActivity())
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(getActivity().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();

        emailstring = SharedPreference.getData("email", getActivity());
        firstnamestring = SharedPreference.getData("firstname", getActivity());
        lastnamestring = SharedPreference.getData("lastname", getActivity());

        email = view.findViewById(R.id.email);
        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        shipping_door_street_input = view.findViewById(R.id.shipping_door_street_input);
        shipping_pin_input = view.findViewById(R.id.shipping_pin_input);
        shipping_city_input = view.findViewById(R.id.shipping_city_input);
        shipping_state_input = view.findViewById(R.id.shipping_state_input);
        shipping_country_input = view.findViewById(R.id.shipping_country_input);

        billing_door_street_input = view.findViewById(R.id.billing_door_street_input);
        billing_city = view.findViewById(R.id.billing_city);
        billing_state = view.findViewById(R.id.billing_state);
        billing_country = view.findViewById(R.id.billing_country);
        billing_pin = view.findViewById(R.id.billing_pin);
        placing = view.findViewById(R.id.placing);
        placing1 = view.findViewById(R.id.placing1);
        layout_placing = view.findViewById(R.id.layout_placing);

        payment_section = view.findViewById(R.id.payment_section);
        same = view.findViewById(R.id.same_address);
        layout_same = view.findViewById(R.id.layout_same);

        s_pincode = shipping_pin_input.getText().toString();
        email.setText(emailstring);
        first_name.setText(firstnamestring);
        last_name.setText(lastnamestring);
//        shipping_city_input.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//                mBuilder.setTitle("City");
//                mBuilder.setSingleChoiceItems(citylist.toArray(new String[citylist.size()]), -1, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        shipping_city_input.setText(citylist.get(i));
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                AlertDialog mDialog = mBuilder.create();
//                mDialog.show();
//            }
//        });
        shipping_pin_input.addTextChangedListener(this);
        billing_pin.addTextChangedListener(this);

        same.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (same.isChecked()) {
                    s_area = shipping_door_street_input.getText().toString();
                    s_city = shipping_city_input.getText().toString();
                    s_state = shipping_state_input.getText().toString();
                    s_country = shipping_country_input.getText().toString();
                    s_pincode = shipping_pin_input.getText().toString();

                    b_area = s_area;
                    b_city = s_city;
                    b_state = s_state;
                    b_country = s_country;
                    b_pincode = s_pincode;
                    layout_same.setVisibility(View.GONE);
                } else {
                    b_area = "";
                    b_city = "";
                    b_state = "";
                    b_country = "";
                    b_pincode = "";
                    layout_same.setVisibility(View.VISIBLE);
                }
            }
        });

        payment_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a == 0) {
                    s_pincode = shipping_pin_input.getText().toString().trim();
                    s_area = shipping_door_street_input.getText().toString().trim();
                    s_state = shipping_state_input.getText().toString().trim();
                    s_city = shipping_city_input.getText().toString().trim();
                    s_country = shipping_country_input.getText().toString().trim();
                    emailstring = email.getText().toString().trim();
                    firstnamestring = first_name.getText().toString().trim();
                    lastnamestring = last_name.getText().toString().trim();

                    Intent intent = new Intent(getActivity(), PayUMoneyActivity.class);
                    intent.putExtra("firstname", firstnamestring);
                    intent.putExtra("lastname", lastnamestring);
                    intent.putExtra("email", emailstring);
                    intent.putExtra("s_area", s_area);
                    intent.putExtra("s_city", s_city);
                    intent.putExtra("s_state", s_state);
                    intent.putExtra("s_country", s_country);
                    intent.putExtra("s_pincode", s_pincode);
                    startActivity(intent);
                } else {
                    layout_placing.findFocus();
                }

            }
        });
        layout_placing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("checkproductlist", String.valueOf(productlist.size()));
                Fragment bag = new Bag();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("nonshipping", productlist);
                bundle.putString("state", state);
                bag.setArguments(bundle);
                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                transaction1.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction1.replace(R.id.home_container, bag, "Bag");
//                    transaction1.addToBackStack(null);
                transaction1.commit();
            }
        });
        return view;
    }


    private void getAddress(String pincode) {
        citylist.clear();
        area = "";
        city = "";
        state = "";
        country = "";
        mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.shippingaddressfetch + pincode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response", response);

                            JSONObject obj = new JSONObject(response);
                            Log.e("response1", response);
                            String status = obj.getString("Status");
                            if (status.equals("Success")) {
                                JSONArray jsonarray = obj.getJSONArray("PostOffice");
                                Log.e("jsonarray", String.valueOf(jsonarray));

                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);
                                    area = object.getString("Name");
                                    city = object.getString("District");
                                    state = object.getString("State");
                                    country = object.getString("Country");
                                    citylist.add(city);

                                }
                            } else {

                            }
                            if (check_ship_bill.trim().equals("shipping")) {
                                Log.e("city", "" + city);
                                shipping_city_input.setText(city);
                                shipping_state_input.setText(state);
                                shipping_country_input.setText(country);
                            } else {
                                Log.e("city", "" + city);
                                billing_city.setText(city);
                                billing_state.setText(state);
                                billing_country.setText(country);
                            }

                            getdataDB(state);

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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence != null) {
            if (charSequence.hashCode() == shipping_pin_input.getText().hashCode()) {
                s_pincode = shipping_pin_input.getText().toString();
                if (s_pincode.trim().length() == 0) {
                    shipping_city_input.setText("");
                    shipping_state_input.setText("");
                    shipping_country_input.setText("");
                } else {
                    check_ship_bill = "shipping";
                    getAddress(s_pincode);

                }
            } else if (charSequence.hashCode() == billing_pin.getText().hashCode()) {
                b_pincode = billing_pin.getText().toString();
                if (b_pincode.trim().length() == 0) {
                    billing_city.setText("");
                    billing_state.setText("");
                    billing_country.setText("");
                } else {
                    check_ship_bill = "billing";
                    getAddress(b_pincode);

                }
            }


        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void getdataDB(String state) {
        include_state = "";
        exclude_state = "";
        productlist.clear();
        cartList.clear();
        DBHelper db = new DBHelper(getActivity());
        cartList = db.getCartList();
        for (int i = 0; i < cartList.size(); i++) {
            String tag = cartList.get(i).getTag();
            Log.e("tag", "" + cartList.get(i).getTag());
            String tagcheck = "EXCLUDES:" + state;
            String exclude = "EXCLUDES";
            String include = "INCLUDES";
            String includecheck = "INCLUDES:" + state;
            if (tag.toLowerCase().contains(exclude.toLowerCase())) {
                a = 1;
                getInclude.clear();
                productlist.add(cartList.get(i).getProduct_varient_id().trim());


                String[] items1 = tag.split(",");
                for (String item : items1) {
                    getInclude.add(item);
                }
                Log.d("getsize", String.valueOf(getInclude.size()));
                for (int j = 0; j < getInclude.size(); j++) {
                    if (getInclude.get(j).toLowerCase().contains(exclude.toLowerCase())) {
                        String[] item = getInclude.get(j).split(":");
                        exclude_state = item[1];
                        Log.d("exclude_state", exclude_state);
                    }
                    Log.d("state", state.trim().toLowerCase());
                    Log.d("exclude", " " + exclude_state.trim().toLowerCase());
                    String excludespace = exclude_state.replace(" ", "");
                    String statespace = state.replace(" ", "");
//                if (tag.toLowerCase().contains(tagcheck.toLowerCase())) {
                    if (excludespace.trim().toLowerCase().contains(statespace.trim().toLowerCase())) {
                        commanCartControler.UpdateShipping(cartList.get(i).getProduct_varient_id().trim(), "false");
                        layout_placing.setVisibility(View.VISIBLE);
                        placing.setText("Few of the products in your cart cannot be shipped to your given " + state + ".");
                        placing1.setText(getResources().getText(R.string.link));
                    } else {
                        Log.e("tag", "not there");
                        layout_placing.setVisibility(View.GONE);
                    }
                }

            }
            if (tag.toLowerCase().contains(include.toLowerCase())) {
                a = 1;
//                productlist.add(cartList.get(i).getProduct_varient_id());
                getInclude.clear();
                productlist.add(cartList.get(i).getProduct_varient_id().trim());

                String[] items = tag.split(",");
                for (String item : items) {
                    getInclude.add(item);
                }
                for (int j = 0; j < getInclude.size(); j++) {
                    if (getInclude.get(j).toLowerCase().contains(include.toLowerCase())) {
                        String[] item = getInclude.get(j).split(":");
                        include_state = item[1];
                        Log.d("include_state", include_state);
                    }

                }
                String excludespace = include_state.replace(" ", "");
                String statespace = state.replace(" ", "");

//                    if (tag.toLowerCase().contains(includecheck.toLowerCase())) {
                if (excludespace.trim().toLowerCase().contains(statespace.trim().toLowerCase())) {
                    layout_placing.setVisibility(View.GONE);

                } else {
                    commanCartControler.UpdateShipping(cartList.get(i).getProduct_varient_id().trim(), "false");
//                    cartList.get(i).setShip("false");
                    layout_placing.setVisibility(View.VISIBLE);
                    placing.setText("Few of the products in your cart cannot be shipped to your given " + state + "." + " Few Products can be Shipped only in" + " " + include_state + ".");
                    placing1.setText(R.string.link);
                }

            }


        }

    }


}
