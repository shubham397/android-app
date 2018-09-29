package com.example.user.trendy.Interface;

import com.shopify.buy3.Storefront;

public interface CommanCartControler {

    public void AddToCart(String id);

    public void AddQuantity(String id);

    public void RemoveQuantity(String id);
    public int getTotalPrice();
public void  UpdateShipping(String id, String value);

    public void AddToWhislist(String id);

}
