package com.wex.purchase.service;

import com.wex.purchase.entity.Purchase;
import org.springframework.stereotype.Service;

@Service
public interface PurchaseService {

    public Purchase save(Purchase purchase);
    public Purchase findById(Long id, String countryCode);
}
