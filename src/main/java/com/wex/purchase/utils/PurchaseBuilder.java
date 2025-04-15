package com.wex.purchase.utils;

import com.wex.purchase.entity.Purchase;

import java.util.Date;

public class PurchaseBuilder {

    public static Purchase build(Double amount, String description, Date date) {
        Purchase purchase = new Purchase();
        purchase.setAmount(amount);
        purchase.setDescription(description);
        purchase.setDate(date);
        return purchase;
    }
}
