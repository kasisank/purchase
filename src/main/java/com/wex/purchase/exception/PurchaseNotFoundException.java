package com.wex.purchase.exception;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(String exception) {
        super(exception);
    }
}
