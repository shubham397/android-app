package com.example.user.trendy.Whislist;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AddWhislistModel {

    private String product_name;
    private String product_varient_id;
    private Double product_price;
    private String product_varient_title;
    private String imageUrl;
    String col_id;

    public String getCol_id() {
        return col_id;
    }

    public void setCol_id(String col_id) {
        this.col_id = col_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_varient_id() {
        return product_varient_id;
    }

    public void setProduct_varient_id(String product_varient_id) {
        this.product_varient_id = product_varient_id;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_varient_title() {
        return product_varient_title;
    }

    public void setProduct_varient_title(String product_varient_title) {
        this.product_varient_title = product_varient_title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @BindingAdapter("imageplace")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

}
