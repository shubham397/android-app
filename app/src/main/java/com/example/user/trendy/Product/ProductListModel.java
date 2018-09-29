package com.example.user.trendy.Product;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.trendy.R;
import com.shopify.buy3.Storefront;
import com.squareup.picasso.Picasso;

public class ProductListModel {

    String productid, image,cost,title;

    public ProductListModel(String productid, String image, String cost, String title) {
        this.productid = productid;
        this.image = image;
        this.cost = cost;
        this.title=title;
    }

    public String getProductid() {
        return productid;
    }

    public String getImage() {
        return image;
    }

    public String getCost() {
        return cost;
    }

    public String getTitle() {
        return title;
    }

    public static void price(TextView textView, String cost) {
        textView.setText("$" +cost);
    }

    @BindingAdapter("imageUrl1")
    public static void loadImage(ImageView view, String image) {

//        if(product.getImages().getEdges()!=null) {
            if (image != null) {
                Picasso.with(view.getContext())
                        .load(image)
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
}
