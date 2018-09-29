package com.example.user.trendy.Category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.R;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.HttpCachePolicy;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SubCategory extends Fragment {
    public String collectionurl = "https://cdn.shopify.com/s/files/1/0012/0658/3356/t/8/assets/tredyfoods.js?86827102922423728";

    private TextView mTextMessage;
    Toolbar toolbar;
    RecyclerView recyclerView;
    GraphClient graphClient;
    ArrayList<CategoryModel> categoryList = new ArrayList<>();
    ArrayList<SubCategoryModel> subCategoryModelArrayList = new ArrayList<>();
    CategoreDetailAdapter categoreDetailAdapter;
    private RequestQueue mRequestQueue;
    String imageurl = "";
    LinearLayout subcategory;
    TextView sublistname,all;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.categories, container, false);

//        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
//        toolbar.setTitle("Categories");
//        toolbar.getMenu().clear();

//        mTextMessage = view.findViewById(R.id.message);
//        mTextMessage.setText(R.string.title_categories);

        subcategory = view.findViewById(R.id.sublayout);
        sublistname = view.findViewById(R.id.subcategorylist);
        all = view.findViewById(R.id.all);
        subcategory.setVisibility(View.VISIBLE);
        all.setText("All");
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.home_container, new Categories(), "Categories");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



//        Log.d("Selected Collection", String.valueOf(detail.getId()));
//        String text="gid://shopify/Collection/"+detail.getId();
//
//        String converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
//        Log.e("coverted",converted.trim());

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

        CategoryModel detail = (CategoryModel) getArguments().getSerializable("category_id");
        String title=detail.getCollectiontitle();
        sublistname.setText(title);
        subCategoryModelArrayList = detail.getSubCategoryModelArrayList();
        Log.e("subarray", "" + String.valueOf(subCategoryModelArrayList.size()));
        Log.e("sizecount", "" + String.valueOf(detail.getSubCategoryModelArrayList().size()));
        for (int i = 0; i < subCategoryModelArrayList.size(); i++) {
            CategoryModel model = new CategoryModel();
            model.setId(subCategoryModelArrayList.get(i).getId());
            model.setCollectiontitle(subCategoryModelArrayList.get(i).getTitle());
            model.setImageurl(subCategoryModelArrayList.get(i).getImage());
            Log.e("sub_id", subCategoryModelArrayList.get(i).getId());
            categoryList.add(model);

        }
        categoreDetailAdapter.notifyDataSetChanged();


        return view;
    }
}
