package main.java.com.fawry.ecommerce;

import main.java.com.fawry.ecommerce.model.*;
import main.java.com.fawry.ecommerce.service.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Setup services
        CartService cartService = new CartService();
        InventoryService inventoryService = new InventoryService();
        PaymentService paymentService = new PaymentService();
        ShippingService shippingService = new ShippingService(cartService);
        CheckoutService checkoutService = new CheckoutService(inventoryService, paymentService, shippingService, cartService);

        // Create test products
        Product cheese = new Product("Cheese", 100.0, 5, LocalDate.now().plusDays(3), true, 0.4);  // Expires in 3 days
        Product tv = new Product("TV", 500.0, 2, null, true, 5.0);  // Only 2 in stock
        Product scratchCard = new Product("Scratch Card", 50.0, 100, null, false, 0.05);
        Product milk = new Product("Milk", 80.0, 10, LocalDate.now().plusDays(1), true, 1.0);  // Expires TOMORROW (delivery fails)

        // Create customers
        Customer Seif = new Customer("cust-1", "Seif", 1000.0);
        Customer Tamer = new Customer("cust-2", "Tamer", 200.0);
        Customer Heakal = new Customer("cust-3", "Heakal", 5000.0);

        // ===== TEST 1: Normal checkout =====
        System.out.println("\n===== TEST 1: Normal Checkout =====");
        Cart cart1 = new Cart();
        cart1.addItem(cheese, 2);
        cart1.addItem(scratchCard, 1);
        checkoutService.checkout(Seif, cart1);
        System.out.println("Remaining cheese: " + cheese.getStock());

        // ===== TEST 2: Expired item during delivery =====
        System.out.println("\n===== TEST 2: Expired Item =====");
        Cart cart2 = new Cart();
        cart2.addItem(milk, 2);
        try {
            checkoutService.checkout(Tamer, cart2);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());//delivery time is 2 days asumption
        }

        // ===== TEST 3: Out of stock =====
        System.out.println("\n===== TEST 3: Out of Stock =====");
        Cart cart3 = new Cart();
        cart3.addItem(tv, 3);
        try {
            checkoutService.checkout(Heakal, cart3);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 4: Empty cart =====
        System.out.println("\n===== TEST 4: Empty Cart =====");
        Cart cart4 = new Cart();
        try {
            checkoutService.checkout(Seif, cart4);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 5: Insufficient balance =====
        System.out.println("\n===== TEST 5: Insufficient Balance =====");
        Cart cart5 = new Cart();
        cart5.addItem(tv, 1);
        try {
            checkoutService.checkout(Tamer, cart5);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 6: Cart modifications (add/remove) =====
        System.out.println("\n===== TEST 6: Cart Modifications =====");
        Cart cart6 = new Cart();
        cart6.addItem(cheese, 3);
        cart6.addItem(tv, 1);
        System.out.println("Cart before removal: " + cart6.getItems());
        cart6.removeItem(tv);
        System.out.println("Cart after removal: " + cart6.getItems());
        checkoutService.checkout(Heakal, cart6);

        // ===== TEST 7: Free shipping (order > 1000) =====
        System.out.println("\n===== TEST 7: Free Shipping =====");
        Cart cart7 = new Cart();
        cart7.addItem(tv, 2);
        checkoutService.checkout(Heakal, cart7);//assume order subtotal is above 1000, so shipping is free

        // ===== TEST 8: Add Non-Existent Product =====
        System.out.println("\n===== TEST 8: Add Non-Existent Product =====");
        Cart cart8 = new Cart();
        Product nonExistentProduct = null;
        try {
            cart8.addItem(nonExistentProduct, 1);
            System.out.println("ERROR: Should not reach here - null product was added!");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 9: Update Item Quantity =====
        System.out.println("\n===== TEST 9: Update Item Quantity =====");

        Product testCheese = new Product("Test Cheese", 100.0, 10, null, true, 0.4); // Stock: 10
        Product testTV = new Product("Test TV", 500.0, 5, null, true, 5.0);

        Cart cart9 = new Cart();
        cart9.addItem(testCheese, 2);
        System.out.println("Initial cart: " + cart9.getItems());

        cart9.updateQuantity(testCheese, 3);
        System.out.println("After updating to 3: " + cart9.getItems());

        // Test invalid update (product not in cart)
        try {
            cart9.updateQuantity(testTV, 1);
            System.out.println("ERROR: Should not reach here!");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        System.out.println("Remaining cheese stock: " + testCheese.getStock());

        // ===== TEST 10: Negative Quantity =====
        System.out.println("\n===== TEST 10: Negative Quantity =====");
        Product testBread = new Product("Bread", 50.0, 20, null, true, 0.3);
        Cart cart10 = new Cart();
        try {
            cart10.addItem(testBread, -1);
            System.out.println("ERROR: Should not reach here!");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 11: Zero Quantity =====
        System.out.println("\n===== TEST 11: Zero Quantity =====");
        Cart cart11 = new Cart();
        try {
            cart11.addItem(testBread, 0);
            System.out.println("ERROR: Should not reach here!");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 12: Negative Price =====
        System.out.println("\n===== TEST 12: Negative Price =====");
        try {
            Product invalidProduct = new Product("Invalid", -100.0, 10, null, true, 1.0);
            System.out.println("ERROR: Should not reach here!");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 13: Zero Price =====
        System.out.println("\n===== TEST 13: Zero Price =====");
        try {
            Product freeProduct = new Product("Free", 0.0, 10, null, true, 1.0);
            System.out.println("ERROR: Should not reach here!");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 14: Negative Balance =====
        System.out.println("\n===== TEST 14: Negative Balance =====");
        try {
            Customer brokeCustomer = new Customer("cust-99", "Broke", -100.0);
            System.out.println("ERROR: Should not reach here!");
        } catch (IllegalArgumentException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 15: Large Transaction =====
        System.out.println("\n===== TEST 15: Large Transaction =====");
        Customer richCustomer = new Customer("cust-100", "Rich", 1_000_000.0);
        Product diamond = new Product("Diamond", 200_000.0, 1, null, true, 0.1);
        Cart cart15 = new Cart();
        cart15.addItem(diamond, 1);
        try {
            checkoutService.checkout(richCustomer, cart15);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 16: Overweight Shipment =====
        System.out.println("\n===== TEST 16: Overweight Shipment =====");
        Product bricks = new Product("Bricks", 10.0, 100, null, true, 10.0);
        Cart cart16 = new Cart();
        cart16.addItem(bricks, 6);
        try {
            checkoutService.checkout(Heakal, cart16);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }
    }
}