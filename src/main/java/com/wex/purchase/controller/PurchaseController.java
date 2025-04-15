package com.wex.purchase.controller;

import com.wex.purchase.entity.Purchase;
import com.wex.purchase.exception.PurchaseNotFoundException;
import com.wex.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {


    @Autowired
    private PurchaseService purchaseService;
    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/save")
    public ResponseEntity<Purchase> save(@RequestBody @Valid Purchase purchase
                                             ) {
        Purchase savedPurchase=purchaseService.save(purchase);
        return ResponseEntity.ok(savedPurchase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchase(@PathVariable("id") long id) {
        Locale loc = LocaleContextHolder.getLocale();
        String countryCode = loc.getCountry();
        if (countryCode.isEmpty()) {
            return ResponseEntity.badRequest().body(new Purchase());
        }
        try {
            return new ResponseEntity<>(purchaseService.findById(id, countryCode),
                    HttpStatus.OK);
        } catch (PurchaseNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application Not Found");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
