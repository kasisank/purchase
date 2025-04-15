package com.wex.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.purchase.entity.Purchase;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PurchaseControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    Purchase savedPurchase;

    @Test
    @Order(1)
    public void savePurchase() throws Exception {
        Purchase purchase = new Purchase();
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(currentDate);

        purchase.setAmount(120.0);
        purchase.setDescription("Testing Purchase");
        purchase.setDate(dateFormat.parse(formattedDate));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(purchase);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<String>(jsonString, headers);
        savedPurchase = restTemplate.postForObject("http://localhost:8080/purchase/save", request, Purchase.class);
        assert(savedPurchase != null);
        assert(savedPurchase.getId() != null);
        assert(savedPurchase.getAmount() != null && savedPurchase.getAmount().equals(120.0));
        assert(savedPurchase.getDescription() != null && savedPurchase.getDescription().equals("Testing Purchase"));
    }

    @Test
    @Order(2)
    public void findPurchaseById() throws Exception {

        //The order is not being maintained
        // call save if savedPurchase is null

        if (savedPurchase == null) {
            savePurchase();
        }
        ResponseEntity<Purchase> response =
                this.restTemplate.getForEntity("http://localhost:8080/purchase/"+savedPurchase.getId(), Purchase.class);
        Purchase purchase = response.getBody();
        assert(purchase != null);
        assert(purchase.getId().equals(savedPurchase.getId()));
        assert(purchase.getDescription().equals(savedPurchase.getDescription()));
        assert (purchase.getAmount() != null && savedPurchase.getAmount().equals(120.0));
        assert (purchase.getDate() != null && savedPurchase.getDate().equals(purchase.getDate()));
        assert (purchase.getConvertedAmount() > 0.0);

    }
}
