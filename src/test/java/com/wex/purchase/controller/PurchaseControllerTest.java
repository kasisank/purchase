package com.wex.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.purchase.entity.Purchase;
import com.wex.purchase.service.PurchaseService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.wex.purchase.utils.PurchaseBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PurchaseService purchaseService;

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
    public void savePurchase() throws Exception {
        setup();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/purchase/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchase));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        //Assert that the return status is OK
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }
}
