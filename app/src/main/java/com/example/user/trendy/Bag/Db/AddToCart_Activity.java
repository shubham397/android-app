package com.example.user.trendy.Bag.Db;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.user.trendy.R;
import com.example.user.trendy.databinding.ActivityAddToCartBinding;


import java.util.List;

public class AddToCart_Activity extends AppCompatActivity implements AddToCart_Adapter.GetTotalCost{

    List<AddToCart_Model> cartList;
    DBHelper db;
    ActivityAddToCartBinding binding;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_to_cart_);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_to_cart_);

        db = new DBHelper(this);

        cartList = db.getCartList();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.addtocartRecyclerView.setFocusable(false);
        binding.addtocartRecyclerView.setHasFixedSize(true);
        binding.addtocartRecyclerView.setLayoutManager(layoutManager);

        if(cartList.size()>=0) {
            adapter = new AddToCart_Adapter(cartList, getApplicationContext(),this);
            binding.addtocartRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
//            Toast.makeText(getApplicationContext(), cartList.get(0).getProduct_name(), Toast.LENGTH_SHORT).show();
        }

        binding.checkOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(AddToCart_Activity.this,PayUMoneyActivity.class);
//                startActivity(intent);

            }
        });
    }



    @Override
    public void totalcostinjterface() {

    }
}
