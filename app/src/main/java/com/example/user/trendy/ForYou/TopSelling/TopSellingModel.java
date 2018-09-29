package com.example.user.trendy.ForYou.TopSelling;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.trendy.R;
import com.shopify.buy3.Storefront;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopSellingModel extends BaseObservable implements Serializable {

        private String Product_ID;
//    private String Product_Name;
    private String Product_title;
//    private ArrayList<Storefront.Image> Product_Image;
//    private List<Storefront.ProductVariant> productVariants;
//    String collectiontitle;
//
    private String imageUrl;
//
//
//    private String Product_description;
    private String price;
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
//
    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public String getProduct_title() {
        return Product_title;
    }

    public void setProduct_title(String product_title) {
        Product_title = product_title;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

//
//    public String getProduct_varient_Id() {
//        return Product_varient_Id;
//    }
//
//    public void setProduct_varient_Id(String product_varient_Id) {
//        Product_varient_Id = product_varient_Id;
//    }
//


    public void setCollectionTitle(String collectionTitle) {
        this.collectionTitle = collectionTitle;
    }

    public String getCollectionid() {
        return collectionid;
    }

    public void setCollectionid(String collectionid) {
        this.collectionid = collectionid;
    }

    Storefront.Collection collection;
    private Storefront.Product product;
    private String collectionTitle,id, collectionid;

    public TopSellingModel(String product_ID, String product_title, String Price,String imageUrl ,String CollectionTitle ) {
        Product_ID = product_ID;
        Product_title = product_title;
        price = Price;
        this.imageUrl = imageUrl;
        collectionTitle=CollectionTitle;
    }

    public TopSellingModel() {
    }

    public TopSellingModel(Storefront.Product product, String collectionTitle, String id) {
        this.product = product;
        this.collectionTitle = collectionTitle;
        this.id=id;
    }

    public Storefront.Product getProduct() {
        return product;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }

    public String getId() {
        return id;
    }

    public Storefront.Collection getCollection() {
        return collection;
    }

    public void setCollection(Storefront.Collection collection) {
        this.collection = collection;
    }

    @BindingAdapter("price1")
    public static void price(TextView textView, String price) {
        textView.setText("$" + price);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        if(imageUrl.trim().length()>0) {
//            if(product.getVariants().getEdges().get(0).getNode().getImage()!=null) {
                Picasso.with(view.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.trendybanner)
                        .into(view);
//            }else{
//                Picasso.with(view.getContext())
//                        .load(R.drawable.trendybanner)
//                        .into(view);
//            }
        }else{
            Picasso.with(view.getContext())
                    .load(R.drawable.trendybanner)
                    .into(view);
        }
    }


}