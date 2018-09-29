package com.example.user.trendy.ForYou.AllCollection;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.user.trendy.R;
import com.shopify.buy3.Storefront;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class AllCollectionModel implements Serializable {
    String id, image, title;

    private Storefront.Collection collection;

    public Storefront.Collection getCollection() {
        return collection;
    }

    public void setCollection(Storefront.Collection collection) {
        this.collection = collection;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public AllCollectionModel(String id, String image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String image) {
        if(image!=null) {
            Picasso.with(view.getContext())
                    .load(image)
                    .into(view);
        } else {
            Picasso.with(view.getContext())
                    .load(R.drawable.ic_dashboard_black_24dp)
                    //.transform(new CircleTransform())
                    .into(view);
        }
    }

}
