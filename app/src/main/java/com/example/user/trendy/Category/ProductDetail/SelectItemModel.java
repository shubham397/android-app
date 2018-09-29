package com.example.user.trendy.Category.ProductDetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.example.user.trendy.Category.ProductModel;
import com.example.user.trendy.R;
import com.shopify.buy3.Storefront;

import java.math.BigDecimal;

public class SelectItemModel extends BaseObservable {

    private ProductModel selectItem;
    private boolean addedTocart = false;
    private Storefront.Product product;

    private int count = 0;
    private int cost;
    private String ship;

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public SelectItemModel() {
    }

    public Storefront.Product getProduct() {
        return product;
    }

    public void setProduct(Storefront.Product product) {
        this.product = product;
    }

    public boolean isAddedTocart() {
        return addedTocart;
    }

    public void setAddedTocart(boolean addedTocart) {
        this.addedTocart = addedTocart;
    }

    public SelectItemModel(ProductModel selectItem) {
        this.selectItem = selectItem;

    }

    public ProductModel getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(ProductModel selectItem) {
        this.selectItem = selectItem;
    }


    public void increment() {
        setCount(getCount() + 1);
//        setTotal(getCount()* selectItem.getProduct().getVariants().getEdges().get(0).getNode().getPrice().intValue());

    }

    public void decrement() {
        if (getCount() != 0) {
            setCount(getCount() - 1);
//            setTotal(getCount()* selectItem.getProduct().getVariants().getEdges().get(0).getNode().getPrice().intValue());
        }

    }

    @Bindable
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        setTotal(count * getProduct().getVariants().getEdges().get(0).getNode().getPrice().intValue());
        notifyPropertyChanged(BR.count);


    }

    public void setTotal(int cost) {
        this.cost = cost;
//        if (cost != 0) {
            notifyPropertyChanged(BR.cost);
//        }

    }

    @Bindable
    public int getCost() {
        return cost;
    }


    @BindingAdapter("desc")
    public void desc(TextView text, SelectItemModel desc) {
        String mHtmlString = desc.selectItem.getProduct().getDescriptionHtml();
        Log.e("desxc", mHtmlString);
        text.setText(Html.fromHtml(Html.fromHtml(mHtmlString).toString()));

    }

}
