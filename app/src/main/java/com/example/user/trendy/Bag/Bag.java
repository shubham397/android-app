package com.example.user.trendy.Bag;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
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

import com.example.user.trendy.Bag.Db.AddToCart_Adapter;
import com.example.user.trendy.Bag.Db.AddToCart_Model;
import com.example.user.trendy.Bag.Db.DBHelper;
import com.example.user.trendy.Interface.CartController;
import com.example.user.trendy.Interface.CommanCartControler;
import com.example.user.trendy.R;
import com.example.user.trendy.Util.SharedPreference;
import com.example.user.trendy.databinding.BagBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag extends Fragment implements AddToCart_Adapter.GetTotalCost {
    RecyclerView bag_recyclerview;
    List<AddToCart_Model> cartList;
    DBHelper db;
    AddToCart_Adapter adapter;
    TextView items, totalcost;
    int totalcost1 = 0;
    BagBinding binding;
    LinearLayout checkoutbtn, check;
    AddToCart_Model addToCart_model = new AddToCart_Model();
    View view;
    TextView nobag;
    ArrayList<String> productlist = new ArrayList<>();
    String state = "";
    CartController cartController;
    CommanCartControler commanCartControler;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bag, container, false);
        view = binding.getRoot();
        cartController = new CartController(getActivity());
        commanCartControler = (CommanCartControler) cartController;

        db = new DBHelper(getActivity());
        cartList = db.getCartList();
        Collections.reverse(cartList); // ADD THIS LINE TO REVERSE ORDER!

        if (getArguments() != null) {
            productlist = getArguments().getStringArrayList("nonshipping");
            state = getArguments().getString("state");
            SharedPreference.saveData("state", state, getActivity());
        }

        Log.d("statev", " " + state);
        if (state.trim().length() > 0) {
            cartList.clear();
            cartList = db.getCartList();
            Collections.reverse(cartList);
        } else {
            for (int i = 0; i < cartList.size(); i++) {
                commanCartControler.UpdateShipping(cartList.get(i).getProduct_varient_id().trim(), "true");
//                cartList.clear();
//                cartList = db.getCartList();
//                Log.d("cccc2", cartList.get(i).getShip());
            }
            cartList.clear();
            cartList = db.getCartList();
            Collections.reverse(cartList);
        }

        bag_recyclerview = view.findViewById(R.id.bag_recyclerview);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        bag_recyclerview.setLayoutManager(layoutManager1);
        bag_recyclerview.setItemAnimator(new DefaultItemAnimator());


        adapter = new AddToCart_Adapter(cartList, getActivity(), this, binding.total);
        bag_recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


//adapter.notifyDataSetChanged();


        checkoutbtn = view.findViewById(R.id.checkoutbtn);
        check = view.findViewById(R.id.check);
        items = view.findViewById(R.id.items);
        totalcost = view.findViewById(R.id.total);
        nobag = view.findViewById(R.id.nobag);
        if (cartList.size() != 0) {
            items.setText(cartList.size() + " " + "Items");

        }


//            Toast.makeText(getApplicationContext(), cartList.get(0).getProduct_name(), Toast.LENGTH_SHORT).show();
        visibilityCheck();
        total();

        checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("collection", "allcollection");
                Fragment fragment = new ShippingAddress();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.home_container, fragment, "fragment");
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                ft.commit();
            }
        });

        return view;


    }

    @SuppressLint("SetTextI18n")
    public void total() {
//        for (int i = 0; i < cartList.size(); i++) {
//            int qty = cartList.get(i).getQty();
//            Double cost = cartList.get(i).getProduct_price();
//            Log.e("qty", "" + String.valueOf(qty));
//            Log.e("cost", "" + String.valueOf(cost));
//            Log.e("icost", "" + String.valueOf(totalcost1));
//            totalcost1 = totalcost1 + (qty * (cost.intValue()));
//            Log.e("cost", "" + String.valueOf(totalcost1));
//
//        }
//        if(totalcost1!=0)

//        totalcost.setText(String.valueOf(getResources().getString(R.string.Rs)+" "+totalcost1));
        CartController cartController;
        CommanCartControler commanCartControler;
        cartController = new CartController(getActivity());
        commanCartControler = (CommanCartControler) cartController;
        SharedPreference.saveData("total", String.valueOf(commanCartControler.getTotalPrice()), getActivity());
        totalcost.setText(Integer.toString(commanCartControler.getTotalPrice()));


    }


    @Override
    public void totalcostinjterface() {
        visibilityCheck();
    }

    public void visibilityCheck() {
//        cartList.clear();
//        cartList = db.getCartList();
        if (cartList.size() == 0) {
            nobag.setVisibility(View.VISIBLE);
            check.setVisibility(View.GONE);
        } else {
            nobag.setVisibility(View.GONE);
            check.setVisibility(View.VISIBLE);
        }
    }

    public interface shipping1 {
        public void shippingvisibility(String state, ArrayList<String> productlist);
    }
}
