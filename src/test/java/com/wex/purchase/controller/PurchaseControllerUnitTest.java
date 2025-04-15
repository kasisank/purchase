package com.wex.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.purchase.entity.Purchase;
import com.wex.purchase.repository.PurchaseRepository;
import com.wex.purchase.service.PurchaseService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.wex.purchase.utils.PurchaseBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@WebMvcTest(PurchaseController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PurchaseControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @MockBean
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void savePurchaseTest() throws Exception{
        setup();
        given(purchaseService.save(any(Purchase.class))).willReturn(purchase);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/purchase/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchase));
        // action
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Purchase purchase = objectMapper
                .readValue(response.getContentAsString(), Purchase.class);
        assertTrue(purchase.getAmount() != null && purchase.getAmount().doubleValue() > 0);
        assertNotNull(purchase.getDescription());
        assertTrue(!purchase.getDescription().isEmpty());
        assertNotNull(purchase.getDate());
    }

    @Test
    public void validPurchaseSave()  throws Exception {
        setup();
        mockMvc.perform(MockMvcRequestBuilders.post("/purchase/save")
                        .content(objectMapper.writeValueAsString(purchase))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void inValidPurchaseSave()  throws Exception {
        setup();
        purchase.setDescription("");
        mockMvc.perform(MockMvcRequestBuilders.post("/purchase/save")
                        .content(objectMapper.writeValueAsString(purchase))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
