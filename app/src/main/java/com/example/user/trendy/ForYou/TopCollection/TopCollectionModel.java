package com.example.user.trendy.ForYou.TopCollection;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.trendy.R;
import com.shopify.buy3.Storefront;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopCollectionModel extends BaseObservable implements Serializable {

//    private String Product_ID;
//    private String Product_Name;
//    private String Product_title;
//    private ArrayList<Storefront.Image> Product_Image;
//    private List<Storefront.ProductVariant> productVariants;
//    String collectiontitle;
//
//    private String imageUrl;
//public Storefront.Collection topcollection;

//    private String Product_description;
//    private String Product_Price;
//    private String Product_varient_Id;
//
//    public List<Storefront.ProductVariant> getProductVariants() {
//        return productVariants;
//    }
//
//    public List<Storefront.Product> productlist;
//
//    public void setProductVariants(List<Storefront.ProductVariant> productVariants) {
//        this.productVariants = productVariants;
//    }
//
//    public List<Storefront.Product> getProductlist() {
//        return productlist;
//    }
//
//    public void setProductlist(List<Storefront.Product> productlist) {
//        this.productlist = productlist;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public ArrayList<Storefront.Image> getProduct_Image() {
//        return Product_Image;
//    }
//
//    public void setProduct_Image(ArrayList<Storefront.Image> product_Image) {
//        Product_Image = product_Image;
//    }
//
//    public String getProduct_Name() {
//        return Product_Name;
//    }
//
//    public void setProduct_Name(String product_Name) {
//        Product_Name = product_Name;
//    }
//
//    public String getProduct_ID() {
//        return Product_ID;
//    }
//
//    public void setProduct_ID(String product_ID) {
//        Product_ID = product_ID;
//    }
//
//    public String getProduct_title() {
//        return Product_title;
//    }
//
//    public void setProduct_title(String product_title) {
//        Product_title = product_title;
//    }
//
//
//    public String getProduct_description() {
//        return Product_description;
//    }
//
//    public void setProduct_description(String product_description) {
//        Product_description = product_description;
//    }
//
//    public String getProduct_Price() {
//        return Product_Price;
//    }
//
//    public void setProduct_Price(String product_Price) {
//        Product_Price = product_Price;
//    }
//
//    public String getProduct_varient_Id() {
//        return Product_varient_Id;
//    }
//
//    public void setProduct_varient_Id(String product_varient_Id) {
//        Product_varient_Id = product_varient_Id;
//    }
//
//    public String getCollectiontitle() {
//        return collectiontitle;
//    }
//
//    public void setCollectiontitle(String collectiontitle) {
//        this.collectiontitle = collectiontitle;
//    }
//
//    public Storefront.Collection getTopcollection() {
//        return topcollection;
//    }
//
//    public void setTopcollection(Storefront.Collection topcollection) {
//        this.topcollection = topcollection;
//    }

    String price,Product_ID,Product_title,imageUrl;
    private Storefront.Product product;
    private String collectionTitle, collectionid;
    private  String id;



    public TopCollectionModel(String product_ID, String product_title, String Price,String imageUrl ,String CollectionTitle ) {
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

    public TopCollectionModel(Storefront.Product product, String collectionTitle, String id) {
        this.product = product;
        this.collectionTitle = collectionTitle;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public Storefront.Product getProduct() {
        return product;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }

    @BindingAdapter("price")
    public static void price(TextView textView,String price)
    {
        textView.setText("$"+ price);
    }
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.trendybanner)
                .into(view);
    }


}