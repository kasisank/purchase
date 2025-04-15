package com.wex.purchase.service;

import com.wex.purchase.entity.Purchase;
import com.wex.purchase.exception.ConversionCurrencyNotFound;
import com.wex.purchase.exception.PurchaseNotFoundException;
import com.wex.purchase.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.wex.purchase.utils.Functions;

import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    PurchaseRepository purchaseRepository;

    @Override
    @Validated
    public Purchase save(Purchase purchase) {
        // get the value from purchase and round it to cents
        purchase.setAmount(Functions.roundToCents(purchase.getAmount()));
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase findById(Long id,String countryCode) {
        Optional<Purchase> optionalPurchase= purchaseRepository.findById(id);
        if (optionalPurchase.isPresent()) {
            Purchase purchase= optionalPurchase.get();
            // convert the amount into local currency
            double convAmount = Functions.convertToLocalCurrency(purchase.getAmount(),
                    purchase.getDate(),countryCode);
            if (convAmount == 0.0){
                throw new ConversionCurrencyNotFound("Conversion currency not found for :"+id);
            }
            purchase.setConvertedAmount(convAmount);
            return purchase;
        } else
            throw new PurchaseNotFoundException("Purchase not found for : "+id);
    }
}
