package com.example.user.trendy.Category;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.trendy.R;
import com.shopify.buy3.Storefront;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseObservable implements Serializable {

    private String Product_ID;
    private String price;
    private String Product_title;
//    private ArrayList<Storefront.Image> Product_Image;
//    private List<Storefront.ProductVariant> productVariants;
//
    private String imageUrl;

    public ProductModel(String product_ID, String price, String product_title, String imageUrl) {
        Product_ID = product_ID;
        this.price = price;
        Product_title = product_title;
        this.imageUrl = imageUrl;
    }

    //
//
//    private String Product_description;
//    private String Product_Price;
//    private String Product_varient_Id;
//
//    public List<Storefront.ProductVariant> getProductVariants() {
//        return productVariants;
//    }
//
//    public void setProductVariants(List<Storefront.ProductVariant> productVariants) {
//        this.productVariants = productVariants;
//    }
//
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
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

    public String getProduct_ID() {
        return Product_ID;
    }
//
    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public String getProduct_title() {
        return Product_title;
    }
//
    public void setProduct_title(String product_title) {
        Product_title = product_title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
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

    private Storefront.Product product;

    public Storefront.Product getProduct() {
        return product;
    }

    public void setProduct(Storefront.Product product) {
        this.product = product;
    }

    //    @BindingAdapter("imageUrl")
//    public static void loadImage(ImageView view, String imageUrl) {
//        Picasso.with(view.getContext())
//                .load(imageUrl)
//                .into(view);
//    }
//    @BindingAdapter("price1")
//    public static void price(TextView textView, Storefront.Product product) {
//        textView.setText("$" + String.valueOf(product.getVariants().getEdges().get(0).getNode().getPrice()));
//    }
//
//    @BindingAdapter("imageUrl1")
//    public static void loadImage(ImageView view, Storefront.Product product) {
//
//        if(product.getImages().getEdges()!=null) {
//            if (product.getVariants().getEdges().get(0).getNode().getImage() != null) {
//                Picasso.with(view.getContext())
//                        .load(product.getVariants().getEdges().get(0).getNode().getImage().getSrc())
//                        .placeholder(R.drawable.trendybanner)
//                        .error(R.drawable.trendybanner)
//                        .resize(200,200)
//                        .into(view);
//            } else {
//                Picasso.with(view.getContext())
//                        .load(R.drawable.trendybanner)
//                        .into(view);
//            }
//        }
//    }


    @BindingAdapter("price1")
    public static void price(TextView textView, String price) {
        textView.setText("$" + price);
    }

    @BindingAdapter("imageUrl1")
    public static void loadImage(ImageView view, String imageUrl) {

        if(imageUrl!=null) {
                Picasso.with(view.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.trendybanner)
                        .error(R.drawable.trendybanner)
                        .resize(200,200)
                        .into(view);
            } else {
                Picasso.with(view.getContext())
                        .load(R.drawable.trendybanner)
                        .into(view);
            }
        }

    @BindingAdapter("desc")
    public void desc(TextView text, Storefront.Product product){
        String mHtmlString=product.getDescriptionHtml();
        text.setText(  Html.fromHtml(Html.fromHtml(mHtmlString).toString()));



    }



}