package com.wex.purchase.repository;

import com.wex.purchase.entity.Purchase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.Rollback;
import com.wex.purchase.utils.PurchaseBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PurchaseRepositoryUnitTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    Purchase purchase;

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
    @DisplayName("Test 1:Save Purchase Test")
    @Order(1)
    @Rollback(value = false)
    public void savePurchaseTest(){

        setup();

        purchaseRepository.save(purchase);

        Assertions.assertThat(purchase.getId()).isGreaterThan(0);

        Purchase savedPurchased = purchaseRepository.findById(purchase.getId())
                .orElse(null);

        Assertions.assertThat(savedPurchased).isNotNull();
        Assertions.assertThat(savedPurchased.getId()).isGreaterThan(0);
        Assertions.assertThat(savedPurchased.getAmount()).isEqualTo(120.0);
        Assertions.assertThat(savedPurchased.getDescription())
                .isEqualTo("Test Purchase");

    }
}
