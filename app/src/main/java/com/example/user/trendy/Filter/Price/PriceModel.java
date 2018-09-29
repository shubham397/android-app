package com.example.user.trendy.Filter.Price;

public class PriceModel {
    String title;
    boolean checked=false;

    public PriceModel(String title,boolean checked) {
        this.title = title;
        this.checked=checked;
    }

    public String getTitle() {
        return title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


}
