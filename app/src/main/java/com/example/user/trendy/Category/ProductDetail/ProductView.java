package com.example.user.trendy.Category.ProductDetail;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trendy.BuildConfig;
import com.example.user.trendy.Category.ProductModel;
import com.example.user.trendy.ForYou.NewArrival.NewArrivalModel;
import com.example.user.trendy.ForYou.TopCollection.TopCollectionModel;
import com.example.user.trendy.ForYou.TopSelling.TopSellingModel;
import com.example.user.trendy.Interface.ProductClickInterface;
import com.example.user.trendy.R;
import com.example.user.trendy.databinding.ProductViewBinding;
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
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class ProductView extends Fragment implements ProductClickInterface {
    SelectItemModel itemModel = new SelectItemModel();
    RecyclerView recyclerView;
    ArrayList<Storefront.Image> itemsList = new ArrayList<>();
    ProductViewBinding productViewBinding;
    TextView product_price, desc;
    RadioButton rbn;
    LinearLayout veg, eggless, fatfree;
    EditText count;
    String mHtmlString;
    private String id;
    private GraphClient graphClient;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        productViewBinding = DataBindingUtil.inflate(inflater, R.layout.product_view, container, false);
        view = productViewBinding.getRoot();
        //  final View view = inflater.inflate(R.layout.product_view, container, false);

        veg = view.findViewById(R.id.veg);
        eggless = view.findViewById(R.id.eggless);
        fatfree = view.findViewById(R.id.fatfree);
        count = view.findViewById(R.id.count);
        graphClient = GraphClient.builder(getActivity())
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpCache(new File(getActivity().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
                .build();

        // desc=view.findViewById(R.id.desc);
//        count.addTextChangedListener(this);
//        productViewBinding.setCount(new CountModel());

        String product = getArguments().getString("category");
        Log.e("topsellingcheck", "" + product);
        if (product.trim().equals("topselling")) {
            TopSellingModel detail = (TopSellingModel) getArguments().getSerializable("category_id");
            Log.e("title", "" + detail.getCollectionTitle());
            id = detail.getProduct_ID();

//            Log.e("itemModel", "" + itemModel.getProduct().getTitle());
        } else if (product.trim().equals("topcollection")) {
            TopCollectionModel detail = (TopCollectionModel) getArguments().getSerializable("category_id");
//            itemModel.setProduct(detail.getProduct());
            id = detail.getProduct_ID().trim();
        } else if (product.trim().equals("newarrival")) {
            NewArrivalModel detail = (NewArrivalModel) getArguments().getSerializable("category_id");
//            itemModel.setProduct(detail.getProduct());
            id = detail.getProduct_ID().trim();
            Log.e("idd",id);
        } else if (product.trim().equals("ca_adapter")) {
            id = getArguments().getString("product_id");
//            String text = "gid://shopify/Product/" + id.trim();
//
//            String converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
//            Log.e("coverted", converted.trim());
//            Log.e("id", id);
//            getProductVariantID(converted.trim());
        } else {
            ProductModel detail = (ProductModel) getArguments().getSerializable("category_id");
//            itemModel.setProduct(detail.getProduct());
            id = detail.getProduct_ID();
//            itemModel = new SelectItemModel(detail);
//            productViewBinding.setProductview(itemModel);
//            Log.e("title", detail.getProduct().getTitle());
//            Log.e("description", detail.getProduct().getDescription());
//            Log.e("descriptionhtml", "" + detail.getProduct().getDescriptionHtml().toString());
//            mHtmlString = detail.getProduct().getDescriptionHtml().toString();
        }

        String text = "gid://shopify/Product/" + id.trim();

        String converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
        Log.e("coverted", converted.trim());
        Log.e("id", id);
        getProductVariantID(converted.trim());
        return view;
    }

    public void getProductVariantID(String productID) {
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
                    itemModel.setProduct(product);

                    productViewBinding.setProductview(itemModel);

                    Log.e("title", itemModel.getProduct().getTitle());
//                    getData();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                });
                return;

            }

            @Override
            public void onFailure(@NonNull GraphError error) {

//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }


        });


    }

    public void getData() {
        if (itemModel != null) {

            Log.e("title", itemModel.getProduct().getTitle());
            Log.e("description", itemModel.getProduct().getDescription());
            Log.e("descriptionhtml", "" + itemModel.getProduct().getDescriptionHtml().toString());
            mHtmlString = itemModel.getProduct().getDescriptionHtml().toString();


//        Log.e("desxc",mHtmlString);
//        Spanned htmlAsSpanned = Html.fromHtml(detail.getProduct().getDescriptionHtml().toString());
//        //desc.setText(  Html.fromHtml(Html.fromHtml(mHtmlString).toString()));
//desc.setText(htmlAsSpanned);

            WebView webView = (WebView) view.findViewById(R.id.webView);
            webView.loadDataWithBaseURL(null, mHtmlString, "text/html", "utf-8", null);
            final WebSettings webSettings = webView.getSettings();
            webSettings.setDefaultFontSize(14);
            webView.setBackgroundColor(Color.TRANSPARENT);


            product_price = view.findViewById(R.id.product_price);
            product_price.setText("$ " + itemModel.getProduct().getVariants().getEdges().get(0).getNode().getPrice().toString());

            recyclerView = view.findViewById(R.id.product_view_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setFocusable(true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);


            RecyclerView.Adapter adapter = new ImageAdapter(getActivity(), itemsList);

            recyclerView.setAdapter(adapter);


            Log.e("product sku", "" + itemModel.getProduct().getVariants().getEdges().get(0).getNode().getWeightUnit());

            for (int i = 0; i < itemModel.getProduct().getImages().getEdges().size(); i++) {
                Log.e("siii", String.valueOf(itemModel.getProduct().getImages().getEdges().get(i).getNode().getSrc()));
                Log.e("siii1", String.valueOf(itemModel.getProduct().getImages().getEdges().size()));
                itemsList.add(itemModel.getProduct().getImages().getEdges().get(i).getNode());
//            adapter.notifyDataSetChanged();
            }

            adapter.notifyDataSetChanged();
            if (itemModel.getProduct().getTags() != null) {
                String product_tag = itemModel.getProduct().getTags().get(0);
                StringTokenizer st = new StringTokenizer(product_tag, ","); //pass comma as delimeter

                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (token.trim().toLowerCase().equals("veg")) {
                        veg.setVisibility(View.VISIBLE);
                    } else if (token.trim().toLowerCase().equals("eggless") || token.trim().toLowerCase().equals("egg less")) {
                        eggless.setVisibility(View.VISIBLE);

                    } else if (token.trim().toLowerCase().equals("fatfree") || token.trim().toLowerCase().equals("fat free")) {
                        fatfree.setVisibility(View.VISIBLE);
                    } else {
                        veg.setVisibility(View.VISIBLE);
                    }
                }

                Log.e("product_tag", "" + product_tag);
            }


            for (int i = 0; i < itemModel.getProduct().getVariants().getEdges().size(); i++) {
                rbn = new RadioButton(getActivity());

                rbn.setId(i);
                String weightunit = itemModel.getProduct().getVariants().getEdges().get(0).getNode().getWeightUnit().toString();
                if (weightunit.trim().equals("GRAMS")) {
                    weightunit = "g";
                }
                rbn.setText(itemModel.getProduct().getVariants().getEdges().get(i).getNode().getWeight().toString() + " " + weightunit);
                rbn.setTag(itemModel.getProduct().getVariants().getEdges().get(i));
                rbn.setTextColor(Color.BLACK);
                rbn.setBackgroundResource(R.drawable.radio_button_bg);
                rbn.setPadding(20, 5, 20, 5);
                rbn.setGravity(Gravity.CENTER_VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 5, 10, 5);
                rbn.setLayoutParams(params);
                productViewBinding.radiogroup.addView(rbn);

                productViewBinding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        int selectedID = productViewBinding.radiogroup.getCheckedRadioButtonId();
                        rbn.setTextColor(Color.BLACK);
                        rbn = (RadioButton) view.findViewById(selectedID);
                        Log.e("selected id", String.valueOf(selectedID));
                        Log.e("selected rdn id", String.valueOf(rbn.getId()));
                        Log.e("child count", String.valueOf(productViewBinding.radiogroup.getChildCount()));

//                    Toast.makeText(getActivity(), rbn.getText(), Toast.LENGTH_SHORT).show();

                        for (int j = 0; j < productViewBinding.radiogroup.getChildCount(); j++) {
                            Log.e("id check", j + String.valueOf(selectedID));
                            if (j == selectedID) {
                                Log.e("check", "white");
                                rbn.setTextColor(Color.WHITE);
                            }
                        }


                    }
                });

            }
        }

    }

    @Override
    public void clickProduct(String productid) {
        Log.e("position1", productid);
        id = productid;
        String text = "gid://shopify/Product/" + id.trim();

        String converted = Base64.encodeToString(text.toString().getBytes(), Base64.DEFAULT);
        Log.e("coverted", converted.trim());
        Log.e("id", id);
        getProductVariantID(converted.trim());
    }

    @Override
    public void OnclickPlus(String productid) {

    }

    @Override
    public void OnclickWhislilst(String productid) {

    }
}
