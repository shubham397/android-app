package com.example.user.trendy.ForYou.NewArrival;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.trendy.R;
import com.shopify.buy3.Storefront;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class NewArrivalModel implements Serializable {

    private Storefront.Product product;
    private Storefront.Collection collection;
    private String collectionTitle;

    public NewArrivalModel() {
    }
    String price,Product_ID,Product_title,imageUrl;
    private String  collectionid;
    private  String id;



    public NewArrivalModel(String product_ID, String product_title, String Price,String imageUrl ,String CollectionTitle ) {
        Product_ID = product_ID;
        Product_title = product_title;
        price = Price;
        this.imageUrl = imageUrl;
        collectionTitle=CollectionTitle;
    }

    public String getPrice() {
        return price;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public String getProduct_title() {
        return Product_title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCollectionid() {
        return collectionid;
    }

    public void setCollectionid(String collectionid) {
        this.collectionid = collectionid;
    }

    public String getId() {
        return id;
    }

    public NewArrivalModel(Storefront.Product product, String collectionTitle) {
        this.product = product;
        this.collectionTitle = collectionTitle;
    }

    public Storefront.Collection getCollection() {
        return collection;
    }

    public void setCollection(Storefront.Collection collection) {
        this.collection = collection;
    }

    public Storefront.Product getProduct() {
        return product;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }

    @BindingAdapter("price")
    public static void price(TextView textView, String price)
    {
        textView.setText("$"+price);
    }
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.trendybanner)
                .into(view);
    }

}
