package com.example.user.trendy.Account.Orders;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.trendy.R;
import com.shopify.buy3.Storefront;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderModel implements Serializable {

    private Storefront.Order orderd;
    private int lineitemsize;

    public int getLineitemsize() {
        return lineitemsize;
    }

    public void setLineitemsize(int lineitemsize) {
        this.lineitemsize = lineitemsize;
    }

    public Storefront.Order getOrderd() {
        return orderd;
    }

    public void setOrderd(Storefront.Order orderd) {
        this.orderd = orderd;
    }

    @BindingAdapter("ordernumber")
    public static void price(TextView textView, Storefront.Order orderd) {
        String number = String.valueOf(orderd.getOrderNumber());
        textView.setText("#" + number);
    }

    @BindingAdapter("totalcost")
    public static void cost(TextView textView, Storefront.Order orderd) {
        String cost = String.valueOf(orderd.getTotalPrice());
        textView.setText("Rs. " + cost);
    }


    @BindingAdapter("date")
    public static void date(TextView textView, Storefront.Order orderd) {
        String date = String.valueOf(orderd.getProcessedAt());
        String[] separated = date.split("T");
        String date1 = separated[0];
        String Time = separated[1];
        if (date1.length() != 0) {
            Date date2 = null;  // <---  yyyy-mm-dd
            try {
                SimpleDateFormat simpleDateFormatinput = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat simpleDateFormatoutput = new SimpleDateFormat("dd-mm-yyyy");
                date2 = simpleDateFormatinput.parse(date1);
                date1 = simpleDateFormatoutput.format(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        textView.setText(date1);
    }



    @BindingAdapter("productname")
    public static void name(TextView textView, Storefront.Order orderd) {
        String name = orderd.getLineItems().getEdges().get(0).getNode().getVariant().getProduct().getTitle();
        Log.e("imagee", " " + name);
        textView.setText( name);
    }

    @BindingAdapter("productcost")
    public static void pcost(TextView textView, Storefront.Order orderd) {
        String pcost = String.valueOf(orderd.getLineItems().getEdges().get(0).getNode().getVariant().getPrice());
        Log.e("pcost", " " + pcost);
        textView.setText( pcost);
    }

    @BindingAdapter("imageUrl1")
    public static void loadImage(ImageView view,Storefront.Order orderd) {
        String imageUrl = orderd.getLineItems().getEdges().get(0).getNode().getVariant().getImage().getSrc();

        if (imageUrl != null) {
            Picasso.with(view.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.trendybanner)
                    .error(R.drawable.trendybanner)
                    .resize(200, 200)
                    .into(view);
        } else {
            Picasso.with(view.getContext())
                    .load(R.drawable.trendybanner)
                    .into(view);
        }
    }

    @BindingAdapter("productquantity")
    public static void quantity(TextView textView, Storefront.Order orderd) {
        String pquantity = String.valueOf(orderd.getLineItems().getEdges().get(0).getNode().getQuantity());
        Log.e("pquantity", " " + pquantity);
        textView.setText( pquantity);
    }


}

