package com.CICD.CICD.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(checkOutController.class)
@DisplayName("CheckOut Controller Tests")
public class checkOutControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    private static final String BASE_URL = "/api/checkout";
    
    @BeforeEach
    void setUp() {
        // Setup method for test initialization if needed
    }
    
    // ==================== GET Endpoint Tests ====================
    
    @Test
    @DisplayName("GET /api/checkout/{id} - Should return checkout details successfully")
    void testGetCheckoutDetails_Success() throws Exception {
        String checkoutId = "123";
        
        mockMvc.perform(get(BASE_URL + "/{id}", checkoutId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString("Checkout Details")))
                .andExpect(jsonPath("$.message").value(containsString(checkoutId)));
    }
    
    @Test
    @DisplayName("GET /api/checkout/{id} - Should handle different checkout IDs")
    void testGetCheckoutDetails_DifferentIds() throws Exception {
        String[] checkoutIds = {"ABC001", "XYZ789", "ORDER-2024"};
        
        for (String id : checkoutIds) {
            mockMvc.perform(get(BASE_URL + "/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString(id)));
        }
    }
    
    // ==================== POST Endpoint Tests ====================
    
    @Test
    @DisplayName("POST /api/checkout/order - Should create order successfully")
    void testCreateOrder_Success() throws Exception {
        String orderId = "ORDER123";
        Double amount = 99.99;
        
        mockMvc.perform(post(BASE_URL + "/order")
                .param("orderId", orderId)
                .param("amount", amount.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString("Order created successfully")))
                .andExpect(jsonPath("$.message").value(containsString(orderId)))
                .andExpect(jsonPath("$.message").value(containsString(amount.toString())));
    }
    
    @Test
    @DisplayName("POST /api/checkout/order - Should reject negative amount")
    void testCreateOrder_NegativeAmount() throws Exception {
        String orderId = "ORDER456";
        Double amount = -50.0;
        
        mockMvc.perform(post(BASE_URL + "/order")
                .param("orderId", orderId)
                .param("amount", amount.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Invalid amount")));
    }
    
    @Test
    @DisplayName("POST /api/checkout/order - Should reject zero amount")
    void testCreateOrder_ZeroAmount() throws Exception {
        String orderId = "ORDER789";
        Double amount = 0.0;
        
        mockMvc.perform(post(BASE_URL + "/order")
                .param("orderId", orderId)
                .param("amount", amount.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.success").value(false));
    }
    
    @Test
    @DisplayName("POST /api/checkout/order - Should handle large amounts")
    void testCreateOrder_LargeAmount() throws Exception {
        String orderId = "BULK_ORDER";
        Double amount = 99999.99;
        
        mockMvc.perform(post(BASE_URL + "/order")
                .param("orderId", orderId)
                .param("amount", amount.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.success").value(true));
    }
    
    // ==================== PUT Endpoint Tests ====================
    
    @Test
    @DisplayName("PUT /api/checkout/payment/{orderId} - Should update to COMPLETED status")
    void testUpdatePaymentStatus_Completed() throws Exception {
        String orderId = "ORDER001";
        String status = "COMPLETED";
        
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString("Payment status updated successfully")))
                .andExpect(jsonPath("$.message").value(containsString(orderId)))
                .andExpect(jsonPath("$.message").value(containsString(status)));
    }
    
    @Test
    @DisplayName("PUT /api/checkout/payment/{orderId} - Should update to PENDING status")
    void testUpdatePaymentStatus_Pending() throws Exception {
        String orderId = "ORDER002";
        String status = "PENDING";
        
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString(status)));
    }
    
    @Test
    @DisplayName("PUT /api/checkout/payment/{orderId} - Should update to FAILED status")
    void testUpdatePaymentStatus_Failed() throws Exception {
        String orderId = "ORDER003";
        String status = "FAILED";
        
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString(status)));
    }
    
    @Test
    @DisplayName("PUT /api/checkout/payment/{orderId} - Should reject invalid status")
    void testUpdatePaymentStatus_InvalidStatus() throws Exception {
        String orderId = "ORDER004";
        String invalidStatus = "INVALID_STATUS";
        
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", invalidStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(containsString("Invalid payment status")));
    }
    
    @Test
    @DisplayName("PUT /api/checkout/payment/{orderId} - Should be case-sensitive for status")
    void testUpdatePaymentStatus_CaseSensitive() throws Exception {
        String orderId = "ORDER005";
        String lowerCaseStatus = "completed";
        
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", lowerCaseStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.success").value(false));
    }
    
    @Test
    @DisplayName("PUT /api/checkout/payment/{orderId} - Should handle multiple status updates")
    void testUpdatePaymentStatus_MultipleUpdates() throws Exception {
        String orderId = "ORDER006";
        String[] statuses = {"PENDING", "COMPLETED"};
        
        // First update to PENDING
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", statuses[0])
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString(statuses[0])));
        
        // Then update to COMPLETED
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", statuses[1])
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString(statuses[1])));
    }
    
    // ==================== Integration Tests ====================
    
    @Test
    @DisplayName("Complete checkout flow - Create order and update payment status")
    void testCompleteCheckoutFlow() throws Exception {
        String orderId = "COMPLETE_ORDER";
        Double amount = 150.50;
        
        // Step 1: Create order
        mockMvc.perform(post(BASE_URL + "/order")
                .param("orderId", orderId)
                .param("amount", amount.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
        
        // Step 2: Update payment status to PENDING
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", "PENDING")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString("PENDING")));
        
        // Step 3: Update payment status to COMPLETED
        mockMvc.perform(put(BASE_URL + "/payment/{orderId}", orderId)
                .param("status", "COMPLETED")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString("COMPLETED")));
        
        // Step 4: Retrieve checkout details
        mockMvc.perform(get(BASE_URL + "/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString("Checkout Details")));
    }
    
    // ==================== Edge Case Tests ====================
    
    @Test
    @DisplayName("GET /api/checkout/{id} - Should handle special characters in ID")
    void testGetCheckoutDetails_SpecialCharacters() throws Exception {
        String checkoutId = "ORDER-2024_TEST";
        
        mockMvc.perform(get(BASE_URL + "/{id}", checkoutId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("POST /api/checkout/order - Should handle decimal amounts")
    void testCreateOrder_DecimalAmount() throws Exception {
        String orderId = "DECIMAL_ORDER";
        Double amount = 10.01;
        
        mockMvc.perform(post(BASE_URL + "/order")
                .param("orderId", orderId)
                .param("amount", amount.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}

