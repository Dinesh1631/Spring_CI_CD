package com.CICD.CICD.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/checkout")
public class checkOutController {
    
    private static final Logger logger = LoggerFactory.getLogger(checkOutController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * GET endpoint to retrieve checkout details
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCheckoutDetails(@PathVariable String id) {
        logger.info("Fetching checkout details for ID: {}", id);
        try {
            ApiResponse<String> response = new ApiResponse<>("success", "Checkout Details for ID: " + id, true);
            logger.debug("Successfully retrieved checkout data");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving checkout details for ID: {}", id, e);
            ApiResponse<String> errorResponse = new ApiResponse<>("error", "Error retrieving checkout details", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * POST endpoint to create a new checkout order
     */
    @PostMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestParam String orderId, @RequestParam Double amount) {
        logger.info("Creating new order with ID: {} and amount: {}", orderId, amount);
        try {
            if (amount <= 0) {
                logger.warn("Invalid amount provided: {}", amount);
                ApiResponse<String> errorResponse = new ApiResponse<>("error", "Invalid amount", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            String message = "Order created successfully. Order ID: " + orderId + ", Amount: $" + amount;
            ApiResponse<String> response = new ApiResponse<>("success", message, true);
            logger.info("Order created successfully with ID: {}", orderId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating order with ID: {}", orderId, e);
            ApiResponse<String> errorResponse = new ApiResponse<>("error", "Error creating order", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * PUT endpoint to update checkout payment status
     */
    @PutMapping(value = "/payment/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePaymentStatus(@PathVariable String orderId, @RequestParam String status) {
        logger.info("Updating payment status for Order ID: {} to status: {}", orderId, status);
        try {
            if (!status.matches("PENDING|COMPLETED|FAILED")) {
                logger.warn("Invalid payment status provided: {}", status);
                ApiResponse<String> errorResponse = new ApiResponse<>("error", "Invalid payment status. Use: PENDING, COMPLETED, or FAILED", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            String message = "Payment status updated successfully. Order ID: " + orderId + ", Status: " + status;
            ApiResponse<String> response = new ApiResponse<>("success", message, true);
            logger.info("Payment status updated for Order ID: {} with status: {}", orderId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating payment status for Order ID: {}", orderId, e);
            ApiResponse<String> errorResponse = new ApiResponse<>("error", "Error updating payment status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Generic API Response wrapper class
     */
    public static class ApiResponse<T> {
        private String status;
        private T message;
        private boolean success;
        
        public ApiResponse(String status, T message, boolean success) {
            this.status = status;
            this.message = message;
            this.success = success;
        }
        
        public String getStatus() {
            return status;
        }
        
        public T getMessage() {
            return message;
        }
        
        public boolean isSuccess() {
            return success;
        }
    }
}
