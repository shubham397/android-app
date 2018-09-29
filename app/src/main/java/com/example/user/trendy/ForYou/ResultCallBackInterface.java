package com.example.user.trendy.ForYou;

import com.example.user.trendy.ForYou.NewArrival.NewArrivalModel;
import com.example.user.trendy.ForYou.TopCollection.TopCollectionModel;
import com.example.user.trendy.ForYou.TopSelling.TopSellingModel;
import com.shopify.buy3.Storefront;

import java.util.ArrayList;

public interface ResultCallBackInterface {
//    public void bestCollection(String collectionid,String id, String title, String price, String image, String collectionname);
//    public void topSelling(String collectionid,String id, String title, String price, String image, String collectionname);
//    public void newArrivals(String collectionid,String id, String title, String price, String image, String collectionname);

    public void bestCollection(ArrayList<TopCollectionModel> arrayList);
    public void topSelling(ArrayList<TopSellingModel> arrayList);
    public void newArrivals(ArrayList<NewArrivalModel> arrayList);
}
