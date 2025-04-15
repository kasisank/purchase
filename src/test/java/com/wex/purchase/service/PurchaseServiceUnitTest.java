package com.wex.purchase.service;

import com.wex.purchase.entity.Purchase;
import com.wex.purchase.repository.PurchaseRepository;
import com.wex.purchase.utils.PurchaseBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PurchaseServiceUnitTest {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseService purchaseService;
    Purchase purchase;


    @BeforeEach
    public void setup(){

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(currentDate);
        try {
            purchase = PurchaseBuilder.build(120.0,
                    "Test Purchase",  dateFormat.parse(formattedDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    public void savePurchaseTest() {
        //BeforeEach not working fine, so have to initialize purchase
        setup();


        //action
        Purchase savedPurchase = purchaseService.save(purchase);
        assertThat(savedPurchase).isNotNull();
        Purchase retrievedPurchase = purchaseService
                .findById(savedPurchase.getId(),"IN");

        Assertions.assertThat(retrievedPurchase).isNotNull();
        Assertions.assertThat(retrievedPurchase.getId()).isGreaterThan(0);
        Assertions.assertThat(retrievedPurchase.getAmount()).isEqualTo(120.0);
        Assertions.assertThat(retrievedPurchase.getDescription())
                .isEqualTo("Test Purchase");
        Assertions.assertThat(retrievedPurchase
                .getConvertedAmount()).isGreaterThan(120.0);

    }

}
